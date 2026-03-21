package github.com_1009project.logicEngine.factories;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.entities.Player;

public class PlayerFactory implements Ifactory<Player>{

    private final AssetManager assetManager;

    public PlayerFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public Player createEntity(float x, float y, float width, float height) {
        Texture idleSheet = assetManager.get("character/human_idle.png", Texture.class);
        Texture runSheet = assetManager.get("character/human_run.png", Texture.class);
        Player entity = new Player(x, y, width, height, idleSheet, runSheet);
        return entity;
    }
}
