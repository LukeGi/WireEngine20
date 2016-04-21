package main.client.renderEngine.mesh;

import main.coreEngine.util.maths.Vector2f;
import main.coreEngine.util.maths.Vector3f;

/**
 * Created by Kelan on 08/04/2016.
 */
public class Vertex
{
    private Vector3f normal;
    private Vector2f texture;
    private Colour colour;
    private Vector3f position;

    private boolean unfinished = false;
    private int index;

    public Vertex(Vector3f normal, Vector2f texture, Colour colour, Vector3f position)
    {
        this.normal = normal;
        this.texture = texture;
        this.colour = colour;
        this.position = position;
    }

    public Vertex(Vector3f position)
    {
        this(null, null, null, position);
    }

    public Vertex()
    {
        this(null);
        unfinished = true;
    }

    public Vector3f getNormal()
    {
        return normal;
    }

    public Vertex setNormal(Vector3f normal)
    {
        this.normal = normal;
        return this;
    }

    public Vector2f getTexture()
    {
        return texture;
    }

    public Vertex setTexture(Vector2f texture)
    {
        this.texture = texture;
        return this;
    }

    public Colour getColour()
    {
        return colour;
    }

    public Vertex setColour(Colour colour)
    {
        this.colour = colour;
        return this;
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public Vertex setPosition(Vector3f position)
    {
        this.position = position;
        return this;
    }

    public Vertex setIndex(int index)
    {
        this.index = index;
        return this;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex vertex = (Vertex) o;

        if (unfinished != vertex.unfinished) return false;
        if (index != vertex.index) return false;
        if (normal != null ? !normal.equals(vertex.normal) : vertex.normal != null) return false;
        if (texture != null ? !texture.equals(vertex.texture) : vertex.texture != null) return false;
        if (colour != null ? !colour.equals(vertex.colour) : vertex.colour != null) return false;
        return position != null ? position.equals(vertex.position) : vertex.position == null;

    }

    @Override
    public int hashCode()
    {
        int result = normal != null ? normal.hashCode() : 0;
        result = 31 * result + (texture != null ? texture.hashCode() : 0);
        result = 31 * result + (colour != null ? colour.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (unfinished ? 1 : 0);
        result = 31 * result + index;
        return result;
    }

    @Override
    public String toString()
    {
        return "Vertex{" + "normal=" + normal + ", texture=" + texture + ", colour=" + colour + ", position=" + position + ", unfinished=" + unfinished + ", index=" + index + '}';
    }
}
