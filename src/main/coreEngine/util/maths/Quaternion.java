package main.coreEngine.util.maths;

import java.nio.FloatBuffer;

public class Quaternion extends Vector implements ReadableVector4f
{

    public float x, y, z, w;

    public Quaternion()
    {
        super();
        setIdentity();
    }

    public Quaternion(ReadableVector4f src)
    {
        set(src);
    }

    public Quaternion(float x, float y, float z, float w)
    {
        set(x, y, z, w);
    }

    public void set(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public void set(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Quaternion set(ReadableVector4f src)
    {
        x = src.getX();
        y = src.getY();
        z = src.getZ();
        w = src.getW();
        return this;
    }

    public Quaternion setIdentity()
    {
        return setIdentity(this);
    }

    public static Quaternion setIdentity(Quaternion q)
    {
        q.x = 0;
        q.y = 0;
        q.z = 0;
        q.w = 1;
        return q;
    }

    public float lengthSquared()
    {
        return x * x + y * y + z * z + w * w;
    }

    public static Quaternion normalise(Quaternion src, Quaternion dest)
    {
        float inv_l = 1f / src.length();

        if (dest == null)
            dest = new Quaternion();

        dest.set(src.x * inv_l, src.y * inv_l, src.z * inv_l, src.w * inv_l);

        return dest;
    }

    public Quaternion normalise(Quaternion dest)
    {
        return normalise(this, dest);
    }

    public static float dot(Quaternion left, Quaternion right)
    {
        return left.x * right.x + left.y * right.y + left.z * right.z + left.w * right.w;
    }

    public Quaternion negate(Quaternion dest)
    {
        return negate(this, dest);
    }

    public static Quaternion negate(Quaternion src, Quaternion dest)
    {
        if (dest == null)
            dest = new Quaternion();

        dest.x = -src.x;
        dest.y = -src.y;
        dest.z = -src.z;
        dest.w = src.w;

        return dest;
    }

    public Vector negate()
    {
        return negate(this, this);
    }

    public Vector load(FloatBuffer buf)
    {
        x = buf.get();
        y = buf.get();
        z = buf.get();
        w = buf.get();
        return this;
    }

    public Vector scale(float scale)
    {
        return scale(scale, this, this);
    }

    public static Quaternion scale(float scale, Quaternion src, Quaternion dest)
    {
        if (dest == null)
            dest = new Quaternion();
        dest.x = src.x * scale;
        dest.y = src.y * scale;
        dest.z = src.z * scale;
        dest.w = src.w * scale;
        return dest;
    }

    public Vector store(FloatBuffer buf)
    {
        buf.put(x);
        buf.put(y);
        buf.put(z);
        buf.put(w);

        return this;
    }

    public final float getX()
    {
        return x;
    }

    public final float getY()
    {
        return y;
    }

    public final void setX(float x)
    {
        this.x = x;
    }

    public final void setY(float y)
    {
        this.y = y;
    }

    public void setZ(float z)
    {
        this.z = z;
    }

    public float getZ()
    {
        return z;
    }

    public void setW(float w)
    {
        this.w = w;
    }

    public float getW()
    {
        return w;
    }

    public String toString()
    {
        return "Quaternion: " + x + " " + y + " " + z + " " + w;
    }

    public static Quaternion mul(Quaternion left, Quaternion right, Quaternion dest)
    {
        if (dest == null)
            dest = new Quaternion();

        dest.set(left.x * right.w + left.w * right.x + left.y * right.z - left.z * right.y, left.y * right.w + left.w * right.y + left.z * right.x - left.x * right.z, left.z * right.w + left.w * right.z + left.x * right.y - left.y * right.x, left.w * right.w - left.x * right.x - left.y * right.y - left.z * right.z);

        return dest;
    }

    public static Quaternion mulInverse(Quaternion left, Quaternion right,
                                        Quaternion dest)
    {
        float n = right.lengthSquared();

        n = (n == 0.0 ? n : 1 / n);

        if (dest == null)
            dest = new Quaternion();

        dest.set((left.x * right.w - left.w * right.x - left.y * right.z + left.z * right.y) * n, (left.y * right.w - left.w * right.y - left.z * right.x + left.x * right.z) * n, (left.z * right.w - left.w * right.z - left.x * right.y + left.y * right.x) * n, (left.w * right.w + left.x * right.x + left.y * right.y + left.z * right.z) * n);

        return dest;
    }

    public final void setFromAxisAngle(Vector4f a1)
    {
        x = a1.x;
        y = a1.y;
        z = a1.z;

        float n = (float) Math.sqrt(x * x + y * y + z * z);
        float s = (float) (Math.sin(0.5 * a1.w) / n);

        x *= s;
        y *= s;
        z *= s;
        w = (float) Math.cos(0.5 * a1.w);
    }

    public final Quaternion setFromMatrix(Matrix4f m)
    {
        return setFromMatrix(m, this);
    }

    public static Quaternion setFromMatrix(Matrix4f m, Quaternion q)
    {
        return q.setFromMat(m.m00, m.m01, m.m02, m.m10, m.m11, m.m12, m.m20, m.m21, m.m22);
    }

    public final Quaternion setFromMatrix(Matrix3f m)
    {
        return setFromMatrix(m, this);
    }

    public static Quaternion setFromMatrix(Matrix3f m, Quaternion q)
    {
        return q.setFromMat(m.m00, m.m01, m.m02, m.m10, m.m11, m.m12, m.m20, m.m21, m.m22);
    }

    private Quaternion setFromMat(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22)
    {

        float s;
        float tr = m00 + m11 + m22;
        if (tr >= 0.0)
        {
            s = (float) Math.sqrt(tr + 1.0);
            w = s * 0.5f;
            s = 0.5f / s;
            x = (m21 - m12) * s;
            y = (m02 - m20) * s;
            z = (m10 - m01) * s;
        } else
        {
            float max = Math.max(Math.max(m00, m11), m22);
            if (max == m00)
            {
                s = (float) Math.sqrt(m00 - (m11 + m22) + 1.0);
                x = s * 0.5f;
                s = 0.5f / s;
                y = (m01 + m10) * s;
                z = (m20 + m02) * s;
                w = (m21 - m12) * s;
            } else if (max == m11)
            {
                s = (float) Math.sqrt(m11 - (m22 + m00) + 1.0);
                y = s * 0.5f;
                s = 0.5f / s;
                z = (m12 + m21) * s;
                x = (m01 + m10) * s;
                w = (m02 - m20) * s;
            } else
            {
                s = (float) Math.sqrt(m22 - (m00 + m11) + 1.0);
                z = s * 0.5f;
                s = 0.5f / s;
                x = (m20 + m02) * s;
                y = (m12 + m21) * s;
                w = (m10 - m01) * s;
            }
        }
        return this;
    }
}