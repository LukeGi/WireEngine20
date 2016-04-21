package main.client;

import main.coreEngine.Engine;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * Created by Kelan on 02/04/2016.
 */
public final class Window
{
    private static long handle = -1;
    private static GLFWErrorCallback error;

    private static IntBuffer width;
    private static IntBuffer height;

    public static void setupWindow(int width, int height, String title)
    {
        error = GLFWErrorCallback.createPrint(System.err);
        Window.width = BufferUtils.createIntBuffer(4);
        Window.height = BufferUtils.createIntBuffer(4);

        if (glfwInit() == GLFW_FALSE)
            throw new RuntimeException("Failed to initialise GLFW window [glfwInit() == GLFW_FALSE]");

        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        if ((handle = glfwCreateWindow(width, height, title, NULL, NULL)) == NULL)
            throw new RuntimeException("Failed to create GLFW window [glfwCreateWindow(...) == NULL]");

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(handle, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

        glfwMakeContextCurrent(handle);
        glfwSwapInterval(1);
        glfwShowWindow(handle);

        InputHandler.init(handle);

        glfwSetKeyCallback(handle, InputHandler.keyboard);
        glfwSetMouseButtonCallback(handle, InputHandler.mouse);
        glfwSetScrollCallback(handle, InputHandler.scrollWheel);

        createCapabilities();
        System.out.println("Creating openGL context on thread " + Thread.currentThread().getName());
    }

    public static void update()
    {
        if (handle == NULL)
            throw new RuntimeException("Cannot update a null window. (Window has not been initialised, or was destroyed)");

        glfwSwapBuffers(handle);

        InputHandler.update();

        if (isCloseRequested())
            Engine.stop();
    }

    public static boolean isCloseRequested()
    {
        return glfwWindowShouldClose(handle) == GLFW_TRUE;
    }

    public static void destroyWindow()
    {
        glfwDestroyWindow(handle);
        glfwDestroyCursor(handle);
        glfwTerminate();
    }

    public static float getAspect()
    {
        return (float) getWidth() / getHeight();
    }

    public static int getWidth()
    {
        glfwGetWindowSize(handle, width, height);
        return width.get(0);
    }

    public static float getHeight()
    {
        glfwGetWindowSize(handle, width, height);
        return height.get(0);
    }
}
