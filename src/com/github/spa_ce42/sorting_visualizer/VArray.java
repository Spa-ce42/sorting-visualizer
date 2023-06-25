package com.github.spa_ce42.sorting_visualizer;

import com.github.spa_ce42.sorting_visualizer.internal.Renderer;

import java.util.LinkedList;

public class VArray {
    private final int[] a;
    private final float x;
    private final float y;
    private final LinkedList<DrawQuadAction> actions;
    private final Object LOCK0 = new Object();
    private final Object LOCK1 = new Object();


    public record DrawQuadAction(float blx, float bly, float trx, float try_, float r, float g, float b) {

    }

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
        this.actions = new LinkedList<>();

        for(int i = 0; i < this.a.length; ++i) {
            this.a[i] = i;
        }

        this.x = 1f / size;
        this.y = 0.95f / size;
        this.resize(width, height);
    }

    public void setColor(int index, float r, float g, float b) {
        synchronized(this.LOCK0) {
            while(this.actions.size() == 100000) {
                try {
                    this.LOCK0.wait();
                } catch(InterruptedException ignored) {

                }
            }

            int j = this.a[index];
            DrawQuadAction dqa = new DrawQuadAction(index * this.x, 0, (index + 1) * this.x, (j + 1) * this.y, r, g, b);
            this.actions.add(dqa);
        }
    }

    public void set(int index, int i) {
        synchronized(this.LOCK1) {
            while(this.actions.size() == 100000) {
                try {
                    this.LOCK1.wait();
                } catch(InterruptedException ignored) {

                }
            }

            DrawQuadAction dqa = new DrawQuadAction(index * this.x, 0, (index + 1) * this.x, 1, 0, 0, 0);
            this.actions.add(dqa);
            DrawQuadAction dqb = new DrawQuadAction(index * this.x, 0, (index + 1) * this.x, (i + 1) * this.y, 1, 1, 1);
            this.actions.add(dqb);
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
        synchronized(this.LOCK0) {
            synchronized(this.LOCK1) {
                if(this.actions.isEmpty()) {
                    Renderer.render();
                    return;
                }


                while(!this.actions.isEmpty()) {
                    DrawQuadAction dqa = this.actions.poll();
                    Renderer.drawQuad(dqa.blx, dqa.bly, dqa.trx, dqa.try_, dqa.r, dqa.g, dqa.b);
                }

                Renderer.push();
                Renderer.render();
                this.LOCK0.notify();
                this.LOCK1.notify();
            }
        }
    }

    public int size() {
        return this.a.length;
    }
}
