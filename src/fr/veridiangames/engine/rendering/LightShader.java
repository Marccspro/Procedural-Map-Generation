package fr.veridiangames.engine.rendering;

import fr.veridiangames.engine.lights.Light;
import fr.veridiangames.engine.maths.Mat4;
import fr.veridiangames.engine.maths.Vec2;
import fr.veridiangames.engine.maths.Vec3;
import fr.veridiangames.engine.maths.Vec4;
import fr.veridiangames.engine.utils.BufferUtil;
import fr.veridiangames.engine.utils.Color4f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class LightShader extends Shader
{
    private Light light;

    public LightShader(String vert_path, String frag_path)
    {
        super(vert_path, frag_path);
    }
}
