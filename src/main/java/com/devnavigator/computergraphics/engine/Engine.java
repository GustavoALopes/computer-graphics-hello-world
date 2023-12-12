package com.devnavigator.computergraphics.engine;

import com.devnavigator.computergraphics.components.Point;
import com.devnavigator.computergraphics.components.Square;
import com.devnavigator.computergraphics.components.base.BaseGraphicModel;
import com.devnavigator.computergraphics.engine.components.KeyboardListener;
import com.devnavigator.computergraphics.engine.components.Renderer;
import com.devnavigator.computergraphics.engine.components.Window;
import com.devnavigator.computergraphics.engine.interfaces.IEngine;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL33;

import java.util.ArrayList;
import java.util.Collection;

public class Engine implements IEngine {

    private Window window;

    private final KeyboardListener keyboardListener;

    private final Renderer renderer;

    private Collection<BaseGraphicModel> models;

    private boolean isRunning;

    public Engine() {
        this.keyboardListener = new KeyboardListener();
        this.renderer = new Renderer(
                1024,
                740,
                this.keyboardListener
        );

        this.models = new ArrayList<>();
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

        this.keyboardListener.bindWindow(this.window);

        this.renderer.init(this.window);

        this.models.add(Square.create(
                Point.create(-0.5f, 0.5f),
                Point.create(-0.5f, -0.5f),
                Point.create(0.5f, -0.5f),
                Point.create(0.5f, 0.5f)
        )
//         .changeColor(1, 255, 255, 255)
//         .translate(0.5f, 0.5f, 0.f)
         .translate(-0.1f, 0.0f, 0.f)
//         .changeScale(0.35f, 0.35f, 1f)
         .addTexture(
                 "src/main/resources/textures/default.png",
                 new float[] {
                         0,0, //VO,
                         0,1, //V1
                         1,1, //V2
                         1,0
                 }
         )
        );


//        this.models.add(Triangle.create(
//                Point.create(0.0f, 0.5f),
//                Point.create(-0.5f, -0.5f),
//                Point.create(0.5f, -0.5f)
//        )
//        .changeColor(
//                1,
//                255,
//                0,
//                100
//        )
//        .translate(-0.5f, 0.5f, 1f)
//        .changeScale(0.35f, 0.35f, 1f));

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
                this.models
            );

            this.window.update();
        }
    }
}
