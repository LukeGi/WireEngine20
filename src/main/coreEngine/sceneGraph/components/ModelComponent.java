package main.coreEngine.sceneGraph.components;

import main.client.renderEngine.mesh.Material;
import main.client.renderEngine.mesh.Texture;
import main.client.renderEngine.mesh.Mesh;
import main.coreEngine.Engine;
import main.coreEngine.sceneGraph.Component;
import main.coreEngine.sceneGraph.Node;
import main.game.TestGame;

import javax.xml.soap.Text;

/**
 * Created by Kelan on 08/04/2016.
 */
public class ModelComponent extends Component
{
    protected Mesh mesh;
    protected Texture texture;
    protected Material material;
    protected boolean billboard;

    public ModelComponent(Mesh mesh, Texture texture, Material material)
    {
        this.mesh = mesh;

        String s = mesh.getResource();
        if (texture == null && mesh.getResource() != null && mesh.getResource().length() > 4)
            this.texture = Texture.loadTexture(s.substring(s.lastIndexOf("/") + 1, s.length() - 4));
        else
            this.texture = texture;

        if (material == null && mesh.getResource() != null)
            this.material = Material.loadMaterial(s.substring(s.lastIndexOf("/") + 1, s.length() - 4));
        else
            this.material = material;
    }

    @Override
    public void tick(Node parent)
    {

    }

    @Override
    public void render(Node parent)
    {
        ((TestGame) Engine.getGame()).getRenderer().send(mesh, texture, material, parent.getTransform());
    }

    @Override
    public void input(Node parent)
    {

    }

    @Override
    public void onAdded(Node node)
    {

    }

    public Mesh getMesh()
    {
        return mesh;
    }

    public Texture getTexture()
    {
        return texture;
    }

    public Material getMaterial()
    {
        return material;
    }

    public boolean isBillboard()
    {
        return billboard;
    }

    public void setBillboard(boolean billboard)
    {
        this.billboard = billboard;
    }
}
