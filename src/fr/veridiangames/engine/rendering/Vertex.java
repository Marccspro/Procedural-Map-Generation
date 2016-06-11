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
