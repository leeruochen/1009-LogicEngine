package github.com_1009project.abstractEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BackgroundLayer extends Layer {
    private Animation<TextureRegion> animation;
    private float stateTime;
    private SpriteBatch batch;
    private AssetManager assets;

    public BackgroundLayer(SpriteBatch batch, AssetManager assets, String folderPath, int frameCount, float frameDuration) {
        this.batch = batch;
        this.assets = assets;
        TextureRegion[] frames = new TextureRegion[frameCount];

        // Load all frames using AssetManager
        for (int i = 0; i < frameCount; i++) {
            String filename = String.format("%s/tile%03d.png", folderPath, i);
            if (!assets.isLoaded(filename)) {
                assets.load(filename, Texture.class);
                assets.finishLoading();
            }
            Texture tex = assets.get(filename, Texture.class);
            frames[i] = new TextureRegion(tex);
        }

        animation = new Animation<>(frameDuration, frames);
        stateTime = 0f;
    }

    @Override
    public void update(float delta) {
        stateTime += delta;
    }

    @Override
    public void render() {
        batch.begin();
        TextureRegion frame = animation.getKeyFrame(stateTime, true);
        batch.draw(frame, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
    }

    @Override
    public void dispose() {
        // Textures are managed by AssetManager; no need to dispose
    }
}