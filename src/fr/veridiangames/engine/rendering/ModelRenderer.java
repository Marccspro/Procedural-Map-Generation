/*
 *  Copyright (C) 2016 Marccspro
 *
 *     This file is part of "Procedural Map Generation".
 *
 *         "Procedural Map Generation" is free software: you can redistribute it and/or modify
 *         it under the terms of the GNU General Public License as published by
 *         the Free Software Foundation, either version 3 of the License, or
 *         (at your option) any later version.
 *
 *         "Procedural Map Generation" is distributed in the hope that it will be useful,
 *         but WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *         GNU General Public License for more details.
 *
 *         You should have received a copy of the GNU General Public License
 *         along with "Procedural Map Generation".  If not, see http://www.gnu.org/licenses/.
 */

package fr.veridiangames.engine.rendering;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static javafx.scene.input.KeyCode.L;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Created by Marc on 27/05/2016.
 */
public class ModelRenderer
{
    public static final ModelRenderer[] TREE_RENDERER = new ModelRenderer[]
     {
            new ModelRenderer(new Model("res/models/tree_LOD1.obj")),
            new ModelRenderer(new Model("res/models/tree_LOD2.obj")),
            new ModelRenderer(new Model("res/models/tree_LOD3.obj")),
            new ModelRenderer(new Model("res/models/tree_LOD4.obj")),
            new ModelRenderer(new Model("res/models/tree_LOD5.obj")),
            new ModelRenderer(new Model("res/models/tree_LOD6.obj")),
            new ModelRenderer(new Model("res/models/tree_LOD7.obj"))
    };

    private int vao, vbo, ibo;
    private FloatBuffer verticesBuffer;
    private IntBuffer indicesBuffer;

    private Model model;

    public ModelRenderer(Model model)
    {
        this.model = model;

        createBufferData();
        createVertexArray();
    }

    private void createBufferData()
    {
        verticesBuffer = BufferUtils.createFloatBuffer(model.getVerticesArray().length);
        verticesBuffer.put(model.getVerticesArray());
        verticesBuffer.flip();

        indicesBuffer = BufferUtils.createIntBuffer(model.getIndicesArray().length);
        indicesBuffer.put(model.getIndicesArray());
        indicesBuffer.flip();
    }

    private void createVertexArray()
    {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        ibo = glGenBuffers();

        glBindVertexArray(vao);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0L);
        glVertexAttribPointer(1, 3, GL_FLOAT, true, Vertex.SIZE * 4, 12L);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, Vertex.SIZE * 4, 24L);
        glVertexAttribPointer(3, 3, GL_FLOAT, false, Vertex.SIZE * 4, 32L);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        glBindVertexArray(0);
    }

    public void render()
    {
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, model.getIndicesArray().length, GL_UNSIGNED_INT, 0L);
        glBindVertexArray(0);
    }
}
