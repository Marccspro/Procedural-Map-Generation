package fr.veridiangames.engine.rendering;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Marc on 31/05/2016.
 */
public class Renderer
{
    public static void enableForwardBlend()
    {
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        glDepthMask(false);
        glDepthFunc(GL_EQUAL);
    }

    public static void disableForwardBlend()
    {
        glDepthFunc(GL_LESS);
        glDepthMask(true);
        glDisable(GL_BLEND);
    }
}
