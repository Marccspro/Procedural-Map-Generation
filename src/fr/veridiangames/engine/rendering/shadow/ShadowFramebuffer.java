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
