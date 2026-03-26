package github.com_1009project.abstractEngine;

import com.badlogic.gdx.InputProcessor;

public abstract class Layer {
    public void update(float deltaTime) {}
    public void render() {}
    public void dispose() {}
    public InputProcessor getInputProcessor() {
        return null;
    }
    public void resize(int width, int height) {}
}
