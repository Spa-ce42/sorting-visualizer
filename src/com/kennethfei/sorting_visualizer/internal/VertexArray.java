package com.kennethfei.sorting_visualizer.internal;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class VertexArray {
    private final int vao;

    public VertexArray() {
        this.vao = glGenVertexArrays();
        glBindVertexArray(this.vao);
    }

    public void setVertexAttributes(VertexArray.VertexAttribute... vas) {
        glBindVertexArray(this.vao);
        int size = 0;

        for(int i = 0; i < vas.length; ++i) {
            glEnableVertexAttribArray(i);
            VertexArray.VertexAttribute va = vas[i];
            size = size + va.getTypeSize() * va.size;
        }

        int stride = 0;

        for(int i = 0; i < vas.length; ++i) {
            VertexArray.VertexAttribute va = vas[i];
            glVertexAttribPointer(i, va.size, va.type, va.normalized, size, stride);
            stride = stride + va.getTypeSize() * va.size;
        }
    }

    public void bind() {
        glBindVertexArray(this.vao);
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    public void dispose() {
        glDeleteVertexArrays(this.vao);
    }

    public record VertexAttribute(int size, int type, boolean normalized) {
        public static final VertexArray.VertexAttribute FLOAT2 = new VertexArray.VertexAttribute(2, GL_FLOAT, false);
        public static final VertexArray.VertexAttribute FLOAT3 = new VertexArray.VertexAttribute(3, GL_FLOAT, false);
        public static final VertexArray.VertexAttribute FLOAT4 = new VertexArray.VertexAttribute(4, GL_FLOAT, false);

        public int getTypeSize() {
            return Float.BYTES;
        }
    }
}
