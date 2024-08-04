package com.devnavigator.computergraphics.engine.components;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
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

        MouseListener.bindWindow(this);

        GLFW.glfwShowWindow(this.id);
    }

    public void update() {
        MouseListener.reset();
        GLFW.glfwSwapBuffers(this.id);
        GLFW.glfwPollEvents();
        Clock.update();
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(this.id);
    }

    public void dispose() {
        GLFW.glfwDestroyWindow(this.id);
    }

    public void addMouseCallbackListener(
            final GLFWMouseButtonCallbackI buttonsCallback,
            final GLFWScrollCallbackI scrollCallback,
            final GLFWCursorPosCallbackI cursorPosCallback
    ) {
        GLFW.glfwSetScrollCallback(
                this.id,
                scrollCallback
        );

        GLFW.glfwSetCursorPosCallback(
                this.id,
                cursorPosCallback
        );

        GLFW.glfwSetMouseButtonCallback(
                this.id,
                buttonsCallback
        );
    }

    public void addKeyboardCallbackListener(final GLFWKeyCallbackI callback) {
        GLFW.glfwSetKeyCallback(
                this.id,
                callback
        );
    }
}
