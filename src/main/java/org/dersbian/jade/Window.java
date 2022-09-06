package org.dersbian.jade;

//lwjgl
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

//log4j
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//jade
import org.dersbian.jade.util.*;


public class Window {
	private static final Logger LOGGER = LogManager.getLogger(Window.class);
	private int width, height;
	private String title;
	private long glfwWindow;

	public float r, g, b, a;
	private boolean fadeToBlack = false;

	private static Window window = null;

	private static Scene currentScene;

	private Window() {
		this.width = 960;
		this.height = 540;
		this.title = "MarioClon";
		this.r = this.b = this.g = this.a = 1;
	}

	public static void changeScene(int newScene) {
		switch (newScene) {
			case 0:
				currentScene = new LevelEditorScene();
				//currentScene.init();
				break;
			case 1:
				currentScene = new LevelScene();
				break;
			default:
				assert false : "Unknown scene '" + newScene + "'";
				break;
		}
	}

	public static Window get(){
		if(Window.window == null){
			Window.window = new Window();
		}
		return Window.window;
	}

	public void run() {
		LOGGER.info("Hello LWJGL {} !",Version.getVersion());

		init();
		loop();

		glfwFreeCallbacks(glfwWindow);
		glfwDestroyWindow(glfwWindow);

		glfwTerminate();
		java.util.Objects.requireNonNull(glfwSetErrorCallback(null)).free();
	}

	private void init() {
		GLFWErrorCallback.createPrint(System.err).set();

		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

		glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
		if ( glfwWindow == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
		glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
		glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
		glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);


		glfwMakeContextCurrent(glfwWindow);
		glfwSwapInterval(1);

		glfwShowWindow(glfwWindow);

		GL.createCapabilities();
		Window.changeScene(0);
	}

	public void loop() {
		float beginTime = Time.getTime();
		float endTime;
		float dt = -1.0f;

		while  ( !glfwWindowShouldClose(glfwWindow) ) {
			glfwPollEvents();

			glClearColor(r, g, b, a);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			if (dt >= 0) {
				currentScene.update(dt);
			}

			glfwSwapBuffers(glfwWindow);

			endTime = Time.getTime();
			dt = endTime - beginTime;
			beginTime = endTime;
		}
	}
}
