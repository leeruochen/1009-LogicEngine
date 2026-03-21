package github.com_1009project.logicEngine.factories;

import github.com_1009project.abstractEngine.Entity;
import com.badlogic.gdx.graphics.Texture;

public interface Ifactory<T extends Entity> {
    T createEntity(float x, float y, float width, float height);
    default T createEntity(float x, float y, float width, float height, Texture texture, String type) {
        throw new UnsupportedOperationException("This factory does not support creating entities with a texture.");
    }
}
