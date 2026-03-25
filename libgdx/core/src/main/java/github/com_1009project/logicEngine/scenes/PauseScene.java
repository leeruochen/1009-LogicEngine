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

import github.com_1009project.abstractEngine.EntityRegistry;
import github.com_1009project.abstractEngine.EventManager;
import github.com_1009project.abstractEngine.Scene;
import github.com_1009project.abstractEngine.SceneManager;
import github.com_1009project.abstractEngine.UILayer;

public class PauseScene extends Scene {

    private ShapeRenderer shapeRenderer;
    private Skin skin;

    public PauseScene(int id, AssetManager assetManager, EntityRegistry entityRegistry,
                      EventManager eventManager, SpriteBatch batch, SceneManager sceneManager) {
        super(id, assetManager, entityRegistry, eventManager, batch, sceneManager);
        init();
    }

    @Override
    public void init() {
        shapeRenderer = new ShapeRenderer();

        UILayer uiLayer = new UILayer(batch);
        layers.add(uiLayer);

        skin = new Skin(Gdx.files.internal("menu/uiskin.json"));

        float cx = Gdx.graphics.getWidth()  / 2f;
        float cy = Gdx.graphics.getHeight() / 2f;

        // "PAUSED" title using warm label style
        Label title = new Label("PAUSED", skin, "warm");
        title.setPosition(cx - title.getPrefWidth() / 2f, cy + 80f);
        uiLayer.getStage().addActor(title);

        // Resume button — warm orange
        TextButton resumeBtn = new TextButton("Resume", skin, "warm-resume");
        resumeBtn.setSize(220f, 50f);
        resumeBtn.setPosition(cx - 110f, cy);
        resumeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.loadPreviousScene();
            }
        });
        uiLayer.getStage().addActor(resumeBtn);

        // settings
        TextButton settingsBtn = new TextButton("Settings", skin, "warm-resume");
        settingsBtn.setSize(220f, 50f);
        settingsBtn.setPosition(cx - 110f, cy - 70f);
        settingsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.loadScene(2);
            }
        });
        uiLayer.getStage().addActor(settingsBtn);

        // Quit button — warm red
        TextButton quitBtn = new TextButton("Exit to Menu", skin, "warm-quit");
        quitBtn.setSize(220f, 50f);
        quitBtn.setPosition(cx - 110f, cy - 140f);
        quitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.unloadScene(1);
                sceneManager.clearHistory();
                sceneManager.loadScene(0);
            }
        });
        uiLayer.getStage().addActor(quitBtn);
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
}
