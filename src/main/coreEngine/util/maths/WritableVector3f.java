package main.coreEngine.util.maths;

public interface WritableVector3f extends WritableVector2f
{
    void setZ(float z);

    void set(float x, float y, float z);
}