package main.coreEngine.sceneGraph.components;

import main.client.InputHandler;
import main.client.Window;
import main.client.renderEngine.RenderEngine;
import main.coreEngine.sceneGraph.Component;
import main.coreEngine.sceneGraph.Node;
import main.coreEngine.util.maths.MathUtil;
import main.coreEngine.util.maths.Matrix4f;
import main.coreEngine.util.maths.Vector3f;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Kelan on 10/04/2016.
 */
public class CameraComponent extends Component
{

    private Matrix4f viewMatrix;
    private Vector3f position;

    @Override
    public void tick(Node parent)
    {

    }

    @Override
    public void render(Node parent)
    {
        viewMatrix = new Matrix4f();
        Matrix4f.rotate(MathUtil.toRadiansf(parent.getTransform().getRotation().x), new Vector3f(1.0F, 0.0F, 0.0F), viewMatrix, viewMatrix);
        Matrix4f.rotate(MathUtil.toRadiansf(parent.getTransform().getRotation().y), new Vector3f(0.0F, 1.0F, 0.0F), viewMatrix, viewMatrix);
        Matrix4f.rotate(MathUtil.toRadiansf(parent.getTransform().getRotation().z), new Vector3f(0.0F, 0.0F, 1.0F), viewMatrix, viewMatrix);
        Matrix4f.translate(parent.getTransform().getPosition().negate(null), viewMatrix, viewMatrix);
        position = parent.getTransform().getPosition();
    }

    @Override
    public void input(Node parent)
    {

    }

    @Override
    public void onAdded(Node node)
    {

    }

    public Matrix4f getMatrix()
    {
        return viewMatrix;
    }

    public Vector3f getPosition()
    {
        return position;
    }
}
