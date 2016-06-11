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

import fr.veridiangames.engine.maths.*;
import fr.veridiangames.engine.rendering.Camera;
import fr.veridiangames.engine.rendering.Texture;
import fr.veridiangames.engine.rendering.Vertex;
import fr.veridiangames.main.world_generation.noise.NoiseGeneration;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

/**
 * Created by Marc on 28/05/2016.
 */
public class GrassCeilRenderer
{
    private Random rand;

    private int num;
    private int renderSize;
    private int size;
    private NoiseGeneration gen;

    private int vao, vbo, vio, ibo;
    private FloatBuffer verticesBuffer;
    private FloatBuffer instanceBuffer;
    private IntBuffer indicesBuffer;
    private Vec2 position;
    private Vec2 centerPosition;
    private boolean isInViewDistance = false;

    public GrassCeilRenderer(int xp, int yp, NoiseGeneration gen)
    {
        this.num = 5000;
        this.size = 50;
        this.position = new Vec2(xp * size, yp * size);
        this.centerPosition = new Vec2(position.x + size / 2, position.y + size / 2);
        this.gen = gen;

        createBufferData();
        createVertexArray();
    }

    private void createBufferData()
    {
        verticesBuffer = BufferUtils.createFloatBuffer(quadVerticesData().length);
        verticesBuffer.put(quadVerticesData());
        verticesBuffer.flip();

        instanceBuffer = BufferUtils.createFloatBuffer(num * 16);
        for (int i = 0; i < num; i++)
        {
            renderSize++;
            float x = Mathf.random(0, size) + position.x;
            float z = Mathf.random(0, size) + position.y;
            float y = gen.getNoise((int)x, (int)z);
            Mat4 transform = Mat4.translate(x, y, z).mul(Mat4.rotate(0, Mathf.random(0, 360), 0));
            instanceBuffer.put(transform.getComponents());
        }
        instanceBuffer.flip();

        indicesBuffer = BufferUtils.createIntBuffer(quadIndicesData().length);
        indicesBuffer.put(quadIndicesData());
        indicesBuffer.flip();
    }

    public void update(Camera camera)
    {
        isInViewDistance = true;
        float dist = camera.getTransform().getPosition().xz().copy().sub(centerPosition).magnitude();
        if (dist > 75)
            isInViewDistance = false;
    }

    private void createVertexArray()
    {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        vio = glGenBuffers();
        ibo = glGenBuffers();

        glBindVertexArray(vao);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);

        glEnableVertexAttribArray(4);
        glEnableVertexAttribArray(5);
        glEnableVertexAttribArray(6);
        glEnableVertexAttribArray(7);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0L);
        glVertexAttribPointer(1, 3, GL_FLOAT, true , Vertex.SIZE * 4, 12L);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, Vertex.SIZE * 4, 24L);
        glVertexAttribPointer(3, 3, GL_FLOAT, false, Vertex.SIZE * 4, 32L);

        glBindBuffer(GL_ARRAY_BUFFER, vio);
        glBufferData(GL_ARRAY_BUFFER, instanceBuffer, GL_DYNAMIC_DRAW);
        glVertexAttribPointer(4, 4, GL_FLOAT, false, 16 * 4, 0L);
        glVertexAttribPointer(5, 4, GL_FLOAT, false, 16 * 4, 16L);
        glVertexAttribPointer(6, 4, GL_FLOAT, false, 16 * 4, 32L);
        glVertexAttribPointer(7, 4, GL_FLOAT, false, 16 * 4, 48L);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        glVertexAttribDivisor(0, 0);
        glVertexAttribDivisor(1, 0);
        glVertexAttribDivisor(2, 0);
        glVertexAttribDivisor(3, 0);

        glVertexAttribDivisor(4, 1);
        glVertexAttribDivisor(5, 1);
        glVertexAttribDivisor(6, 1);
        glVertexAttribDivisor(7, 1);

        glBindVertexArray(0);
    }

    public void render()
    {
        if(!isInViewDistance)
            return;

        Texture.GRASS_BILLBOARD.bind();
        glBindVertexArray(vao);
        glDrawElementsInstanced(GL_TRIANGLES, quadIndicesData().length, GL_UNSIGNED_INT, 0L, renderSize);
        glBindVertexArray(0);
        Texture.GRASS_BILLBOARD.unbind();
    }

    private float[] quadVerticesData()
    {
        return new float[] {
                -1, 0, 0,   0, 1, 0,    0, 1,   0, 0, 0,
                1, 0, 0,   0, 1, 0,     1, 1,   0, 0, 0,
                1, 1, 0,   0, 1, 0,     1, 0,   0, 0, 0,
                -1, 1, 0,   0, 1, 0,    0, 0,   0, 0, 0
        };
    }

    private int[] quadIndicesData()
    {
        return new int[] {
            0, 1, 2,
            0, 2, 3
        };
    }
}
