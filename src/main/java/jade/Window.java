package jade;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
//import org.lwjgl.system.*;
//import java.nio.IntBuffer;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
//import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Window {
	private static final Logger LOGGER = LogManager.getLogger(Window.class);
	private int width, height;
	private String title;
	private long glfwWindow;

	private static  Window window = null;
	private Window() {
		this.width = 960;
		this.height = 540;
		this.title = "MarioClon";
	}

	public static Window get(){
		if(Window.window == null){
			Window.window = new Window();
		}
		return Window.window;
	}

	public void run() {
		LOGGER.info("Hello LWJGL {} !", org.lwjgl.Version.getVersion());

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

		glfwMakeContextCurrent(glfwWindow);
		glfwSwapInterval(1);

		glfwShowWindow(glfwWindow);
		GL.createCapabilities();
	}

	private void loop() {
		while ( !glfwWindowShouldClose(glfwWindow) ) {
			glfwPollEvents();

			glClearColor( 1, 0, 0, 0.0f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

			glfwSwapBuffers(glfwWindow);
		}
	}
}