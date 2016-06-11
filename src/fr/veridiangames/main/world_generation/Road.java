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
