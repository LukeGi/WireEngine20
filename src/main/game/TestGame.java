package main.game;

import main.client.InputHandler;
import main.client.renderEngine.RenderEngine;
import main.client.renderEngine.lighting.PointLight;
import main.client.renderEngine.mesh.*;
import main.coreEngine.*;
import main.coreEngine.sceneGraph.components.CameraComponent;
import main.coreEngine.sceneGraph.Node;
import main.coreEngine.sceneGraph.Transformation;
import main.coreEngine.sceneGraph.components.ControllerComponent;
import main.coreEngine.sceneGraph.components.LightComponent;
import main.coreEngine.sceneGraph.components.ModelComponent;
import main.coreEngine.sceneGraph.nodes.NodePlayer;
import main.coreEngine.util.maths.Vector3f;
import main.server.ServerThread;
import main.client.ClientThread;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Kelan on 01/04/2016.
 */
public class TestGame implements IGameHandler
{
    private Node rootNode = new Node();
    @Client
    private ClientThread clientThread;
    @Server
    private ServerThread serverThread;
    @Observer
    private CameraComponent camera;

    private Node test;
    private Node test1;
    private Node test2;
    private Node test3;
    private Node test4;
    private Node player;

    @Override
    public Node getRootNode()
    {
        return rootNode;
    }

    @Override
    public void preInit()
    {
        System.out.println("Pre-initialising game on thread " + Thread.currentThread().getName());
    }

    @Override
    public void postInit()
    {
        System.out.println("Post-initialising game on thread " + Thread.currentThread().getName());

        test = new Node(new Transformation(new Vector3f(4.0F, 0.0F, 0.0F)));
        test1 = new Node(new Transformation(new Vector3f(0.0F, 0.0F, -3.0F)).setScale(new Vector3f(1.0F, 1.0F, 1.0F)));
        test2 = new Node(new Transformation(new Vector3f(0.0F, 0.0F, 0.0F)).setScale(new Vector3f(100.0F, 100.0F, 100.0F)));
        test3 = new Node(new Transformation(new Vector3f(-2.0F, 1.0F, 6.0F)).setScale(new Vector3f(3.0F, 3.0F, 3.0F)));
        test4 = new Node(new Transformation(new Vector3f(2.0F, 1.0F, 2.0F)));
        player = new NodePlayer();

        player.addComponent(camera).addComponent(new ControllerComponent(2.0F, 1.2F, 0.0F)).getTransform().setPosition(new Vector3f(0.0F, 2.0F, 0.0F));

        LightComponent light = new LightComponent(new PointLight(new Vector3f(), new Vector3f(1.0F, 0.01F, 0.004F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F), new Vector3f(0.0F, -0.5F, 0.0F));
        test4.addComponent(light);

        test.addComponent(MeshBuilder.compile("bunny"));
        test1.addComponent(MeshBuilder.compile("stall"));
        test2.addComponent(new ModelComponent(MeshFactory.getNewQuadPlane(), Texture.loadTexture("white"), null));
        test3.addComponent(new ModelComponent(MeshFactory.getNewQuadPlane(), Texture.loadTexture("wall"), null));

        rootNode.addChild(player).addChild(test4).addChild(test3).addChild(test2).addChild(test);
    }

    @Override
    public void onTick()
    {
        rootNode.tick(null);
    }

    @Override
    public void onRender()
    {
        test.getTransform().incrRotation(new Vector3f(0.0F, 0.1F, 0.0F));
        test2.getTransform().incrRotation(new Vector3f(0.0F, 0.1F, 0.0F));
        test3.getTransform().incrRotation(new Vector3f(-0.3F, 0.0F, 0.0F));
        rootNode.render(null);
    }

    @Override
    public void onInput()
    {
        rootNode.input(null);

        if (InputHandler.keyPressed(GLFW_KEY_F1))
            getRenderer().setWireframeMode(!getRenderer().isWireframeMode());
    }

    public ClientThread getClientThread()
    {
        return clientThread;
    }

    public ServerThread getServerThread()
    {
        return serverThread;
    }

    @Override
    public Node getRoot()
    {
        return rootNode;
    }

    public RenderEngine getRenderer()
    {
        return clientThread.getRenderEngine();
    }
}
