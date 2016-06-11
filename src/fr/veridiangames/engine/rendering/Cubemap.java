package fr.veridiangames.engine.rendering;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;

/**
 * Created by Marc on 28/05/2016.
 */
public class Cubemap
{
    private int cubemap;

    public Cubemap(String[] textures)
    {
        System.out.println("Loading cubemap:");
        long beforeTime = System.nanoTime();
        cubemap = glGenTextures();
        glBindTexture(GL_TEXTURE_CUBE_MAP, cubemap);

        for (int i = 0; i < textures.length; i++)
        {
            System.out.print("  - Loading cubemap texture: " + textures[i] + "... ");
            int[] pixels = null;
            int width = 0, height = 0;
            try
            {
                BufferedImage image = ImageIO.read(new File(textures[i]));
                width = image.getWidth();
                height = image.getHeight();
                pixels = new int[width * height];
                image.getRGB(0, 0, width, height, pixels, 0, width);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            int[] data = new int[pixels.length];
            for (int j = 0; j < pixels.length; j++)
            {
                int a = (pixels[j] & 0xff000000) >> 24;
                int r = (pixels[j] & 0xff0000) >> 16;
                int g = (pixels[j] & 0xff00) >> 8;
                int b = (pixels[j] & 0xff);

                data[j] = a << 24 | b << 16 | g << 8 | r;
            }

            IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
            buffer.put(data);
            buffer.flip();

            glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
            System.out.println("Done !");
        }

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        System.out.println("Done in " + ((System.nanoTime() - beforeTime) / 1000000.0) + "ms !");
    }

    public void bind()
    {
        glBindTexture(GL_TEXTURE_CUBE_MAP, cubemap);
    }

    public void unbind()
    {
        glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
    }

    public int getCubemap()
    {
        return cubemap;
    }

    public void setCubemap(int cubemap)
    {
        this.cubemap = cubemap;
    }
}
