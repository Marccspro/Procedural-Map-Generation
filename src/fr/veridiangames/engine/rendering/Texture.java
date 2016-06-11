package fr.veridiangames.engine.rendering;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_LOD_BIAS;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture
{
    /*  *************** TERRAIN TEXTURES **************** */
    public static final Texture TREE = new Texture("res/textures/tree_diffuse.png", GL_LINEAR);
    public static final Texture TREE_NORMAL = new Texture("res/textures/tree_normal.png", GL_LINEAR);
    public static final Texture TREE_BILLBOARD = new Texture("res/textures/tree_billboard.png", GL_LINEAR);

    public static final Texture GRASS = new Texture("res/textures/grass_texture.jpg", GL_LINEAR);
    public static final Texture GRASS_NORMAL = new Texture("res/textures/grass_normal.png", GL_LINEAR);
    public static final Texture GRASS_BILLBOARD = new Texture("res/textures/leaf_texture.png", GL_LINEAR);

    public static final Texture ROAD = new Texture("res/textures/road_diffuse.png", GL_LINEAR);

    /*  *************** DEBUG TEXTURES **************** */
    public static final Texture LIGHT_DIRECTION_DEBUG = new Texture("res/textures/debug/direction_light.png", GL_LINEAR);

    private int id;
    private int width, height;

    public Texture(String path, int filter)
    {
        System.out.print("Loading texture: " + path + "... ");
        long beforeTime = System.nanoTime();
        int[] pixels = null;
        try
        {
            BufferedImage image = ImageIO.read(new File(path));
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        int[] data = new int[pixels.length];
        for (int i = 0; i < pixels.length; i++)
        {
            int a = (pixels[i] & 0xff000000) >> 24;
            int r = (pixels[i] & 0xff0000) >> 16;
            int g = (pixels[i] & 0xff00) >> 8;
            int b = (pixels[i] & 0xff);

            data[i] = a << 24 | b << 16 | g << 8 | r;
        }

        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);

        if (filter == GL_LINEAR) {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -1f);
        }else {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -1f);
        }

        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glGenerateMipmap(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);

        System.out.println("Done in " + ((System.nanoTime() - beforeTime) / 1000000.0) + "ms !");

        this.id = id;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getID() { return id; }

    public void bind()
    {
        glBindTexture(GL_TEXTURE_2D, id);
    }
    public void unbind()
    {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}