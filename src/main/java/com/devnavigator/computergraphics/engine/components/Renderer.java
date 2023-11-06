package com.devnavigator.computergraphics.engine.components;

import com.devnavigator.computergraphics.components.base.BaseGraphicModel;
import com.devnavigator.computergraphics.engine.components.renderer.ProgramShader;
import com.devnavigator.computergraphics.engine.components.renderer.Shader;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;

import java.nio.FloatBuffer;
import java.util.Collection;

public class Renderer {

//    private VertexArrayObject vao;
//
//    private VertexBufferObject vbo;

    private FloatBuffer buffer;

    private int numVertex;

    private ProgramShader program;


    private static final float[] vertices = {
            //Top triangles
            -0.5f, -0.5f,
            -0.5f, 0.5f,
            0.5f, 0.5f,
            //Bottom triangles
            0.5f, 0.5f,
            0.5f, -0.5f,
            -0.5f, -0.5f
    };

    public Renderer() {
        this.numVertex = 0;
        this.buffer = BufferUtils.createFloatBuffer(4096);
    }

    public void init() {
        GL.createCapabilities();

        this.program = compileAndLinkShaders();
        this.program.use();
    }

    public void render(final Collection<BaseGraphicModel> models) {
        models.forEach(this::render);
        this.cleanup();
    }

    public void render(
        final BaseGraphicModel model
    ) {
        GL33.glBindVertexArray(model.getModel().getVaoId());

        this.program.use();

        model.incrementPosition(0.001f, 0f, 0f);
//        model.rotate(
//            (float)Math.toRadians(
//                    GLFW.glfwGetTime() * 360.0
//            )
//        );

        final var location = this.program.getUniformLocation("Matrix");

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
