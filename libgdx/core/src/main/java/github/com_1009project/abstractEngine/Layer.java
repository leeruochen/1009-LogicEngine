package github.com_1009project.abstractEngine;

import com.badlogic.gdx.InputProcessor;

// The Layer class represents a layer in the game, which can contain multiple entities and systems.

public abstract class Layer {
    public void update(float deltaTime) {}
    public void render() {}
    public void dispose() {}
    public InputProcessor getInputProcessor() {
        return null;
    }
    public void resize(int width, int height) {}
}
