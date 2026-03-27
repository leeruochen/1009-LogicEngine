package github.com_1009project.abstractEngine;

// The Ifactory interface defines a contract for creating entities in the game. It provides a method for creating an entity with basic parameters (position and size) and an optional method for creating an entity with additional parameters (texture and type).

import com.badlogic.gdx.graphics.Texture;

public interface Ifactory<T extends Entity> {
    T createEntity(float x, float y, float width, float height);
    default T createEntity(float x, float y, float width, float height, Texture texture, String type) {
        throw new UnsupportedOperationException("This factory does not support creating entities with a texture.");
    }
}