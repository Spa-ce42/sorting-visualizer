package com.kennethfei.sorting_visualizer;

import com.kennethfei.sorting_visualizer.internal.HistogramRenderer;
import com.kennethfei.sorting_visualizer.internal.Window;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

public abstract class SV implements Runnable {
    private final Window window;
    private final boolean shouldRun;
    private int[] a;
    private int maxValue;
    private int[] coloredIndices;
    private int coloredIndicesPosition;
    private int updateInterval;
    private int updateCalls;

    public SV() {
        this.window = new Window(1280, 720, "Sorting Visualizer", NULL, NULL);
        this.shouldRun = true;
        this.window.addWindowSizeCallback((l, i, i1) -> glViewport(0, 0, i, i1));
        this.coloredIndices = new int[2];
        this.updateInterval = 1;
    }

    private void start() {
        this.window.clear(GL_COLOR_BUFFER_BIT);
        this.run();
        this.window.update();

        while(!this.window.shouldClose() && this.shouldRun) {
            this.window.clear(GL_COLOR_BUFFER_BIT);
            HistogramRenderer.render();
            this.window.update();
        }

        this.window.dispose();
    }

    @Override
    public abstract void run();

    public void array(int[] a, int maxValue) {
        this.a = new int[a.length];
        this.maxValue = maxValue;
        System.arraycopy(a, 0, this.a, 0, a.length);
        HistogramRenderer.initialize(this.a.length);

        for(int i = 0; i < this.a.length; ++i) {
            HistogramRenderer.setHeight(i, (float)a[i] / this.maxValue);
        }

        this.forceUpdate();
    }

    public void array(int[] a) {
        int maxValue = a[0];

        for(int i = 1; i < a.length; ++i) {
            if(a[i] > maxValue) {
                maxValue = a[i];
            }
        }

        this.array(a, maxValue);
    }

    private void setMaxValue(int newMaxValue) {
        this.maxValue = newMaxValue;

        for(int i = 0; i < this.a.length; ++i) {
            HistogramRenderer.setHeight(i, (float)this.a[i] / this.maxValue);
        }
    }

    public void arraySet(int index, int value) {
        this.a[index] = value;

        if(value > this.maxValue) {
            this.setMaxValue(value);
            return;
        }

        HistogramRenderer.setHeight(index, (float)value / this.maxValue);
    }

    public int arrayGet(int index) {
        return this.a[index];
    }

    public void arraySwap(int i, int j) {
        int temp = this.a[i];
        this.a[i] = this.a[j];
        this.a[j] = temp;
        this.arraySet(i, this.a[i]);
        this.arraySet(j, this.a[j]);
    }

    public int arrayLength() {
        return this.a.length;
    }

    public void forceUpdate() {
        this.window.clear(GL_COLOR_BUFFER_BIT);
        HistogramRenderer.render();
        this.window.update();
    }

    public void delay(long delayMillis) {
        long delayNanos = TimeUnit.MILLISECONDS.toNanos(delayMillis);
        long expected = System.nanoTime() + delayNanos;

        while(System.nanoTime() < expected) {
            if(this.window.shouldClose()) {
                this.window.dispose();
                System.exit(0);
            }

            glfwPollEvents();
        }
    }

    public void color(int index, float r, float g, float b) {
        if(this.coloredIndicesPosition >= this.coloredIndices.length) {
            for(int coloredIndex : this.coloredIndices) {
                HistogramRenderer.setColor(coloredIndex, 1, 1, 1);
            }

            this.coloredIndicesPosition = 0;
        }

        HistogramRenderer.setColor(index, r, g, b);
        this.coloredIndices[this.coloredIndicesPosition++] = index;
    }

    public void color(int index, int r, int g, int b) {
        this.color(index, r / 255f, g / 255f, b / 255f);
    }

    public void setMaxColoredBars(int max) {
        for(int i = 0; i < this.coloredIndicesPosition; ++i) {
            HistogramRenderer.setColor(this.coloredIndices[i], 1, 1, 1);
        }

        this.coloredIndicesPosition = 0;

        if(max != this.coloredIndices.length) {
            this.coloredIndices = new int[max];
        }
    }

    public void clearColor() {
        this.setMaxColoredBars(this.coloredIndices.length);
    }

    public void setUpdateInterval(int newInterval) {
        this.updateInterval = newInterval;
    }

    public void update() {
        if(++this.updateCalls < this.updateInterval) {
            return;
        }

        this.updateCalls = 0;
        this.forceUpdate();
    }

    public static void launch(SV sv) {
        sv.start();
    }

    public static void launch() {
        try {
            Class<?> c = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName());
            Constructor<?> d = c.getDeclaredConstructor();
            SV sv = (SV)d.newInstance();
            HistogramRenderer.initialize(0);
            sv.start();
        } catch(ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
