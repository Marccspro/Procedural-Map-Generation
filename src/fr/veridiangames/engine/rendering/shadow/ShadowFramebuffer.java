package fr.veridiangames.engine.rendering.shadow;

import fr.veridiangames.engine.rendering.Framebuffer;

/**
 * Created by Marc on 04/06/2016.
 */
public class ShadowFramebuffer
{
    private Framebuffer framebuffer;

    public ShadowFramebuffer()
    {
        framebuffer = new Framebuffer(4096, 4096);
    }

    public void bind()
    {
        framebuffer.bindDepth();
    }

    public void unbind()
    {
        framebuffer.unbind();
    }

    public int getDepthTexture()
    {
        return framebuffer.getDepthTexture();
    }
}
