package main.client.renderEngine.mesh;

import main.coreEngine.sceneGraph.components.ModelComponent;
import main.coreEngine.util.maths.Vector;
import main.coreEngine.util.maths.Vector2f;
import main.coreEngine.util.maths.Vector3f;
import main.coreEngine.util.maths.Vector4f;
import sun.plugin.dom.exception.InvalidStateException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kelan on 17/04/2016.
 */
public class MeshBuilder
{
    public static final String MESH_RESOURCE_LOCATION = "src/resources/models/mesh/";
    public static final String MATERIAL_RESOURCE_LOCATION = "src/resources/models/material/";

    public static final String COMMENT = "#";
    public static final String OBJ_VERTEX_POSITION = "v";
    public static final String OBJ_VERTEX_NORMAL = "vn";
    public static final String OBJ_VERTEX_TEXTURE = "vt";
    public static final String OBJ_FACE = "f";
    public static final String OBJ_GROUP_NAME = "g";
    public static final String OBJ_OBJECT_NAME = "o";
    public static final String OBJ_SMOOTHING_GROUP = "s";
    public static final String OBJ_POINT = "p";
    public static final String OBJ_LINE = "l";
    public static final String OBJ_MAPLIB = "maplib";
    public static final String OBJ_USEMAP = "usemap";
    public static final String OBJ_MTLLIB = "mtllib";
    public static final String OBJ_USEMTL = "usemtl";
    public static final String MTL_NEWMTL = "newmtl";
    public static final String MTL_KA = "Ka";
    public static final String MTL_KD = "Kd";
    public static final String MTL_KS = "Ks";
    public static final String MTL_TF = "Tf";
    public static final String MTL_ILLUM = "illum";
    public static final String MTL_D = "d";
    public static final String MTL_D_DASHHALO = "-halo";
    public static final String MTL_NS = "Ns";
    public static final String MTL_SHARPNESS = "sharpness";
    public static final String MTL_NI = "Ni";
    public static final String MTL_MAP_KA = "map_Ka";
    public static final String MTL_MAP_KD = "map_Kd";
    public static final String MTL_MAP_KS = "map_Ks";
    public static final String MTL_MAP_NS = "map_Ns";
    public static final String MTL_MAP_D = "map_d";
    public static final String MTL_DISP = "disp";
    public static final String MTL_DECAL = "decal";
    public static final String MTL_BUMP = "bump";
    public static final String MTL_REFL = "refl";
    public static final String MTL_REFL_TYPE_SPHERE = "sphere";
    public static final String MTL_REFL_TYPE_CUBE_TOP = "cube_top";
    public static final String MTL_REFL_TYPE_CUBE_BOTTOM = "cube_bottom";
    public static final String MTL_REFL_TYPE_CUBE_FRONT = "cube_front";
    public static final String MTL_REFL_TYPE_CUBE_BACK = "cube_back";
    public static final String MTL_REFL_TYPE_CUBE_LEFT = "cube_left";
    public static final String MTL_REFL_TYPE_CUBE_RIGHT = "cube_right";

    public List<Vector3f> vertexPositions = new ArrayList<>();
    public List<Vector3f> vertexNormals = new ArrayList<>();
    public List<Vector2f> vertexTextures = new ArrayList<>();
    public List<Vector4f> vertexColours = new ArrayList<>();
    public List<Face> faces = new ArrayList<>();

    private MeshBuilder()
    {
    }

    public static ModelComponent compile(String name)
    {
        final MeshBuilder mb = new MeshBuilder();

        boolean flag = false;
        try
        {
            flag = parseMesh(name, mb);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        Mesh mesh = mb.compile();
        if (mesh == null)
        {
            return null;
        }

        Texture texture = Texture.loadTexture(name);

        return new ModelComponent(mesh, texture, null);
    }

    private static boolean parseMesh(String file, final MeshBuilder mb) throws IOException
    {
        if (mb == null)
            System.err.println("Cannot parse mesh without a meshbuilder");

        if (!new File(file).exists())
        {
            if (!file.startsWith(MESH_RESOURCE_LOCATION))
                file = MESH_RESOURCE_LOCATION + file;

            if (new File(file + ".obj").exists())
                file = file + ".obj";

            if (!new File(file).exists())
            {
                System.err.println("Could not find or load the obj file " + file + ". Is the name spelled correctly?");
                return false;
            }
        }

        BufferedReader reader = new BufferedReader(new FileReader(new File(file)));

        int errors = 0;
        for (String line = reader.readLine(); line != null; line = reader.readLine())
        {
            boolean error = false;

            if (line == null)
                break;
            else if (line.startsWith(COMMENT) || line.isEmpty())
                continue;
            else if (line.startsWith(OBJ_VERTEX_POSITION + " "))
                error = !processVertexPosition(line, mb);
            else if (line.startsWith(OBJ_VERTEX_NORMAL + " "))
                error = !processVertexNormal(line, mb);
            else if (line.startsWith(OBJ_VERTEX_TEXTURE + " "))
                error = !processVertexTexture(line, mb);
            else if (line.startsWith(OBJ_FACE + " "))
                error = !processFace(line, mb);
            else if (line.startsWith(OBJ_GROUP_NAME + " "))
                error = !processGroupName(line, mb);
            else if (line.startsWith(OBJ_OBJECT_NAME + " "))
                error = !processObjectName(line, mb);
            else if (line.startsWith(OBJ_SMOOTHING_GROUP + " "))
                error = !processSmoothingGroup(line, mb);
            else if (line.startsWith(OBJ_POINT + " "))
                error = !processPoint(line, mb);
            else if (line.startsWith(OBJ_LINE + " "))
                error = !processLine(line, mb);
            else if (line.startsWith(OBJ_MAPLIB + " "))
                error = !processMapLib(line, mb);
            else if (line.startsWith(OBJ_USEMAP + " "))
                error = !processUseMap(line, mb);
            else if (line.startsWith(OBJ_USEMTL + " "))
                error = !processUseMaterial(line, mb);
            else if (line.startsWith(OBJ_MTLLIB + " "))
                error = !processMaterialLib(line, mb);
            else
            {
                System.err.println("An unknown line was encountered while parsing this OBJ file; " + line);
                error = true;
            }

            if (error)
            {
                System.err.println("Error occurred while processing line \"" + line + "\" in the model file " + file);
                errors++;
            }
        }

        if (errors > 0)
            System.err.println("Compiled mesh with " + errors + " errors.");
        return errors > 0;
    }

    private static boolean processVertexPosition(String line, MeshBuilder mb)
    {
        if (line.startsWith(OBJ_VERTEX_POSITION))
        line = line.substring(OBJ_VERTEX_POSITION.length()).trim();

        Vector3f v = null;
        try
        {
            String[] position = line.split(" ");
            v = new Vector3f(Float.parseFloat(position[0]), Float.parseFloat(position[1]), Float.parseFloat(position[2]));
        } catch (Exception e) {}

        if (v != null)
            mb.vertexPositions.add(v);

        return v != null;
    }

    private static boolean processVertexNormal(String line, MeshBuilder mb)
    {
        if (line.startsWith(OBJ_VERTEX_NORMAL))
            line = line.substring(OBJ_VERTEX_NORMAL.length()).trim();

        Vector3f v = null;
        try
        {
            String[] position = line.split(" ");
            v = new Vector3f(Float.parseFloat(position[0]), Float.parseFloat(position[1]), Float.parseFloat(position[2]));
        } catch (Exception e)
        {
        }

        if (v != null)
            mb.vertexNormals.add(v);

        return v != null;
    }

    private static boolean processVertexTexture(String line, MeshBuilder mb)
    {
        if (line.startsWith(OBJ_VERTEX_TEXTURE))
            line = line.substring(OBJ_VERTEX_TEXTURE.length()).trim();

        Vector2f v = null;
        try
        {
            String[] position = line.split(" ");
            v = new Vector2f(Float.parseFloat(position[0]), 1.0F - Float.parseFloat(position[1]));
        } catch (Exception e)
        {
        }

        if (v != null)
            mb.vertexTextures.add(v);

        return v != null;
    }

    private static boolean processFace(String line, MeshBuilder mb)
    {
        Face face1 = null;
        try
        {
            if (line.startsWith(OBJ_FACE))
                line = line.substring(OBJ_FACE.length()).trim();

            String[] comps = line.split(" ");
            Vertex[] face = new Vertex[3];

            for (int i = 0; i < face.length; i++)
            {
                String[] vertex = comps[i].split("/");

                Vector3f position = mb.vertexPositions.get(Integer.parseInt(vertex[0]) - 1);
                Vector2f texture = new Vector2f();
                Vector3f normal = new Vector3f();

                if (vertex[1] != null && !vertex[1].isEmpty())
                    texture = mb.vertexTextures.get(Integer.parseInt(vertex[1]) - 1);

                if (vertex[2] != null && !vertex[2].isEmpty())
                    normal = mb.vertexNormals.get(Integer.parseInt(vertex[2]) - 1);

                face[i] = new Vertex(normal, texture, null, position);
            }

            face1 = new Face(face[0], face[1], face[2]);
        } catch (Exception e) {}

        if (face1 != null)
            mb.faces.add(face1);

        return face1 != null;
    }

    private static boolean processGroupName(String line, MeshBuilder mb)
    {
        return false;
    }

    private static boolean processObjectName(String line, MeshBuilder mb)
    {
        return false;
    }

    private static boolean processSmoothingGroup(String line, MeshBuilder mb)
    {
        return false;
    }

    private static boolean processPoint(String line, MeshBuilder mb)
    {
        return false;
    }

    private static boolean processLine(String line, MeshBuilder mb)
    {
        return false;
    }

    private static boolean processMapLib(String line, MeshBuilder mb)
    {
        return false;
    }

    private static boolean processUseMap(String line, MeshBuilder mb)
    {
        return false;
    }

    private static boolean processUseMaterial(String line, MeshBuilder mb)
    {
        return false;
    }

    private static boolean processMaterialLib(String line, MeshBuilder mb)
    {
        return false;
    }

    public Mesh compile()
    {
        return Mesh.compile(faces);
    }
}
