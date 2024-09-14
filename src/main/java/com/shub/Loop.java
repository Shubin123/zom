package com.shub;

import org.lwjgl.Version;
import org.lwjgl.opengl.GL;
import org.lwjgl.stb.STBImage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Loop {
    private long window;
    private int shaderProgram;
    private int textureID;

    public Loop(long window) {
        this.window = window;
    }

    public void start() throws Exception {
        GL.createCapabilities();
        Shader m_shader = new Shader();

        // Load and compile shaders (add these methods yourself)
        int m_vertexShader = m_shader.createShader("./src/resources/shaders/vert", GL_VERTEX_SHADER);
        int m_fragmentShader = m_shader.createShader("./src/resources/shaders/frag", GL_FRAGMENT_SHADER);

        // Link shaders into a program
//        m_shaderProgram = glCreateProgram();
        shaderProgram = m_shader.getShaderProgramId();
        glAttachShader(shaderProgram, m_vertexShader);
        glAttachShader(shaderProgram, m_fragmentShader);
        glLinkProgram(shaderProgram);

        // Check for linking errors
        if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
            System.err.println("Program Linking failed!");
            System.err.println(glGetProgramInfoLog(shaderProgram));
        }

        // Delete the shaders as they're linked into the program now and no longer necessary
        glDeleteShader(m_vertexShader);
        glDeleteShader(m_fragmentShader);

        // Vertex data (including texture coordinates)
        float[] vertices = {
                // Positions       // Texture Coords
                -0.5f, -0.5f, 0f,   0.0f, 0.0f,  // Lower-left corner
                0.5f, -0.5f, 0f,   1.0f, 0.0f,  // Lower-right corner
                0f,    0.5f, 0f,   0.5f, 1.0f   // Top center
        };
        int[] indices = { 0, 1, 2 };

        // Create Mesh
        Mesh mesh = Draw.createMesh(vertices, indices);

        // Load the texture
        textureID = Texture.loadTexture("./src/resources/textures/city.png");
        String filePath = "./src/resources/textures/city.png";
        Texture.checkLoaded(filePath);
        // Uniform locations for texture offset and scale
        int texOffsetLocation = glGetUniformLocation(shaderProgram, "uTexOffset");
        int texScaleLocation = glGetUniformLocation(shaderProgram, "uTexScale");

        // Set the initial offset and scale values
        glUniform2f(texOffsetLocation, 0.0f, 0.0f);  // No offset initially
        glUniform2f(texScaleLocation, 1.0f, 1.0f);   // Default scale (no resampling)




        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Use the shader program
            glUseProgram(shaderProgram);

            // Dynamically set the offset and scale during each render loop
            float time = (float) glfwGetTime();
            float offsetX = (float) Math.sin(time) * 0.5f; // Example: move texture based on time
            float scale = 1.0f + (float) Math.sin(time) * 0.5f; // Example: resample texture dynamically

            // Update uniform values
            glUniform2f(texOffsetLocation, offsetX, 0.0f); // Horizontal movement
            glUniform2f(texScaleLocation, scale, scale);   // Resample the texture

            // Bind the texture
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, textureID);
            glUniform1i(glGetUniformLocation(shaderProgram, "texture1"), 0);

            // Bind the mesh and draw
            glBindVertexArray(mesh.getVaoID());
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1); // Enable texture coordinate attribute

            glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

            // Disable attributes and unbind
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glBindVertexArray(0);

            // Swap buffers and poll events
            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        // Cleanup
        glUseProgram(0);
        glDeleteProgram(shaderProgram);
        glDeleteTextures(textureID); // Delete texture when done
    }
}
