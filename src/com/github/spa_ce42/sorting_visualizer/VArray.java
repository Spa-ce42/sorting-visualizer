package com.github.spa_ce42.sorting_visualizer;

import com.github.spa_ce42.sorting_visualizer.internal.OrthographicCamera;
import com.github.spa_ce42.sorting_visualizer.internal.Shader;
import com.github.spa_ce42.sorting_visualizer.internal.VertexArray;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

public class VArray {
    private final VertexArray va;
    private final int vbo;
    private final Shader shader;
    private final float[] vertices;
    private final OrthographicCamera camera;
    private int size;
    private int[] a;
    private float y;

    @SuppressWarnings("PointlessArithmeticExpression")
    public VArray(int size) {
        this.a = new int[size];
        for(int i = 0; i < this.a.length; ++i) {
            this.a[i] = i;
        }

        this.va = new VertexArray();
        this.va.bind();

        this.vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        this.size = size;
        float x = 1f / size;
        this.y = 95f / 100f / size;
        int k = 0;
        this.vertices = new float[4 * 5 * size];
        for(int i = 0; i < size; ++i) {
            this.vertices[k + 0] = i * x;       //0
            this.vertices[k + 1] = (0);           //1
            this.vertices[k + 2] = (1);           //2
            this.vertices[k + 3] = (1);           //3
            this.vertices[k + 4] = (1);           //4

            this.vertices[k + 5] = ((i + 1) * x); //5
            this.vertices[k + 6] = (0);           //6
            this.vertices[k + 7] = (1);           //7
            this.vertices[k + 8] = (1);           //8
            this.vertices[k + 9] = (1);           //9

            this.vertices[k + 10] = ((i + 1) * x); //10
            this.vertices[k + 11] = ((i + 1) * y); //11
            this.vertices[k + 12] = (1);           //12
            this.vertices[k + 13] = (1);           //13
            this.vertices[k + 14] = (1);           //14

            this.vertices[k + 15] = (i * x);       //15
            this.vertices[k + 16] = ((i + 1) * y); //16
            this.vertices[k + 17] = (1);           //17
            this.vertices[k + 18] = (1);           //18
            this.vertices[k + 19] = (1);           //19

            k = k + 20;
        }

        glBufferData(GL_ARRAY_BUFFER, (long)this.vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);
        glBufferSubData(GL_ARRAY_BUFFER, 0, this.vertices);

        va.setVertexAttributes(
                VertexArray.VertexAttribute.FLOAT2,
                VertexArray.VertexAttribute.FLOAT3
        );

        int ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);

        int[] indices = new int[6 * size];
        int j = 0;

        for(int i = 0; i < indices.length; i = i + 6) {
            indices[i + 0] = j + 0;
            indices[i + 1] = j + 1;
            indices[i + 2] = j + 2;
            indices[i + 3] = j + 0;
            indices[i + 4] = j + 2;
            indices[i + 5] = j + 3;
            j = j + 4;
        }

        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        shader = new Shader(
                """
                #version 330 core
                
                layout(location = 0) in vec2 position;
                layout(location = 1) in vec3 color;
                
                uniform mat4 viewProjection;
                
                out vec3 fColor;
                
                void main() {
                    fColor = color;
                    gl_Position = viewProjection * vec4(position, 0, 1);
                }
                """,
                """
                #version 330 core
                
                in vec3 fColor;
                out vec4 color;
                
                void main() {
                    color = vec4(fColor, 1);
                }
                """
        );


        this.camera = new OrthographicCamera(0, 1, 0, 1);
        shader.bind();
        shader.setMatrix4f("viewProjection", this.camera.getViewProjectionMatrix());
    }

    private int getRectangleIndex(int index) {
        return index * 20;
    }

    private int[] indices = {0, 1};

    public void setColor(int index, float r, float g, float b) {
        synchronized(this.vertices) {
            int j = this.getRectangleIndex(index);
            this.vertices[j + 2] = (r);
            this.vertices[j + 3] = (g);
            this.vertices[j + 4] = (b);

            this.vertices[j + 7] = (r);
            this.vertices[j + 8] = (g);
            this.vertices[j + 9] = (b);

            this.vertices[j + 12] = (r);
            this.vertices[j + 13] = (g);
            this.vertices[j + 14] = (b);

            this.vertices[j + 17] = (r);
            this.vertices[j + 18] = (g);
            this.vertices[j + 19] = (b);
        }
    }

    public void set(int index, int i) {
        synchronized(this.vertices) {
            int j = this.getRectangleIndex(index);
            this.vertices[j + 11] = ((i + 1) * this.y);
            this.vertices[j + 16] = ((i + 1) * this.y);
            this.a[index] = i;
        }
    }

    public int get(int index) {
        return this.a[index];
    }

    public void swap(int i, int j) {
        int temp = this.a[i];
        this.a[i] = this.a[j];
        this.set(i, this.a[j]);
        this.a[j] = temp;
        this.set(j, temp);
    }

    public void draw() {
        this.shader.bind();
        this.shader.setMatrix4f("viewProjection", this.camera.getViewProjectionMatrix());
        glBindBuffer(GL_ARRAY_BUFFER, this.vbo);

        synchronized(this.vertices) {
            glBufferSubData(GL_ARRAY_BUFFER, 0, this.vertices);
        }

        this.va.bind();
        glDrawElements(GL_TRIANGLES, 6 * this.size, GL_UNSIGNED_INT, 0);
    }

    public int size() {
        return this.size;
    }
}
