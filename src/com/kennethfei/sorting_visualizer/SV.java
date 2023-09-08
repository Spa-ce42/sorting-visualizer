package com.kennethfei.sorting_visualizer;

import com.kennethfei.sorting_visualizer.internal.HistogramRenderer;
import com.kennethfei.sorting_visualizer.internal.Window;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

public abstract class SV implements Runnable {
    private Window window;
    private int[] a;
    private int maxValue;
    private int[] coloredIndices;
    private int coloredIndicesPosition;
    private int updateInterval;
    private int updateCalls;
    private boolean autoColor;

    public SV() {

    }

    private void start() {

        this.window.clear(GL_COLOR_BUFFER_BIT);
        this.run();
        this.window.update();

        while(!this.window.shouldClose()) {
            this.window.clear(GL_COLOR_BUFFER_BIT);
            HistogramRenderer.render();
            this.window.update();
        }

        this.window.dispose();
    }

    private void initialize() {

        SVConfiguration svConfig = new SVConfiguration();
        this.init(svConfig);
        this.window = new Window(svConfig.getWidth(), svConfig.getHeight(), svConfig.getTitle(), NULL, NULL);
        this.window.setExitOnClose(true);
        this.window.addWindowSizeCallback((l, width, height) -> glViewport(0, 0, width, height));
        this.window.addKeyCallback((window, key, scancode, action, mods) -> {
            if(key == GLFW_KEY_R && action == GLFW_PRESS) {
                SV.this.run();
            }
        });

        this.coloredIndices = new int[svConfig.getMaxColoredBars()];
        this.updateInterval = svConfig.getUpdateInterval();
        this.autoColor = svConfig.isAutoColor();
    }

    public void init(SVConfiguration svConfig) {

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

    public void set(int index, int value) {
        if(this.autoColor) {
            this.color(index, 1, 0, 0);
        }

        this.a[index] = value;

        if(value > this.maxValue) {
            this.setMaxValue(value);
            return;
        }

        HistogramRenderer.setHeight(index, (float)value / this.maxValue);
    }

    public int get(int index) {
        if(this.autoColor) {
            this.color(index, 1, 0, 0);
        }

        return this.a[index];
    }

    public void swap(int i, int j) {
        if(this.autoColor) {
            this.color(i, 1, 0, 0);
            this.color(j, 1, 0, 0);
        }

        int temp = this.a[i];
        this.a[i] = this.a[j];
        this.a[j] = temp;
        this.set(i, this.a[i]);
        this.set(j, this.a[j]);
    }

    public int length() {
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
        while(System.nanoTime() < expected);
    }

    public void color(int index, float r, float g, float b) {
        HistogramRenderer.setColor(this.coloredIndices[this.coloredIndicesPosition], 1, 1, 1);
        HistogramRenderer.setColor(index, r, g, b);
        this.coloredIndices[this.coloredIndicesPosition++] = index;

        if(coloredIndicesPosition >= this.coloredIndices.length) {
            this.coloredIndicesPosition = 0;
        }
    }

    public void setMaxColoredBars(int max) {
        for(int coloredIndex : this.coloredIndices) {
            HistogramRenderer.setColor(coloredIndex, 1, 1, 1);
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

    public void setAutoColor(boolean flag) {
        this.autoColor = flag;
    }

    public static void launch(SV sv) {
        sv.start();
    }

    public static void launch() {
        try {
            Class<?> c = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName());
            Constructor<?> d = c.getDeclaredConstructor();
            SV sv = (SV)d.newInstance();
            sv.initialize();
            HistogramRenderer.initialize(0);
            sv.start();
        } catch(ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
