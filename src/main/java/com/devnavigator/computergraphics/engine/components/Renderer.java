package com.devnavigator.computergraphics.engine.components;

import com.devnavigator.computergraphics.components.base.GraphicModel;
import com.devnavigator.computergraphics.engine.components.renderer.ProgramShader;
import com.devnavigator.computergraphics.engine.components.renderer.Shader;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;

import java.util.Collection;

public class Renderer {

    private int numVertex;

    private final Projection projection;

    private final Camera camera;

    private ProgramShader program;


    public Renderer(
            final float displayWidth,
            final float displayHeight,
            final KeyboardListener keyboardListener
    ) {
        this.numVertex = 0;
//        this.buffer = BufferUtils.createFloatBuffer(4096);
        this.projection = Projection.create(
                displayWidth,
                displayHeight
        );

        this.camera = new Camera(keyboardListener);
    }

    public void init(final Window window) {
        GL.createCapabilities();

        this.program = compileAndLinkShaders();
        this.program.use();

        this.projection.init(this.program);

//        window.addCallbackListener(this.camera::move);
    }

    public void render(final Collection<GraphicModel> models) {
        models.forEach(this::render);
        this.cleanup();
    }

    public void render(
        final GraphicModel model
    ) {
        this.program.use();
        this.camera.update(this.program);

        GL33.glBindVertexArray(model.getModel().getVaoId());


//        model.increasePosition(0f, 0f, -0.01f);
        model.increaseRotation(0f, 10f, 0);
//        model.rotate(
//            (float)Math.toRadians(
//                    GLFW.glfwGetTime() * 360.0
//            )
//        );

        final var location = this.program.getUniformLocation("transformationMatrix");

        this.program.updateUniformValue(
                location,
                model.render()
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
        this.numVertex = 0;
        GL33.glBindVertexArray(0);
    }


    public void dispose() {
        this.program.dispose();
    }

    private ProgramShader compileAndLinkShaders() {
        final var vertexShader = Shader.loadShader(
                GL33.GL_VERTEX_SHADER,
                "src/main/resources/shaders/default.vert"
        );

        final var fragmentShader = Shader.loadShader(
                GL33.GL_FRAGMENT_SHADER,
                "src/main/resources/shaders/default.frag"
        );

        final var program = new ProgramShader();
        program.attachShader(vertexShader);
        program.attachShader(fragmentShader);
        program.link();
        program.use();

        vertexShader.delete();
        fragmentShader.delete();

        return program;
    }
}
