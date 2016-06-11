package fr.veridiangames.engine.lights;

import fr.veridiangames.engine.maths.Mat4;
import fr.veridiangames.engine.rendering.Shader;
import fr.veridiangames.engine.utils.Color4f;
import fr.veridiangames.main.rendering.MainRenderer;

/**
 * Created by Marc on 28/05/2016.
 */
public class AmbientLight extends Light
{
    public AmbientLight(Color4f color, float intensity)
    {
        super(Shader.AMBIANT_LIGHT, color, intensity);
    }

    public void bind(MainRenderer renderer, Mat4 projectionMatrix, Mat4 modelViewMatrix)
    {
        super.bind(renderer, projectionMatrix, modelViewMatrix);
        shader.setColor4f(shader.getUniform("light.color"), color);
        shader.setFloat(shader.getUniform("light.intensity"), intensity);
    }
}
