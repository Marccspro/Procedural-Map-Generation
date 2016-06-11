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

package fr.veridiangames.engine.rendering.shadow;

import fr.veridiangames.engine.lights.DirectionalLight;
import fr.veridiangames.engine.maths.Mat4;
import fr.veridiangames.engine.maths.Vec3;
import fr.veridiangames.engine.rendering.Camera;
import fr.veridiangames.engine.rendering.MeshRenderer;
import fr.veridiangames.main.rendering.MainRenderer;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

/**
 * Created by Marc on 04/06/2016.
 */
public class ShadowRenderer
{
    private ShadowFramebuffer framebuffer;
    private Camera shadowCamera;
    private int time;

    public ShadowRenderer()
    {
        framebuffer = new ShadowFramebuffer();
        shadowCamera = new Camera();
        shadowCamera.setProjection(Mat4.orthographic(-20, 20, 20, -20, -50, 50));
        time = 0;
    }

    public void update()
    {
        time++;
    }

    public void render(List<MeshRenderer> meshes, DirectionalLight sun, MainRenderer renderer)
    {
        shadowCamera.setProjection(Mat4.orthographic(-100, 100, 100, -100, -200, 200));
        shadowCamera.getTransform().setLocalPosition(new Vec3(250, 0, 250));
        shadowCamera.getTransform().setLocalRotation(sun.getRotation());

        if (time < 60.0 / 24.0)
            return;
        time = 0;

        framebuffer.bind();
        glClear(GL_DEPTH_BUFFER_BIT);
        for (int i = 0; i < meshes.size(); i++)
        {
            MeshRenderer mesh = meshes.get(i);
            mesh.render(sun, shadowCamera.getProjection(), renderer);
        }
        framebuffer.unbind();
    }

    public ShadowFramebuffer getFramebuffer()
    {
        return framebuffer;
    }

    public Camera getShadowCamera()
    {
        return shadowCamera;
    }
}
