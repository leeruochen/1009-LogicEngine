
package github.com_1009project.abstractEngine;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class Scene {
    private int id;
    protected List<Layer> layers = new ArrayList<>();;
    protected AssetManager assetManager;
    protected EntityRegistry entityRegistry;
    protected EventManager eventManager;
    protected SpriteBatch batch;
    protected SceneManager sceneManager;

    public Scene(int id, AssetManager assetManager, EntityRegistry entityRegistry, EventManager eventManager, SpriteBatch batch, SceneManager sceneManager) {
        this.id = id;
        this.assetManager = assetManager;
        this.entityRegistry = entityRegistry;
        this.eventManager = eventManager;
        this.batch = batch;
        this.sceneManager = sceneManager;
    }
    public int getId() {
        return id;
    }
    public abstract void init();

    public void onEnter() {
        System.out.println("Scene " + getId() + " has been switched and now is active!");
        
        // Create the multiplexer
        InputMultiplexer multiplexer = new InputMultiplexer();

        // Look for the UILayer and add its stage first
        // This ensures that clicking a button "traps" the touch so it doesn't move the player
        for (Layer layer : layers) {
            if (layer instanceof UILayer) {
                Stage stage = ((UILayer) layer).getStage();
                if (stage != null) {
                    multiplexer.addProcessor(stage);
                }  
            }
        }
        // Add your EventManager so keyboard movement works
        if (eventManager != null) {
            multiplexer.addProcessor(eventManager);
        }
        // Tell LibGDX to listen to the multiplexer
        Gdx.input.setInputProcessor(multiplexer);
    }

    public void onExit() {
        // used to stop music or other things when scene is no longer active
    }
    public void update(float deltaTime) {
        for (Layer layer : layers) {
            layer.update(deltaTime);
        }
    }
    public void render() {
        for (Layer layer : layers) {
            layer.render(); 
        }
    }

    public void dispose() {
        for (Layer layer : layers) {
            layer.dispose();
        }
    }
}
