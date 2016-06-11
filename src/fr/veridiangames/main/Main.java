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

package fr.veridiangames.main;

import fr.veridiangames.engine.maths.Vec2;
import fr.veridiangames.engine.maths.Vec3;
import fr.veridiangames.engine.rendering.*;
import fr.veridiangames.main.rendering.MainRenderer;
import fr.veridiangames.main.rendering.RoadRenderer;
import fr.veridiangames.main.rendering.TerrainMesh;
import fr.veridiangames.main.world_generation.OldCity;
import fr.veridiangames.main.world_generation.OldWorldGeneration;
import fr.veridiangames.main.world_generation.Road;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import java.util.Random;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;

/**
 * Created by Marc on 25/05/2016.
 */
public class Main
{
    MainRenderer mainRenderer;
    OldWorldGeneration gen;
    TerrainMesh terrain;

    public Main()
    {
        int terrainSize = 500;

        mainRenderer = new MainRenderer();
        gen = new OldWorldGeneration(new Random().nextInt(9999), terrainSize + 1);

//        terrain = new TerrainMesh(terrainSize, gen.getTerrainNoise());
//        Material terrainMaterial = new Material(Texture.GRASS.getID(), Texture.GRASS_NORMAL.getID());
//        terrainMaterial.addShadowTexture(mainRenderer.getShadowRenderer().getFramebuffer().getDepthTexture());
//        terrainMaterial.setSpecular(2, 10);
//        MeshRenderer terrainRenderer = new MeshRenderer(terrain.getMesh(), terrainMaterial);
//        mainRenderer.add(terrainRenderer);

        this.generateRoads();
//        Material treeMaterial = new Material(Texture.TREE.getID(), Texture.TREE_NORMAL.getID());
//        treeMaterial.addShadowTexture(mainRenderer.getShadowRenderer().getFramebuffer().getDepthTexture());
//        treeMaterial.setSpecular(5, 100);
//
//        List<Mesh> treeLods = new ArrayList<>();
//        treeLods.add(new Model("res/models/tree_LOD1.obj").getMesh());
//        treeLods.add(new Model("res/models/tree_LOD2.obj").getMesh());
//        treeLods.add(new Model("res/models/tree_LOD3.obj").getMesh());
//        treeLods.add(new Model("res/models/tree_LOD4.obj").getMesh());
//        treeLods.add(new Model("res/models/tree_LOD5.obj").getMesh());
//        treeLods.add(new Model("res/models/tree_LOD6.obj").getMesh());
//
//        List<Float> treeLodsDistances = new ArrayList<>();
//        treeLodsDistances.add(10.0f);
//        treeLodsDistances.add(20.0f);
//        treeLodsDistances.add(30.0f);
//        treeLodsDistances.add(40.0f);
//        treeLodsDistances.add(50.0f);
//        treeLodsDistances.add(60.0f);
//
//        for (int i = 0; i < 100; i++)
//        {
//            float x = Mathf.random(0, terrainSize);
//            float z = Mathf.random(0, terrainSize);
//            float y = gen.getNoise((int) x, (int) z);
//
//            MeshRenderer tree = new MeshRenderer(treeLods, treeLodsDistances, treeMaterial);
//            tree.getTransform().setLocalPosition(new Vec3(x, y, z));
//            mainRenderer.add(tree);
//        }
    }

    public void generateRoads()
    {
        Material roadMaterial = new Material(Texture.ROAD.getID());
        roadMaterial.addShadowTexture(mainRenderer.getShadowRenderer().getFramebuffer().getDepthTexture());
        roadMaterial.setSpecular(0.5f, 2);

        OldCity city = new OldCity(new Vec2(250, 250), 300);
        int radius = city.getRadius();
        Vec2 pos = city.getPosition();

        for (int j = 0; j < city.getHouseLevelCount(); j++)
        {
            int houseCount = 0;
            for (int i = 0; i < city.getHousesNodes().size(); i++)
            {
                Vec3 cpos = city.getHousesNodes().get(i);
                if (cpos.z != j)
                    continue;
                houseCount++;
                Vec3 a = city.getHousesNodes().get(i);
                if (houseCount >= city.getHouseCountPerLevel()[j])
                {
                    Vec3 b = city.getHousesNodes().get(i - houseCount + 1);
                    RoadRenderer road = new RoadRenderer(new Road(new Vec3(a.x, 0, a.y), new Vec3(b.x, 0, b.y), new Vec3(a.x, 0, a.y), new Vec3(b.x, 0, b.y)), gen.getTerrainNoise());
                    MeshRenderer roadRenderer = new MeshRenderer(road.getMesh(), roadMaterial);
                    mainRenderer.add(roadRenderer);
                    continue;
                }
                Vec3 b = city.getHousesNodes().get(i + 1);

                if (b.z == j)
                {
                    RoadRenderer road = new RoadRenderer(new Road(new Vec3(a.x, 0, a.y), new Vec3(b.x, 0, b.y), new Vec3(a.x, 0, a.y), new Vec3(b.x, 0, b.y)), gen.getTerrainNoise());
                    MeshRenderer roadRenderer = new MeshRenderer(road.getMesh(), roadMaterial);
                    mainRenderer.add(roadRenderer);
                }
            }
        }
    }

    public void update()
    {
        mainRenderer.update();
    }

    public void render()
    {
        mainRenderer.renderAll();
    }

    public static void main(String[] args)
    {
        try
        {
            Display.setDisplayMode(new DisplayMode(1280, 720));
            Display.setResizable(true);
            Display.setTitle("Terrain");
            Display.create(new PixelFormat(0, 8, 0, 4));

            glEnable(GL_DEPTH_TEST);
            glEnable(GL_ALPHA_TEST);
            glEnable(GL_TEXTURE_2D);
            glEnable(GL_MULTISAMPLE);

            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        }
        catch (LWJGLException e)
        {
            e.printStackTrace();
        }

        Main main = new Main();

        long before = System.nanoTime();
        long now = 0L;
        double elapsed = 0.0;

        int frames = 0;
        int ticks = 0;

        while (!Display.isCloseRequested())
        {
            now = System.nanoTime();
            elapsed = now - before;

            if (elapsed > 1000000000.0 / 60.0)
            {
                main.update();
                ticks++;
                before += 1000000000.0 / 60.0;
                if (ticks % 60 == 0)
                {
                    Display.setTitle("FPS: " + frames + "  TPS: " + ticks);
                    ticks = 0;
                    frames = 0;
                }
            }
            else
            {
                main.render();
                frames++;
            }
//            try
//            {
//                Thread.sleep(5);
//            } catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
            Display.update();
            if (Display.wasResized())
                glViewport(0, 0, Display.getWidth(), Display.getHeight());
        }

        Display.destroy();
        System.exit(0);
    }
}
