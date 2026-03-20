package github.com_1009project.logicEngine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import github.com_1009project.abstractEngine.Entity;
import github.com_1009project.abstractEngine.IRenderable;

public class Station extends Entity implements IRenderable {
    private Texture texture;

    public Station(float x, float y, float w, float h, Texture texture) {
        super();
        this.setPosition(x, y);
        this.setSize(w, h);
        this.texture = texture;
        this.createCollisionComponent(w, h);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!this.isActive()) {return;}
        batch.draw(texture, this.getPosition().x, this.getPosition().y, this.getSize().x, this.getSize().y);
    }
}