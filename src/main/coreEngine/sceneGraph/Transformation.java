package main.coreEngine.sceneGraph;

import main.coreEngine.util.maths.MathUtil;
import main.coreEngine.util.maths.Matrix4f;
import main.coreEngine.util.maths.Vector3f;

/**
 * Created by Kelan on 10/04/2016.
 */
public class Transformation
{
    public Vector3f position = new Vector3f();
    public Vector3f rotation = new Vector3f();
    public Vector3f scale = new Vector3f(1.0F, 1.0F, 1.0F);

    public Transformation(Vector3f position, Vector3f rotation, Vector3f scale)
    {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Transformation(Vector3f position, Vector3f rotation)
    {
        this(position, rotation, new Vector3f(1.0F, 1.0F, 1.0F));
    }

    public Transformation(Vector3f position)
    {
        this(position, new Vector3f());
    }

    public Transformation()
    {
        this(new Vector3f(), new Vector3f());
    }

    public Vector3f getPosition()
    {
        return position;
    }


    public Transformation setPosition(Vector3f position)
    {
        this.position = position;
        return this;
    }

    public Transformation incrPosition(Vector3f distance)
    {
        Vector3f.add(position, distance, position);
        return this;
    }

    public Vector3f getRotation()
    {
        return rotation;
    }

    public Transformation setRotation(Vector3f rotation)
    {
        this.rotation = rotation;
        return this;
    }

    public Transformation incrRotation(Vector3f distance)
    {
        Vector3f.add(rotation, distance, rotation);
        return this;
    }

    public Vector3f getScale()
    {
        return scale;
    }

    public Transformation setScale(Vector3f scale)
    {
        this.scale = scale;
        return this;
    }

    public Transformation incrScale(Vector3f distance)
    {
        Vector3f.add(scale, distance, scale);
        return this;
    }

    public Matrix4f getMatrix()
    {
        Matrix4f m = new Matrix4f();
        m.setIdentity();
        Matrix4f.translate(getPosition(), m, m);
        Matrix4f.rotate(MathUtil.toRadiansf(getRotation().x), new Vector3f(1.0F, 0.0F, 0.0F), m, m);
        Matrix4f.rotate(MathUtil.toRadiansf(getRotation().y), new Vector3f(0.0F, 1.0F, 0.0F), m, m);
        Matrix4f.rotate(MathUtil.toRadiansf(getRotation().z), new Vector3f(0.0F, 0.0F, 1.0F), m, m);
        Matrix4f.scale(getScale(), m, m);

        return m;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transformation that = (Transformation) o;

        if (position != null ? !position.equals(that.position) : that.position != null) return false;
        if (rotation != null ? !rotation.equals(that.rotation) : that.rotation != null) return false;
        return scale != null ? scale.equals(that.scale) : that.scale == null;

    }

    @Override
    public int hashCode()
    {
        int result = position != null ? position.hashCode() : 0;
        result = 31 * result + (rotation != null ? rotation.hashCode() : 0);
        result = 31 * result + (scale != null ? scale.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "Transformation{" + "position=" + position + ", rotation=" + rotation + ", scale=" + scale + '}';
    }
}