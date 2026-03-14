package github.com_1009project.abstractEngine;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import github.com_1009project.logicEngine.Door;
import github.com_1009project.logicEngine.PauseScene;
import github.com_1009project.logicEngine.TestScene;
import github.com_1009project.logicEngine.testEntity;

public class SceneManager implements EventObserver {
    private Map<Integer, Scene> scenes = new HashMap<>();
    private Scene currentScene;
    private AssetManager resourceManager; // reference to resource manager to pass to scenes
    private EntityManager entityManager;
    private EventManager eventManager;
    private SpriteBatch batch;
    private int previousSceneId = 1; // Default to main game

    public SceneManager(AssetManager resourceManager, EntityManager entityManager, EventManager eventManager, SpriteBatch batch) {
        this.resourceManager = resourceManager;
        this.entityManager = entityManager;
        this.eventManager = eventManager;
        this.batch = batch;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    // Load a scene by ID (can be extended to load specific subclasses like TestScene)
    public void loadScene(int id) {
        if (scenes.containsKey(id)) {
            currentScene = scenes.get(id);
        } else {
            if (id == 99) {
                currentScene = new PauseScene(id, resourceManager, entityManager, eventManager, batch, this);
            } else if (id == 1) {
                currentScene = new TestScene(id, resourceManager, entityManager, eventManager, batch);
            } else {
                throw new IllegalArgumentException("Unknown scene ID: " + id);
            }
            scenes.put(id, currentScene);
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
        if (currentScene == null) return;

        if (event == Event.GamePause && !up) { // Only trigger on key press, not release
            if (currentScene instanceof PauseScene) { // If we're already in the pause scene, return to the previous scene
                loadScene(previousSceneId); 
            } else {
                previousSceneId = currentScene.getId();  // Save the current scene ID before switching to pause
                loadScene(99); // Load the pause scene (ID 99)
            }
        }
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
                if (entity instanceof testEntity) {
                    testEntity player = (testEntity) entity;
                    Door door = player.nearbyDoor;
                    if (door != null) {
                        String dest = door.getDestination();
                        if (dest != null && !dest.isEmpty()) {
                            player.mapToLoad = dest;
                        }
                    }
                }
            }
        } else { // Key Released
            if (event == Event.PlayerInteract) {
            }
        }
    }
}