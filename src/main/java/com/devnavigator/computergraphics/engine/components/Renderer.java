package com.devnavigator.computergraphics.engine.components;

import com.devnavigator.computergraphics.components.base.BaseGraphicModel;
import com.devnavigator.computergraphics.engine.components.interfaces.IGraphicModel;
import com.devnavigator.computergraphics.engine.components.math.Matrix4f;
import com.devnavigator.computergraphics.engine.components.renderer.ProgramShader;
import com.devnavigator.computergraphics.engine.components.renderer.Shader;
import com.devnavigator.computergraphics.engine.components.renderer.VertexArrayObject;
import com.devnavigator.computergraphics.engine.components.renderer.VertexBufferObject;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.Collection;
import java.util.Collections;

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
        this.numVertex += model.getNumVertex();

        this.program.use();

        final var location = this.program.getUniformLocation("Matrix");
        model.rotate(
                location,
                (float)Math.toRadians(
                        GLFW.glfwGetTime() * 360.0
                )
        );

        try(final var stack = MemoryStack.stackPush()) {
            final var buffer = stack.mallocFloat(4*4);
            model.render().toBuffer(buffer);
            GL33.glUniformMatrix4fv(location, false, buffer);
        }


//        this.program.enableVertexAttribArray(0);

        this.flush();
    }

    public void flush() {
        GL33.glDrawElements(
                GL33.GL_TRIANGLES,
                this.numVertex,
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
