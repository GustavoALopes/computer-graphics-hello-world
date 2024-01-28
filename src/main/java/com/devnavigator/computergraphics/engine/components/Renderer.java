package com.devnavigator.computergraphics.engine.components;

import com.devnavigator.computergraphics.components.Light;
import com.devnavigator.computergraphics.components.base.GraphicModel;
import com.devnavigator.computergraphics.engine.components.renderer.ProgramShader;
import com.devnavigator.computergraphics.engine.components.renderer.Shader;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;

import java.util.Collection;

public class Renderer {

    private final Projection projection;

    private final Camera camera;

    private ProgramShader program;


    public Renderer(
            final float displayWidth,
            final float displayHeight,
            final KeyboardListener keyboardListener
    ) {
        GL.createCapabilities();
//        this.buffer = BufferUtils.createFloatBuffer(4096);
        this.projection = Projection.create(
                displayWidth,
                displayHeight
        );

        this.camera = new Camera(keyboardListener);
    }

    public void init() {
        this.enableCullFace(GL33.GL_BACK);
        this.projection.init(this.program);
    }

    public void attachProgramShader(final ProgramShader programShader) {
        this.program = programShader;
        this.program.link();
        this.program.use();
    }

    private void enableCullFace(final int mode) {
        GL33.glEnable(GL33.GL_CULL_FACE);
        GL33.glCullFace(mode);
    }

    public void render(
            final Collection<GraphicModel> models,
            final Collection<Light> lights
    ) {
        final var light = lights.stream().findFirst().orElse(null);
        models.forEach(entity -> {
            this.render(entity, light);
        });
        this.cleanup();
    }

    public void render(
        final GraphicModel model,
        final Light light
    ) {
        this.camera.update(this.program);

        GL33.glBindVertexArray(model.getModel().getVaoId());


//        model.increasePosition(0f, 0f, -0.01f);
//        model.increaseRotation(0f, 10f, 0);
//        model.rotate(
//            (float)Math.toRadians(
//                    GLFW.glfwGetTime() * 360.0
//            )
//        );

        final var location = this.program.getUniformLocation("transformationMatrix");
        final var lightPosition = this.program.getUniformLocation("lightPosition");
        final var lightColor = this.program.getUniformLocation("lightColor");
        final var shineDamper = this.program.getUniformLocation("shineDamper");
        final var reflectivity = this.program.getUniformLocation("reflectivity");

        this.program.updateUniformValue(
                location,
                model.render()
        );


        this.program.updateUniformValue(
                lightPosition,
                light.getPosition()
        );

        this.program.updateUniformValue(
                lightColor,
                light.getColor()
        );

        this.program.updateUniformValue(
                shineDamper,
                model.getShineDamper()
        );

        this.program.updateUniformValue(
                reflectivity,
                model.getReflectivity()
        );

        this.flush(model.getNumVertex());
    }

    public void flush(final int numVertex) {
        GL33.glDrawElements(
                GL33.GL_TRIANGLES,
                numVertex,
                GL33.GL_UNSIGNED_INT,
                0
        );
    }

    private void cleanup() {
//        this.program.disableVertexAttribArray(0);
        GL33.glBindVertexArray(0);
    }


    public void dispose() {
        this.program.dispose();
    }
}
