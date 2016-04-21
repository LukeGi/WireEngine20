package main.client.renderEngine.mesh;

import main.coreEngine.util.maths.Vector4f;

/**
 * Created by Kelan on 08/04/2016.
 */
public class Colour
{
    private Vector4f colour;

    public Colour(float r, float g, float b, float a)
    {
        this(new Vector4f(r, g, b, a));
    }

    public Colour(Vector4f colour)
    {
        this.colour = colour;
    }

    public Vector4f getRGBA()
    {
        return colour;
    }

    public void setColour(Vector4f colour)
    {
        this.colour = colour;
    }

    public void setRGBA(int rgba)
    {
        float r = (float) ((rgba & 0xFF000000) >> 24) / 255.0F;
        float g = (float) ((rgba & 0x00FF0000) >> 16) / 255.0F;
        float b = (float) ((rgba & 0x0000FF00) >> 8) / 255.0F;
        float a = (float) ((rgba & 0x000000FF)) / 255.0F;

        this.colour.set(r, g, b, a);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Colour colour1 = (Colour) o;

        return colour != null ? colour.equals(colour1.colour) : colour1.colour == null;
    }

    @Override
    public int hashCode()
    {
        return colour != null ? colour.hashCode() : 0;
    }

    @Override
    public String toString()
    {
        return "Colour{" + "colour=" + colour.toString().replace("x=", "r=").replace("y=", "g=").replace("z=", "b=").replace("w=", "a=") + '}';
    }
}
