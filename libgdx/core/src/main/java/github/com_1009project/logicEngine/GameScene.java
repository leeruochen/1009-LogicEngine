package github.com_1009project.logicEngine;

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

import github.com_1009project.abstractEngine.CameraManager;
import github.com_1009project.abstractEngine.CollisionManager;
import github.com_1009project.abstractEngine.Entity;
import github.com_1009project.abstractEngine.EntityManager;
import github.com_1009project.abstractEngine.EventManager;
import github.com_1009project.abstractEngine.MapManager;
import github.com_1009project.abstractEngine.Scene;
import github.com_1009project.abstractEngine.SceneManager;
import github.com_1009project.logicEngine.entities.Player;
import github.com_1009project.abstractEngine.UIFactory;
import github.com_1009project.abstractEngine.UILayer;

public class GameScene extends Scene {

    private CameraManager camera;
    private MapManager mapManager;
    private CollisionManager collisionManager;
    private Player player;
    private FoodQueueSystem foodQueueSystem;
    private InteractionManager interactionManager;
    private static final float ROUND_DURATION = 300f; // 5 minutes
    private float timeRemaining;
    private boolean roundOver = false;
    private Label timerLabel; 
    private ProgressBar timerBar;

    private ShapeRenderer shapeRenderer; // For debugging collision boxes

    public GameScene(int id, AssetManager assetManager, EntityManager entityManager,
                     EventManager eventManager, SpriteBatch batch, SceneManager sceneManager, int width, int height) {
        super(id, assetManager, entityManager, eventManager, batch, sceneManager);
        this.camera = new CameraManager(width, height);
        this.camera.setBounds(4000, 4000);

        this.collisionManager = new CollisionManager(64);
        this.mapManager = new MapManager(entityManager);
        this.mapManager.setScale(4.0f);

        this.shapeRenderer = new ShapeRenderer();
        this.foodQueueSystem = new FoodQueueSystem(batch, assetManager, eventManager);
        this.foodQueueSystem.create();

        // Create the interaction manager, wired to the food queue for order submission
        this.interactionManager = new InteractionManager(entityManager, assetManager, foodQueueSystem.getFoodQueue());
        eventManager.addObserver(interactionManager);

        init();
    }

    @Override
    public void init() {
        timeRemaining = ROUND_DURATION;

        UILayer uiLayer = new UILayer(batch);
        layers.add(uiLayer);
 
        Skin skin = new Skin(Gdx.files.internal("menu/uiskin.json"));
        UIFactory factory = new UIFactory(uiLayer, skin, eventManager);
 
        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();
        timerLabel = factory.createTimerLabel(ROUND_DURATION, sw / 2f - 100f, 50f);
        timerBar   = factory.createTimerBar(ROUND_DURATION);

        loadMap("maps/kitchen.tmx");
    };

    @Override
    public void onEnter() {
        super.onEnter();
    }

    @Override
    public void update(float delta) {
        entityManager.update(delta);
        collisionManager.updateCollision(entityManager.getCollidableEntities());
        camera.cameraUpdate(delta);
        foodQueueSystem.update(delta);
        timeRemaining -= delta;

        if (timeRemaining <= 0) {
            timeRemaining = 0;
        }

        timerLabel.setText(formatTime(timeRemaining));
        timerLabel.setColor(timeRemaining <= 30f ? Color.RED : Color.WHITE);
        timerBar.setValue(timeRemaining);
    }    

    @Override
    public void render() {
        mapManager.render(camera.getCamera());

        batch.setProjectionMatrix(camera.getCamera().combined);
        batch.begin();
        entityManager.render(batch);
        batch.end();

        super.render();

        // Debug rendering for collision boxes
        shapeRenderer.setProjectionMatrix(camera.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);

        for (Entity entity : entityManager.getCollidableEntities()) {
            if (entity.isActive() && entity.getCollisionComponent().isActive()) {
                Rectangle bounds = entity.getCollisionComponent().getBounds();
                shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
            }
        }
        shapeRenderer.end();
        foodQueueSystem.render(Gdx.graphics.getDeltaTime());
    }

    private void loadMap(String mapName) {
        entityManager.disposeAll();

        mapManager.setMap(assetManager.get(mapName, TiledMap.class));
        System.out.println("Loaded map: " + mapName);
        mapManager.loadEntities();

        for (Entity entity : entityManager.getEntities()) {
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
        shapeRenderer.dispose();
        mapManager.dispose();
        foodQueueSystem.dispose(); 
    }
}
