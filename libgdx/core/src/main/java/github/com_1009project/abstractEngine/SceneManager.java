package github.com_1009project.abstractEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Supplier;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// The SceneManager class is responsible for managing different scenes in the game. It allows for registering scenes with unique IDs, loading scenes, switching between scenes, and maintaining a history of previously loaded scenes. 
// The SceneManager uses a Map to store registered scenes and their corresponding suppliers, which are used to create new instances of scenes when needed.

public class SceneManager {
    private Map<Integer, Scene> scenes = new HashMap<>();
    private Map<Integer, Supplier<Scene>> sceneSuppliers = new HashMap<>();
    private Scene currentScene;
    private SpriteBatch batch;
    private Stack<Integer> sceneHistory = new Stack<>();

    public SceneManager(SpriteBatch batch) {
        this.batch = batch;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void registerScene(int id, Supplier<Scene> sceneSupplier) {
        sceneSuppliers.put(id, sceneSupplier);
    }

    public void loadScene(int id) {
        if (currentScene != null && currentScene.getId() != id) {
            sceneHistory.push(currentScene.getId());
        }
        switchScene(id);

        currentScene = scenes.get(id);
        currentScene.onEnter();
        currentScene.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void loadPreviousScene() {
        if (!sceneHistory.isEmpty()) {
            int previousSceneId = sceneHistory.pop();
            switchScene(previousSceneId);
        }

        currentScene.onEnter();
        currentScene.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void clearHistory() {
        sceneHistory.clear();
    }

    public void switchScene(int id) {
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
}
