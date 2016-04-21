package main.coreEngine.sceneGraph;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kelan on 01/04/2016.
 */
public class Node
{
    private List<Node> children = new ArrayList<>();
    private List<Component> components = new ArrayList<>();
    private Transformation transform;

    public Node(Transformation transform)
    {
        this.transform = transform;
    }

    public Node()
    {
        this(new Transformation());
    }

    public Node addChild(Node child)
    {
        this.children.add(child);
        return this;
    }

    public Node removeChild(Node child)
    {
        this.children.remove(child);
        return this;
    }

    public Node addComponent(Component component)
    {
        this.components.add(component);
        component.onAdded(this);
        return this;
    }

    public Node removeComponent(Component component)
    {
        this.components.remove(component);
        return this;
    }

    public void tick(Node parent)
    {
        for (Node child : children)
            child.tick(this);

        for (Component component : components)
            component.tick(this);
    }

    public void render(Node parent)
    {
        for (Node child : children)
            child.render(this);

        for (Component component : components)
            component.render(this);
    }

    public void input(Node parent)
    {
        for (Node child : children)
            child.input(this);

        for (Component component : components)
            component.input(this);
    }

    public Transformation getTransform()
    {
        return transform;
    }

    public Node setTransform(Transformation transform)
    {
        this.transform = transform;
        return this;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (children != null ? !children.equals(node.children) : node.children != null) return false;
        if (components != null ? !components.equals(node.components) : node.components != null) return false;
        return transform != null ? transform.equals(node.transform) : node.transform == null;

    }

    @Override
    public int hashCode()
    {
        int result = children != null ? children.hashCode() : 0;
        result = 31 * result + (components != null ? components.hashCode() : 0);
        result = 31 * result + (transform != null ? transform.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "Node{" + "children=" + children + ", components=" + components + ", transform=" + transform + '}';
    }
}
