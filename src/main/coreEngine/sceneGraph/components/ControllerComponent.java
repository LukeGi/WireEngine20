package main.coreEngine.sceneGraph.components;

import main.client.InputHandler;
import main.client.renderEngine.RenderEngine;
import main.coreEngine.sceneGraph.Component;
import main.coreEngine.sceneGraph.Node;
import main.coreEngine.util.maths.Vector3f;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Kelan on 12/04/2016.
 */
public class ControllerComponent extends Component
{
    private float mouseSensitivity;
    private float moveSpeed;
    private float jumpForce;
    private float acceleration;

    private float currentMoveVelocity = 0.0F;

    private Vector3f position = new Vector3f();
    private Vector3f rotation = new Vector3f();

    public ControllerComponent(float mouseSensitivity, float moveSpeed, float jumpForce)
    {
        this.mouseSensitivity = mouseSensitivity;
        this.moveSpeed = moveSpeed;
        this.jumpForce = jumpForce;

        acceleration = 4F;
    }

    @Override
    public void tick(Node parent)
    {

    }

    @Override
    public void render(Node parent)
    {

    }

    @Override
    public void input(Node parent)
    {
        if (!InputHandler.isCursorGrabbed())
            InputHandler.grabCursor(true);

        float moveSpeed = ((1.0F / (RenderEngine.getCurrentFPS() > 0 ? RenderEngine.getCurrentFPS() : 60)) * this.moveSpeed);
        float mouseSpeed = (1.0F / (RenderEngine.getCurrentFPS() > 0 ? RenderEngine.getCurrentFPS() : 60)) * this.mouseSensitivity;
        float acceleration;

        if (this.acceleration > 0)
            acceleration = this.acceleration / (RenderEngine.getCurrentFPS() > 0 ? RenderEngine.getCurrentFPS() : 60);
        else
            acceleration = 1.0F;

        if (InputHandler.keyDown(GLFW.GLFW_KEY_W) || InputHandler.keyDown(GLFW.GLFW_KEY_S) || InputHandler.keyDown(GLFW.GLFW_KEY_A) || InputHandler.keyDown(GLFW.GLFW_KEY_D))
        {
            currentMoveVelocity += acceleration;

            if (currentMoveVelocity > 1.0F)
                currentMoveVelocity = 1.0F;

            if (InputHandler.keyDown(GLFW.GLFW_KEY_W))
            {
                position.x += currentMoveVelocity * moveSpeed * (Math.cos(Math.toRadians(parent.getTransform().getRotation().y - 90)));
                position.z += currentMoveVelocity * moveSpeed * (Math.sin(Math.toRadians(parent.getTransform().getRotation().y - 90)));
            }
            if (InputHandler.keyDown(GLFW.GLFW_KEY_S))
            {
                position.x += currentMoveVelocity * moveSpeed * (Math.cos(Math.toRadians(parent.getTransform().getRotation().y + 90)));
                position.z += currentMoveVelocity * moveSpeed * (Math.sin(Math.toRadians(parent.getTransform().getRotation().y + 90)));
            }
            if (InputHandler.keyDown(GLFW.GLFW_KEY_A))
            {
                position.x += currentMoveVelocity * moveSpeed * (Math.cos(Math.toRadians(parent.getTransform().getRotation().y - 180)));
                position.z += currentMoveVelocity * moveSpeed * (Math.sin(Math.toRadians(parent.getTransform().getRotation().y - 180)));
            }
            if (InputHandler.keyDown(GLFW.GLFW_KEY_D))
            {
                position.x += currentMoveVelocity * moveSpeed * (Math.cos(Math.toRadians(parent.getTransform().getRotation().y + 0)));
                position.z += currentMoveVelocity * moveSpeed * (Math.sin(Math.toRadians(parent.getTransform().getRotation().y + 0)));
            }
        } else
        {
            currentMoveVelocity -= acceleration;

            if (currentMoveVelocity < 0.0F)
                currentMoveVelocity = 0.0F;
            if (currentMoveVelocity > 1.0F)
                currentMoveVelocity = 1.0F;

            position.scale(currentMoveVelocity);
        }

        if (position.length() > this.moveSpeed)
            position.setLength(this.moveSpeed);

        rotation.y += InputHandler.cursorPosition().x * mouseSpeed;
        rotation.x += InputHandler.cursorPosition().y * mouseSpeed;

        parent.getTransform().incrPosition(position);
        parent.getTransform().incrRotation(rotation);

        rotation = new Vector3f();
    }

    @Override
    public void onAdded(Node node)
    {

    }
}
