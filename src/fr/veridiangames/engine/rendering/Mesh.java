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

import fr.veridiangames.engine.maths.Vec2;
import fr.veridiangames.engine.maths.Vec3;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Created by Marc on 31/05/2016.
 */
public class Mesh
{
    private float[] positions;
    private float[] normals;
    private float[] texCoords;
    private float[] tangents;
    private int[] indices;

    private int vao, vbo, nbo, tcbo, tbo, ibo;

    private FloatBuffer positionsBuffer;
    private FloatBuffer normalsBuffer;
    private FloatBuffer texCoordsBuffer;
    private FloatBuffer tangentsBuffer;
    private IntBuffer indicesBuffer;

    public  Mesh(Vertex[] vertices, int[] indices)
    {
        this(vertices, indices, false);
    }

    public Mesh(Vertex[] vertices, int[] indices, boolean calcTangents)
    {
        this.positions = new float[vertices.length * 3];
        this.normals = new float[vertices.length * 3];
        this.texCoords = new float[vertices.length * 2];
        this.tangents = new float[vertices.length * 3];
        this.indices = indices;
        List<Vec3> vertexTangents = null;

        if (calcTangents)
            vertexTangents = calcTangents(vertices, indices);

        for (int i = 0; i < vertices.length; i++)
        {
            Vertex v = vertices[i];

            int size = 3;
            positions[i * size + 0] = v.getPosition().x;
            positions[i * size + 1] = v.getPosition().y;
            positions[i * size + 2] = v.getPosition().z;

            size = 3;
            normals[i * size + 0] = v.getNormal().x;
            normals[i * size + 1] = v.getNormal().y;
            normals[i * size + 2] = v.getNormal().z;

            size = 2;
            texCoords[i * size + 0] = v.getTexCoord().x;
            texCoords[i * size + 1] = v.getTexCoord().y;

            size = 3;
            if (calcTangents)
            {
                tangents[i * size + 0] = vertexTangents.get(i).x;
                tangents[i * size + 1] = vertexTangents.get(i).y;
                tangents[i * size + 2] = vertexTangents.get(i).z;
            }
            else
            {
                tangents[i * size + 0] = v.getTangent().x;
                tangents[i * size + 1] = v.getTangent().y;
                tangents[i * size + 2] = v.getTangent().z;
            }
        }

        create();
    }

    public void create()
    {
        createBufferData();
        createVertexArray();
    }

    private void createBufferData()
    {
        positionsBuffer = BufferUtils.createFloatBuffer(positions.length);
        positionsBuffer.put(positions);
        positionsBuffer.flip();

        normalsBuffer = BufferUtils.createFloatBuffer(normals.length);
        normalsBuffer.put(normals);
        normalsBuffer.flip();

        texCoordsBuffer = BufferUtils.createFloatBuffer(texCoords.length);
        texCoordsBuffer.put(texCoords);
        texCoordsBuffer.flip();

        tangentsBuffer = BufferUtils.createFloatBuffer(tangents.length);
        tangentsBuffer.put(tangents);
        tangentsBuffer.flip();

        indicesBuffer = BufferUtils.createIntBuffer(indices.length);
        indicesBuffer.put(indices);
        indicesBuffer.flip();
    }

    private void createVertexArray()
    {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        nbo = glGenBuffers();
        tcbo = glGenBuffers();
        tbo = glGenBuffers();
        ibo = glGenBuffers();

        glBindVertexArray(vao);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, positionsBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0L);

        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 3, GL_FLOAT, true, 0, 0L);

        glBindBuffer(GL_ARRAY_BUFFER, tcbo);
        glBufferData(GL_ARRAY_BUFFER, texCoordsBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0L);

        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glBufferData(GL_ARRAY_BUFFER, tangentsBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, 0L);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        glBindVertexArray(0);
    }

    public List<Vec3> calcTangents(Vertex[] vertices, int[] indices)
    {
        List<Vec3> tangents = new ArrayList<Vec3>();
        for (int i = 0; i < vertices.length; i++)
        {
            tangents.add(new Vec3());
            tangents.add(new Vec3());
            tangents.add(new Vec3());
        }
        for (int i = 0; i < indices.length; i += 3)
        {
            int i0 = indices[i];
            int i1 = indices[i + 1];
            int i2 = indices[i + 2];

            Vec3 pos0 = new Vec3(vertices[i0].getPosition());
            Vec3 pos1 = new Vec3(vertices[i1].getPosition());
            Vec3 pos2 = new Vec3(vertices[i2].getPosition());

            Vec3 edge1 = pos1.sub(pos0);
            Vec3 edge2 = pos2.sub(pos0);

            Vec2 tex0 = new Vec2(vertices[i0].getTexCoord());
            Vec2 tex1 = new Vec2(vertices[i1].getTexCoord());
            Vec2 tex2 = new Vec2(vertices[i2].getTexCoord());

            float deltaU1 = tex1.x - tex0.x;
            float deltaV1 = tex1.y - tex0.y;
            float deltaU2 = tex2.x - tex0.x;
            float deltaV2 = tex2.y - tex0.y;

            float dividend = (deltaU1 * deltaV2 - deltaU2 * deltaV1);
            float f = dividend == 0 ? 0.0f : 1.0f / dividend;

            Vec3 tangent = new Vec3(0, 0, 0);
            tangent.x = f * (deltaV2 * edge1.x - deltaV1 * edge2.x);
            tangent.y = f * (deltaV2 * edge1.y - deltaV1 * edge2.y);
            tangent.z = f * (deltaV2 * edge1.z - deltaV1 * edge2.z);

            tangents.get(i0).set(tangents.get(i0).add(tangent));
            tangents.get(i1).set(tangents.get(i1).add(tangent));
            tangents.get(i2).set(tangents.get(i2).add(tangent));
        }
        return tangents;
    }

    public float[] getPositions()
    {
        return positions;
    }

    public void setPositions(float[] positions)
    {
        this.positions = positions;
    }

    public float[] getNormals()
    {
        return normals;
    }

    public void setNormals(float[] normals)
    {
        this.normals = normals;
    }

    public float[] getTexCoords()
    {
        return texCoords;
    }

    public void setTexCoords(float[] texCoords)
    {
        this.texCoords = texCoords;
    }

    public float[] getTangents()
    {
        return tangents;
    }

    public void setTangents(float[] tangents)
    {
        this.tangents = tangents;
    }

    public int[] getIndices()
    {
        return indices;
    }

    public void setIndices(int[] indices)
    {
        this.indices = indices;
    }

    public int getVao()
    {
        return vao;
    }

    public int getVbo()
    {
        return vbo;
    }

    public int getNbo()
    {
        return nbo;
    }

    public int getTcbo()
    {
        return tcbo;
    }

    public int getTbo()
    {
        return tbo;
    }

    public int getIbo()
    {
        return ibo;
    }

    public FloatBuffer getPositionsBuffer()
    {
        return positionsBuffer;
    }

    public void setPositionsBuffer(FloatBuffer positionsBuffer)
    {
        this.positionsBuffer = positionsBuffer;
    }

    public FloatBuffer getNormalsBuffer()
    {
        return normalsBuffer;
    }

    public void setNormalsBuffer(FloatBuffer normalsBuffer)
    {
        this.normalsBuffer = normalsBuffer;
    }

    public FloatBuffer getTexCoordsBuffer()
    {
        return texCoordsBuffer;
    }

    public void setTexCoordsBuffer(FloatBuffer texCoordsBuffer)
    {
        this.texCoordsBuffer = texCoordsBuffer;
    }

    public FloatBuffer getTangentsBuffer()
    {
        return tangentsBuffer;
    }

    public void setTangentsBuffer(FloatBuffer tangentsBuffer)
    {
        this.tangentsBuffer = tangentsBuffer;
    }

    public IntBuffer getIndicesBuffer()
    {
        return indicesBuffer;
    }

    public void setIndicesBuffer(IntBuffer indicesBuffer)
    {
        this.indicesBuffer = indicesBuffer;
    }
}
