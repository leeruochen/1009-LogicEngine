package github.com_1009project.logicEngine.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import github.com_1009project.abstractEngine.CameraManager;
import github.com_1009project.abstractEngine.CollisionManager;
import github.com_1009project.abstractEngine.Entity;
import github.com_1009project.abstractEngine.EntityRegistry;
import github.com_1009project.abstractEngine.EntityRenderer;
import github.com_1009project.abstractEngine.EventManager;
import github.com_1009project.abstractEngine.InputManager;
import github.com_1009project.abstractEngine.MapEntityLoader;
import github.com_1009project.abstractEngine.MapManager;
import github.com_1009project.abstractEngine.Scene;
import github.com_1009project.abstractEngine.SceneManager;
import github.com_1009project.logicEngine.FoodQueueSystem;
import github.com_1009project.logicEngine.InteractionManager;
import github.com_1009project.logicEngine.entities.Player;
import github.com_1009project.abstractEngine.UIFactory;
import github.com_1009project.abstractEngine.UILayer;
import github.com_1009project.abstractEngine.Event;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GameScene extends Scene {

    private CameraManager camera;
    private MapManager mapManager;
    private CollisionManager collisionManager;
    private InteractionManager interactionManager;
    private EntityRenderer entityRenderer;
    private MapEntityLoader mapEntityLoader;
    private Player player;
    private FoodQueueSystem foodQueueSystem;
    private static final float ROUND_DURATION = 300f; // 5 minutes
    private float timeRemaining;
    private boolean roundOver = false;
    private Label timerLabel; 
    private ProgressBar timerBar;
    private SceneController sceneController;
    private enum GameState { PLAYING, TIMES_UP, RESULTS }
    private GameState currentState;
    private float postGameTimer;
    private ShapeRenderer shapeRenderer;
    private UILayer uiLayer;
    private Skin skin;
    private Label timesUpLabel;
    private Table resultsTable;

    public GameScene(int id, AssetManager assetManager, EntityRegistry entityRegistry, EntityRenderer entityRenderer, MapEntityLoader mapEntityLoader,
                     EventManager eventManager, InputManager inputManager, SpriteBatch batch, SceneManager sceneManager, int width, int height) {
        super(id, assetManager, entityRegistry, eventManager, inputManager, batch, sceneManager);
        this.entityRenderer = entityRenderer;
        this.mapEntityLoader = mapEntityLoader;
        this.camera = new CameraManager(width, height);
        this.camera.setBounds(4000, 4000);
        this.collisionManager = new CollisionManager(64);
        this.mapManager = new MapManager(mapEntityLoader);
        this.mapManager.setScale(4.0f);
        this.foodQueueSystem = new FoodQueueSystem(batch, assetManager, eventManager);
        this.foodQueueSystem.create();
        this.sceneController = new SceneController(sceneManager, eventManager);

        // Create the interaction manager, wired to the food queue for order submission
        this.interactionManager = new InteractionManager(entityRegistry, assetManager, foodQueueSystem.getFoodQueue(), eventManager);
        eventManager.addObserver(interactionManager);

        init();
    }

    @Override
    public void init() {
        timeRemaining = ROUND_DURATION;
        currentState = GameState.PLAYING;
        postGameTimer = 0f;

        shapeRenderer = new ShapeRenderer();
        uiLayer = new UILayer(batch);
        layers.add(uiLayer);
 
        if (assetManager.isLoaded("menu/uiskin.json")) {
            this.skin = assetManager.get("menu/uiskin.json", Skin.class);
        } else {
            // Fallback in case it wasn't loaded yet
            this.skin = new Skin(Gdx.files.internal("menu/uiskin.json"));
        }
        UIFactory factory = new UIFactory(uiLayer, skin, eventManager);
 
        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();
        timerLabel = factory.createTimerLabel(ROUND_DURATION, sw / 2f - 100f, 50f);
        timerBar   = factory.createTimerBar(ROUND_DURATION);

        timesUpLabel = new Label("TIME'S UP!", skin, "default");
        timesUpLabel.setFontScale(4.0f);
        timesUpLabel.setColor(Color.RED);
        timesUpLabel.layout();
        timesUpLabel.setPosition((sw - timesUpLabel.getPrefWidth()) / 2f, sh / 2f);
        timesUpLabel.setVisible(false);
        uiLayer.getStage().addActor(timesUpLabel);

        loadMap("maps/kitchen.tmx");
    };

    @Override
    public void onEnter() {
        super.onEnter();
        eventManager.eventTrigger(Event.GameStart);
    }

    @Override
    public void update(float delta) {
        if (currentState == GameState.PLAYING) {
            // Normal game loop
            entityRegistry.update(delta);
            collisionManager.updateCollision(entityRegistry.getCollidableEntities());
            camera.cameraUpdate(delta);
            foodQueueSystem.update(delta);
            interactionManager.update(delta);
            
            timeRemaining -= delta;

            if (timeRemaining <= 0) {
                timeRemaining = 0;
                currentState = GameState.TIMES_UP;
                timesUpLabel.setVisible(true); // Show the pop-up text
                timesUpLabel.setFontScale(5.0f);
            }

            timerLabel.setText(formatTime(timeRemaining));
            timerLabel.setColor(timeRemaining <= 30f ? Color.RED : Color.WHITE);
            timerBar.setValue(timeRemaining);

        } else if (currentState == GameState.TIMES_UP) {
            // Wait a few seconds before showing results
            postGameTimer += delta;
            if (postGameTimer >= 3.0f) { // 3 second delay
                currentState = GameState.RESULTS;
                timesUpLabel.setVisible(false);
                showResultsUI();
            }
        } 
        // If state is RESULTS, we don't need to do anything in update; Scene2D handles UI clicks automatically.
    }

    @Override
    public void render() {
        mapManager.render(camera.getCamera());
        batch.setProjectionMatrix(camera.getCamera().combined);
        batch.begin();
        entityRenderer.render(batch);
        batch.end();
        super.render();
        foodQueueSystem.render(Gdx.graphics.getDeltaTime());

        if (currentState != GameState.PLAYING) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0f, 0f, 0f, 0.65f); // 65% opacity black
            shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
        super.render(); // Render UI on top of everything else
    }
    private void showResultsUI() {
        if (skin == null || skin == null) return; // Safety check

        Table resultsTable = new Table();
        resultsTable.setFillParent(true);
        resultsTable.center();
        
        // Calculate score and stars
        int score = foodQueueSystem.getFoodQueue().getClearedCount();
        int stars = Math.min(score, 3); 

        Label titleLabel = new Label("ROUND COMPLETE", skin, "default");
        titleLabel.setFontScale(2.5f);
        titleLabel.setColor(Color.GOLD);

        Label scoreLabel = new Label("Dishes Served: " + score, skin);
        scoreLabel.setFontScale(1.5f);

        Label starsLabel = new Label("Rating: " + stars + " Stars", skin);
        starsLabel.setFontScale(1.5f);
        starsLabel.setColor(stars > 0 ? Color.YELLOW : Color.GRAY);

        TextButton retryBtn = new TextButton("Retry", skin, "warm-resume");
        retryBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.unloadScene(1);
                sceneManager.loadScene(1);
            }
        });

        TextButton settingsBtn = new TextButton("Settings", skin, "warm-resume");
        settingsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.loadScene(2);
            }
        });

        TextButton exitBtn = new TextButton("Main Menu", skin, "warm-quit");
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.unloadScene(1);
                sceneManager.loadScene(0);
            }
        });

        // Add to table
        resultsTable.add(titleLabel).padBottom(30).row();
        resultsTable.add(scoreLabel).padBottom(10).row();
        resultsTable.add(starsLabel).padBottom(40).row();
        resultsTable.add(retryBtn).width(250).height(60).padBottom(10).row();
        resultsTable.add(settingsBtn).width(250).height(60).padBottom(10).row();
        resultsTable.add(exitBtn).width(250).height(60).row();

        uiLayer.getStage().addActor(resultsTable);
    }

    private void loadMap(String mapName) {
        entityRegistry.disposeAll();
        mapManager.setMap(assetManager.get(mapName, TiledMap.class));
        System.out.println("Loaded map: " + mapName);
        mapManager.loadEntities();

        for (Entity entity : entityRegistry.getEntities()) {
            if (entity instanceof Player) {
                player = (Player) entity;
                break;
            }
        }
        camera.setTarget(player);
    }

    private String formatTime(float seconds) {
        int m = (int) (seconds / 60);
        int s = (int) (seconds % 60);
        return String.format("%02d:%02d", m, s);
    }

    public void resize(int width, int height) {
        camera.resize(width, height);
        mapManager.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        mapManager.dispose();
        foodQueueSystem.dispose(); 
        eventManager.removeObserver(interactionManager);
        sceneController.dispose(eventManager);
        if (shapeRenderer != null) shapeRenderer.dispose();
    }
}