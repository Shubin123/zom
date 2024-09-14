package com.shub;

import org.lwjgl.opengl.GL20;

import static com.shub.Draw.createMesh;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glUseProgram;
import static org.lwjgl.opengl.GL30.glGetUniformLocation;

public class Object {
    private Mesh mesh;
    private int shaderProgram;
    private int textureID;

    public void setup() throws Exception {
        // Create capabilities and shader setup
        Shader shader = new Shader();
        int vertexShader = shader.createShader("./src/resources/shaders/vert", GL20.GL_VERTEX_SHADER);
        int fragmentShader = shader.createShader("./src/resources/shaders/frag", GL20.GL_FRAGMENT_SHADER);

        // Create the shader program and attach shaders
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);

        // Check for shader program linking errors
        int linkStatus = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (linkStatus == GL_FALSE) {
            String infoLog = glGetProgramInfoLog(shaderProgram);
            throw new RuntimeException("Shader program linking failed: " + infoLog);
        }

        // Clean up shaders (they're linked, no need to keep them)
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

        // Load mesh data (vertices, UVs, and indices)
        final float[] vertices = {
                // Positions          // Texture Coords
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,  // Bottom-left-back
                0.5f, -0.5f, -0.5f,  1.0f, 0.0f,  // Bottom-right-back
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,  // Top-right-back
                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,  // Top-left-back

                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,  // Bottom-left-front
                0.5f, -0.5f,  0.5f,  1.0f, 0.0f,  // Bottom-right-front
                0.5f,  0.5f,  0.5f,  1.0f, 1.0f,  // Top-right-front
                -0.5f,  0.5f,  0.5f,  0.0f, 1.0f   // Top-left-front
        };

        final int[] indices = {
                // Back face
                0, 1, 2,
                2, 3, 0,
                // Front face
                4, 5, 6,
                6, 7, 4,
                // Left face
                0, 3, 7,
                7, 4, 0,
                // Right face
                1, 5, 6,
                6, 2, 1,
                // Top face
                3, 2, 6,
                6, 7, 3,
                // Bottom face
                0, 1, 5,
                5, 4, 0
        };
        mesh = createMesh(vertices, indices);

        // Load texture
        textureID = Texture.loadTexture("./src/resources/textures/uv.png");

        // Set uniform locations for the texture
        int texOffsetLocation = glGetUniformLocation(shaderProgram, "uTexOffset");
        int texScaleLocation = glGetUniformLocation(shaderProgram, "uTexScale");

        // Set default uniform values (no offset, no rescale initially)
        glUseProgram(shaderProgram); // Ensure shader program is active
        glUniform2f(texOffsetLocation, 0.0f, 0.0f);
        glUniform2f(texScaleLocation, 1.0f, 1.0f);
        glUseProgram(0); // Unbind the shader program
    }

    public int getShaderProgram() {
        return shaderProgram;
    }

    public int getTextureID() {
        return textureID;
    }

    public Mesh getMesh() {
        return mesh;
    }
}
