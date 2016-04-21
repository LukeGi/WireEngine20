package main.coreEngine.util.maths;

import java.nio.FloatBuffer;

public interface ReadableVector
{

    float length();

    float lengthSquared();

    Vector store(FloatBuffer buf);
}