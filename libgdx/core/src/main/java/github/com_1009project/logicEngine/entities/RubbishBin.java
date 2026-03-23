package github.com_1009project.logicEngine.entities;

import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.Station;

/**
 * A rubbish bin station. When the player interacts with it while holding an item,
 * the held item is discarded. Handled by InteractionManager.
 */
public class RubbishBin extends Station {
    public RubbishBin(float x, float y, float w, float h, Texture texture) {
        super(x, y, w, h, texture);
    }
}
