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

import fr.veridiangames.engine.maths.Vec2;
import fr.veridiangames.engine.maths.Vec3;
import fr.veridiangames.engine.rendering.Mesh;
import fr.veridiangames.engine.rendering.Shader;
import fr.veridiangames.engine.rendering.Texture;
import fr.veridiangames.engine.rendering.Vertex;
import fr.veridiangames.main.world_generation.noise.NoiseGeneration;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Created by Marc on 26/05/2016.
 */
public class TerrainMesh
{
    private float[] positionsArray;
    private float[] normalsArray;
    private float[] coordsArray;
    private int[] indicesArray;

    private NoiseGeneration gen;
    private int size;

    private Mesh mesh;

    public TerrainMesh(int size, NoiseGeneration gen)
    {
        this.gen = gen;
        this.size = size;
        createTerrainMesh();
    }

    private void createTerrainMesh()
    {
        positionsArray = new float[3 * size * size];
        normalsArray = new float[3 * size * size];
        for (int i = 0; i < positionsArray.length; i++)
        {
            int index = i * 3;
            if (index >= positionsArray.length)
                break;

            int x = (int) ((float) i % size);
            int y = (int) ((float) i / size);

            Vec3 a = new Vec3(x, gen.getNoise(x, y), y);
            Vec3 b = new Vec3(x + 1, gen.getNoise(x + 1, y), y);
            Vec3 c = new Vec3(x, gen.getNoise(x, y + 1), y + 1);

            Vec3 tangent = new Vec3(a).sub(b).normalize();
            Vec3 biTangent = new Vec3(a).sub(c).normalize();

            Vec3 normal = biTangent.copy().cross(tangent);

            positionsArray[index + 0] = a.x;
            positionsArray[index + 1] = a.y;
            positionsArray[index + 2] = a.z;

            normalsArray[index + 0] = normal.x;
            normalsArray[index + 1] = normal.y;
            normalsArray[index + 2] = normal.z;
        }

        coordsArray = new float[2 * size * size];
        for (int i = 0; i < coordsArray.length; i++)
        {
            float xt = ((float) i % size);
            float yt = ((float) i / size);

            int index = i * 2;
            if (index >= coordsArray.length)
                break;
            coordsArray[index + 0] = xt;
            coordsArray[index + 1] = yt;
        }

        indicesArray = new int[6 * (size - 1) * (size - 1)];
        int indexOffs = 0;
        for (int i = 0; i < indicesArray.length + size - 2; i++)
        {
            if (i % size == size - 1)
            {
                indexOffs++;
                continue;
            }

            int index = (i - indexOffs) * 6;
            if (index >= indicesArray.length)
                break;

            indicesArray[index + 0] = i + size;
            indicesArray[index + 1] = i;
            indicesArray[index + 2] = i + 1;

            indicesArray[index + 3] = i + size + 1;
            indicesArray[index + 4] = i + size;
            indicesArray[index + 5] = i + 1;
        }

        Vertex[] vertices = new Vertex[size * size];
        for (int i = 0; i < vertices.length; i++)
        {
            int size = 3;
            Vec3 position = new Vec3(positionsArray[i * size], positionsArray[i * size + 1], positionsArray[i * size + 2]);
            Vec3 normal = new Vec3(normalsArray[i * size], normalsArray[i * size + 1], normalsArray[i * size + 2]);
            size = 2;
            Vec2 coord = new Vec2(coordsArray[i * size], coordsArray[i * size + 1]);
            vertices[i] = new Vertex(position, coord, normal);
        }

        mesh = new Mesh(vertices, indicesArray, true);
    }

    public Mesh getMesh()
    {
        return mesh;
    }
}
