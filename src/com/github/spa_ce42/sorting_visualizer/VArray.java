package com.github.spa_ce42.sorting_visualizer;

import com.github.spa_ce42.sorting_visualizer.internal.ActionBuffer;
import com.github.spa_ce42.sorting_visualizer.internal.Renderer;

public class VArray {
    private final int[] a;
    private final float x;
    private final float y;
    private final ActionBuffer actions;

    public void resize(int width, int height) {
        Renderer.resize(width, height);

        for(int i = 0; i < this.a.length; ++i) {
            int j = this.a[i];
            Renderer.drawQuad(i * this.x, 0, (i + 1) * this.x, (j + 1) * this.y, 1, 1, 1);
        }

        Renderer.push();
    }

    public VArray(int width, int height, int size) {
        this.a = new int[size];
        this.actions = new ActionBuffer(100000);

        for(int i = 0; i < this.a.length; ++i) {
            this.a[i] = i;
        }

        this.x = 1f / size;
        this.y = 0.95f / size;
        this.resize(width, height);
    }

    public void setColor(int index, float r, float g, float b) {
        synchronized(this.actions) {
            while(this.actions.isFull()) {
                try {
                    this.actions.wait();
                } catch(InterruptedException ignored) {

                }
            }

            int j = this.a[index];
            this.actions.add(index * this.x, 0, (index + 1) * this.x, (j + 1) * this.y, r, g, b);
        }
    }

    public void set(int index, int i) {
        synchronized(this.actions) {
            while(this.actions.isFull()) {
                try {
                    this.actions.wait();
                } catch(InterruptedException ignored) {

                }
            }

            this.actions.add(index * this.x, 0, (index + 1) * this.x, 1, 0, 0, 0);
            this.actions.add(index * this.x, 0, (index + 1) * this.x, (i + 1) * this.y, 1, 1, 1);
            this.a[index] = i;
        }
    }

    public int get(int index) {
        return this.a[index];
    }

    public void swap(int i, int j) {
        synchronized(this.actions) {
            int temp = this.a[i];
            this.set(i, this.a[j]);
            this.set(j, temp);
        }
    }

    public void draw() {
        synchronized(this.actions) {
            if(this.actions.isEmpty()) {
                Renderer.render();
                return;
            }

            while(!this.actions.isEmpty()) {
                Renderer.drawQuad(this.actions.poll(), this.actions.poll(), this.actions.poll(), this.actions.poll(), this.actions.poll(), this.actions.poll(), this.actions.poll());
            }

            this.actions.reset();
            Renderer.push();
            Renderer.render();
            this.actions.notify();
        }
    }

    public int size() {
        return this.a.length;
    }
}
