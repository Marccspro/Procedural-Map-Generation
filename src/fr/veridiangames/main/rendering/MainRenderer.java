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

import fr.veridiangames.engine.lights.AmbientLight;
import fr.veridiangames.engine.lights.DirectionalLight;
import fr.veridiangames.engine.lights.Light;
import fr.veridiangames.engine.maths.*;
import fr.veridiangames.engine.rendering.*;
import fr.veridiangames.engine.rendering.debug.LightDebugRenderer;
import fr.veridiangames.engine.rendering.shadow.ShadowRenderer;
import fr.veridiangames.engine.utils.Color4f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Marc on 27/05/2016.
 */
public class MainRenderer
{
    private Camera mainCamera;
    private Shader guiShader;

    private List<Light> lights;
    private List<MeshRenderer> meshes;

    private Light mainLight;
    private DirectionalLight sunLight;

    private ShadowRenderer shadowRenderer;

    private LightDebugRenderer lightDebugRenderer;

    private QuadRenderer quad;
    private boolean wireframeMode;

    public MainRenderer()
    {
        mainCamera = new Camera();
        mainCamera.setProjection(Mat4.perspective(70.0f, (float)Display.getWidth()/(float)Display.getHeight(), 0.5f, 1000.0f));

        meshes = new ArrayList<>();
        lights = new ArrayList<>();

        guiShader = new Shader("res/shaders/gui.vert", "res/shaders/gui.frag");
        quad = new QuadRenderer(Display.getWidth(), Display.getHeight());

        shadowRenderer = new ShadowRenderer();

        lightDebugRenderer = new LightDebugRenderer();

        mainLight = new AmbientLight(new Color4f(0.9f, 0.8f, 1.0f), 0.2f);

        Quat sunRotation = Quat.lookAt(new Vec3(0), new Vec3(1, 0, 0));
        sunLight = new DirectionalLight(sunRotation, new Color4f(1.0f, 0.9f, 0.8f), 1.5f);
        sunLight.setPosition(new Vec3(250, 50, 250));
        addLight(sunLight);

        wireframeMode = false;
    }

    public void add(MeshRenderer mesh)
    {
        meshes.add(mesh);
    }

    public void addLight(Light light)
    {
        lights.add(light);
    }

    int time = 0;
    public void update()
    {
        updateDebugKeys();
        time++;
        sunLight.getRotation().set(Quat.createFromAxisAngle(Vec3.RIGHT, Mathf.toRadians(time * 0.01f + 220)));
        mainCamera.update();
        shadowRenderer.update();
        lightDebugRenderer.update(lights);

        for (int i = 0; i < meshes.size(); i++)
        {
            MeshRenderer mesh = meshes.get(i);
            mesh.update(mainCamera);
        }
    }

    public void renderAll()
    {
        shadowRenderer.render(meshes, sunLight, this);

        renderForward();

        lightDebugRenderer.render(lights, mainCamera.getProjection());

        glDisable(GL_DEPTH_TEST);
        renderGui();
        glEnable(GL_DEPTH_TEST);
    }

    public void renderScene(Light light, Mat4 projectionMatrix)
    {
        if (wireframeMode)
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        for (int i = 0; i < meshes.size(); i++)
        {
            MeshRenderer mesh = meshes.get(i);
            mesh.render(light, projectionMatrix, this);
        }
        if (wireframeMode)
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }

    public void renderForward()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        renderScene(mainLight, mainCamera.getProjection());
        Renderer.enableForwardBlend();
        for (Light light : lights)
        {
            renderScene(light, mainCamera.getProjection());
        }
        Renderer.disableForwardBlend();
    }

    public void renderGui()
    {
        guiShader.bind();
        guiShader.setMat4(guiShader.getUniform("projectionMatrix"), Mat4.orthographic(0, Display.getWidth(), Display.getHeight(), 0, -1, 1));
        guiShader.setMat4(guiShader.getUniform("modelViewMatrix"), Mat4.translate(-0.5f, -0.5f, -0.5f).mul(Mat4.scale(0.5f, 0.5f, 0.5f)));

        glBindTexture(GL_TEXTURE_2D, shadowRenderer.getFramebuffer().getDepthTexture());
        quad.render();
    }

    public void updateDebugKeys()
    {
        while (Keyboard.next())
        {
            if (Keyboard.isKeyDown(Keyboard.KEY_F1))
                lightDebugRenderer.setShow(!lightDebugRenderer.isShow());
            if (Keyboard.isKeyDown(Keyboard.KEY_F2))
                Light.setLightOnly(!Light.isLightOnly());
            if (Keyboard.isKeyDown(Keyboard.KEY_F3))
                wireframeMode = !wireframeMode;
        }
    }

    public Mat4 getLightProjection()
    {
        return shadowRenderer.getShadowCamera().getProjection();
    }

    public Camera getMainCamera()
    {
        return mainCamera;
    }

    public ShadowRenderer getShadowRenderer()
    {
        return shadowRenderer;
    }
}
