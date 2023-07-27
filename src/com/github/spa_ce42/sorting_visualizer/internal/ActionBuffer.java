package com.github.spa_ce42.sorting_visualizer.internal;

public class ActionBuffer {
    private final float[] t;
    private int read;
    private int write;

    public ActionBuffer(int size) {
        this.t = new float[7 * size];
    }

    public boolean isEmpty() {
        return this.read >= this.write;
    }

    public boolean isFull() {
        return this.write >= this.t.length;
    }

    public void add(float a, float b, float c, float d, float e, float f, float g) {
        this.t[this.write++] = a;
        this.t[this.write++] = b;
        this.t[this.write++] = c;
        this.t[this.write++] = d;
        this.t[this.write++] = e;
        this.t[this.write++] = f;
        this.t[this.write++] = g;
    }

    public float poll() {
        return this.t[this.read++];
    }

    public int getReadPosition() {
        return this.read;
    }

    public int getWritePosition() {
        return this.write;
    }

    public int capacity() {
        return this.t.length;
    }

    public void reset() {
        this.read = 0;
        this.write = 0;
    }
}
