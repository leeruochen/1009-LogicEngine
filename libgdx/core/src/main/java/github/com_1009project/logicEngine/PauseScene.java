package github.com_1009project.logicEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import github.com_1009project.abstractEngine.EntityManager;
import github.com_1009project.abstractEngine.EventManager;
import github.com_1009project.abstractEngine.Scene;
import github.com_1009project.abstractEngine.SceneManager;
import github.com_1009project.abstractEngine.UILayer;

public class PauseScene extends Scene {
    private SceneManager sceneManager;

    public PauseScene(int id, AssetManager resourceManager, EntityManager entityManager, 
                      EventManager eventManager, SpriteBatch batch, SceneManager sceneManager) {
        super(id, resourceManager, entityManager, eventManager, batch);
        this.sceneManager = sceneManager;
        init();
    }

    @Override
    public void init() {
        // Create a simple UI Layer
        UILayer uiLayer = new UILayer(batch);
        layers.add(uiLayer);

        // Setup a basic Skin for the button (can customize)
        Skin skin = new Skin();
        BitmapFont font = new BitmapFont(); // Default font
        skin.add("default", font);

        // Create a simple button background
        Pixmap pixmap = new Pixmap(100, 50, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GRAY);
        pixmap.fill();
        skin.add("background", new Texture(pixmap));

        // Create a TextButton style
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = skin.newDrawable("background", Color.GRAY);
        style.down = skin.newDrawable("background", Color.DARK_GRAY);
        style.font = skin.getFont("default");

        // Create the "RESUME" button
        TextButton resumeButton = new TextButton("RESUME", style);
        resumeButton.setPosition(Gdx.graphics.getWidth() / 2f - 50, Gdx.graphics.getHeight() / 2f);

        // The logic to return to the game
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // When clicked, tell the manager to switch back to the main game scene (ID 1)
                sceneManager.loadScene(1); 
            }
        });

        uiLayer.getStage().addActor(resumeButton);
    }
}