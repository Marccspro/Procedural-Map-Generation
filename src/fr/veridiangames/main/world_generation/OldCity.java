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

package fr.veridiangames.main.world_generation;

import fr.veridiangames.engine.maths.Mathf;
import fr.veridiangames.engine.maths.Vec2;
import fr.veridiangames.engine.maths.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marc on 29/05/2016.
 */
public class OldCity
{
    private Vec2 position;
    private int size;
    private List<OldCity> links;
    private List<Vec2> linkNodes;
    private List<Vec3> housesNodes;
    private int houseLevelCount;
    private int[] houseCountPerLevel;
    private List<House> houses;

    public OldCity(Vec2 position, int size)
    {
        this.position = position;
        this.size = size;
        this.links = new ArrayList<OldCity>();
        this.linkNodes = new ArrayList<Vec2>();
        this.housesNodes = new ArrayList<Vec3>();
        this.houses = new ArrayList<House>();
        generateHouses();
    }

    private void generateHouses()
    {
        float radius = size / 2;
        this.houseLevelCount = getHouseCercleCount(radius);
        houseCountPerLevel = new int[houseLevelCount];

        for (int i = 0; i < houseLevelCount; i++)
        {
            float radDist = radius / houseLevelCount;
            float rad = radDist * (i + 1) - radDist;
            int num = getHouseCountAroundCercle(rad);
            houseCountPerLevel[i] = num;
            float distAngle = 360.0f / num;

            for (int j = 0; j < num; j++)
            {
                float angle = distAngle * (j + 1);
                float x = position.x + Mathf.cos(Mathf.toRadians(angle)) * rad;
                float y = position.y + Mathf.sin(Mathf.toRadians(angle)) * rad;
                housesNodes.add(new Vec3(x, y, i));
            }
        }
        for (int i = 0; i < housesNodes.size(); i++)
        {
            Vec2 pos = housesNodes.get(i).xy();
            Vec2 dir = pos.copy().sub(position).normalize();
            Vec2 housePos = new Vec2(pos).add(dir.copy().mul(15));
            if (housePos.equals(new Vec2(0)))
                continue;
            if ((int) (dir.x * 100) == 0 || (int) (dir.y * 100) == 0)
                continue;
            House house = new House(housePos, i);
            houses.add(house);
        }
        for (int i = 0; i < 4; i++)
        {
            float angle = 90 * i;
            float x = position.x + Mathf.cos(Mathf.toRadians(angle)) * radius;
            float y = position.y + Mathf.sin(Mathf.toRadians(angle)) * radius;
            linkNodes.add(new Vec2(x, y));
        }
    }

    private int getHouseCountAroundCercle(float radius)
    {
        float n = Mathf.PI * radius * 2 / 70;

        if (n < 4)
            n = 4;
        else if (n < 8)
            n = 8;
        else if (n < 12)
            n = 12;
        else if (n < 16)
            n = 16;
        else
            n = 20;
        return (int) n;
    }


    public int[] getHouseCountPerLevel()
    {
        return houseCountPerLevel;
    }

    private int getHouseCercleCount(float radius)
    {
        return (int) (radius / 70);
    }

    public void addLink(OldCity c)
    {
        links.add(c);
    }

    public List<OldCity> getLinks()
    {
        return links;
    }

    public List<Vec2> getLinkNodes()
    {
        return linkNodes;
    }

    public int getHouseLevelCount()
    {
        return houseLevelCount;
    }

    public List<Vec3> getHousesNodes()
    {
        return housesNodes;
    }

    public List<House> getHouses()
    {
        return houses;
    }

    public Vec2 getPosition()
    {
        return position;
    }

    public int getSize()
    {
        return size;
    }

    public int getRadius()
    {
        return (int) ((float) size / 2);
    }
}
