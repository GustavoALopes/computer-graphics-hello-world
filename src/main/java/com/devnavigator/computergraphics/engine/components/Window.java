package com.devnavigator.computergraphics.engine.components;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

public class Window {

    private final long id;

    public Window(
            final int width,
            final int height,
            final String title
    ) {
        GLFW.glfwInit();
        this.id = GLFW.glfwCreateWindow(
                width,
                height,
                title,
                MemoryUtil.NULL,
                MemoryUtil.NULL
        );

        GLFW.glfwMakeContextCurrent(this.id);
        GLFW.glfwSwapInterval(1);

        GLFW.glfwShowWindow(this.id);
    }

    public void update() {
        GLFW.glfwSwapBuffers(this.id);
        GLFW.glfwPollEvents();
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(this.id);
    }

    public void dispose() {
        GLFW.glfwDestroyWindow(this.id);
    }
}
