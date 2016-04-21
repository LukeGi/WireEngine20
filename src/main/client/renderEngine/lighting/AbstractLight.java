package main.client.renderEngine.lighting;

import javafx.scene.effect.Light;
import main.client.renderEngine.shader.AbstractShaderProgram;
import main.coreEngine.util.maths.Vector3f;

import static org.lwjgl.opengl.GL20.*;

/**
 * Created by Kelan on 15/04/2016.
 */
public abstract class AbstractLight
{
    public static final int TYPE_POINT = 0;
    public static final int TYPE_SPOT = 1;
    public static final int TYPE_DIRECTION = 2;

    protected Vector3f position = new Vector3f();
    protected Vector3f attenuation = new Vector3f();
    protected Vector3f direction = new Vector3f();
    protected Vector3f colour = new Vector3f();
    protected float intensity = 0.0F;
    protected float coneCutoff = 0.0F;
    private final int type;

    public AbstractLight(int type, Vector3f colour, float intensity)
    {
        this.type = type;
        this.colour = colour;
        this.intensity = intensity;
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public void setPosition(Vector3f position)
    {
        this.position = position;
    }

    public Vector3f getAttenuation()
    {
        return attenuation;
    }

    public void setAttenuation(Vector3f attenuation)
    {
        this.attenuation = attenuation;
    }

    public Vector3f getDirection()
    {
        return direction;
    }

    public void setDirection(Vector3f direction)
    {
        this.direction = direction;
    }

    public Vector3f getColour()
    {
        return colour;
    }

    public void setColour(Vector3f colour)
    {
        this.colour = colour;
    }

    public float getIntensity()
    {
        return intensity;
    }

    public void setIntensity(float intensity)
    {
        this.intensity = intensity;
    }

    public float getConeCutoff()
    {
        return coneCutoff;
    }

    public void setConeCutoff(float coneCutoff)
    {
        this.coneCutoff = coneCutoff;
    }

    public int getType()
    {
        return type;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractLight that = (AbstractLight) o;

        if (Float.compare(that.intensity, intensity) != 0) return false;
        if (Float.compare(that.coneCutoff, coneCutoff) != 0) return false;
        if (type != that.type) return false;
        if (position != null ? !position.equals(that.position) : that.position != null) return false;
        if (attenuation != null ? !attenuation.equals(that.attenuation) : that.attenuation != null) return false;
        if (direction != null ? !direction.equals(that.direction) : that.direction != null) return false;
        return colour != null ? colour.equals(that.colour) : that.colour == null;

    }

    @Override
    public int hashCode()
    {
        int result = position != null ? position.hashCode() : 0;
        result = 31 * result + (attenuation != null ? attenuation.hashCode() : 0);
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        result = 31 * result + (colour != null ? colour.hashCode() : 0);
        result = 31 * result + (intensity != +0.0f ? Float.floatToIntBits(intensity) : 0);
        result = 31 * result + (coneCutoff != +0.0f ? Float.floatToIntBits(coneCutoff) : 0);
        result = 31 * result + type;
        return result;
    }

    @Override
    public String toString()
    {
        return "AbstractLight{" + "position=" + position + ", attenuation=" + attenuation + ", direction=" + direction + ", colour=" + colour + ", intensity=" + intensity + ", coneCutoff=" + coneCutoff + ", type=" + type + '}';
    }
}
