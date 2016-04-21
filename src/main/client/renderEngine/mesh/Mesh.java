package main.client.renderEngine.mesh;

import main.client.renderEngine.shader.AbstractShaderProgram;
import org.lwjgl.BufferUtils;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by Kelan on 08/04/2016.
 */
public final class Mesh
{
    public static final int FLOAT_SIZE_BYTES = 4;
    public static final int FLOATS_PER_POSITION = 3;
    public static final int FLOATS_PER_NORMAL = 3;
    public static final int FLOATS_PER_TEXTURE = 2;
    public static final int FLOATS_PER_COLOUR = 4;
    public static final int VERTEX_SIZE_FLOATS = FLOATS_PER_POSITION + FLOATS_PER_NORMAL + FLOATS_PER_TEXTURE + FLOATS_PER_COLOUR;
    public static final int VERTEX_SIZE_BYTES = VERTEX_SIZE_FLOATS * FLOAT_SIZE_BYTES;

    public static final int POSITION_OFFSET_FLOATS = 0;
    public static final int NORMAL_OFFSET_FLOATS = POSITION_OFFSET_FLOATS + FLOATS_PER_POSITION;
    public static final int TEXTURE_OFFSET_FLOATS = NORMAL_OFFSET_FLOATS + FLOATS_PER_NORMAL;
    public static final int COLOUR_OFFSET_FLOATS = TEXTURE_OFFSET_FLOATS + FLOATS_PER_TEXTURE;
    public static final int POSITION_OFFSET_BYTES = POSITION_OFFSET_FLOATS * FLOAT_SIZE_BYTES;
    public static final int NORMAL_OFFSET_BYTES = NORMAL_OFFSET_FLOATS * FLOAT_SIZE_BYTES;
    public static final int TEXTURE_OFFSET_BYTES = TEXTURE_OFFSET_FLOATS * FLOAT_SIZE_BYTES;
    public static final int COLOUR_OFFSET_BYTES = COLOUR_OFFSET_FLOATS * FLOAT_SIZE_BYTES;
    public static final int POSITION_STRIDE_BYTES = VERTEX_SIZE_BYTES;
    public static final int NORMAL_STRIDE_BYTES = VERTEX_SIZE_BYTES;
    public static final int TEXTURE_STRIDE_BYTES = VERTEX_SIZE_BYTES;
    public static final int COLOUR_STRIDE_BYTES = VERTEX_SIZE_BYTES;

    public final static int VERTICES_PER_FACE = 3;

    public static final int ATTRIBUTE_LOCATION_POSITION = 0;
    public static final int ATTRIBUTE_LOCATION_NORMAL = 1;
    public static final int ATTRIBUTE_LOCATION_TEXTURE = 2;
    public static final int ATTRIBUTE_LOCATION_COLOUR = 3;

    private int vaoID;
    private int iboID;
    private int indexCount;
    private String resource;

    private Mesh(int vaoID, int iboID, int indexCount)
    {
        this.vaoID = vaoID;
        this.iboID = iboID;
        this.indexCount = indexCount;
    }

    public void draw(AbstractShaderProgram shader, Texture texture)
    {
        glEnable(GL_TEXTURE_2D);
        if (texture != null) texture.bind(shader);
        else Texture.MISSING_TEXTURE.bind(shader);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(ATTRIBUTE_LOCATION_POSITION);
        glEnableVertexAttribArray(ATTRIBUTE_LOCATION_NORMAL);
        glEnableVertexAttribArray(ATTRIBUTE_LOCATION_TEXTURE);
        glEnableVertexAttribArray(ATTRIBUTE_LOCATION_COLOUR);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glDisableVertexAttribArray(ATTRIBUTE_LOCATION_POSITION);
        glDisableVertexAttribArray(ATTRIBUTE_LOCATION_NORMAL);
        glDisableVertexAttribArray(ATTRIBUTE_LOCATION_TEXTURE);
        glDisableVertexAttribArray(ATTRIBUTE_LOCATION_COLOUR);

        glBindVertexArray(0);

        glDisable(GL_TEXTURE_2D);
    }

    public void destroy()
    {
        IntBuffer ib = BufferUtils.createIntBuffer(1);
        ib.reset();
        ib.put(vaoID);
        glDeleteBuffers(ib);
        ib.reset();
        ib.put(iboID);
        glDeleteBuffers(ib);
    }

    public String getResource()
    {
        return resource;
    }

    public static Mesh compile(List<Face> faces)
    {
        if (faces.size() <= 0)
            throw new RuntimeException("Failed to parse mesh. No faces were provided.");

        HashMap<Vertex, Integer> indexMap = new HashMap<>();
        ArrayList<Vertex> vertices = new ArrayList<>();
        int vertexCount = 0;

        for (Face face : faces)
        {
            for (Vertex vertex : face.getVertices())
            {
                if (!indexMap.containsKey(vertex))
                {
                    indexMap.put(vertex, vertexCount++);
                    vertices.add(vertex);
                }
            }
        }

        int indicesCount = faces.size() * VERTICES_PER_FACE;
        int dataSize = vertexCount * VERTEX_SIZE_FLOATS;

        FloatBuffer vertexData = BufferUtils.createFloatBuffer(dataSize);
        if (vertexData == null)
            System.err.println("Failed to allocate FloatBuffer with size " + dataSize);

        for (Vertex vertex : vertices)
        {
            vertexData.put(vertex.getPosition().x);
            vertexData.put(vertex.getPosition().y);
            vertexData.put(vertex.getPosition().z);
            vertexData.put(vertex.getNormal() == null ? 1.0F : vertex.getNormal().x);
            vertexData.put(vertex.getNormal() == null ? 1.0F : vertex.getNormal().y);
            vertexData.put(vertex.getNormal() == null ? 1.0F : vertex.getNormal().z);
            vertexData.put(vertex.getTexture() == null ? 0.0F : vertex.getTexture().x);
            vertexData.put(vertex.getTexture() == null ? 0.0F : vertex.getTexture().y);
            vertexData.put(vertex.getColour() == null ? 1.0F : vertex.getColour().getRGBA().x);
            vertexData.put(vertex.getColour() == null ? 1.0F : vertex.getColour().getRGBA().y);
            vertexData.put(vertex.getColour() == null ? 1.0F : vertex.getColour().getRGBA().z);
            vertexData.put(vertex.getColour() == null ? 1.0F : vertex.getColour().getRGBA().w);
        }
        vertexData.flip();

        IntBuffer indices = BufferUtils.createIntBuffer(indicesCount);
        for (Face face : faces)
        {
            for (Vertex vertex : face.getVertices())
            {
                int index = indexMap.get(vertex);
                indices.put(index);
            }
        }
        indices.flip();

        int vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        int vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);

        glVertexAttribPointer(ATTRIBUTE_LOCATION_POSITION, FLOATS_PER_POSITION, GL_FLOAT, false, POSITION_STRIDE_BYTES, POSITION_OFFSET_BYTES);
        glVertexAttribPointer(ATTRIBUTE_LOCATION_NORMAL, FLOATS_PER_NORMAL, GL_FLOAT, false, NORMAL_STRIDE_BYTES, NORMAL_OFFSET_BYTES);
        glVertexAttribPointer(ATTRIBUTE_LOCATION_TEXTURE, FLOATS_PER_TEXTURE, GL_FLOAT, false, TEXTURE_STRIDE_BYTES, TEXTURE_OFFSET_BYTES);
        glVertexAttribPointer(ATTRIBUTE_LOCATION_COLOUR, FLOATS_PER_COLOUR, GL_FLOAT, false, COLOUR_STRIDE_BYTES, COLOUR_OFFSET_BYTES);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        int iboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glBindVertexArray(0);
        return new Mesh(vaoID, iboID, indicesCount);
    }
}