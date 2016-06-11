package fr.veridiangames.main;

import fr.veridiangames.engine.lights.AmbientLight;
import fr.veridiangames.engine.lights.DirectionalLight;
import fr.veridiangames.engine.lights.Light;
import fr.veridiangames.engine.maths.Mat4;
import fr.veridiangames.engine.maths.Quat;
import fr.veridiangames.engine.maths.Vec3;
import fr.veridiangames.engine.rendering.Camera;
import fr.veridiangames.engine.rendering.Cubemap;
import fr.veridiangames.engine.rendering.Framebuffer;
import fr.veridiangames.engine.rendering.Shader;
import fr.veridiangames.engine.utils.Color4f;
import fr.veridiangames.main.rendering.*;
import fr.veridiangames.main.world_generation.noise.NoiseGeneration;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Marc on 31/05/2016.
 */
public class Terrain
{
    public static final String[] SKYBOX_TEXTURES = new String[]
            {
                    "res/textures/skybox/sides.jpg",
                    "res/textures/skybox/sides.jpg",
                    "res/textures/skybox/top.jpg",
                    "res/textures/skybox/bottom.jpg",
                    "res/textures/skybox/sides.jpg",
                    "res/textures/skybox/sides.jpg"
            };

    private NoiseGeneration worldGen;

    private Camera camera;

    private Light mainLight;
    private List<Light> lights;

    private Shader terrainShader;
    private Shader treeShader;
    private Shader grassShader;
    private Shader skyboxShader;
    private Shader sunShader;
    private Shader guiShader;
    private Shader shadowShader;

    private DirectionalLight sunLight;
    private SpherePrimitive sun;
    private SkyboxRenderer skybox;
    private TerrainMesh terrain;
    private TreeRenderer tree;
    private GrassCeilRenderer[] grass;
    private QuadRenderer viewport;

    private Framebuffer viewportFramebuffer;

    private int time;

    public Terrain()
    {
        camera = new Camera();
        int worldSize = 1000;
        int grassCeilCount = (int) ((float) worldSize / 50);

        worldGen = new NoiseGeneration(new Random().nextInt(), worldSize + 1);
        mainLight = new AmbientLight(new Color4f(0.8f, 0.9f, 1, 1), 0.2f);
        lights = new ArrayList<Light>();
        sunLight = new DirectionalLight(new Quat(new Vec3(0, 0, 45)), new Color4f(1, 0.8f, 0.6f, 1), 1.5f);
        lights.add(sunLight);

        sun = new SpherePrimitive(4);

        viewport = new QuadRenderer(Display.getWidth(), Display.getHeight());
        viewportFramebuffer = new Framebuffer(Display.getWidth(), Display.getHeight());

        skybox = new SkyboxRenderer(new Cubemap(SKYBOX_TEXTURES), 2);
        terrain = new TerrainMesh(worldSize, worldGen);
        tree = new TreeRenderer(1000, worldSize, worldGen);
        grass = new GrassCeilRenderer[grassCeilCount * grassCeilCount];

        for (int i = 0; i < grass.length; i++)
        {
            int x = i % grassCeilCount;
            int y = i / grassCeilCount;
            grass[i] = new GrassCeilRenderer(x, y, worldGen);
        }

        terrainShader = new Shader("res/shaders/terrain.vert", "res/shaders/terrain.frag");
        treeShader = new Shader("res/shaders/tree.vert", "res/shaders/tree.frag");
        grassShader = new Shader("res/shaders/grass.vert", "res/shaders/grass.frag");
        skyboxShader = new Shader("res/shaders/skybox.vert", "res/shaders/skybox.frag");
        guiShader = new Shader("res/shaders/sun_shafts.vert", "res/shaders/sun_shafts.frag");
        sunShader = new Shader("res/shaders/sun.vert", "res/shaders/sun.frag");
    }

    public void update()
    {
        time++;
        camera.update();
        for (int i = 0; i < grass.length; i++)
            grass[i].update(camera);
    }

    public void renderAll()
    {
        renderPostProcessing();
        renderGui();
    }

    public void renderScene(Light light, Mat4 cameraProjection)
    {
        renderTerrainGeometry(light, cameraProjection, false);
        renderTrees(light, cameraProjection, false);
        renderGrass(light, cameraProjection, false);
    }

    public void renderForward(Mat4 cameraProjection)
    {
        renderSkybox(cameraProjection);
        renderSun(cameraProjection);

        renderScene(mainLight, cameraProjection);
        enableForwardBlend();
        for (Light light : lights)
        {
            renderScene(light, cameraProjection);
        }
        disableForwardBlend();
    }

    public void renderPostProcessing()
    {
        Mat4 cameraProjection = camera.getProjection();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        renderForward(cameraProjection);

        viewportFramebuffer.bindColor();
        renderSunShafts(cameraProjection);
        viewportFramebuffer.unbind();
    }

    public void renderGui()
    {
        glDisable(GL_DEPTH_TEST);
        Mat4 cameraProjection = camera.getProjection();

        guiShader.bind();
        guiShader.setMat4(guiShader.getUniform("projectionMatrix"), Mat4.orthographic(Display.getWidth(), 0, Display.getHeight(), 0, -1, 1));
        guiShader.setMat4(guiShader.getUniform("modelViewMatrix"), Mat4.identity());
        guiShader.setMat4(guiShader.getUniform("sun_projection"), cameraProjection.mul(Mat4.translate(camera.getTransform().getPosition()).mul(Mat4.rotate(0, 0, 180 - 45).mul(Mat4.translate(20, 0, 0)))));

        glBindTexture(GL_TEXTURE_2D, viewportFramebuffer.getColorTexture());
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        viewport.render();
        glDisable(GL_BLEND);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void enableForwardBlend()
    {
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        glDepthMask(false);
        glDepthFunc(GL_EQUAL);
    }

    public void disableForwardBlend()
    {
        glDepthFunc(GL_LESS);
        glDepthMask(true);
        glDisable(GL_BLEND);
    }

    public void renderSunShafts(Mat4 cameraProjection)
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(0.2f, 0.2f, 0.2f, 1);

        renderSun(cameraProjection);
        renderTerrainGeometry(null, cameraProjection, true);
        renderTrees(null, cameraProjection, true);
//        renderGrass(null, cameraProjection, true);
    }

    public void renderTerrainGeometry(Light light, Mat4 cameraProjection, boolean black)
    {
        terrainShader.bind();
        terrainShader.setMat4(terrainShader.getUniform("projectionMatrix"), cameraProjection);
        terrainShader.setMat4(terrainShader.getUniform("modelViewMatrix"), Mat4.translate(0, 0, 0));
        terrainShader.setInt(terrainShader.getUniform("black"), black ? 1 : 0);
       // if (light != null) light.bindUniforms(terrainShader, null);
    }

    public void renderTrees(Light light, Mat4 cameraProjection, boolean black)
    {
        treeShader.bind();
        treeShader.setMat4(treeShader.getUniform("projectionMatrix"), cameraProjection);
        treeShader.setInt(terrainShader.getUniform("black"), black ? 1 : 0);
       // if (light != null) light.bindUniforms(treeShader, null);
        tree.render(treeShader, camera);
    }

    public void renderGrass(Light light, Mat4 cameraProjection, boolean black)
    {
        grassShader.bind();
        grassShader.setMat4(grassShader.getUniform("projectionMatrix"), cameraProjection);
        grassShader.setMat4(grassShader.getUniform("modelViewMatrix"), Mat4.translate(0, 0f, 0));
        grassShader.setVec3(grassShader.getUniform("cameraPosition"), camera.getTransform().getPosition());
        grassShader.setInt(terrainShader.getUniform("black"), black ? 1 : 0);
       // if (light != null) light.bindUniforms(grassShader, null);
        for (int i = 0; i < grass.length; i++)
            grass[i].render();
    }

    public void renderSkybox(Mat4 cameraProjection)
    {
        glDisable(GL_DEPTH_TEST);
        skyboxShader.bind();
        skyboxShader.setMat4(skyboxShader.getUniform("projectionMatrix"), cameraProjection);
        skyboxShader.setMat4(skyboxShader.getUniform("modelViewMatrix"), Mat4.translate(camera.getTransform().getPosition()));
        skybox.render();
        glEnable(GL_DEPTH_TEST);
    }

    public void renderSun(Mat4 cameraProjection)
    {
        glDisable(GL_DEPTH_TEST);
        sunShader.bind();
        sunShader.setMat4(sunShader.getUniform("projectionMatrix"), cameraProjection);
        sunShader.setMat4(sunShader.getUniform("modelViewMatrix"), Mat4.translate(camera.getTransform().getPosition()).mul(Mat4.rotate(0, 0, 180 - 45).mul(Mat4.translate(25, 0, 0))));
        sun.render();
        glEnable(GL_DEPTH_TEST);
    }

    public Camera getCamera()
    {
        return camera;
    }
}
