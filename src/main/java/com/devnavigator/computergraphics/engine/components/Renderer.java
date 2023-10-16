package com.devnavigator.computergraphics.engine.components;

import com.devnavigator.computergraphics.engine.components.interfaces.IGraphicModel;
import com.devnavigator.computergraphics.engine.components.renderer.ProgramShader;
import com.devnavigator.computergraphics.engine.components.renderer.Shader;
import com.devnavigator.computergraphics.engine.components.renderer.VertexArrayObject;
import com.devnavigator.computergraphics.engine.components.renderer.VertexBufferObject;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;

import java.nio.FloatBuffer;

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
//
//        this.vao = new VertexArrayObject();
//        this.vao.bind();
//
//        this.vbo = new VertexBufferObject();
//        this.vbo.bind();

//        this.vbo.updateData(
//                GL33.GL_ARRAY_BUFFER,
//                vertices,
//                GL33.GL_STATIC_DRAW
//        );

//        this.program.addVertexAttrib(
//                0,
//                2,
//                0,
//                0L
//        );
//
//        this.program.enableVertexAttribArray(0);
    }

    public void render(final IGraphicModel model) {
        GL33.glBindVertexArray(model.getModel().getVaoId());
//        this.numVertex += model.getNumVertex();
//
//        this.program.addVertexAttrib(
//                0,
//                2,
//                0,
//                0L
//        );
//
        this.program.enableVertexAttribArray(0);

        this.flush(model.getNumVertex());

        this.cleanup();
    }

    public void flush(final int numVertex) {

        GL33.glDrawElements(
                GL33.GL_TRIANGLES,
                numVertex,
                GL33.GL_UNSIGNED_INT,
                0
        );

//        GL33.glDrawArrays(
//                GL33.GL_TRIANGLES,
//                0,
//                numVertex
//        );

    }

    private void cleanup() {
        this.program.disableVertexAttribArray(0);
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
