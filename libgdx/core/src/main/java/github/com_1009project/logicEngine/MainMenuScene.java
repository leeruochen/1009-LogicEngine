package github.com_1009project.logicEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import github.com_1009project.abstractEngine.EntityManager;
import github.com_1009project.abstractEngine.EventManager;
import github.com_1009project.abstractEngine.Scene;
import github.com_1009project.abstractEngine.SceneManager;
import github.com_1009project.abstractEngine.UILayer;

public class MainMenuScene extends Scene {

    private Texture background;
    private Skin skin;
    private BitmapFont titleFont;

    public MainMenuScene(int id, AssetManager assetManager, EntityManager entityManager,
                         EventManager eventManager, SpriteBatch batch, SceneManager sceneManager) {
        super(id, assetManager, entityManager, eventManager, batch, sceneManager);
        init();
    }

    @Override
    public void init() {
        background = assetManager.get("imgs/background.png", Texture.class);

        UILayer uiLayer = new UILayer(batch);
        layers.add(uiLayer);

        skin = new Skin(Gdx.files.internal("menu/uiskin.json"));
        titleFont = new BitmapFont(Gdx.files.internal("menu/title.fnt"), Gdx.files.internal("menu/title.png"), false);

        float cx = Gdx.graphics.getWidth()  / 2f;
        float cy = Gdx.graphics.getHeight() / 2f;

        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.WHITE);
        Label title = new Label("UnderCooked", titleStyle);
        title.setPosition(cx - title.getPrefWidth() / 2f, cy + 120f);
        uiLayer.getStage().addActor(title);

        // play button
        TextButton playBtn = new TextButton("Play", skin, "warm-resume");
        playBtn.setSize(260f, 60f);
        playBtn.setPosition(cx - 130f, cy);

        playBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.loadScene(3); // load tutorial scene
            }
        });
        uiLayer.getStage().addActor(playBtn);

        // quit button
        TextButton quitBtn = new TextButton("Quit", skin, "warm-quit");
        quitBtn.setSize(260f, 60f);
        quitBtn.setPosition(cx - 130f, cy - 80f);
        quitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        uiLayer.getStage().addActor(quitBtn);
    }

    @Override
    public void render() {
        // draw background first then UI on top
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
        titleFont.dispose();
    }
}