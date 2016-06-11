package fr.veridiangames.engine.rendering.debug;

import fr.veridiangames.engine.lights.Light;
import fr.veridiangames.engine.maths.Mat4;
import fr.veridiangames.engine.rendering.Camera;
import fr.veridiangames.engine.rendering.Shader;
import fr.veridiangames.engine.rendering.Texture;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.GL_RASTERIZER_DISCARD;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL31.glDrawArraysInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

/**
 * Created by Marc on 04/06/2016.
 */
public class LightDebugRenderer
{
    private Shader shader;

    private int vao, vbo, vio;
    private FloatBuffer verticesBuffer;
    private FloatBuffer instanceBuffer;

    private boolean show;

    public LightDebugRenderer()
    {
        shader = new Shader("res/shaders/debug/light_debug.vert", "res/shaders/debug/light_debug.frag");
        show = false;

        createBufferData();
        createVertexArray();
    }

    private void createBufferData()
    {
        verticesBuffer = BufferUtils.createFloatBuffer(quadData().length);
        verticesBuffer.put(quadData());
        verticesBuffer.flip();

        instanceBuffer = BufferUtils.createFloatBuffer(1000 * 3);
        instanceBuffer.flip();
    }

    private void createVertexArray()
    {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        vio = glGenBuffers();

        glBindVertexArray(vao);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0L);

        glBindBuffer(GL_ARRAY_BUFFER, vio);
        glBufferData(GL_ARRAY_BUFFER, instanceBuffer, GL_DYNAMIC_DRAW);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0L);

        glVertexAttribDivisor(0, 0);
        glVertexAttribDivisor(1, 1);

        glBindVertexArray(0);
    }

    public void update(List<Light> lights)
    {
        if (!show)
            return;

        instanceBuffer.clear();
        for (int i = 0; i < lights.size(); i++)
        {
            Light light = lights.get(i);
            instanceBuffer.put(light.getPosition().x);
            instanceBuffer.put(light.getPosition().y);
            instanceBuffer.put(light.getPosition().z);
        }
        instanceBuffer.flip();

        glBindBuffer(GL_ARRAY_BUFFER, vio);
        glBufferData(GL_ARRAY_BUFFER, instanceBuffer, GL_DYNAMIC_DRAW);
    }

    public void render(List<Light> lights, Mat4 projectionMatrix)
    {
        if (!show)
            return;

        shader.bind();
        shader.setMat4(shader.getUniform("projectionMatrix"), projectionMatrix);
        shader.setMat4(shader.getUniform("modelViewMatrix"), Mat4.identity());

        Texture.LIGHT_DIRECTION_DEBUG.bind();
        glDepthMask(false);
        glEnable(GL_POINT_SPRITE);
        glTexEnvi(GL_POINT_SPRITE, GL_COORD_REPLACE, GL_TRUE);
        glEnable(GL_VERTEX_PROGRAM_POINT_SIZE);
        glDisable(GL_RASTERIZER_DISCARD);
        glBindVertexArray(vao);
        glDrawArraysInstanced(GL_POINTS, 0, 1, lights.size());
        glBindVertexArray(0);
        glDisable(GL_POINT_SPRITE);
        glDisable(GL_VERTEX_PROGRAM_POINT_SIZE);
        glDepthMask(true);
        Texture.LIGHT_DIRECTION_DEBUG.unbind();

        shader.unbind();
    }

    private float[] quadData()
    {
        return new float[]
            {
                0, 0, 0
            };
    }

    public boolean isShow()
    {
        return show;
    }

    public void setShow(boolean show)
    {
        this.show = show;
    }
}
