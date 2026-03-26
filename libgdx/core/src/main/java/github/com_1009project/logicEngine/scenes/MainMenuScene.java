package github.com_1009project.logicEngine.scenes;

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
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import github.com_1009project.abstractEngine.EntityRegistry;
import github.com_1009project.abstractEngine.EventManager;
import github.com_1009project.abstractEngine.InputManager;
import github.com_1009project.abstractEngine.Scene;
import github.com_1009project.abstractEngine.SceneManager;
import github.com_1009project.abstractEngine.UILayer;
import github.com_1009project.abstractEngine.Event;
import github.com_1009project.logicEngine.helpers.GameContext;

public class MainMenuScene extends Scene {

    private Texture background;
    private Skin skin;
    private BitmapFont titleFont;
    private EventManager eventManager;
    private SceneManager sceneManager;
    private InputManager inputManager;
    private EntityRegistry entityRegistry;
    private AssetManager assetManager;
    private SpriteBatch batch;

    public MainMenuScene(int id, GameContext gameContext) {
        super(id);
        this.assetManager = gameContext.getAssetManager();
        this.entityRegistry = gameContext.getEntityRegistry();
        this.eventManager = gameContext.getEventManager();
        this.inputManager = gameContext.getInputManager();
        this.sceneManager = gameContext.getSceneManager();
        this.batch = gameContext.getSpriteBatch();
        init();
    }

    @Override
    public void init() {
        background = assetManager.get("imgs/background.png", Texture.class);

        UILayer uiLayer = new UILayer(batch);
        layers.add(uiLayer);

        skin = new Skin(Gdx.files.internal("menu/uiskin.json"));
        titleFont = new BitmapFont(Gdx.files.internal("menu/title.fnt"), Gdx.files.internal("menu/title.png"), false);

        Table table = new Table();
        table.setFillParent(true);
        uiLayer.getStage().addActor(table);

        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.WHITE);
        Label title = new Label("UnderCooked", titleStyle);

        // play button
        TextButton playBtn = new TextButton("Play", skin, "warm-resume");
        playBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.loadScene(3); // load tutorial scene
            }
        });

        // settings button
        TextButton settingsBtn = new TextButton("Settings", skin, "warm-resume");
        settingsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.loadScene(2); // load settings scene
            }
        });

        // quit button
        TextButton quitBtn = new TextButton("Quit", skin, "warm-quit");
        quitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        table.add(title).padBottom(80f).row();
        table.add(playBtn).size(260f, 60f).padBottom(20f).row();
        table.add(settingsBtn).size(260f, 60f).padBottom(20f).row();
        table.add(quitBtn).size(260f, 60f);
    }

    @Override
    public void onEnter() {
        super.onEnter();
        eventManager.eventTrigger(Event.MenuEnter);
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

    @Override
    public InputManager getSceneInputProcessor() {
        return inputManager;
    }
}
