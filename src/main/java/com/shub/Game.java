package com.shub;

import org.lwjgl.Version;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.vulkan.*;

import java.io.IOException;

public class Game {

    public long window;


    Game(long window) {
        this.window = window;
    }

    public void start() throws IOException {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");


        Init i = new Init(window);

        window = i.start();

        Loop l = new Loop(window);

        l.start();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
