package sortingvisualizer.internal;

import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_POSITION_X;
import static org.lwjgl.glfw.GLFW.GLFW_POSITION_Y;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowCloseCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

public class Window {
    private final long window;
    private int width;
    private int height;
    private String title;
    private final List<GLFWWindowSizeCallbackI> windowSizeCallbacks;
    private final List<GLFWKeyCallbackI> keyCallbacks;

    public Window(int width, int height, String title, long monitor, long share) {
        if(!glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW");
        }

        GLFWVidMode glfwVidMode = Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor()));
        glfwWindowHint(GLFW_POSITION_X, (glfwVidMode.width() - width) / 2);
        glfwWindowHint(GLFW_POSITION_Y, (glfwVidMode.height() - height) / 2);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        this.width = width;
        this.height = height;
        this.title = title;
        this.window = glfwCreateWindow(this.width, this.height, this.title, monitor, share);
        glfwMakeContextCurrent(this.window);
        GL.createCapabilities();
        glClearColor(0, 0, 0, 1);
        this.windowSizeCallbacks = new ArrayList<>();
        glfwSetWindowSizeCallback(this.window, (window, width1, height1) -> {
            Window.this.width = width1;
            Window.this.height = height1;

            for(GLFWWindowSizeCallbackI windowSizeCallback : this.windowSizeCallbacks) {
                windowSizeCallback.invoke(window, width1, height1);
            }
        });

        this.keyCallbacks = new ArrayList<>();
        glfwSetKeyCallback(this.window, (window, key, scancode, action, mods) -> {
            for(GLFWKeyCallbackI keyCallback : this.keyCallbacks) {
                keyCallback.invoke(window, key, scancode, action, mods);
            }
        });
    }

    public void setTitle(String title) {
        glfwSetWindowTitle(this.window, title);
        this.title = title;
    }

    public void addWindowSizeCallback(GLFWWindowSizeCallbackI windowSizeCallback) {
        this.windowSizeCallbacks.add(windowSizeCallback);
    }

    public void addKeyCallback(GLFWKeyCallbackI keyCallback) {
        this.keyCallbacks.add(keyCallback);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(this.window);
    }

    public void dispose() {
        glfwDestroyWindow(this.window);
        glfwTerminate();
    }

    public void clear(int mask) {
        glClear(mask);
    }

    public void update() {
        glfwSwapBuffers(this.window);
        glfwPollEvents();
    }

    public void setExitOnClose(boolean flag) {
        if(flag) {
            glfwSetWindowCloseCallback(this.window, l -> {
                glfwDestroyWindow(l);
                glfwTerminate();
                System.exit(0);
            });

            return;
        }

        glfwSetWindowCloseCallback(this.window, l -> {

        });
    }

    public void resize(int width, int height) {
        glfwSetWindowSize(this.window, width, height);
    }
}
