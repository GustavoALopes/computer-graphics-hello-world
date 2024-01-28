package com.devnavigator.computergraphics.engine.components.renderer;

import org.lwjgl.opengl.GL33;

import java.io.*;

public class Shader {

    private final int id;

    public Shader(final int type) {
        this.id = GL33.glCreateShader(type);
    }

    public int getId() {
        return this.id;
    }

    private void source(final CharSequence source) {
        GL33.glShaderSource(
                this.id,
                source
        );
    }

    private void compile() {
        GL33.glCompileShader(this.id);

        this.checkStatus();
    }

    private void checkStatus() {
        final var status = GL33.glGetShaderi(
            this.id,
            GL33.GL_COMPILE_STATUS
        );

        if(status != GL33.GL_TRUE) {
            throw new RuntimeException(GL33.glGetShaderInfoLog(this.id));
        }
    }

    public static Shader loadShader(
            final Type type,
            final String path
    ) {
        final var script = new StringBuffer(100);
        try(final var in = new FileInputStream(path)) {
            final var reader = new BufferedReader(
                    new InputStreamReader(in)
            );

            String line;
            while ((line = reader.readLine()) != null) {
                script.append(line).append("\n");
            }
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final var source = script.toString();

        return createShader(type, source);
    }

    private static Shader createShader(
        final Type type,
        final CharSequence source
    ) {
        final var shader = new Shader(type.getValue());
        shader.source(source);
        shader.compile();

        return shader;
    }

    public void delete() {
        GL33.glDeleteShader(this.id);
    }

    public enum Type {
        VERTEX(GL33.GL_VERTEX_SHADER),
        FRAGMENT(GL33.GL_FRAGMENT_SHADER);

        private int value;

        Type(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
