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

package fr.veridiangames.engine.lights;

import fr.veridiangames.engine.maths.Mat4;
import fr.veridiangames.engine.maths.Vec3;
import fr.veridiangames.engine.rendering.Shader;
import fr.veridiangames.engine.utils.Color4f;
import fr.veridiangames.main.rendering.MainRenderer;

/**
 * Created by Marc on 28/05/2016.
 */
public abstract class Light
{
    protected Color4f color;
    protected float intensity;
    protected Vec3 position;

    protected Shader shader;
    private static boolean lightOnly = false;

    public Light(Shader shader, Color4f color, float intensity)
    {
        this.color = color;
        this.intensity = intensity;
        this.shader = shader;
        this.position = new Vec3(0, 0, 0);
    }

    public void bind(MainRenderer renderer, Mat4 projectionMatrix, Mat4 modelViewMatrix)
    {
        shader.bind();
        shader.setMat4(shader.getUniform("projectionMatrix"), projectionMatrix);
        shader.setMat4(shader.getUniform("modelViewMatrix"), modelViewMatrix);
        shader.setVec3(shader.getUniform("camera_position"), renderer.getMainCamera().getTransform().getPosition());
        shader.setInt(shader.getUniform("light_only"), lightOnly ? 1 : 0);
    }

    public Color4f getColor()
    {
        return color;
    }

    public void setColor(Color4f color)
    {
        this.color = color;
    }

    public float getIntensity()
    {
        return intensity;
    }

    public void setIntensity(float intensity)
    {
        this.intensity = intensity;
    }

    public Shader getShader()
    {
        return shader;
    }

    public Vec3 getPosition()
    {
        return position;
    }

    public void setPosition(Vec3 position)
    {
        this.position = position;
    }

    public static boolean isLightOnly()
    {
        return lightOnly;
    }

    public static void setLightOnly(boolean lightOnly)
    {
        Light.lightOnly = lightOnly;
    }
}
