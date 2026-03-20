package github.com_1009project.logicEngine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import github.com_1009project.abstractEngine.Entity;
import github.com_1009project.abstractEngine.IRenderable;

public abstract class Ingredient extends Entity implements IRenderable {
    private String name;
    private FoodState state;
    private Texture texture;

    public Ingredient(String name, FoodState state, Texture texture) {
        this.name = name;
        this.state = state;
        this.texture = texture;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FoodState getState() {
        return state;
    }

    public void setState(FoodState state) {
        this.state = state;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, this.getPosition().x, this.getPosition().y, this.getSize().x, this.getSize().y);
    }
}