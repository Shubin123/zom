package com.shub;

import org.lwjgl.opengl.GLDebugMessageCallback;

import static org.lwjgl.opengl.GL11C.glEnable;
import static org.lwjgl.opengl.GL43C.GL_DEBUG_OUTPUT;
import static org.lwjgl.opengl.GL43C.glDebugMessageCallback;





public class Misc {

    public void enableDebugOutput() {
//        DONT USE CAUSES CRASH ON MACOS
        glEnable(GL_DEBUG_OUTPUT);
        glDebugMessageCallback((source, type, id, severity, length, message, userParam) -> {
            System.err.println("GL DEBUG: " + GLDebugMessageCallback.getMessage(length, message));
        }, 0);
    }
}
