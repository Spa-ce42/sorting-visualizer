package com.kennethfei.sorting_visualizer.internal;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.system.MemoryUtil.memFree;

public class HistogramRenderer {
    private static VertexArray histogramVertexArray;
    private static int colorVertexBuffer;
    private static FloatBuffer colorBuffer;
    private static int heightVertexBuffer;
    private static FloatBuffer heightBuffer;
    private static int indexBuffer;
    private static Shader histogramShader;
    private static int count;
    private static OrthographicCamera oc;

    private static boolean initialized;

    public static void initialize(int count) {
        if(initialized) {
            histogramVertexArray.dispose();
            glDeleteBuffers(colorVertexBuffer);
            memFree(colorBuffer);
            glDeleteBuffers(heightVertexBuffer);
            memFree(heightBuffer);
            glDeleteBuffers(indexBuffer);
            histogramShader.dispose();
        }

        HistogramRenderer.count = count;
        oc = new OrthographicCamera(0, 1, 0, 1.05f);

        histogramVertexArray = new VertexArray();

        colorVertexBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, colorVertexBuffer);

        colorBuffer = MemoryUtil.memAllocFloat(3 * count);

        for(int i = 0; i < 3 * count; ++i) {
            colorBuffer.put(i, 1);
        }

        glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glVertexAttribDivisor(0, 1);

        heightVertexBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, heightVertexBuffer);

        heightBuffer = memAllocFloat(count);

        float g = 1f / count;
        for(int i = 0; i < count; i = i + 1) {
            heightBuffer.put(i, (i + 1) * g);
        }

        glBufferData(GL_ARRAY_BUFFER, heightBuffer, GL_STATIC_DRAW);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 1, GL_FLOAT, false, Float.BYTES, 0);
        glVertexAttribDivisor(1, 1);

        indexBuffer = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);

        glBufferData(GL_ELEMENT_ARRAY_BUFFER, new int[] {
                0, 1, 2,
                2, 3, 0
        }, GL_STATIC_DRAW);

        histogramShader = new Shader(
                """
                #version 330 core
                
                layout(location = 0) in vec3 color;
                layout(location = 1) in float height;
                
                uniform mat4 viewProjectionMatrix;
                uniform int count;
                
                out vec3 s_Color;
                
                void main() {
                    float x, h, a;
                    a = 1 / float(count);
                    
                    switch(gl_VertexID) {
                        case 0:
                            x = 0;
                            h = 0;
                            break;
                        case 1:
                            x = a;
                            h = 0;
                            break;
                        case 2:
                            x = a;
                            h = height;
                            break;
                        default:
                            x = 0;
                            h = height;
                            break;
                    }
                    
                    gl_Position = viewProjectionMatrix * vec4(x + gl_InstanceID * a, h, 0, 1);
                    s_Color = color;
                }
                """,
                """
                #version 330 core
                
                in vec3 s_Color;
                out vec4 color;
                
                void main() {
                    color = vec4(s_Color, 1);
                }
                """
        );

        histogramShader.bind();
        histogramShader.setMatrix4f("viewProjectionMatrix", oc.getViewProjectionMatrix());
        histogramShader.setInteger("count", count);
        initialized = true;
    }

    public static void setBound(float left, float right, float bottom, float top) {
        oc.setProjection(left, right, bottom, top);
        histogramShader.bind();
        histogramShader.setMatrix4f("viewProjectionMatrix", oc.getViewProjectionMatrix());
    }

    public static void setColor(int index, float r, float g, float b) {
        colorBuffer.put(index * 3 + 0, r);
        colorBuffer.put(index * 3 + 1, g);
        colorBuffer.put(index * 3 + 2, b);
    }

    public static void clearColor(float r, float g, float b) {
        for(int i = 0; i < 3 * count; i = i + 3) {
            colorBuffer.put(i, r);
            colorBuffer.put(i + 1, g);
            colorBuffer.put(i + 2, b);
        }
    }

    public static void setHeight(int index, float h) {
        heightBuffer.put(index, h);
    }

    public static void render() {
        histogramShader.bind();
        histogramVertexArray.bind();

        glBindBuffer(GL_ARRAY_BUFFER, colorVertexBuffer);
        glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, heightVertexBuffer);
        glBufferData(GL_ARRAY_BUFFER, heightBuffer, GL_STATIC_DRAW);

        glDrawElementsInstanced(GL_TRIANGLES, 6, GL_UNSIGNED_INT, NULL, count);
    }
}
