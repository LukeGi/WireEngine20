package main.coreEngine.sceneGraph;

/**
 * Created by Kelan on 01/04/2016.
 */
public abstract class Component
{
    public abstract void tick(Node parent);

    public abstract void render(Node parent);

    public abstract void input(Node parent);

    public abstract void onAdded(Node node);
}
