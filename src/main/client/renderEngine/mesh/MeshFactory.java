package main.client.renderEngine.mesh;

import main.coreEngine.util.maths.Vector2f;
import main.coreEngine.util.maths.Vector3f;
import main.coreEngine.util.maths.Vector4f;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kelan on 10/04/2016.
 */
public class MeshFactory
{
    public static final String MESH_RESOURCE_DIR = "src/resources/models/mesh/";
    private static final String MATERIAL_RESOURCE_DIR = "src/resources/models/material/";
    public static final String OBJ_VERTEX_TEXTURE = "vt";
    public static final String OBJ_VERTEX_NORMAL = "vn";
    public static final String OBJ_VERTEX = "v";
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

    private MeshFactory()
    {

    }

//    public static Mesh parseOBJ(String file)
//    {
//        if (!new File(file).exists())
//        {
//            if (!file.startsWith(MESH_RESOURCE_DIR))
//                file = MESH_RESOURCE_DIR + file;
//
//            if (new File(file + ".obj").exists())
//                return parseOBJ(file + ".obj");
//
//            throw new RuntimeException("Could not find or load the obj file " + file + ". Is the name spelled correctly?");
//        }
//
//        MeshBuilder mb = new MeshBuilder();
//
//        try (FileReader fr = new FileReader(new File(file)); BufferedReader br = new BufferedReader(fr))
//        {
//            String line;
//
//            while ((line = br.readLine()) != null)
//            {
//                String[] components = line.trim().split(" ");
//
//                if (line.isEmpty() || line.trim().startsWith("#") || line.trim().startsWith(OBJ_OBJECT_NAME + " ") || line.startsWith(OBJ_SMOOTHING_GROUP + " "))
//                    continue;
//                else if (components[0].trim().equals(OBJ_VERTEX))
//                    mb.addVertexPosition(Float.parseFloat(components[1]), Float.parseFloat(components[2]), Float.parseFloat(components[3]));
//                else if (components[0].trim().equals(OBJ_VERTEX_NORMAL))
//                    mb.addVertexNormal(Float.parseFloat(components[1]), Float.parseFloat(components[2]), Float.parseFloat(components[3]));
//                else if (components[0].trim().equals(OBJ_VERTEX_TEXTURE))
//                    mb.addVertexTexture(Float.parseFloat(components[1]), Float.parseFloat(components[2]));
//                else if (components[0].trim().equals(OBJ_FACE))
//                {
//                    mb.processFace(line);
//                } else
//                    System.err.println("Invalid line in OBJ file \"" + line + "\" Ignoring this line.");
//            }
//
//        } catch (FileNotFoundException e)
//        {
//            e.printStackTrace();
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//        Mesh m = Mesh.compile(mb.getFaces());
//        try
//        {
//            Field f = m.getClass().getDeclaredField("resource");
//            f.setAccessible(true);
//            f.set(m, file);
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//        return m;
//    }
//
//    public static Material parseMTL(String file)
//    {
//        if (!new File(file).exists())
//        {
//            if (!file.startsWith(MATERIAL_RESOURCE_DIR))
//                file = MATERIAL_RESOURCE_DIR + file;
//
//            if (new File(file + ".mtl").exists())
//                return parseMTL(file + ".mtl");
//
//            throw new RuntimeException("Could not find or load the mtl file " + file + ". Is the name spelled correctly?");
//        }
//
//        try (FileReader fr = new FileReader(new File(file)); BufferedReader br = new BufferedReader(fr))
//        {
//            String line;
//
//            while ((line = br.readLine()) != null)
//            {
//                line = line.trim();
//
//                if (line.isEmpty() || line.startsWith("#"))
//                    continue;
//                else if (line.startsWith(MTL_NEWMTL))
//                    processNewmtl(line);
//                else if (line.startsWith(MTL_KA))
//                    processReflectivityTransmissivity(MTL_KA, line);
//                else if (line.startsWith(MTL_KD))
//                    processReflectivityTransmissivity(MTL_KD, line);
//                else if (line.startsWith(MTL_KS))
//                    processReflectivityTransmissivity(MTL_KS, line);
//                else if (line.startsWith(MTL_TF))
//                    processReflectivityTransmissivity(MTL_TF, line);
//                else if (line.startsWith(MTL_ILLUM))
//                    processIllum(line);
//                else if (line.startsWith(MTL_D))
//                    processD(line);
//                else if (line.startsWith(MTL_NS))
//                    processNs(line);
//                else if (line.startsWith(MTL_SHARPNESS))
//                    processSharpness(line);
//                else if (line.startsWith(MTL_NI))
//                    processNi(line);
//                else if (line.startsWith(MTL_MAP_KA))
//                    processMapDecalDispBump(MTL_MAP_KA, line);
//                else if (line.startsWith(MTL_MAP_KD))
//                    processMapDecalDispBump(MTL_MAP_KD, line);
//                else if (line.startsWith(MTL_MAP_KS))
//                    processMapDecalDispBump(MTL_MAP_KS, line);
//                else if (line.startsWith(MTL_MAP_NS))
//                    processMapDecalDispBump(MTL_MAP_NS, line);
//                else if (line.startsWith(MTL_MAP_D))
//                    processMapDecalDispBump(MTL_MAP_D, line);
//                else if (line.startsWith(MTL_DISP))
//                    processMapDecalDispBump(MTL_DISP, line);
//                else if (line.startsWith(MTL_DECAL))
//                    processMapDecalDispBump(MTL_DECAL, line);
//                else if (line.startsWith(MTL_BUMP))
//                    processMapDecalDispBump(MTL_BUMP, line);
//                else if (line.startsWith(MTL_REFL))
//                    processRefl(line);
//                else
//                    System.err.println("Unknown line in MTL file; skipping.");
//            }
//
//            br.close();
//            fr.close();
//        } catch (FileNotFoundException e)
//        {
//            e.printStackTrace();
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    private void processNewmtl(String line)
//    {
//        line = line.substring(MTL_NEWMTL.length()).trim();
//        builder.newMtl(line);
//    }
//
//    private void processReflectivityTransmissivity(String fieldName, String line)
//    {
//        int type = BuilderInterface.MTL_KA;
//        if (fieldName.equals(MTL_KD))
//        {
//            type = BuilderInterface.MTL_KD;
//        } else if (fieldName.equals(MTL_KS))
//        {
//            type = BuilderInterface.MTL_KS;
//        } else if (fieldName.equals(MTL_TF))
//        {
//            type = BuilderInterface.MTL_TF;
//        }
//
//        String[] tokens = StringUtils.parseWhitespaceList(line.substring(fieldName.length()));
//        if (null == tokens)
//        {
//            System.err.println("Got Ka line with no tokens, line = |" + line + "|");
//            return;
//        }
//        if (tokens.length <= 0)
//        {
//            System.err.println("Got Ka line with no tokens, line = |" + line + "|");
//            return;
//        }
//        if (tokens[0].equals("spectral"))
//        {
//            // Ka spectral file.rfl factor_num
//            System.err.println("Sorry Charlie, this parse doesn't handle \'spectral\' parsing.  (Mostly because I can't find any info on the spectra.rfl file.)");
//            return;
//// 	    if(tokens.length < 2) {
//// 		System.err.println("Got spectral line with not enough tokens, need at least one token for spectral file and one value for factor, found "+(tokens.length-1)+" line = |"+line+"|");
//// 		return;
//// 	    }
//        } else if (tokens[0].equals("xyz"))
//        {
//            // Ka xyz x_num y_num z_num
//
//            if (tokens.length < 2)
//            {
//                System.err.println("Got xyz line with not enough x/y/z tokens, need at least one value for x, found " + (tokens.length - 1) + " line = |" + line + "|");
//                return;
//            }
//            float x = Float.parseFloat(tokens[1]);
//            float y = x;
//            float z = x;
//            if (tokens.length > 2)
//            {
//                y = Float.parseFloat(tokens[2]);
//            }
//            if (tokens.length > 3)
//            {
//                z = Float.parseFloat(tokens[3]);
//            }
//            builder.setXYZ(type, x, y, z);
//        } else
//        {
//            // Ka r_num g_num b_num
//            float r = Float.parseFloat(tokens[0]);
//            float g = r;
//            float b = r;
//            if (tokens.length > 1)
//            {
//                g = Float.parseFloat(tokens[1]);
//            }
//            if (tokens.length > 2)
//            {
//                b = Float.parseFloat(tokens[2]);
//            }
//            builder.setRGB(type, r, g, b);
//        }
//    }
//
//    private void processIllum(String line)
//    {
//        line = line.substring(MTL_ILLUM.length()).trim();
//        int illumModel = Integer.parseInt(line);
//        if ((illumModel < 0) || (illumModel > 10))
//        {
//            System.err.println("Got illum model value out of range (0 to 10 inclusive is allowed), value=" + illumModel + ", line=" + line);
//            return;
//        }
//        builder.setIllum(illumModel);
//    }
//
//    private void processD(String line)
//    {
//        line = line.substring(MTL_D.length()).trim();
//        boolean halo = false;
//        if (line.startsWith(MTL_D_DASHHALO))
//        {
//            halo = true;
//            line = line.substring(MTL_D_DASHHALO.length()).trim();
//        }
//        float factor = Float.parseFloat(line);
//        builder.setD(halo, factor);
//    }
//
//    private void processNs(String line)
//    {
//        line = line.substring(MTL_NS.length()).trim();
//        float exponent = Float.parseFloat(line);
//        builder.setNs(exponent);
//    }
//
//    private void processSharpness(String line)
//    {
//        line = line.substring(MTL_SHARPNESS.length()).trim();
//        float value = Float.parseFloat(line);
//        builder.setSharpness(value);
//    }
//
//    private void processNi(String line)
//    {
//        line = line.substring(MTL_NI.length()).trim();
//        float opticalDensity = Float.parseFloat(line);
//        builder.setNi(opticalDensity);
//    }
//
//    private void processMapDecalDispBump(String fieldname, String line)
//    {
//        int type = BuilderInterface.MTL_MAP_KA;
//        if (fieldname.equals(MTL_MAP_KD))
//        {
//            type = BuilderInterface.MTL_MAP_KD;
//        } else if (fieldname.equals(MTL_MAP_KS))
//        {
//            type = BuilderInterface.MTL_MAP_KS;
//        } else if (fieldname.equals(MTL_MAP_NS))
//        {
//            type = BuilderInterface.MTL_MAP_NS;
//        } else if (fieldname.equals(MTL_MAP_D))
//        {
//            type = BuilderInterface.MTL_MAP_D;
//        } else if (fieldname.equals(MTL_DISP))
//        {
//            type = BuilderInterface.MTL_DISP;
//        } else if (fieldname.equals(MTL_DECAL))
//        {
//            type = BuilderInterface.MTL_DECAL;
//        } else if (fieldname.equals(MTL_BUMP))
//        {
//            type = BuilderInterface.MTL_BUMP;
//        }
//
//        String filename = line.substring(fieldname.length()).trim();
//        builder.setMapDecalDispBump(type, filename);
//
//        // @TODO: Add processing of the options...?
//    }
//
//    private void processRefl(String line)
//    {
//        String filename = null;
//
//        int type = BuilderInterface.MTL_REFL_TYPE_UNKNOWN;
//        line = line.substring(MTL_REFL.length()).trim();
//        if (line.startsWith("-type"))
//        {
//            line = line.substring("-type".length()).trim();
//            if (line.startsWith(MTL_REFL_TYPE_SPHERE))
//            {
//                type = BuilderInterface.MTL_REFL_TYPE_SPHERE;
//                filename = line.substring(MTL_REFL_TYPE_SPHERE.length()).trim();
//            } else if (line.startsWith(MTL_REFL_TYPE_CUBE_TOP))
//            {
//                type = BuilderInterface.MTL_REFL_TYPE_CUBE_TOP;
//                filename = line.substring(MTL_REFL_TYPE_CUBE_TOP.length()).trim();
//            } else if (line.startsWith(MTL_REFL_TYPE_CUBE_BOTTOM))
//            {
//                type = BuilderInterface.MTL_REFL_TYPE_CUBE_BOTTOM;
//                filename = line.substring(MTL_REFL_TYPE_CUBE_BOTTOM.length()).trim();
//            } else if (line.startsWith(MTL_REFL_TYPE_CUBE_FRONT))
//            {
//                type = BuilderInterface.MTL_REFL_TYPE_CUBE_FRONT;
//                filename = line.substring(MTL_REFL_TYPE_CUBE_FRONT.length()).trim();
//            } else if (line.startsWith(MTL_REFL_TYPE_CUBE_BACK))
//            {
//                type = BuilderInterface.MTL_REFL_TYPE_CUBE_BACK;
//                filename = line.substring(MTL_REFL_TYPE_CUBE_BACK.length()).trim();
//            } else if (line.startsWith(MTL_REFL_TYPE_CUBE_LEFT))
//            {
//                type = BuilderInterface.MTL_REFL_TYPE_CUBE_LEFT;
//                filename = line.substring(MTL_REFL_TYPE_CUBE_LEFT.length()).trim();
//            } else if (line.startsWith(MTL_REFL_TYPE_CUBE_RIGHT))
//            {
//                type = BuilderInterface.MTL_REFL_TYPE_CUBE_RIGHT;
//                filename = line.substring(MTL_REFL_TYPE_CUBE_RIGHT.length()).trim();
//            } else
//            {
//                System.err.println("unknown material refl -type, line = |" + line + "|");
//                return;
//            }
//        } else
//        {
//            filename = line;
//        }
//
//        builder.setRefl(type, filename);
//    }

    public static Mesh getNewQuadPlane()
    {
        List<Face> faces = new ArrayList<>();

        Vertex v1 = new Vertex(new Vector3f(0.0F, 1.0F, 0.0F), new Vector2f(0.0F, 0.0F), null, new Vector3f(-0.5F, 0.0F, +0.5F));
        Vertex v2 = new Vertex(new Vector3f(0.0F, 1.0F, 0.0F), new Vector2f(0.0F, 1.0F), null, new Vector3f(-0.5F, 0.0F, -0.5F));
        Vertex v3 = new Vertex(new Vector3f(0.0F, 1.0F, 0.0F), new Vector2f(1.0F, 1.0F), null, new Vector3f(+0.5F, 0.0F, -0.5F));
        Vertex v4 = new Vertex(new Vector3f(0.0F, 1.0F, 0.0F), new Vector2f(1.0F, 0.0F), null, new Vector3f(+0.5F, 0.0F, +0.5F));

        faces.add(new Face(v1, v2, v4));
        faces.add(new Face(v4, v2, v3));

        return Mesh.compile(faces);
    }

    public static Mesh getNewCube()
    {
        List<Face> faces = new ArrayList<>();

        Vertex v0 = new Vertex(new Vector3f(-0.5F, +0.5F, -0.5F)).setColour(new Colour(0, 0, 0, 1.0F));
        Vertex v1 = new Vertex(new Vector3f(-0.5F, -0.5F, -0.5F)).setColour(new Colour(0, 0, 1, 1.0F));
        Vertex v2 = new Vertex(new Vector3f(+0.5F, -0.5F, -0.5F)).setColour(new Colour(0, 1, 0, 1.0F));
        Vertex v3 = new Vertex(new Vector3f(+0.5F, +0.5F, -0.5F)).setColour(new Colour(0, 1, 1, 1.0F));
        Vertex v4 = new Vertex(new Vector3f(-0.5F, +0.5F, +0.5F)).setColour(new Colour(1, 0, 0, 1.0F));
        Vertex v5 = new Vertex(new Vector3f(-0.5F, -0.5F, +0.5F)).setColour(new Colour(1, 0, 1, 1.0F));
        Vertex v6 = new Vertex(new Vector3f(+0.5F, -0.5F, +0.5F)).setColour(new Colour(1, 1, 0, 1.0F));
        Vertex v7 = new Vertex(new Vector3f(+0.5F, +0.5F, +0.5F)).setColour(new Colour(1, 1, 1, 1.0F));

        faces.add(new Face(v0, v1, v3).setUseFaceNormal(true));
        faces.add(new Face(v3, v1, v2).setUseFaceNormal(true));
        faces.add(new Face(v4, v5, v7).setUseFaceNormal(true));
        faces.add(new Face(v7, v5, v6).setUseFaceNormal(true));
        faces.add(new Face(v3, v2, v7).setUseFaceNormal(true));
        faces.add(new Face(v7, v2, v6).setUseFaceNormal(true));
        faces.add(new Face(v0, v1, v4).setUseFaceNormal(true));
        faces.add(new Face(v4, v1, v5).setUseFaceNormal(true));
        faces.add(new Face(v4, v0, v7).setUseFaceNormal(true));
        faces.add(new Face(v7, v0, v3).setUseFaceNormal(true));
        faces.add(new Face(v5, v1, v6).setUseFaceNormal(true));
        faces.add(new Face(v6, v1, v2).setUseFaceNormal(true));

        return Mesh.compile(faces);
    }


    private static class MeshBuilder
    {
        private List<Vector3f> positions = new ArrayList<>();
        private List<Vector3f> normals = new ArrayList<>();
        private List<Vector2f> textures = new ArrayList<>();
        private List<Vector4f> colours = new ArrayList<>();

        private List<Face> faces = new ArrayList<>();

        private MeshBuilder()
        {
        }

        public void addVertexPosition(float x, float y, float z)
        {
            positions.add(new Vector3f(x, y, z));
        }

        public void addVertexNormal(float x, float y, float z)
        {
            normals.add(new Vector3f(x, y, z));
        }

        public void addVertexTexture(float u, float v)
        {
            textures.add(new Vector2f(u, 1.0F - v));
        }

        public List<Face> getFaces()
        {
            return faces;
        }

        public void processFace(String line)
        {
            String[] vertices = line.substring(2).trim().split(" ");
            Vertex[] face = new Vertex[3];

            for (int i = 0; i < 3; i++)
            {
                String[] vertex = vertices[i].split("/");

                int vertexIndex = vertex[0] == null || vertex[0].isEmpty() ? -1 : Integer.parseInt(vertex[0]) - 1;
                int textureIndex = vertex[1] == null || vertex[1].isEmpty() ? -1 : Integer.parseInt(vertex[1]) - 1;
                int normalIndex = vertex[2] == null || vertex[2].isEmpty() ? -1 : Integer.parseInt(vertex[2]) - 1;

                Vector3f position = vertexIndex >= 0 ? positions.get(vertexIndex) : null;
                Vector2f texture = textureIndex >= 0 ? textures.get(textureIndex) : null;
                Vector3f normal = normalIndex >= 0 ? normals.get(normalIndex) : null;

                if (position == null)
                {
                    System.err.println("Cannot process face with null position. Normal and UVs can be approximated, but not positions. Skipping this face");
                    return;
                }
                face[i] = new Vertex(normal, texture, null, position);
            }

            faces.add(new Face(face[0], face[1], face[2]));
        }
    }
}
