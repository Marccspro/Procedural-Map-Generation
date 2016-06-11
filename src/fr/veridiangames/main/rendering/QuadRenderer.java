package fr.veridiangames.main.rendering;

import fr.veridiangames.engine.rendering.Cubemap;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Created by Marc on 28/05/2016.
 */
public class QuadRenderer
{
    private int vao, vbo;
    private FloatBuffer verticesBuffer;
    private int width, height;

    public QuadRenderer(int width, int height)
    {
        this.width = width;
        this.height = height;
        createBufferData();
        createVertexArray();
    }

    private void createBufferData()
    {
        verticesBuffer = BufferUtils.createFloatBuffer(cubeVerticesData(width, height).length);
        verticesBuffer.put(cubeVerticesData(width, height));
        verticesBuffer.flip();
    }

    private void createVertexArray()
    {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();

        glBindVertexArray(vao);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * 4, 0L);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * 4, 12L);

        glBindVertexArray(0);
    }

    public void render()
    {
        glBindVertexArray(vao);
        glDrawArrays(GL_QUADS, 0, 4);
        glBindVertexArray(0);
    }

    private float[] cubeVerticesData(float width, float height)
    {
        return new float[]
        {
            0, 0, 0,            0, 0,
            width, 0, 0,        1, 0,
            width, height, 0,   1, 1,
            0, height, 0,       0, 1
        };
    }
}
