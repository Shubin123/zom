package com.shub;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class Loop {
    private long window;
    private Object object;
    private Camera camera; // Add the camera

    public Loop(long window) {
        this.window = window;
        this.object = new Object();
    }

    public void start() throws Exception {
        // Initialize OpenGL capabilities
        GL.createCapabilities();
//        glEnable(GL_CULL_FACE);
        glEnable (GL_DEPTH_TEST);
//        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE); // mesh view mode

//        // Set the culling to back face only
//        glCullFace(GL_BACK); // IF TRANSPARENCY IS ON WE ARE FUCKCUFCKCKCKKCKED
//        // Set the front face orientation (default is counter-clockwise)
//        glFrontFace(GL_CW);


        // Set up the objects (shaders, textures, mesh)
        object.setup();

        // Set up the camera
        camera = new Camera(45.0f, 800, 600, 0.1f, 100.0f);

        int shaderProgram = object.getShaderProgram();
        int textureID = object.getTextureID();
        Mesh mesh = object.getMesh();

        // Uniform locations for texture offset and scale
        int texOffsetLocation = glGetUniformLocation(shaderProgram, "uTexOffset");
        int texScaleLocation = glGetUniformLocation(shaderProgram, "uTexScale");

        // Uniform locations for matrices
        int modelLoc = glGetUniformLocation(shaderProgram, "model");
        int viewLoc = glGetUniformLocation(shaderProgram, "view");
        int projLoc = glGetUniformLocation(shaderProgram, "projection");

        if (modelLoc == -1 || viewLoc == -1 || projLoc == -1) {
            System.out.println("Error: Uniform locations not found.");
        }

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Use the shader program
            glUseProgram(shaderProgram);

            // Dynamically set offset and scale during each render loop
            float time = (float) glfwGetTime();
            float offsetX = (float) Math.sin(time) * 0.5f;
            float scale = 1.0f + (float) Math.sin(time) * 0.5f;


            // Update matrices and send them to the shader
            camera.update();

            Matrix4f model = new Matrix4f().rotateX((float) Math.toRadians(time*100)); // Example rotation
            glUniformMatrix4fv(modelLoc, false, model.get(new float[16]));
            glUniformMatrix4fv(viewLoc, false, camera.getViewMatrix().get(new float[16]));
            glUniformMatrix4fv(projLoc, false, camera.getProjectionMatrix().get(new float[16]));


            // Update texture uniforms
            glUniform2f(texOffsetLocation, offsetX, 0.0f);
            glUniform2f(texScaleLocation, scale, scale);

            // Bind texture and mesh for rendering
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, textureID);
            glUniform1i(glGetUniformLocation(shaderProgram, "texture1"), 0);

            glBindVertexArray(mesh.getVaoID());
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);

            // Render the mesh
            glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

            // Disable attributes and unbind VAO
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
        glDeleteTextures(textureID);
    }
}
