package com.shub;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Draw {
    private static List<Integer> vaos = new ArrayList<>();
    private static List<Integer> vbos = new ArrayList<>();

    private static FloatBuffer createFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private static IntBuffer createIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private static void storeData(int attribute, int dimensions, float[] data) {
        int vbo = GL15.glGenBuffers(); // Creates a VBO ID
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo); // Loads the current VBO to store the data
        FloatBuffer buffer = createFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attribute, dimensions, GL11.GL_FLOAT, false, dimensions * Float.BYTES, 0); // Adjust stride based on attribute size
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); // Unloads the current VBO when done.
    }

    private static void bindIndices(int[] data) {
        int vbo = GL15.glGenBuffers(); // Generate EBO ID
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
        IntBuffer buffer = createIntBuffer(data);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    public static Mesh createMesh(float[] vertices, int[] indices) {
        int vao = genVAO(); // Generate VAO
        bindIndices(indices); // Bind the element (index) buffer

        // Store vertex position data (3 floats per vertex, starting at 0)
        storeData(0, 3, vertices);

        // Store texture coordinates (2 floats per vertex, starting after 3 floats)
        storeData(1, 2, vertices);

        GL30.glBindVertexArray(0); // Unbind VAO
        return new Mesh(vao, indices.length); // Return mesh with VAO and index count
    }

    private static int genVAO() {
        int vao = GL30.glGenVertexArrays(); // Generate VAO ID
        vaos.add(vao);
        GL30.glBindVertexArray(vao); // Bind VAO for the following operations
        return vao;
    }
}
