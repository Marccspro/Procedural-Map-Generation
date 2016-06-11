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

package fr.veridiangames.main.rendering;

import fr.veridiangames.engine.rendering.Cubemap;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Created by Marc on 28/05/2016.
 */
public class SkyboxRenderer
{
    private int vao, vbo;
    private FloatBuffer verticesBuffer;

    private Cubemap cubemap;
    private float size;

    public SkyboxRenderer(Cubemap cubemap, float size)
    {
        this.cubemap = cubemap;
        this.size = size;
        createBufferData();
        createVertexArray();
    }

    private void createBufferData()
    {
        verticesBuffer = BufferUtils.createFloatBuffer(cubeVerticesData(size).length);
        verticesBuffer.put(cubeVerticesData(size));
        verticesBuffer.flip();
    }

    private void createVertexArray()
    {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();

        glBindVertexArray(vao);

        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0L);

        glBindVertexArray(0);
    }

    public void render()
    {
        cubemap.bind();
        glBindVertexArray(vao);
        glDrawArrays(GL_QUADS, 0, 4 * 6);
        glBindVertexArray(0);
        cubemap.unbind();
    }

    private float[] cubeVerticesData(float size)
    {
        return new float[]
        {
            +size, -size, -size, -size, -size, -size, -size, +size, -size, +size, +size, -size,
            -size, -size, +size, +size, -size, +size, +size, +size, +size, -size, +size, +size,
            -size, -size, -size, +size, -size, -size, +size, -size, +size, -size, -size, +size,
            +size, +size, -size, -size, +size, -size, -size, +size, +size, +size, +size, +size,
            -size, +size, -size, -size, -size, -size, -size, -size, +size, -size, +size, +size,
            +size, -size, -size, +size, +size, -size, +size, +size, +size, +size, -size, +size
        };
    }
}
