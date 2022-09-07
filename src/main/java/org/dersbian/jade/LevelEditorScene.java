package org.dersbian.jade;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LevelEditorScene extends Scene {
	private static final Logger LOGGER = LogManager.getLogger(LevelEditorScene.class);
	private String vertexShaderSrc = """
			#version 330 core
			layout (location = 0) in vec3 aPos;
			layout (location = 0) in vec4 aColor;
			
			out vec4 fColor;
			
			void main() {
			    fColor = aColor;
			    gl_Position = vec4(aPos, 1.0);
			}
			""";
	private String fragmentShaderSrc = """
			#version 330 core
			
			in vec4 fColor;
			out vec4 color;
			
			void main() {
			    color = fColor;
			}
			""";
	private int vertexID, fragmentID, shaderProgram;

	private float[] vertexArray = {
			// position               // color
			0.5f, -0.5f, 0.0f,       1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
			-0.5f,  0.5f, 0.0f,       0.0f, 1.0f, 0.0f, 1.0f, // Top left     1
			0.5f,  0.5f, 0.0f ,      1.0f, 0.0f, 1.0f, 1.0f, // Top right    2
			-0.5f, -0.5f, 0.0f,       1.0f, 1.0f, 0.0f, 1.0f, // Bottom left  3
	};

	// IMPORTANT: Must be in counter-clockwise order
	private int[] elementArray = {
			2, 1, 0, // Top right triangle
			0, 1, 3 // bottom left triangle
	};
	private int vaoID, vboID, eboID;


	public LevelEditorScene() {
		LOGGER.info("Inside level editor scene");
	}

	@Override
	public void init() {
		vertexID = glCreateShader(GL_VERTEX_SHADER);

		glShaderSource(vertexID, vertexShaderSrc);
		glCompileShader(vertexID);

		int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
			LOGGER.error("defaultShader.glsl' {}Vertex shader compilation failed.", "\n\t");
			LOGGER.error("{}",glGetShaderInfoLog(vertexID, len));
			assert false : "";
		}

		fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentID, fragmentShaderSrc);
		glCompileShader(fragmentID);

		success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
			LOGGER.error("defaultShader.glsl' {}Fragment shader compilation failed.", "\n\t");
			LOGGER.error("{}", glGetShaderInfoLog(fragmentID, len));
			assert false : "";
		}

		shaderProgram = glCreateProgram();
		glAttachShader(shaderProgram, vertexID);
		glAttachShader(shaderProgram, fragmentID);
		glLinkProgram(shaderProgram);

		success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
		if (success == GL_FALSE) {
			int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
			LOGGER.error("defaultShader.glsl' {}Linking of shaders failed.", "\n\t");
			LOGGER.error("{}", glGetProgramInfoLog(shaderProgram, len));
			assert false : "";
		}

		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);

		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
		vertexBuffer.put(vertexArray).flip();

		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

		// Create the indices and upload
		IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
		elementBuffer.put(elementArray).flip();

		eboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

		// Add the vertex attribute pointers
		int positionsSize = 3;
		int colorSize = 4;
		int floatSizeBytes = 4;
		int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;
		glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
		glEnableVertexAttribArray(0);

		glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
		glEnableVertexAttribArray(1);
	}

	@Override
	public void update(float dt) {
		// Bind shader program
		glUseProgram(shaderProgram);
		// Bind the VAO that we're using
		glBindVertexArray(vaoID);

		// Enable the vertex attribute pointers
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

		// Unbind everything
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);

		glBindVertexArray(0);

		glUseProgram(0);
	}
}