package com.devnavigator.computergraphics.engine.components;

import org.lwjgl.glfw.GLFW;

public final class Clock {

    private static final float FPS = 120f;

    private static float lastFrameTime = (float) GLFW.glfwGetTime();

    private static float delta;

    public static void update() {
        final var time = (float) GLFW.glfwGetTime();
        delta = (float) ((GLFW.glfwGetTime() - lastFrameTime)/FPS);
        lastFrameTime = time;
    }

    public static float getTimeFrameSecond() {
        return delta;
    }


}
