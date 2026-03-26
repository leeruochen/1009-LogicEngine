package github.com_1009project.logicEngine.helpers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import github.com_1009project.abstractEngine.EntityRegistry;
import github.com_1009project.abstractEngine.EntityRenderer;
import github.com_1009project.abstractEngine.EventManager;
import github.com_1009project.abstractEngine.InputManager;
import github.com_1009project.abstractEngine.MapEntityLoader;
import github.com_1009project.abstractEngine.SceneManager;
import github.com_1009project.abstractEngine.OutputManager;
import github.com_1009project.abstractEngine.MovementManager;

public class GameContext {
    private final AssetManager assetManager;
    private final EntityRegistry entityRegistry;
    private final EntityRenderer entityRenderer; 
    private final MapEntityLoader mapEntityLoader; 
    private final EventManager eventManager;
    private final InputManager inputManager;
    private final SpriteBatch batch;
    private final SceneManager sceneManager;
    private final OutputManager outputManager;
    private final MovementManager movementManager;

    public GameContext(AssetManager assetManager, EntityRegistry entityRegistry, EntityRenderer entityRenderer,
                       MapEntityLoader mapEntityLoader, EventManager eventManager,
                       InputManager inputManager, SpriteBatch spriteBatch, SceneManager sceneManager, OutputManager outputManager, MovementManager movementManager) {
        this.assetManager = assetManager;
        this.entityRegistry = entityRegistry;
        this.entityRenderer = entityRenderer;
        this.mapEntityLoader = mapEntityLoader;
        this.eventManager = eventManager;
        this.inputManager = inputManager;
        this.batch = spriteBatch;
        this.sceneManager = sceneManager;
        this.outputManager = outputManager;
        this.movementManager = movementManager;
    }

    public AssetManager getAssetManager() { return assetManager; }
    public SpriteBatch getSpriteBatch() { return batch; }
    public EntityRegistry getEntityRegistry() { return entityRegistry; }
    public EntityRenderer getEntityRenderer() { return entityRenderer; }
    public MapEntityLoader getMapEntityLoader() { return mapEntityLoader; }
    public EventManager getEventManager() { return eventManager; }
    public InputManager getInputManager() { return inputManager; }
    public SceneManager getSceneManager() { return sceneManager; }
    public OutputManager getOutputManager() { return outputManager; }
    public MovementManager getMovementManager() { return movementManager; }
}
