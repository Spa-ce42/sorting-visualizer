package com.github.spa_ce42.sorting_visualizer.internal;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static com.github.spa_ce42.sorting_visualizer.internal.VertexArray.VertexAttribute.FLOAT2;
import static com.github.spa_ce42.sorting_visualizer.internal.VertexArray.VertexAttribute.FLOAT3;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

public class Renderer {
    private static VertexArray framebufferVertexArray;
    private static Shader framebufferShader;
    private static FrameBuffer fb;
    private static VertexArray colorVertexArray;
    private static int colorVertexBuffer;
    private static FloatBuffer f;
    private static Shader colorShader;
    private static int position;
    private static OrthographicCamera oc;
    private static int vertices;

    private Renderer() {
        throw new AssertionError();
    }

    public static void initialize(int maxQuads) {
        //Setup rendering to Window
        {
            FrameBuffer.Specification s = new FrameBuffer.Specification();
            s.width = 1280;
            s.height = 720;
            fb = new FrameBuffer(s);

            framebufferVertexArray = new VertexArray();
            int framebufferVertexBuffer = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, framebufferVertexBuffer);

            float[] f = {
                    -1.0f, -1.0f,  0.0f,  0f, 0f,
                    1.0f, -1.0f,  0.0f,  1f, 0f,
                    1.0f,  1.0f,  0.0f,  1f, 1f,
                    -1.0f,  1.0f,  0.0f,  0f, 1f
            };

            glBufferData(GL_ARRAY_BUFFER, f, GL_STATIC_DRAW);
            framebufferVertexArray.setVertexAttributes(FLOAT3, FLOAT2);

            int ibo = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
            int[] i = {
                    0, 1, 2,
                    2, 3, 0
            };

            glBufferData(GL_ELEMENT_ARRAY_BUFFER, i, GL_STATIC_DRAW);

            //language = GLSL
            framebufferShader = new Shader(
                    """
                    #version 330 core
                    
                    layout(location = 0) in vec3 position;
                    layout(location = 1) in vec2 texCoord;
                    
                    out vec2 s_TexCoord;
                    
                    void main() {
                        gl_Position = vec4(position, 1);
                        s_TexCoord = texCoord;
                    }
                    """,
                    """
                    #version 330 core
                    
                    out vec4 color;
                    in vec2 s_TexCoord;
                    uniform sampler2D u_Texture;
                    
                    void main() {
                        color = texture(u_Texture, s_TexCoord);
                    }
                    """
            );

            framebufferVertexArray.unbind();
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
            framebufferShader.unbind();
        }

        {
            colorVertexArray = new VertexArray();

            colorVertexBuffer = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, colorVertexBuffer);

            f = MemoryUtil.memAllocFloat(5 * 4 * maxQuads);

            glBufferData(GL_ARRAY_BUFFER, (long)f.capacity() * Float.BYTES, GL_DYNAMIC_DRAW);
            colorVertexArray.setVertexAttributes(FLOAT2, FLOAT3);

            int ibo = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);

            int[] indices = new int[6 * maxQuads];
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

            //language = GLSL
            colorShader = new Shader(
                    """
                    #version 330 core
                    
                    layout(location = 0) in vec2 position;
                    layout(location = 1) in vec3 color;
                    
                    uniform mat4 viewProjection;
                    
                    out vec4 s_Color;
                    
                    void main() {
                        gl_Position = viewProjection * vec4(position, 0, 1);
                        s_Color = vec4(color, 1);
                    }
                    """,
                    """
                    #version 330 core
                    
                    in vec4 s_Color;
                    out vec4 color;
                    
                    void main() {
                        color = s_Color;
                    }
                    """
            );

            oc = new OrthographicCamera(0, 1, 0, 1);
            colorShader.bind();
            colorShader.setMatrix4f("viewProjection", oc.getViewProjectionMatrix());
        }
    }

    public static void drawQuad(float bottomLeftX, float bottomLeftY, float topRightX, float topRightY, float r, float g, float b) {
        if(position >= f.capacity()) {
            push();
        }

        vertices = vertices + 6;

        f.put(position++, bottomLeftX);
        f.put(position++, bottomLeftY);
        f.put(position++, r);
        f.put(position++, g);
        f.put(position++, b);

        f.put(position++, topRightX);
        f.put(position++, bottomLeftY);
        f.put(position++, r);
        f.put(position++, g);
        f.put(position++, b);

        f.put(position++, topRightX);
        f.put(position++, topRightY);
        f.put(position++, r);
        f.put(position++, g);
        f.put(position++, b);

        f.put(position++, bottomLeftX);
        f.put(position++, topRightY);
        f.put(position++, r);
        f.put(position++, g);
        f.put(position++, b);
    }

    public static void push() {
        colorVertexArray.bind();
        glBindBuffer(GL_ARRAY_BUFFER, colorVertexBuffer);
        glBufferSubData(GL_ARRAY_BUFFER, 0, f);

        fb.bind();
        colorShader.bind();
        colorShader.setMatrix4f("viewProjection", oc.getViewProjectionMatrix());
        colorVertexArray.bind();
        glDrawElements(GL_TRIANGLES, vertices, GL_UNSIGNED_INT, 0);
        fb.unbind();
        position = 0;
        System.out.println(vertices / 4);
        vertices = 0;
    }

    public static void render() {
        fb.unbind();
        framebufferShader.bind();
        int ca = fb.getColorAttachment();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, ca);
        framebufferVertexArray.bind();
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
    }

    public static void resize(int width, int height) {
        fb.resize(width, height);
    }

    public static void invalidate() {
        fb.invalidate();
    }
}
