package github.com_1009project.logicEngine.factories;

import github.com_1009project.abstractEngine.Assets;
import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.entities.Counter;

public class CounterFactory implements Ifactory<Counter> {

    @Override
    public Counter createEntity(float x, float y, float width, float height) {
        // Create and return a new Counter entity with default properties
        return new Counter(x, y, width, height, Assets.getAssetManager().get("foodstations/counter.png", Texture.class)); // Texture can be set later
    }

}
