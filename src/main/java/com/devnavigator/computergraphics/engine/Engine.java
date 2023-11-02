package com.devnavigator.computergraphics.engine;

import com.devnavigator.computergraphics.components.Point;
import com.devnavigator.computergraphics.components.Square;
import com.devnavigator.computergraphics.components.Triangle;
import com.devnavigator.computergraphics.components.base.BaseGraphicModel;
import com.devnavigator.computergraphics.engine.components.Renderer;
import com.devnavigator.computergraphics.engine.components.Window;
import com.devnavigator.computergraphics.engine.components.interfaces.IGraphicModel;
import com.devnavigator.computergraphics.engine.components.math.Matrix4f;
import com.devnavigator.computergraphics.engine.interfaces.IEngine;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

public class Engine implements IEngine {

    private Window window;

    private final Renderer renderer;

    private BaseGraphicModel model;

    private boolean isRunning;

    public Engine() {
        this.renderer = new Renderer();
    }

    public void start() {
        this.init();
        this.loop();
        this.dispose();
    }

    public void stop() {
        this.isRunning = false;
    }

    private void dispose() {
        this.window.dispose();
        this.renderer.dispose();
    }

    private void init() {
        this.window = new Window(
                1024,
                740,
                "Hello world"
        );

        this.renderer.init();

        this.model = Triangle.create(
                Point.create(0.0f, 0.5f),
                Point.create(-0.5f, -0.5f),
                Point.create(0.5f, -0.5f)
        )
        .changeColor(
                1,
                255,
                0,
                0
        );

//        this.model = Square.create(
//                Point.create(-0.5f, 0.5f),
//                Point.create(-0.5f, -0.5f),
//                Point.create(0.5f, -0.5f),
//                Point.create(0.5f, 0.5f)
//        );

        this.isRunning = true;
    }

    private void loop() {
        final var startTime = GLFW.glfwGetTime();
        while(this.isRunning) {

            if(this.window.shouldClose()) {
                this.isRunning = false;
            }

            GL33.glClear(GL33.GL_COLOR_BUFFER_BIT);

            this.renderer.render(
                this.model
            );

            this.window.update();
        }
    }
}
