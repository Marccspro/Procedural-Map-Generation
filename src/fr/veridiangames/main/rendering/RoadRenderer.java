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

import fr.veridiangames.engine.maths.Mathf;
import fr.veridiangames.engine.maths.Vec2;
import fr.veridiangames.engine.maths.Vec3;
import fr.veridiangames.engine.rendering.Mesh;
import fr.veridiangames.engine.rendering.Vertex;
import fr.veridiangames.main.world_generation.Road;
import fr.veridiangames.main.world_generation.noise.NoiseGeneration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marc on 04/06/2016.
 */
public class RoadRenderer
{
    private Road road;
    private Mesh mesh;
    private NoiseGeneration gen;

    private List<Vec3> positionsList;
    private List<Vec2> coordsList;
    private List<Integer> indicesList;

    public RoadRenderer(Road road, NoiseGeneration gen)
    {
        this.road = road;
        this.gen = gen;
        this.positionsList = new ArrayList<>();
        this.coordsList = new ArrayList<>();
        this.indicesList = new ArrayList<>();

        this.generateRoadMesh(4.0f);
    }

    private void generateRoadMesh(float precision)
    {
        Vec3 a = road.getStartPoint();
        Vec3 b = road.getEndPoint();

        Vec3 da = road.getStartDir();
        Vec3 db = road.getEndDir();

        Vec3 delta = new Vec3(b).sub(a);
        Vec3 dir = delta.copy().normalize();
        float dist = delta.copy().magnitude();
        int distFixed = (int) dist;
        float margin = (dist / 4) - (distFixed / 4);
        float count = dist / precision + 1;

        for (int i = 0; i < count; i++)
        {
            for (int j = 0; j < roadTemplate(0).length; j++)
            {
                float m = 1;
                if (i >= count - 1)
                    m = margin;
                float segmentSize = i * precision - (precision - precision * m);

                Vec3 dirFactor = dir.copy().mul(segmentSize);

                Vec3 currentPosition = new Vec3(roadTemplate(0)[j]);
                Vec3 rotation = new Vec3(currentPosition);
                rotation.x = currentPosition.x * dir.z + currentPosition.z * dir.x;
                rotation.z = currentPosition.z * dir.z - currentPosition.x * dir.x;
                currentPosition = new Vec3(rotation).add(a).add(dirFactor);
                currentPosition.y += gen.getExactNoise(currentPosition.x, currentPosition.z);

                positionsList.add(currentPosition);

                float xt = (roadTemplate(0)[j].x + 4) / roadTemplate(0).length;
                float yt = segmentSize / 4.0f;

                coordsList.add(new Vec2(xt, yt));
            }
        }

        int s = 8;
        for (int i = 0; i < positionsList.size() - 8; i++)
        {
            if (i % 8 == 7)
                continue;

            indicesList.add(i);
            indicesList.add(i + 1);
            indicesList.add(i + s);

            indicesList.add(i + 1);
            indicesList.add(i + 1 + s);
            indicesList.add(i + s);
        }

        Vertex[] vertices = new Vertex[positionsList.size()];
        for (int i = 0; i < positionsList.size(); i++)
        {
            vertices[i] = new Vertex(positionsList.get(i), coordsList.get(i), new Vec3(0, 1, 0));
        }

        int[] indices = new int[indicesList.size()];
        for (int i = 0; i < indices.length; i++)
        {
            indices[i] = indicesList.get(i);
        }

        mesh = new Mesh(vertices, indices);
    }

    private static Vec2 interpolatePolynome(Vec2 p0, Vec2 p1, Vec2 p2, Vec2 p3, float t) {
        float u = 1 - t;
        float tt = t * t;
        float uu = u * u;
        float uuu = uu * u;
        float ttt = tt * t;

        Vec2 p = p0.copy().mul(uuu);
        p = p.copy().add(p1.copy().mul(3 * uu * t));
        p = p.copy().add(p2.copy().mul(3 * u * tt));
        p = p.copy().add(p3.copy().mul(ttt));

        return p;
    }

    private Vec3[] roadTemplate(float height)
    {
        return new Vec3[]
            {
                new Vec3(-4, -2.0f + height, 0),
                new Vec3(-3, 0.2f + height, 0),
                new Vec3(-2, 0.2f + height, 0),
                new Vec3(-2, 0.1f + height, 0),
                new Vec3(+2, 0.1f + height, 0),
                new Vec3(+2, 0.2f + height, 0),
                new Vec3(+3, 0.2f + height, 0),
                new Vec3(+4, -2.0f + height, 0)
            };
    }

    public Mesh getMesh()
    {
        return mesh;
    }
}
