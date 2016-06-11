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

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

/**
 * Created by Marc on 31/05/2016.
 */
public class Material
{
    private HashMap<String, Integer> textures;

    private boolean diffuseTexture;
    private boolean normalTexture;

    private float specularIntensity;
    private float specularPower;

    public Material()
    {
        textures = new HashMap<String, Integer>();
        diffuseTexture = false;
        normalTexture = false;
        specularIntensity = 0;
        specularPower = 0;
    }

    public Material(int diffuseTexture)
    {
        this();
        addDiffuseTexture(diffuseTexture);
    }

    public Material(int diffuseTexture, int normalTexture)
    {
        this();
        addDiffuseTexture(diffuseTexture);
        addNormalTexture(normalTexture);
    }

    public void addTexture(String name, int texture)
    {
        textures.put(name, texture);
    }

    public void addDiffuseTexture(int texture)
    {
        textures.put("diffuse_sampler", texture);
        diffuseTexture = true;
    }

    public void addNormalTexture(int texture)
    {
        textures.put("normal_sampler", texture);
        normalTexture = true;
    }

    public void addShadowTexture(int texture)
    {
        textures.put("shadow_sampler", texture);
    }

    public void setSpecular(float factor, float power)
    {
        this.setSpecularIntensity(factor);
        this.setSpecularPower(power);
    }

    public float getSpecularIntensity()
    {
        return specularIntensity;
    }

    public void setSpecularIntensity(float specularIntensity)
    {
        this.specularIntensity = specularIntensity;
    }

    public float getSpecularPower()
    {
        return specularPower;
    }

    public void setSpecularPower(float specularPower)
    {
        this.specularPower = specularPower;
    }

    public boolean isDiffuseTexture()
    {
        return diffuseTexture;
    }

    public boolean isNormalTexture()
    {
        return normalTexture;
    }

    public void setShader(Shader shader)
    {
        int bindID = 0;
        for (Map.Entry<String, Integer> entry : textures.entrySet())
        {
            String name = entry.getKey();
            shader.setInt(shader.getUniform(name), bindID);
            bindID++;
        }
    }

    public void bind(Shader shader)
    {
        int bindID = 0;
        for (Map.Entry<String, Integer> entry : textures.entrySet())
        {
            int texture = entry.getValue();
            glActiveTexture(GL_TEXTURE0 + bindID);
            glBindTexture(GL_TEXTURE_2D, texture);
            bindID++;
        }
        shader.setFloat(shader.getUniform("specular_intensity"), specularIntensity);
        shader.setFloat(shader.getUniform("specular_power"), specularPower);
        shader.setInt(shader.getUniform("use_texture"), isDiffuseTexture() ? 1 : 0);
        shader.setInt(shader.getUniform("use_normal"), isNormalTexture() ? 1 : 0);
    }

    public void unbind(Shader shader)
    {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public HashMap<String, Integer> getTextures()
    {
        return textures;
    }
}
