package github.com_1009project.logicEngine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.assets.AssetManager;

import github.com_1009project.abstractEngine.CameraManager;
import github.com_1009project.abstractEngine.CollisionManager;
import github.com_1009project.abstractEngine.Entity;
import github.com_1009project.abstractEngine.EntityManager;
import github.com_1009project.abstractEngine.EventManager;
import github.com_1009project.abstractEngine.MapManager;
import github.com_1009project.abstractEngine.Scene;
import github.com_1009project.abstractEngine.SceneManager;
import github.com_1009project.logicEngine.entities.Player;

public class GameScene extends Scene {

    private CameraManager camera;
    private MapManager mapManager;
    private CollisionManager collisionManager;
    private Player player;

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
        init();
    }

    @Override
    public void init() {
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
    }

    @Override
    public void render() {
        mapManager.render(camera.getCamera());

        // batch will render entities according to cameraPosition
        batch.setProjectionMatrix(camera.getCamera().combined);
        batch.begin();
        entityManager.render(batch);
        batch.end();

        super.render();

        // Debug rendering for collision boxes
        shapeRenderer.setProjectionMatrix(camera.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line); // "Line" means empty boxes (outlines)
        shapeRenderer.setColor(Color.RED); // Set the pen color to red

        // Loop through the fast-lane list you made earlier!
        for (Entity entity : entityManager.getCollidableEntities()) {
            // Safety check to ensure the entity and its collision are active
            if (entity.isActive() && entity.getCollisionComponent().isActive()) {
                Rectangle bounds = entity.getCollisionComponent().getBounds();
                
                // Draw a rectangle using the exact math from your CollisionComponent
                shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
            }
        }
        shapeRenderer.end();
    }

    // this method is used to load a new map, it clears the current entities and loads the new map's entities
    // used when transitioning between maps, the player entity will have a variable that specifies which map to load, and when the variable is not null, this method will be called with the new map name
    private void loadMap(String mapName) {
        entityManager.dispose();

        mapManager.setMap(assetManager.get(mapName, TiledMap.class));
        System.out.println("Loaded map: " + mapName);
        mapManager.loadEntities();

        // find player entity and set camera target to player.
        for (Entity entity : entityManager.getEntities()) {
            if (entity instanceof Player) {
                player = (Player) entity;
                break;
            }
        }
        camera.setTarget(player);
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
    }
}
