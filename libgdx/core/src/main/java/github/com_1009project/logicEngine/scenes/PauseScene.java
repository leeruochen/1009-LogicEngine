package github.com_1009project.logicEngine.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
import github.com_1009project.logicEngine.helpers.GameContext;

public class PauseScene extends Scene {

    private ShapeRenderer shapeRenderer;
    private Skin skin;
    private EventManager eventManager;
    private SceneManager sceneManager;
    private InputManager inputManager;
    private EntityRegistry entityRegistry;
    private AssetManager assetManager;
    private SpriteBatch batch;

    public PauseScene(int id, GameContext gameContext) {
        super(id);
        this.assetManager = gameContext.getAssetManager();
        this.entityRegistry = gameContext.getEntityRegistry();
        this.eventManager = gameContext.getEventManager();
        this.inputManager = gameContext.getInputManager();
        this.batch = gameContext.getSpriteBatch();
        this.sceneManager = gameContext.getSceneManager();
        init();
    }

    @Override
    public void init() {
        shapeRenderer = new ShapeRenderer();

        UILayer uiLayer = new UILayer(batch);
        layers.add(uiLayer);

        skin = new Skin(Gdx.files.internal("menu/uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        uiLayer.getStage().addActor(table);

        // "PAUSED" title using warm label style
        Label title = new Label("PAUSED", skin, "warm");

        // Resume button — warm orange
        TextButton resumeBtn = new TextButton("Resume", skin, "warm-resume");
        resumeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.loadPreviousScene();
            }
        });

        // settings
        TextButton settingsBtn = new TextButton("Settings", skin, "warm-resume");
        settingsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.loadScene(2);
            }
        });
        // Quit button — warm red
        TextButton quitBtn = new TextButton("Exit to Menu", skin, "warm-quit");
        quitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.unloadScene(1);
                sceneManager.clearHistory();
                sceneManager.loadScene(0);
            }
        });

        table.add(title).padBottom(60f).row();
        table.add(resumeBtn).size(220f, 50f).padBottom(20f).row();
        table.add(settingsBtn).size(220f, 50f).padBottom(20f).row();
        table.add(quitBtn).size(220f, 50f);
    }

    @Override
    public void render() {
        // dim overlay so game world shows behind menu
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.10f, 0.06f, 0.03f, 0.72f);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        shapeRenderer.dispose();
        skin.dispose();
    }

    @Override
    public InputManager getSceneInputProcessor() {
        return inputManager;
    }
    
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        shapeRenderer.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
    }
}
