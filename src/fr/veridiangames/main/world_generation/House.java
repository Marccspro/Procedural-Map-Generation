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
import fr.veridiangames.engine.maths.Vec2i;
import fr.veridiangames.engine.maths.Vec3;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Created by Marc on 29/05/2016.
 */
public class House
{
    private Random rand;
    private Vec2 position;
    private Vec2i size;
    private int parentNodeID;
    private int[] voxels;

    public House(Vec2 position, int parentNodeID)
    {
        this.rand = new Random();
        this.rand.setSeed((int) Mathf.random(0, 9999));

        this.position = position;
        this.size = new Vec2i(getRandomRange(30, 40), getRandomRange(20, 30));
        this.parentNodeID = parentNodeID;

        voxels = new int[size.x * size.y];

        generateInteriorWalls();
        generateExteriorWalls();
    }

    public void generateExteriorWalls()
    {
        int i = 0;
        for (int x = 0; x < size.x; x++)
        {
            for (int y = 0; y < size.y; y++)
            {
                if (x > 0 && x < size.x - 1 && y > 0 && y < size.y - 1)
                    continue;

                voxels[x + y * size.x] = 1;

                boolean inCorner = (x == 0 && y == 0) || (x == size.x-1 && y == 0) || (x == size.x-1 && y == size.y-1) || (x == 0 && y == size.y-1);
                boolean inWall =  false;

                if (x == 0)
                    inWall = getVoxel(x + 1, y) != 0;
                else if (x == size.x - 1)
                    inWall = getVoxel(x - 1, y) != 0;
                else if (y == 0)
                    inWall = getVoxel(x, y + 1) != 0;
                else if (y == size.y - 1)
                    inWall = getVoxel(x, y - 1) != 0;

                i++;
                if (getRandomRange(0, 100) > 80 && i > 10 && !inCorner && !inWall)
                {
                    i = 0;
                    voxels[x + y * size.x] = 3;
                }
            }
        }
        int doorPos = size.x / 2;
        if (getVoxel(doorPos, size.y - 2) == 1)
            doorPos += 2;

        voxels[doorPos + (size.y - 1) * size.x] = 4;
    }

    public void generateInteriorWalls()
    {
        int pos = getRandomRange((int) (size.y * 4.0f/8.0f), (int) (size.y * 5.0f/8.0f));

        int num1 = 1, num2 = 1;

        for (int x = 1; x < size.x - 1; x++)
        {
            for (int y = 1; y < size.y - 1; y++)
            {
                voxels[x + y * size.x] = 0;
            }
        }
        num1 = getRandomRange(1, 4);
        for (int i = 0; i < num1; i++)
        {
            int rPos = (int) ((float)size.x / (float)num1 * (float)i) + getRandomRange(-1, 1);
            for (int y = 0; y < size.y - pos; y++)
            {
                int yy = y + pos;
                if (rPos < 0 || rPos >= size.x)
                    continue;
                voxels[rPos + yy * size.x] = 1;
            }
            if (i == 0)
                continue;
            int doorPos = getRandomRange(2, size.y - pos - 2) + pos;
            voxels[rPos + doorPos * size.x] = 2;
        }

        if (num1 < 3)
            num2 = getRandomRange(num1 + 1, 4);
        else
            num2 = 3;

        for (int i = 0; i < num2; i++)
        {
            int rPos = (int) ((float)size.x / (float)num2 * (float)i) + getRandomRange(-1, 1);
            for (int y = 0; y < pos; y++)
            {
                int yy = y;
                if (rPos < 0 || rPos >= size.x)
                    continue;
                voxels[rPos + yy * size.x] = 1;
            }
            if (i == 0)
                continue;
            int doorPos = getRandomRange(2, pos - 2);
            voxels[rPos + doorPos * size.x] = 2;
        }

        for (int x = 0; x < size.x; x++)
        {
            voxels[x + pos * size.x] = 1;
        }

        int numDoors = (int) Mathf.max(num1, num2);
        System.out.println(numDoors);
        for (int i = 0; i < numDoors; i++)
        {
            int dpos = (int) ((float)size.x / (float)numDoors * i) + getRandomRange(3, 7);
            if (voxels[dpos + (pos + 1) * size.x] == 1)
                dpos += 2;
            voxels[dpos + pos * size.x] = 2;
        }
    }

    public int getVoxel(int x, int y)
    {
        if (x < 0 || x >= size.x || y < 0 || y >= size.y)
            return 0;
        return voxels[x + y * size.x];
    }

    public int getRandomRange(int start, int end)
    {
        return rand.nextInt(end - start) + start;
    }

    public Vec2 getPosition()
    {
        return position;
    }

    public int getParentNode()
    {
        return parentNodeID;
    }

    public static void main(String[] args)
    {
        while (true)
        {
            House house = new House(new Vec2(20, 20), 0);
            int size = house.voxels.length;
            BufferedImage img = new BufferedImage(house.size.x, house.size.y, BufferedImage.TYPE_INT_RGB);

            int[] pixels = new int[size];

            for (int i = 0; i < size; i++)
            {
                int voxel = house.voxels[i];
                if (voxel == 1)
                    pixels[i] = 0xff00ff;
                if (voxel == 2)
                    pixels[i] = 0xffff00;
                if (voxel == 3)
                    pixels[i] = 0x0000ff;
                if (voxel == 4)
                    pixels[i] = 0x00ffff;
            }

            img.setRGB(0, 0, house.size.x, house.size.y, pixels, 0, house.size.x);
            JOptionPane.showMessageDialog(null, null, "Another", JOptionPane.YES_NO_OPTION, new ImageIcon(img.getScaledInstance(house.size.x * 10, house.size.y * 10, Image.SCALE_AREA_AVERAGING)));
        }
    }
}
