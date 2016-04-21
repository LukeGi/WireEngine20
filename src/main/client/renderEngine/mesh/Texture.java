package main.client.renderEngine.mesh;

import main.client.renderEngine.shader.AbstractShaderProgram;
import org.lwjgl.BufferUtils;
import org.w3c.dom.Text;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Pack200;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;

/**
 * Created by Kelan on 07/04/2016.
 */
public class Texture
{
    private static Map<String, Texture> loaded = new HashMap<>();
    private static final String TEXTURE_RESOURCE_DIR = "src/resources/models/texture/";
    private final int textureUnit;
    private int texture;
    private int width;
    private int height;
    private ByteBuffer data;

    public static final Texture MISSING_TEXTURE = loadTexture("src/resources/models/texture/missing.png");

    private Texture(int width, int height, int textureUnit, ByteBuffer data)
    {
        this.texture = glGenTextures();
        this.width = width;
        this.height = height;
        this.textureUnit = textureUnit;
        this.data = data;

        glActiveTexture(GL_TEXTURE0 + textureUnit);
        glBindTexture(GL_TEXTURE_2D, texture);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
        glGenerateMipmap(GL_TEXTURE_2D);

    }

    public void bind(AbstractShaderProgram shader)
    {
        glActiveTexture(GL_TEXTURE0 + textureUnit);
        glBindTexture(GL_TEXTURE_2D, texture);
        shader.setUniform("texture", textureUnit);
    }

    public void dispose()
    {
        glDeleteTextures(texture);
    }

    public int getTextureUnit()
    {
        return textureUnit;
    }

    public int getTexture()
    {
        return texture;
    }

    public void setTexture(int texture)
    {
        this.texture = texture;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Texture texture1 = (Texture) o;

        if (textureUnit != texture1.textureUnit) return false;
        if (texture != texture1.texture) return false;
        if (width != texture1.width) return false;
        if (height != texture1.height) return false;
        return data != null ? data.equals(texture1.data) : texture1.data == null;

    }

    @Override
    public int hashCode()
    {
        int result = textureUnit;
        result = 31 * result + texture;
        result = 31 * result + width;
        result = 31 * result + height;
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "Texture{" + "textureUnit=" + textureUnit + ", texture=" + texture + ", width=" + width + ", height=" + height + ", data=" + data + '}';
    }

    public static Texture loadTexture(String file)
    {
        String[] extensions = new String[] {"", ".png", ".jpg", ".jpeg", ".bmp", ".tga"};

        if (!new File(file).exists())
        {
            if (!file.startsWith(TEXTURE_RESOURCE_DIR))
                file = TEXTURE_RESOURCE_DIR + file;

            for (String s : extensions)
                if (new File(file + s).exists())
                    return loadTexture(file + s);

            if (!new File(file).exists())
            {
                System.err.println("Could not find or load the texture file " + file + ". Is the name spelled correctly?");
                return MISSING_TEXTURE;
            }
        }

        if (!loaded.containsKey(file))
        {
            IntBuffer width = BufferUtils.createIntBuffer(1);
            IntBuffer height = BufferUtils.createIntBuffer(1);
            IntBuffer comp = BufferUtils.createIntBuffer(1);

            ByteBuffer data = stbi_load(file, width, height, comp, STBI_rgb_alpha);

            if (data == null)
            {
                System.err.println("Texture file \"" + file + "\" failed to set. \n" + stbi_failure_reason());
                return null;
            }

            data.flip();

            int width0 = width.get(0);
            int height0 = height.get(0);

            Texture t = new Texture(width0, height0, 0, data);
            loaded.put(file, t);
            return t;
        } else
        {
            return loaded.get(file);
        }
    }
}
