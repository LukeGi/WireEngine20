package main.server;

import main.coreEngine.Engine;
import main.coreEngine.IInitializable;
import main.coreEngine.TickableThread;

/**
 * Created by Kelan on 02/04/2016.
 */
public class ServerThread extends TickableThread implements IInitializable
{
    public ServerThread()
    {
        super ("SERVER");
    }

    @Override
    protected void tick()
    {
        Engine.getGame().onTick();
    }

    @Override
    public int getTickrate()
    {
        return 60;
    }

    @Override
    public void onGameClosed()
    {

    }

    @Override
    public void preInit()
    {

    }

    @Override
    public void postInit()
    {

    }
}
