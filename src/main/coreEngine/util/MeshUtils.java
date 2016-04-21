package main.coreEngine.util;

import main.client.renderEngine.mesh.Face;
import main.coreEngine.util.maths.Vector3f;

/**
 * Created by Kelan on 08/04/2016.
 */
public class MeshUtils
{
    public static Vector3f avgNormal(Vector3f... normals)
    {
        Vector3f result = new Vector3f();
        int i;

        for (i = 0; i < normals.length; i++)
        {
            Vector3f.add(normals[i], result, result);
        }

        return (Vector3f) result.scale(1.0F / i).normalise();
    }

    public Face merge(Face... faces)
    {
        for (Face f : faces)
        {

        }

        return null;
    }
}
