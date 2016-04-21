package main.coreEngine;

import main.coreEngine.sceneGraph.Node;
import main.server.ServerThread;
import main.client.ClientThread;

/**
 * Created by Kelan on 01/04/2016.
 */
public interface IGameHandler extends IInitializable
{
    Node getRootNode();

    void preInit();

    void postInit();

    void onTick();

    void onRender();

    void onInput();

    ClientThread getClientThread();

    ServerThread getServerThread();

    Node getRoot();
}
