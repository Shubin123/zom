package com.shub;

import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Texture {
    // Load texture using STB
    public static int loadTexture(String filePath) {
        // Generate texture ID
        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        // Set the texture wrapping/filtering options
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // Load the texture image
        try (org.lwjgl.system.MemoryStack stack = stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer nrChannels = stack.mallocInt(1);

            ByteBuffer data = STBImage.stbi_load(filePath, width, height, nrChannels, 4); // Load as RGBA

            if (data != null) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
                glGenerateMipmap(GL_TEXTURE_2D); // Generate mipmaps
                STBImage.stbi_image_free(data);  // Free memory
            } else {
                throw new RuntimeException("Failed to load texture: " + filePath);
            }
        }

        return textureID;
    }

    public static void checkLoaded(String filePath)
    {
        try (org.lwjgl.system.MemoryStack stack = stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer nrChannels = stack.mallocInt(1);

            ByteBuffer data = STBImage.stbi_load(filePath, width, height, nrChannels, 4); // Load as RGBA

            if (data != null) {
                System.out.println("Loaded texture: " + filePath + ", Width: " + width.get(0) + ", Height: " + height.get(0) + ", Channels: " + nrChannels.get(0));
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
                glGenerateMipmap(GL_TEXTURE_2D);
                STBImage.stbi_image_free(data);
            } else {
                throw new RuntimeException("Failed to load texture: " + filePath);
            }
        }
    }

}
