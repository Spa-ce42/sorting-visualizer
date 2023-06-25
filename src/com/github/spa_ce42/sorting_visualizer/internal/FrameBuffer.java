package com.github.spa_ce42.sorting_visualizer.internal;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_DEPTH24_STENCIL8;
import static org.lwjgl.opengl.GL30.GL_DEPTH_STENCIL;
import static org.lwjgl.opengl.GL30.GL_DEPTH_STENCIL_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.GL_UNSIGNED_INT_24_8;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.system.MemoryUtil.NULL;

public class FrameBuffer {
    private final Specification s;
    private int id;
    private int colorAttachment;
    private int depthAttachment;

    public FrameBuffer(Specification s) {
        this.s = new Specification(s);
        this.invalidate();
    }

    public void invalidate() {
        this.id = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, this.id);

        this.colorAttachment = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, this.colorAttachment);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, this.s.width, this.s.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, NULL);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.colorAttachment, 0);

        this.depthAttachment = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, this.depthAttachment);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH24_STENCIL8, this.s.width, this.s.height, 0, GL_DEPTH_STENCIL, GL_UNSIGNED_INT_24_8, NULL);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_TEXTURE_2D, this.depthAttachment, 0);

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw new IllegalStateException("Framebuffer is incomplete");
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void resize(int width, int height) {
        this.s.width = width;
        this.s.height = height;
        this.invalidate();
    }

    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, this.id);
    }

    public void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void dispose() {
        glDeleteFramebuffers(this.id);
        glDeleteTextures(this.colorAttachment);
        glDeleteTextures(this.depthAttachment);
    }

    public int getColorAttachment() {
        return this.colorAttachment;
    }

    public static class Specification {
        public int width;
        public int height;
        public int samples = 1;
        public boolean swapChainTarget = false;

        public Specification(Specification s) {
            this.width = s.width;
            this.height = s.height;
            this.samples = s.samples;
            this.swapChainTarget = s.swapChainTarget;
        }

        public Specification() {

        }
    }
}
