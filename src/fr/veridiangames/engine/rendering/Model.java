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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marc on 27/05/2016.
 */
public class Model
{
    private Mesh mesh;

    private float[] verticesArray;
    private int[] indicesArray;

    private static List<Vertex> vertices;
    private static List<Vec3> positions;
    private static List<Vec2> texCoords;
    private static List<Vec3> normals;
    private static List<Vec3> tangents;

    public Model(String path) {
        vertices = new ArrayList<Vertex>();
        positions = new ArrayList<Vec3>();
        texCoords = new ArrayList<Vec2>();
        normals = new ArrayList<Vec3>();
        tangents = new ArrayList<Vec3>();

        loadModel(path);
        createMesh(path);

    }

    private void loadModel(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));

            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(" ");
                String prefix = values[0];

                if (prefix.equals("v")) {
                    float x = Float.parseFloat(values[1]);
                    float y = Float.parseFloat(values[2]);
                    float z = Float.parseFloat(values[3]);

                    positions.add(new Vec3(x, y, z));
                }else if (prefix.equals("vt")) {
                    float u = Float.parseFloat(values[1]);
                    float v = (1 -Float.parseFloat(values[2]));

                    texCoords.add(new Vec2(u, v));
                }else if (prefix.equals("vn")) {
                    float x = Float.parseFloat(values[1]);
                    float y = Float.parseFloat(values[2]);
                    float z = Float.parseFloat(values[3]);

                    normals.add(new Vec3(x, y, z));
                }else if (prefix.equals("f")) {
                    int p1 = Integer.parseInt(values[1].split("/")[0]) - 1;
                    int t1 = Integer.parseInt(values[1].split("/")[1]) - 1;
                    int n1 = Integer.parseInt(values[1].split("/")[2]) - 1;

                    vertices.add(new Vertex(positions.get(p1), texCoords.get(t1), normals.get(n1)));

                    int p2 = Integer.parseInt(values[3].split("/")[0]) - 1;
                    int t2 = Integer.parseInt(values[3].split("/")[1]) - 1;
                    int n2 = Integer.parseInt(values[3].split("/")[2]) - 1;

                    vertices.add(new Vertex(positions.get(p2), texCoords.get(t2), normals.get(n2)));

                    int p3 = Integer.parseInt(values[2].split("/")[0]) - 1;
                    int t3 = Integer.parseInt(values[2].split("/")[1]) - 1;
                    int n3 = Integer.parseInt(values[2].split("/")[2]) - 1;

                    vertices.add(new Vertex(positions.get(p3), texCoords.get(t3), normals.get(n3)));

                    tangents.add(new Vec3());
                    tangents.add(new Vec3());
                    tangents.add(new Vec3());
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createMesh(String name) {
        indicesArray = new int[vertices.size()];

        for (int i = 0; i < indicesArray.length; i++) {
            indicesArray[i] = i;
        }

        calcTangents(vertices, indicesArray);

        verticesArray = toFloatArray(vertices, tangents);
        mesh = new Mesh(toVertexArray(vertices, tangents), indicesArray);

        vertices.clear();
        positions.clear();
        texCoords.clear();
        normals.clear();
        tangents.clear();
    }

    public void calcTangents(List<Vertex> vertices, int[] indices) {
        for (int i = 0; i < indices.length; i += 3) {
            int i0 = indices[i];
            int i1 = indices[i + 1];
            int i2 = indices[i + 2];

            Vec3 pos0 = new Vec3(vertices.get(i0).getPosition());
            Vec3 pos1 = new Vec3(vertices.get(i1).getPosition());
            Vec3 pos2 = new Vec3(vertices.get(i2).getPosition());

            Vec3 edge1 = pos1.sub(pos0);
            Vec3 edge2 = pos2.sub(pos0);

            Vec2 tex0 = new Vec2(vertices.get(i0).getTexCoord());
            Vec2 tex1 = new Vec2(vertices.get(i1).getTexCoord());
            Vec2 tex2 = new Vec2(vertices.get(i2).getTexCoord());

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

        for (int i = 0; i < tangents.size(); i++)
            tangents.get(i).set(tangents.get(i).normalize());
    }

    public static float[] toFloatArray(List<Vertex> list, List<Vec3> tangents) {
        float[] array = new float[list.size() * Vertex.SIZE];

        for (int i = 0; i < array.length / Vertex.SIZE; i++) {
            Vertex v = list.get(i);
            v.setTangent(tangents.get(i));

            int index = i * Vertex.SIZE;

            array[index + 0] = v.getPosition().x;
            array[index + 1] = v.getPosition().y;
            array[index + 2] = v.getPosition().z;

            array[index + 3] = v.getNormal().x;
            array[index + 4] = v.getNormal().y;
            array[index + 5] = v.getNormal().z;

            array[index + 6] = v.getTexCoord().x;
            array[index + 7] = v.getTexCoord().y;

            array[index + 8] = v.getTangent().x;
            array[index + 9] = v.getTangent().y;
            array[index + 10] = v.getTangent().z;
        }

        return array;
    }

    public static Vertex[] toVertexArray(List<Vertex> list, List<Vec3> tangents) {
        Vertex[] array = new Vertex[list.size()];
        for (int i = 0; i < array.length; i++) {
            Vertex v = list.get(i);
            v.setTangent(tangents.get(i));

            array[i] = v;
        }
        return array;
    }

    public int[] getIndicesArray()
    {
        return indicesArray;
    }

    public void setIndicesArray(int[] indicesArray)
    {
        this.indicesArray = indicesArray;
    }

    public float[] getVerticesArray()
    {
        return verticesArray;
    }

    public void setVerticesArray(float[] verticesArray)
    {
        this.verticesArray = verticesArray;
    }

    public Mesh getMesh()
    {
        return mesh;
    }
}
