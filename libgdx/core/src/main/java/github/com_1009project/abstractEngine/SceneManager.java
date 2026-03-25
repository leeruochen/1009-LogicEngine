package github.com_1009project.abstractEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SceneManager implements EventObserver {
    private Map<Integer, Scene> scenes = new HashMap<>();
    private Map<Integer, Supplier<Scene>> sceneSuppliers = new HashMap<>();
    private Scene currentScene;
    private AssetManager resourceManager; // reference to resource manager to pass to scenes
    private EntityManager entityManager;
    private EventManager eventManager;
    private SpriteBatch batch;

    public SceneManager(AssetManager resourceManager, EntityManager entityManager, EventManager eventManager, SpriteBatch batch) {
        this.resourceManager = resourceManager;
        this.entityManager = entityManager;
        this.eventManager = eventManager;
        this.batch = batch;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void registerScene(int id, Supplier<Scene> sceneSupplier) {
        sceneSuppliers.put(id, sceneSupplier);
    }

    // Load a scene by ID (can be extended to load specific subclasses like TestScene)
    public void loadScene(int id) {
        if (scenes.containsKey(id)) {
            currentScene = scenes.get(id);
        } else if (sceneSuppliers.containsKey(id)) {
            currentScene = sceneSuppliers.get(id).get();
            scenes.put(id, currentScene);
        } else {
            throw new IllegalArgumentException("Unknown scene ID: " + id);
        }

        currentScene.onEnter();
    }

    // Update the current scene
    public void updateScene(float deltaTime) {
        if (currentScene != null) {
            currentScene.update(deltaTime);
        }
    }

    // Render the current scene
    public void renderScene() {
        if (currentScene != null) {
            currentScene.render();
        }
    }

    // Unload a specific scene
    public void unloadScene(int id) {
        Scene scene = scenes.remove(id);
        if (scene != null) {
            scene.dispose();
        }
    }

    // Dispose all scenes
    public void dispose() {
        for (Scene scene : scenes.values()) {
            scene.dispose();
        }
        scenes.clear();
    }

    // Optional: get the current scene
    public Scene getCurrentScene() {
        return currentScene;
    }

    @Override
    public void onNotify(Event event, Boolean up) {
        if (up == null) return; 

        if (event == Event.GamePause && !up) { // Only trigger on key press, not release
            if (currentScene != null && currentScene.getId() == 0) {
                return;
            }

            if (currentScene != null && currentScene.getId() == 2) { // If we're already in the pause scene, return to the previous scene
                currentScene = null; // Clear current scene to avoid rendering issues 
            } else {
                loadScene(99); // Load the pause scene (ID 99)
            }
            return;
        }
        if (currentScene == null) return; // No scene to notify

		// Only loop through entities that have explicitly flagged they want input
		for (Entity entity : entityManager.getEntities()) {
			if (entity.isActive()) {
				if (entity.isInputEnabled()) {
					this.changeScene(entity, event, up);
				}
			}
		}
	}

	@Override // this is for handling mouse events
	public void onNotify(Event event, Boolean up, int screenX, int screenY) {

	}
    
    public void changeScene(Entity entity, Event event, boolean isUp) {
        if (entity == null) return;

        if (!isUp) { // Key Pressed
            if (event == Event.PlayerInteract) {
            }
        } else { // Key Released
            if (event == Event.PlayerInteract) {
            }
        }
    }
}