package com.shub;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
public class Mesh {
    private int vaoID;
    private int vboID;
    private int vertexCount;

    // Mesh constructor expects a pre-existing VAO and the actual vertex data
    public Mesh(int vaoID, float[] vertices, int[] indices) {
        this.vaoID = vaoID;
        this.vertexCount = indices.length;

        // Bind the VAO (which was generated earlier)
        glBindVertexArray(vaoID);

        // Generate and bind the VBO for vertex data
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, createFloatBuffer(vertices), GL_STATIC_DRAW);

        // Define the vertex attribute pointers
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0); // Position attribute
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES); // Texture coordinates
        glEnableVertexAttribArray(1);

        glBindVertexArray(0); // Unbind the VAO
    }

    private FloatBuffer createFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
