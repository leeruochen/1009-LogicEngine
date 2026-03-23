package github.com_1009project.logicEngine.entities;

import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.Station;

/**
 * The submission counter where the player places a finished Plate to submit an order.
 * The InteractionManager handles the actual submission logic by calling FoodQueue.submitOrder().
 */
public class CounterSubmission extends Station {

    public CounterSubmission(float x, float y, float w, float h, Texture texture) {
        super(x, y, w, h, texture);
    }
}
