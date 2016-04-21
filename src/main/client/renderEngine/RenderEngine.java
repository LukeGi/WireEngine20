package main.client.renderEngine;

import main.client.ClientThread;
import main.client.Window;
import main.client.renderEngine.lighting.AbstractLight;
import main.client.renderEngine.mesh.Material;
import main.client.renderEngine.mesh.Texture;
import main.client.renderEngine.mesh.Mesh;
import main.client.renderEngine.shader.Shader;
import main.client.renderEngine.shader.ShaderProgramGeneric;
import main.coreEngine.Engine;
import main.coreEngine.IInitializable;
import main.coreEngine.sceneGraph.Transformation;
import main.coreEngine.util.maths.MathUtil;
import main.coreEngine.util.maths.Matrix4f;
import main.coreEngine.util.maths.Vector3f;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;

/**
 * Created by Kelan on 04/04/2016.
 */
public class RenderEngine implements IInitializable
{
    private ShaderProgramGeneric shader;
    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private List<ModelData> toRender = new ArrayList<>();
    private boolean wireframeMode = false;

    public static float FOV = 70.0F;
    public static float FAR_PLANE = 1000.0F;
    public static float NEAR_PLANE = (1.0F / FAR_PLANE) + 0.0005F;
    private List<AbstractLight> lights = new ArrayList<>();

    public static float getCurrentFPS()
    {
        return Engine.getGame().getClientThread().getUpdateFrequency();
    }

    public void render()
    {
        glClearColor(0.5F, 0.5F, 0.5F, 1.0F);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        setupProjection3D();
        render3D();
        setupProjection2D();
        render2D();

        Window.update();

        toRender.clear();
        lights.clear();
    }

    private void setupProjection3D()
    {
        glEnable(GL_DEPTH_TEST);

        if (wireframeMode)
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        projectionMatrix.setIdentity();
        projectionMatrix.m00 = (MathUtil.cotf(MathUtil.toRadiansf(FOV * 0.5F))) / (Window.getAspect());
        projectionMatrix.m11 = (MathUtil.cotf(MathUtil.toRadiansf(FOV * 0.5F)));
        projectionMatrix.m22 = -(FAR_PLANE + NEAR_PLANE) / (FAR_PLANE - NEAR_PLANE);
        projectionMatrix.m23 = -1.0F;
        projectionMatrix.m32 = -(2.0F * FAR_PLANE * NEAR_PLANE) / (FAR_PLANE - NEAR_PLANE);
        projectionMatrix.m33 = 0.0F;

        viewMatrix = Engine.getObserver().getMatrix();
    }

    private void setupProjection2D()
    {
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }

    private void render3D()
    {
        shader.useProgram(true);
        shader.setUniform("projectionMatrix", projectionMatrix);
        shader.setUniform("viewMatrix", viewMatrix);
        shader.setUniform("useFaceColours", true);
        shader.setUniform("cameraPos", Engine.getObserver().getPosition());
        shader.setUniform("minBrightness", 0.1F);
        shader.setUniform("lightModel", true);
        shader.setUniform("width", Window.getWidth());
        shader.setUniform("height", Window.getHeight());

        for (AbstractLight light : lights)
        {
            shader.setUniform("light", light);
        }

        for (ModelData m : toRender)
        {
            shader.setUniform("modelMatrix", m.getTransform().getMatrix());
            glEnable(GL_DEPTH_TEST);
            glEnable(GL_BLEND);
            glBlendEquation(GL_FUNC_ADD);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glEnable(GL_ALPHA_TEST);
            glEnable(GL_SAMPLE_ALPHA_TO_COVERAGE);
            glEnable(GL_TEXTURE_2D);

            m.getMesh().draw(shader, m.getTexture());

            glDisable(GL_TEXTURE_2D);
            glDisable(GL_BLEND);
            glDisable(GL_DEPTH_TEST);
        }

    }

    public void send(Mesh mesh, Texture texture, Material material, Transformation transform)
    {
        toRender.add(new ModelData(mesh, texture, material, transform));
    }

    public void send(AbstractLight light)
    {
        lights.add(light);
    }

    private void render2D()
    {
        shader.useProgram(false);
    }

    @Override
    public void preInit()
    {
        this.shader = new ShaderProgramGeneric(new Shader(Shader.VERTEX_SHADER, new File("src/resources/shaders/generic/GenericVert.glsl")), new Shader(Shader.GEOMETRY_SHADER, new File("src/resources/shaders/generic/GenericGeom.glsl")), new Shader(Shader.FRAGMENT_SHADER, new File("src/resources/shaders/generic/GenericFrag.glsl")));
        projectionMatrix = new Matrix4f();
    }

    @Override
    public void postInit()
    {

    }

    public boolean isWireframeMode()
    {
        return wireframeMode;
    }

    public void setWireframeMode(boolean wireframeMode)
    {
        this.wireframeMode = wireframeMode;
    }

    private class ModelData
    {
        private Mesh mesh;
        private Texture texture;
        private Material material;
        private Transformation transform;

        public ModelData(Mesh mesh, Texture texture, Material material, Transformation transform)
        {
            this.mesh = mesh;
            this.texture = texture;
            this.material = material;
            this.transform = transform;
        }

        public Mesh getMesh()
        {
            return mesh;
        }

        public void setMesh(Mesh mesh)
        {
            this.mesh = mesh;
        }

        public Texture getTexture()
        {
            return texture;
        }

        public void setTexture(Texture texture)
        {
            this.texture = texture;
        }

        public Material getMaterial()
        {
            return material;
        }

        public void setMaterial(Material material)
        {
            this.material = material;
        }

        public Transformation getTransform()
        {
            return transform;
        }

        public void setTransform(Transformation transform)
        {
            this.transform = transform;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ModelData modelData = (ModelData) o;

            if (mesh != null ? !mesh.equals(modelData.mesh) : modelData.mesh != null) return false;
            if (texture != null ? !texture.equals(modelData.texture) : modelData.texture != null) return false;
            if (material != null ? !material.equals(modelData.material) : modelData.material != null) return false;
            return transform != null ? transform.equals(modelData.transform) : modelData.transform == null;

        }

        @Override
        public int hashCode()
        {
            int result = mesh != null ? mesh.hashCode() : 0;
            result = 31 * result + (texture != null ? texture.hashCode() : 0);
            result = 31 * result + (material != null ? material.hashCode() : 0);
            result = 31 * result + (transform != null ? transform.hashCode() : 0);
            return result;
        }

        @Override
        public String toString()
        {
            return "ModelData{" + "mesh=" + mesh + ", texture=" + texture + ", material=" + material + ", transform=" + transform + '}';
        }
    }
}
