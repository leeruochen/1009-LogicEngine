package github.com_1009project.logicEngine.factories;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.abstractEngine.Ifactory;
import github.com_1009project.logicEngine.entities.CounterSubmission;

public class CounterSubmissionFactory implements Ifactory<CounterSubmission> {

    private final AssetManager assetManager;

    public CounterSubmissionFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public CounterSubmission createEntity(float x, float y, float width, float height) {
        // Create and return a new CounterSubmission entity with default properties
        return new CounterSubmission(x, y, width, height, assetManager.get("foodstations/counter_submission.png", Texture.class)); // Texture can be set later
    }

}
