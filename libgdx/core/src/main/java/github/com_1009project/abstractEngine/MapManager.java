package github.com_1009project.abstractEngine;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;

import com.badlogic.gdx.Gdx;
//https://libgdx.com/wiki/graphics/2d/tile-maps

public class MapManager implements Disposable {

    private EntityManager entityManager;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private float map_scale;
    private OrthographicCamera staticCam;

    // constructor initializes map manager with entity manager and default scale
    // also sets up a static camera for rendering the map if needed
    public MapManager(EntityManager entityManager) {
        this.map_scale = 1.0f;
        this.entityManager = entityManager;
        this.staticCam = new OrthographicCamera();
        this.staticCam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void setScale(float scale) {
        // set scale if needed
        this.map_scale = scale;
    }

    public void setMap(TiledMap map) {
        // set the map
        this.map = map;
        this.renderer = new OrthogonalTiledMapRenderer(map, map_scale);
    }
    
    public TiledMap getMap() {
        // return the current map
        return this.map;
    }

    // render static map
    public void render() {
        if (renderer == null) return;
        renderer.setView(staticCam);
        renderer.render();
    }

    // render dynamic map with camera
    public void render(OrthographicCamera camera) {
        if (renderer == null) return;
        renderer.setView(camera);
        renderer.render();
    }

    // load entities from map objects
    public void loadEntities() {
        Iterable<MapLayer> mapLayers = map.getLayers();
        
        for (MapLayer layer : mapLayers) {
            for (MapObject object : layer.getObjects()) {
                entityManager.createEntity(object, map_scale);
            }
        }
    }

    // returns static camera
    public OrthographicCamera getCamera() {
        return staticCam;
    }

    public void resize(int width, int height) {
        // update camera viewport on window resize
        this.staticCam.setToOrtho(false, width, height);
        this.staticCam.update();
    }

    // dispose of map resources
    public void dispose() {
        if (renderer != null) renderer.dispose();
    }
}