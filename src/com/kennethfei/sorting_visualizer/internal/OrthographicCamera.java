package com.kennethfei.sorting_visualizer.internal;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class OrthographicCamera {
    private final Matrix4f viewProjectionMatrix;
    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Vector3f position;
    private float rotation;
    private Vector3f deltaPosition;

    public OrthographicCamera(float left, float right, float bottom, float top) {
        this.projectionMatrix = new Matrix4f().ortho(left, right, bottom, top, -1, 1);
        this.viewMatrix = new Matrix4f();
        this.viewProjectionMatrix = new Matrix4f();
        this.projectionMatrix.mul(this.viewMatrix, this.viewProjectionMatrix);
        this.position = new Vector3f();
        this.deltaPosition = new Vector3f();
    }

    public void setProjection(float left, float right, float bottom, float top) {
        this.projectionMatrix = new Matrix4f().ortho(left, right, bottom, top, -1, 1);
        this.projectionMatrix.mul(this.viewMatrix, this.viewProjectionMatrix);
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public void setPosition(Vector3f newPosition) {
        this.deltaPosition = newPosition.sub(this.position, this.deltaPosition);
        this.position = new Vector3f(newPosition);
        this.recalculateViewMatrix();
    }

    public float getRotation() {
        return this.rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        this.recalculateViewMatrix();
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public Matrix4f getViewMatrix() {
        return this.viewMatrix;
    }

    public Matrix4f getViewProjectionMatrix() {
        return this.viewProjectionMatrix;
    }

    private void recalculateViewMatrix() {
        Matrix4f transform = new Matrix4f().translate(this.position).mul(new Matrix4f().rotate(this.rotation, 0, 0, 1));
        this.viewMatrix = transform.invert();
        this.projectionMatrix.mul(this.viewMatrix, this.viewProjectionMatrix);
        this.deltaPosition = new Vector3f();
    }
}
