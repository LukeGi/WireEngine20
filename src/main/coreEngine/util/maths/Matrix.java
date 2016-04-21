package main.coreEngine.util.maths;

import java.io.Serializable;
import java.nio.FloatBuffer;

public abstract class Matrix implements Serializable
{
    protected Matrix()
    {
        super();
    }

    public abstract Matrix setIdentity();

    public abstract Matrix invert();

    public abstract Matrix load(FloatBuffer buf);

    public abstract Matrix loadTranspose(FloatBuffer buf);

    public abstract Matrix negate();

    public abstract Matrix store(FloatBuffer buf);

    public abstract Matrix storeTranspose(FloatBuffer buf);

    public FloatBuffer storeFlipped(FloatBuffer buf)
    {
        store(buf);
        buf.flip();
        return buf;
    }

    public FloatBuffer storeTransposeFlipped(FloatBuffer buf)
    {
        storeTranspose(buf);
        buf.flip();
        return buf;
    }

    public abstract Matrix transpose();

    public abstract Matrix setZero();

    public abstract float determinant();
}
