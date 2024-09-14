package com.shub;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Vector3f position;
    private float pitch;  // Rotation around X-axis
    private float yaw;    // Rotation around Y-axis
    private float roll;   // Rotation around Z-axis

    public Camera(float fov, float width, float height, float nearPlane, float farPlane) {
        projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(fov), width / height, nearPlane, farPlane);
        viewMatrix = new Matrix4f();
        position = new Vector3f(0.0f, 0.0f, 3.0f); // Start camera a bit away from the origin
    }

    public void update() {
        // Simulating non-stationary movement (rotating around the Y-axis for now)
        yaw += 0.01f;

        // Create view matrix
        viewMatrix.identity();
        viewMatrix.rotateX(pitch)
                .rotateY(yaw)
                .rotateZ(roll)
                .translate(-position.x, -position.y, -position.z);
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }
}
