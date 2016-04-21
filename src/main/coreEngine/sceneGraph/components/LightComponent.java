package main.coreEngine.sceneGraph.components;

import main.client.renderEngine.RenderEngine;
import main.client.renderEngine.lighting.AbstractLight;
import main.coreEngine.Engine;
import main.coreEngine.sceneGraph.Component;
import main.coreEngine.sceneGraph.Node;
import main.coreEngine.util.maths.Vector3f;

/**
 * Created by Kelan on 16/04/2016.
 */
public class LightComponent extends Component
{
    private AbstractLight light;
    private Vector3f offset;

    public LightComponent(AbstractLight light, Vector3f offset)
    {
        this.light = light;
        this.offset = offset;
    }

    public LightComponent(AbstractLight light)
    {
        this(light, new Vector3f());
    }

    @Override
    public void tick(Node parent)
    {

    }

    @Override
    public void render(Node parent)
    {
        light.setPosition(Vector3f.add(parent.getTransform().getPosition(), offset, null));
        Engine.getGame().getClientThread().getRenderEngine().send(light);
    }

    @Override
    public void input(Node parent)
    {

    }

    @Override
    public void onAdded(Node node)
    {
        light.setPosition(Vector3f.add(node.getTransform().getPosition(), offset, null));
    }
}
