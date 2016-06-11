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

import fr.veridiangames.engine.maths.Vec2;
import fr.veridiangames.engine.maths.Vec3;

/**
 * Created by Marc on 27/05/2016.
 */
public class Vertex
{
    public static final int SIZE = 3 + 3 + 2 + 3;

    private Vec3 position;
    private Vec3 normal;
    private Vec2 texCoord;
    private Vec3 tangent;

    public Vertex(Vec3 position)
    {
        this.position = position;
    }

    public Vertex(Vec3 position, Vec2 texCoord, Vec3 normal)
    {
        this.position = position;
        this.normal = normal;
        this.texCoord = texCoord;
        this.tangent = new Vec3();
    }

    public Vertex(Vec3 position, Vec2 texCoord, Vec3 normal, Vec3 tangent)
    {
        this.position = position;
        this.normal = normal;
        this.texCoord = texCoord;
        this.tangent = tangent;
    }

    public Vec3 getPosition()
    {
        return position;
    }

    public void setPosition(Vec3 position)
    {
        this.position = position;
    }

    public Vec3 getNormal()
    {
        return normal;
    }

    public void setNormal(Vec3 normal)
    {
        this.normal = normal;
    }

    public Vec2 getTexCoord()
    {
        return texCoord;
    }

    public void setTexCoord(Vec2 texCoord)
    {
        this.texCoord = texCoord;
    }

    public Vec3 getTangent()
    {
        return tangent;
    }

    public void setTangent(Vec3 tangent)
    {
        this.tangent = tangent;
    }
}
