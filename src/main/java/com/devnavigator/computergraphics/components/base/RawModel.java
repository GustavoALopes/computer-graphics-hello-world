package com.devnavigator.computergraphics.components.base;

import com.devnavigator.computergraphics.engine.components.IndexBuffer;
import com.devnavigator.computergraphics.engine.components.renderer.VertexArrayObject;
import com.devnavigator.computergraphics.engine.components.renderer.VertexBufferObject;

import java.util.Map;

public class RawModel {

    private final VertexArrayObject vao;

    private final Map<String, VertexBufferObject> vbos;

    private final IndexBuffer indexes;

    private final int numbersOfVertex;

    private RawModel(
            final VertexArrayObject vao,
            final Map<String, VertexBufferObject> vbos,
            final IndexBuffer index,
            final int numbersOfVertex
    ) {
        this.vao = vao;
        this.vbos = vbos;
        this.indexes = index;
        this.numbersOfVertex = numbersOfVertex;
    }

    public VertexArrayObject getVao() {
        return this.vao;
    }

    public int getNumbersOfVertex() {
        return this.numbersOfVertex;
    }

    public static RawModel create(
            final VertexArrayObject vao,
            final Map<String,VertexBufferObject> vbos,
            final IndexBuffer index,
            final int vertexCount
    ) {
        return new RawModel(
                vao,
                vbos,
                index,
                vertexCount
        );
    }
}
