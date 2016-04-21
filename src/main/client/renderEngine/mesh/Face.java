package main.client.renderEngine.mesh;

import main.coreEngine.util.maths.Vector3f;

/**
 * Created by Kelan on 08/04/2016.
 */
public class Face
{
    private Vertex v1;
    private Vertex v2;
    private Vertex v3;
    private Vector3f normal;
    private Colour overrideColour;
    private boolean useFaceNormal;

    public Face(Vertex e1, Vertex e2, Vertex e3)
    {
        this.v1 = e1;
        this.v2 = e2;
        this.v3 = e3;

        normal = Vector3f.cross(Vector3f.sub(v2.getPosition(), v1.getPosition(), null), Vector3f.sub(v3.getPosition(), v1.getPosition(), null), null);
    }

    public Vertex[] getVertices()
    {
        return new Vertex[]{v1, v2, v3};
    }

    public boolean adjacent(Face other)
    {
        for (Vertex v1 : getVertices())
        {
            for (Vertex v2 : other.getVertices())
            {
                if (v1.equals(v2))
                    return true;
            }
        }

        return false;
    }

    public Face setUseFaceNormal(boolean useFaceNormal)
    {
        this.useFaceNormal = useFaceNormal;
        return this;
    }

    public Face setOverrideColour(Colour overrideColour)
    {
        this.overrideColour = overrideColour;
        return this;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Face face = (Face) o;

        if (useFaceNormal != face.useFaceNormal) return false;
        if (v1 != null ? !v1.equals(face.v1) : face.v1 != null) return false;
        if (v2 != null ? !v2.equals(face.v2) : face.v2 != null) return false;
        if (v3 != null ? !v3.equals(face.v3) : face.v3 != null) return false;
        if (normal != null ? !normal.equals(face.normal) : face.normal != null) return false;
        return overrideColour != null ? overrideColour.equals(face.overrideColour) : face.overrideColour == null;

    }

    @Override
    public int hashCode()
    {
        int result = v1 != null ? v1.hashCode() : 0;
        result = 31 * result + (v2 != null ? v2.hashCode() : 0);
        result = 31 * result + (v3 != null ? v3.hashCode() : 0);
        result = 31 * result + (normal != null ? normal.hashCode() : 0);
        result = 31 * result + (overrideColour != null ? overrideColour.hashCode() : 0);
        result = 31 * result + (useFaceNormal ? 1 : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "Face{" + "v1=" + v1 + ", v2=" + v2 + ", v3=" + v3 + ", normal=" + normal + ", overrideColour=" + overrideColour + ", useFaceNormal=" + useFaceNormal + '}';
    }
}
