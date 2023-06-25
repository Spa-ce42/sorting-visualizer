package com.github.spa_ce42.sorting_visualizer;

public class Highlighter {
    private final VArray va;
    private final int[] indices;
    private int pointer;

    public Highlighter(VArray va, int bufferSize) {
        this.va = va;
        this.indices = new int[bufferSize];
        this.pointer = 0;
    }

    private void incrementPointer() {
        ++this.pointer;

        if(this.pointer >= this.indices.length) {
            this.pointer = 0;
        }
    }

    public void highlight(int index, float r, float g, float b) {
        this.va.setColor(this.indices[this.pointer], 1, 1, 1);
        this.va.setColor(index, r, g, b);
        this.indices[this.pointer] = index;
        this.incrementPointer();
    }

    public int get(int index) {
        this.highlight(index, 1, 0, 0);
        return this.va.get(index);
    }

    public void set(int index, int i) {
        this.highlight(index, 1, 0, 0);
        this.va.set(index, i);
    }

    public void swap(int i, int j) {
        this.highlight(i, 1, 0, 0);
        this.highlight(j, 1, 0, 0);
        this.va.swap(i, j);
    }

    public void clear() {
        for(int i = 0; i < this.indices.length; ++i) {
            this.va.setColor(this.indices[i], 1, 1, 1);
            this.indices[i] = 0;
        }
    }

    public VArray getVArray() {
        return this.va;
    }
}
