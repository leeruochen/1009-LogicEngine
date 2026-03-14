package github.com_1009project.logicEngine;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import github.com_1009project.abstractEngine.EntityManager;
import github.com_1009project.abstractEngine.EventManager;
import github.com_1009project.abstractEngine.Scene;

public class TestScene extends Scene {
    public TestScene(int id, AssetManager resourceManager,
                     EntityManager entityManager, EventManager eventManager,
                     SpriteBatch batch) {
        super(id, resourceManager, entityManager, eventManager, batch);
    }

    @Override
    public void init() {
        super.init(); // adds BackgroundLayer, EntityLayer, UILayer
        System.out.println("TestScene initialized");
    }

    @Override
    public void onEnter() {
        super.onEnter();
        // System.out.println("Entered TestScene");
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        System.out.println("Disposed TestScene");
    }
}