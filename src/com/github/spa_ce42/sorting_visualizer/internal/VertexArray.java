package com.github.spa_ce42.sorting_visualizer.internal;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class VertexArray {
    private final int vao;

    public VertexArray() {
        this.vao = glGenVertexArrays();
    }

    public void setVertexAttributes(VertexAttribute... vas) {
        glBindVertexArray(this.vao);
        int size = 0;

        for(int i = 0; i < vas.length; ++i) {
            glEnableVertexAttribArray(i);
            VertexAttribute va = vas[i];
            size = size + va.getTypeSize() * va.size;
        }

        int stride = 0;

        for(int i = 0; i < vas.length; ++i) {
            VertexAttribute va = vas[i];
            glVertexAttribPointer(i, va.size, va.type, va.normalized, size, stride);
            stride = stride + va.getTypeSize() * va.size;
        }
    }

    public void bind() {
        glBindVertexArray(this.vao);
    }

    public record VertexAttribute(int size, int type, boolean normalized) {
        public static final VertexAttribute FLOAT2 = new VertexAttribute(2, GL_FLOAT, false);
        public static final VertexAttribute FLOAT3 = new VertexAttribute(3, GL_FLOAT, false);
        public static final VertexAttribute FLOAT4 = new VertexAttribute(4, GL_FLOAT, false);

        public int getTypeSize() {
            return Float.BYTES;
        }
    }
}
