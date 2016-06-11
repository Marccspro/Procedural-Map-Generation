package fr.veridiangames.engine.lights;

import fr.veridiangames.engine.maths.Mat4;
import fr.veridiangames.engine.maths.Vec3;
import fr.veridiangames.engine.rendering.Shader;
import fr.veridiangames.engine.utils.Color4f;
import fr.veridiangames.main.rendering.MainRenderer;

/**
 * Created by Marc on 28/05/2016.
 */
public abstract class Light
{
    protected Color4f color;
    protected float intensity;
    protected Vec3 position;

    protected Shader shader;
    private static boolean lightOnly = false;

    public Light(Shader shader, Color4f color, float intensity)
    {
        this.color = color;
        this.intensity = intensity;
        this.shader = shader;
        this.position = new Vec3(0, 0, 0);
    }

    public void bind(MainRenderer renderer, Mat4 projectionMatrix, Mat4 modelViewMatrix)
    {
        shader.bind();
        shader.setMat4(shader.getUniform("projectionMatrix"), projectionMatrix);
        shader.setMat4(shader.getUniform("modelViewMatrix"), modelViewMatrix);
        shader.setVec3(shader.getUniform("camera_position"), renderer.getMainCamera().getTransform().getPosition());
        shader.setInt(shader.getUniform("light_only"), lightOnly ? 1 : 0);
    }

    public Color4f getColor()
    {
        return color;
    }

    public void setColor(Color4f color)
    {
        this.color = color;
    }

    public float getIntensity()
    {
        return intensity;
    }

    public void setIntensity(float intensity)
    {
        this.intensity = intensity;
    }

    public Shader getShader()
    {
        return shader;
    }

    public Vec3 getPosition()
    {
        return position;
    }

    public void setPosition(Vec3 position)
    {
        this.position = position;
    }

    public static boolean isLightOnly()
    {
        return lightOnly;
    }

    public static void setLightOnly(boolean lightOnly)
    {
        Light.lightOnly = lightOnly;
    }
}
