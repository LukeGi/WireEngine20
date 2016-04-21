package main.coreEngine.sceneGraph.nodes;

import main.coreEngine.Engine;
import main.coreEngine.sceneGraph.Node;
import main.coreEngine.sceneGraph.components.ControllerComponent;

/**
 * Created by Kelan on 12/04/2016.
 */
public class NodePlayer extends Node
{
    public NodePlayer()
    {
        addComponent(Engine.getObserver());
        addComponent(new ControllerComponent(2.5F, 0.3F, 2.0F));
    }
}
