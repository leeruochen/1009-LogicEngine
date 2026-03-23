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
        Texture chopSheet1 = assetManager.get("character/human_chop1.png", Texture.class);
        Texture chopSheet2 = assetManager.get("character/human_chop2.png", Texture.class);
        Texture deathSheet = assetManager.get("character/human_death.png", Texture.class);
        Player entity = new Player(x, y, width, height, idleSheet, runSheet, chopSheet1, chopSheet2, deathSheet);
        return entity;
    }
}
