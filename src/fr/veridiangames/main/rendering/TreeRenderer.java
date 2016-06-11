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

import fr.veridiangames.engine.maths.Mat4;
import fr.veridiangames.engine.maths.Mathf;
import fr.veridiangames.engine.maths.Vec3;
import fr.veridiangames.engine.rendering.*;
import fr.veridiangames.main.world_generation.noise.NoiseGeneration;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;

/**
 * Created by Marc on 27/05/2016.
 */
public class TreeRenderer
{
    private int num;
    private int size;
    private NoiseGeneration gen;
    private Model[] treeModel;
    private Texture treeTexture;

    private List<Vec3> trees;

    public TreeRenderer(int num, int size, NoiseGeneration gen)
    {
        this.num = num;
        this.size = size;
        this.gen = gen;
        this.treeModel = new Model[] {
                new Model("res/models/tree_LOD1.obj"),
                new Model("res/models/tree_LOD2.obj"),
                new Model("res/models/tree_LOD3.obj"),
                new Model("res/models/tree_LOD4.obj")
        };
        this.treeTexture = Texture.TREE;
        this.trees = new ArrayList<Vec3>();

        createBufferData();
    }

    private void createBufferData()
    {
        for (int i = 0; i < num; i++)
        {
            int x = (int) Mathf.random(0, size);
            int z = (int) Mathf.random(0, size);
            float y = gen.getNoise(x, z);

            trees.add(new Vec3(x, y, z));
        }
    }

    public void render(Shader shader, Camera camera)
    {
        Texture.TREE.bind();
        for (int i = 0; i < trees.size(); i++)
        {
            shader.setMat4(shader.getUniform("modelViewMatrix"), Mat4.translate(trees.get(i)));
            float dist = camera.getTransform().getPosition().copy().sub(trees.get(i)).magnitude();
            if (dist < 20)
                ModelRenderer.TREE_RENDERER[0].render();
            else if (dist < 40)
                ModelRenderer.TREE_RENDERER[1].render();
            else if (dist < 60)
                ModelRenderer.TREE_RENDERER[3].render();
//            else if (dist < 40)
//                ModelRenderer.TREE_RENDERER[3].render();
//            else if (dist < 50)
//                ModelRenderer.TREE_RENDERER[4].render();
//            else if (dist < 60)
//                ModelRenderer.TREE_RENDERER[5].render();
            else
            {
                Texture.TREE_BILLBOARD.bind();
                ModelRenderer.TREE_RENDERER[6].render();
                Texture.TREE.bind();
            }
        }
        Texture.TREE.unbind();
    }
}
