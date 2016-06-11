package fr.veridiangames.engine.rendering;

import fr.veridiangames.engine.lights.Light;
import fr.veridiangames.engine.maths.Mat4;
import fr.veridiangames.engine.maths.Transform;
import fr.veridiangames.engine.maths.Vec3;
import fr.veridiangames.main.rendering.MainRenderer;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

/**
 * Created by Marc on 31/05/2016.
 */
public class MeshRenderer
{
    private List<Mesh>  meshLods;

    private List<Float> lodDistances;
    private int         currentLOD;

    private Material    material;
    private Transform   transform;

    public MeshRenderer(Mesh mesh, Material material)
    {
        this(new ArrayList<Mesh>(), new ArrayList<Float>(), material);
        this.meshLods.add(mesh);
    }

    public MeshRenderer(List<Mesh> meshLods, List<Float> lodDistances, Material material)
    {
        this.meshLods = meshLods;
        this.lodDistances = lodDistances;
        this.material = material;
        this.transform = new Transform();
        if (meshLods.size() == 0)
            this.currentLOD = 0;
        else
            this.currentLOD = meshLods.size() - 1;
    }

    public void update(Camera camera)
    {
        Vec3 cameraPosition = camera.getTransform().getPosition();
        Vec3 meshPosition = transform.getPosition();
        float dist = new Vec3(cameraPosition).sub(meshPosition).magnitude();

        for (int i = lodDistances.size() - 1; i >= 0; i--)
        {
            float lod = lodDistances.get(i);
            if (dist < lod)
                currentLOD = i;
        }
    }

    public void render(Light light, Mat4 projectionMatrix, MainRenderer renderer)
    {
        light.bind(renderer, projectionMatrix, transform.toMatrix());
        material.setShader(light.getShader());
        material.bind(light.getShader());

        glBindVertexArray(meshLods.get(currentLOD).getVao());
        glDrawElements(GL_TRIANGLES, meshLods.get(currentLOD).getIndices().length, GL_UNSIGNED_INT, 0L);
        glBindVertexArray(0);

        material.unbind(light.getShader());
    }

    public Transform getTransform()
    {
        return transform;
    }

    public void setTransform(Transform transform)
    {
        this.transform = transform;
    }

    public Material getMaterial()
    {
        return material;
    }

    public void setMaterial(Material material)
    {
        this.material = material;
    }
}
