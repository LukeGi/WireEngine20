package main.client;

import main.client.renderEngine.RenderEngine;
import main.coreEngine.Engine;
import main.coreEngine.IInitializable;
import main.coreEngine.TickableThread;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Kelan on 01/04/2016.
 */
public class ClientThread extends TickableThread implements IInitializable
{
    private RenderEngine renderEngine;

    public ClientThread()
    {
        super("CLIENT");
    }

    @Override
    protected void tick()
    {
        Engine.getGame().onRender();
        renderEngine.render();
//        if (InputHandler.keyDown(GLFW.GLFW_KEY_SPACE))
//            System.out.println("Space key down");
//
//        if (InputHandler.keyReleased(GLFW.GLFW_KEY_SPACE))
//            System.out.println("Space key released");
//
//        if (InputHandler.keyPressed(GLFW.GLFW_KEY_SPACE))
//            System.out.println("Space key pressed");
    }

    @Override
    public int getTickrate()
    {
        return 120;
    }

    @Override
    public void onGameClosed()
    {

    }

    @Override
    public void preInit()
    {
        Window.setupWindow(1600, 900, "WireEngine 2.0");
        System.out.println("Setting up window on thread " + Thread.currentThread().getName());
        Engine.getGame().preInit();
        renderEngine = new RenderEngine();
        renderEngine.preInit();
    }

    public RenderEngine getRenderEngine()
    {
        return renderEngine;
    }

    @Override
    public void postInit()
    {
        Engine.getGame().postInit();
        renderEngine.postInit();
    }
}
