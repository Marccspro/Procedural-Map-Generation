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
import fr.veridiangames.main.world_generation.noise.NoiseGeneration;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Created by Marc on 29/05/2016.
 */
public class OldWorldGeneration
{
    private NoiseGeneration terrainNoise;

    private Random random;
    private OldCity[] cities;

    public OldWorldGeneration(long seed, int worldSize)
    {
        random = new Random(seed);
        cities = new OldCity[5];
        for (int i = 0; i < cities.length; i++)
        {
            if (i == 0)
            {
                float x = worldSize / 2;
                float y = worldSize / 2;
                cities[i] = new OldCity(new Vec2(x, y), 142);
                continue;
            }
            float x = random.nextInt(worldSize - 20) + 10;
            float y = random.nextInt(worldSize - 20) + 10;
            cities[i] = new OldCity(new Vec2(x, y), random.nextInt(100 - 60) + 60);
        }
        for (int i = 0; i < cities.length; i++)
        {
            for (int j = 0; j < cities.length; j++)
            {
                OldCity a = cities[i];
                OldCity b = cities[j];
                if (a == b)
                    continue;
                Vec2 delta = a.getPosition().copy().sub(b.getPosition());
                Vec2 dir = delta.copy().normalize();
                float dist = delta.magnitude();
                float goodDist = a.getRadius() + b.getRadius() + 100;

                if (dist < goodDist)
                {
                    System.out.println(dist + " " + goodDist);
                    System.out.println("old pos: " + a.getPosition());

                    float overlap = Mathf.abs(dist - a.getRadius() - b.getRadius()) / 2 + 25;
                    System.out.println("overlap: " + dir);

                    a.getPosition().add(dir.copy().mul(overlap));


                    System.out.println("new pos: " + a.getPosition());
                }
            }
        }
        for (int i = 0; i < cities.length; i++)
        {
            for (int j = 0; j < cities.length; j++)
            {
                OldCity a = cities[i];
                OldCity b = cities[j];
                if (a == null || b == null)
                    continue;

                if (a == b)
                    continue;
                Vec2 delta = a.getPosition().copy().sub(b.getPosition());
                float dist = delta.magnitude();
                float goodDist = a.getRadius() + b.getRadius() + 20;

                if (dist < goodDist)
                {
                    cities[j] = null;
                }
            }
        }


        terrainNoise = new NoiseGeneration(seed, worldSize);
        //terrainNoise.add(new NoisePass(seed, 100, 5, 0, 1));
        terrainNoise.calcFinalNoise();
    }

    public NoiseGeneration getTerrainNoise()
    {
        return terrainNoise;
    }

    public OldCity[] getCities()
    {
        return cities;
    }

    public static void main(String[] args)
    {
       // while (true)
        {
            int size = 600;
            Random rand = new Random();
            long seed = rand.nextInt(9999);
            System.out.println("======================SEED: " + seed);
//            OldWorldGeneration gen = new OldWorldGeneration(seed, size);
            BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

            int[] pixels = new int[size * size];
            boolean renderCity = true;

            if (! renderCity)
            {
//                for (int x = 0; x < size; x++)
//                {
//                    for (int y = 0; y < size; y++)
//                    {
//                        float h = gen.getTerrainNoise().getNoise(x, y) + 16;
//                        int color = 0xffffff;
//                        Color4f c = new Color4f(color);
//                        int r = (int) (c.r * h * 4);
//                        int g = (int) (c.g * h * 4);
//                        int b = (int) (c.b * h * 4);
//
//                        pixels[x + y * size] = r << 16 | g << 8 | b;
//                    }
//                }
//                for (int i = 0; i < gen.getCities().length; i++)
//                {
//                    if (gen.getCities()[i] == null)
//                        continue;
//                    int xp = (int) gen.getCities()[i].getPosition().x;
//                    int yp = (int) gen.getCities()[i].getPosition().y;
//                    int s = gen.getCities()[i].getSize();
//
//                    for (int x = 0; x < s; x++)
//                    {
//                        for (int y = 0; y < s; y++)
//                        {
//                            int xx = x + xp - s / 2;
//                            int yy = y + yp - s / 2;
//                            int xc = x - s / 2;
//                            int yc = y - s / 2;
//                            int dist = (int) Mathf.sqrt(xc * xc + yc * yc);
//                            if (dist > s / 2 - 2)
//                                continue;
//                            if (xx < 0 || yy < 0 || xx >= size || yy >= size)
//                                continue;
//
//                            Color4f c = new Color4f(pixels[xx + yy * size]);
//
//                            int r = (int) ((c.r + (1 - c.r) * 0.5) * 255);
//                            int g = (int) ((c.g + (0 - c.g) * 0.5) * 255);
//                            int b = (int) ((c.b + (1 - c.b) * 0.4) * 255);
//
//                            pixels[xx + yy * size] = r << 16 | g << 8 | b;
//                        }
//                    }
//                }
//
//                for (int i = 0; i < gen.getCities().length; i++)
//                {
//                    for (int j = 0; j < gen.getCities().length; j++)
//                    {
//                        OldCity a = gen.getCities()[i];
//                        OldCity b = gen.getCities()[j];
//
//                        if (a == null || b == null)
//                            continue;
//                        if (a == b)
//                            continue;
//                        if (!a.getLinks().contains(b) && !b.getLinks().contains(a) && a.getLinks().size() < 1)
//                        {
//                            a.addLink(b);
//                            b.addLink(a);
//                            int dist = (int) a.getPosition().copy().sub(b.getPosition()).magnitude();
//                            for (int l = 0; l < dist; l++)
//                            {
//                                int xa = (int) a.getPosition().x;
//                                int ya = (int) a.getPosition().y;
//                                int xb = (int) b.getPosition().x;
//                                int yb = (int) b.getPosition().y;
//                                float factor = (float) l / dist;
//
//                                int xx = (int) (xa + (xb - xa) * factor);
//                                int yy = (int) (ya + (yb - ya) * factor);
//
//                                if (xx < 0 || yy < 0 || xx >= size || yy >= size)
//                                    continue;
//                                pixels[xx + yy * size] = 0xffff00;
//                            }
//                        }
//                    }
//                }
            }
            else
            {
//                OldCity city = new OldCity(new Vec2(300, 300), 600);
//                int radius = city.getRadius();
//                Vec2 pos = city.getPosition();
//
//                renderCercle(radius, pos, 0x555555, pixels);
//                renderCercle(16, pos, 0x000000, pixels);
//                for (int j = 0; j < city.getHouseLevelCount(); j++)
//                {
//                    int houseCount = 0;
//                    for (int i = 0; i < city.getHousesNodes().size(); i++)
//                    {
//                        Vec3 cpos = city.getHousesNodes().get(i);
//                        if (cpos.z != j)
//                            continue;
//                       //renderCercle(5, cpos.xy(), 0x00ffff, pixels);
//                        houseCount++;
//                        Vec3 a = city.getHousesNodes().get(i);
//                        if (houseCount >= city.getHouseCountPerLevel()[j])
//                        {
//                            Vec3 b = city.getHousesNodes().get(i - houseCount + 1);
//
//                            renderSpline(a.xy(), a.xy(), b.xy(), b.xy(), 0xff00ff, pixels);
//                            continue;
//                        }
//
//                        Vec3 b = city.getHousesNodes().get(i + 1);
//
//                        if (b.z == j)
//                        {
//                            renderSpline(a.xy(), a.xy(), b.xy(), b.xy(), 0xff00ff, pixels);
//                        }
//                    }
//                }
//                for (int i = 0; i < city.getHouses().size(); i++)
//                {
//                    House house = city.getHouses().get(i);
//                   // renderLine(house.getPosition(), city.getHousesNodes().get(house.getParentNode()).xy(), 600, 0x00ffff, pixels);
//                   // renderCercle(10, house.getPosition(), 0x00cc00, pixels);
//                }
//                for (int i = 0; i < city.getLinkNodes().size(); i++)
//                {
//                    Vec2 nodePos = city.getLinkNodes().get(i);
//                    //renderCercle(5, nodePos, 0x00ffff, pixels);
//                }
            }

            img.setRGB(0, 0, size, size, pixels, 0, size);
            JOptionPane.showMessageDialog(null, null, "Another", JOptionPane.YES_NO_OPTION, new ImageIcon(img.getScaledInstance(size, size, Image.SCALE_AREA_AVERAGING)));
        }
    }


    public static void renderCercle(int radius, Vec2 pos, int color, int[] pixels)
    {
        int size = 600;
        for (int x = 0; x < size; x++)
        {
            for (int y = 0; y < size; y++)
            {
                int xp = (int)pos.x;
                int yp = (int)pos.y;
                int s = radius * 2;

                int xx = x + xp - s / 2;
                int yy = y + yp - s / 2;
                int xc = x - s / 2;
                int yc = y - s / 2;
                float dist = Mathf.sqrt(xc * xc + yc * yc);
                if (dist > (float) s / 2 - 2)
                    continue;
                if (xx < 0 || yy < 0 || xx >= size || yy >= size)
                    continue;

                pixels[xx + yy * size] = color;
            }
        }
    }

    public static void renderLine(Vec2 a, Vec2 b, int displaySize, int color, int[] pixels)
    {
        int dist = (int) a.copy().sub(b).magnitude();
        for (int i = 0; i < dist; i++)
        {
            float xa = a.x;
            float ya = a.y;
            float xb = b.x;
            float yb = b.y;

            float factor = (float) i / dist;
            int xx = (int) (xa + (xb - xa) * factor);
            int yy = (int) (ya + (yb - ya) * factor);

            if (xx < 0 || yy < 0 || xx >= displaySize || yy >= displaySize)
                continue;
            pixels[xx + yy * displaySize] = color;
        }
    }

    public static void renderSpline(Vec2 point0, Vec2 point1, Vec2 point2, Vec2 point3, int color, int[] pixels) {

        float dist = point0.copy().sub(point3).magnitude() * 1.5f;
        for (int i = 0; i <= dist; i++) {
            float factor = (float) i / dist;

            Vec2 pixel = interpolatePolynome(point0, point1, point2, point3, factor);

            int x = (int) pixel.x;
            int y = (int) pixel.y;

            renderCercle(3, new Vec2(x, y), color, pixels);
        }
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
}
