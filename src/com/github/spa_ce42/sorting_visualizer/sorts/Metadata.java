package com.github.spa_ce42.sorting_visualizer.sorts;

public class Metadata {
    public static final long SLEEP_NANOS = 30000;

    public static void sleep() {
        long expectedTime = System.nanoTime() + SLEEP_NANOS;
        while(true) {
            if(System.nanoTime() > expectedTime) {
                break;
            }
        }
    }
}
