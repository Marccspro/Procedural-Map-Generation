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

import fr.veridiangames.engine.maths.Vec3;

/**
 * Created by Marc on 04/06/2016.
 */
public class Road
{
    private Vec3 startPoint;
    private Vec3 endPoint;
    private Vec3 startDir;
    private Vec3 endDir;

    public Road(Vec3 startPoint, Vec3 endPoint, Vec3 startDir, Vec3 endDir)
    {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.startDir = startDir;
        this.endDir = endDir;
    }

    public Vec3 getStartPoint()
    {
        return startPoint;
    }

    public void setStartPoint(Vec3 startPoint)
    {
        this.startPoint = startPoint;
    }

    public Vec3 getEndPoint()
    {
        return endPoint;
    }

    public void setEndPoint(Vec3 endPoint)
    {
        this.endPoint = endPoint;
    }

    public Vec3 getStartDir()
    {
        return startDir;
    }

    public void setStartDir(Vec3 startDir)
    {
        this.startDir = startDir;
    }

    public Vec3 getEndDir()
    {
        return endDir;
    }

    public void setEndDir(Vec3 endDir)
    {
        this.endDir = endDir;
    }
}
