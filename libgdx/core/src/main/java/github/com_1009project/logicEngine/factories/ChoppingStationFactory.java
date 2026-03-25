package github.com_1009project.logicEngine.factories;

import github.com_1009project.abstractEngine.Assets;
import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.entities.ChoppingStation;

public class ChoppingStationFactory implements Ifactory<ChoppingStation> {

    @Override
    public ChoppingStation createEntity(float x, float y, float width, float height) {
        // Create and return a new ChoppingStation entity with default properties
        return new ChoppingStation(x, y, width, height, Assets.getAssetManager().get("foodstations/counter_choppingboard.png", Texture.class)); // Texture can be set later
    }

}