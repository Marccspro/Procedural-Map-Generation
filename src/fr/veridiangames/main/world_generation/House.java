package fr.veridiangames.main.world_generation;

import fr.veridiangames.engine.maths.Vec2;
import fr.veridiangames.engine.maths.Vec3;

/**
 * Created by Marc on 29/05/2016.
 */
public class House
{
    private Vec2 position;
    private int parentNodeID;

    public House(Vec2 position, int parentNodeID)
    {
        this.position = position;
        this.parentNodeID = parentNodeID;
    }

    public Vec2 getPosition()
    {
        return position;
    }

    public int getParentNode()
    {
        return parentNodeID;
    }
}
