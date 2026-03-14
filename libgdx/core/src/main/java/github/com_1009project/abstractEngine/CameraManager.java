package github.com_1009project.abstractEngine;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

//https://libgdx.com/wiki/graphics/2d/orthographic-camera

public class CameraManager {
    // camera instance for managing view and movement
    private OrthographicCamera camera = null;
    private Entity target;

    // interpolation factor for smooth camera movement, can be adjusted for faster or slower following
    private float lerp = 0.1f; // interpolation factor for smooth camera movement

    // shake effect attributes
    private float shakeIntensity = 0f;
    private float shakeDuration = 0f;

    // world bounds attributes
    private float worldWidth;
    private float worldHeight;
    private boolean boundsActive = false;

    // constructor initializes camera with given viewport size
    public CameraManager(int width, int height) {
        camera = new OrthographicCamera();
        // set camera's viewport size
        camera.setToOrtho(false, width, height);
    }

    // allows setting the target entity for the camera to follow
    public void setTarget(Entity target) {
        this.target = target;
    }

    // allows setting world bounds for the camera to prevent it from moving outside the game world
    public void setBounds(float worldWidth, float worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.boundsActive = true;
    }

    // update camera position based on target and apply shake effect if active
    public void cameraUpdate(float delta) {
        // get coordinates of the target entity
        float targetX = target.getPosition().x + target.getSize().x / 2;
        float targetY = target.getPosition().y + target.getSize().y / 2;

        // move camera towards the target position
        Vector3 position = camera.position;
        position.x += (targetX - position.x) * lerp;
        position.y += (targetY - position.y) * lerp;

        if (shakeDuration > 0) {
            // create shake effect by adding random offsets
            // mathutils.random() gives a float between 0 and 1
            float shakeX = (float)(MathUtils.random() - 0.5f) * shakeIntensity;
            float shakeY = (float)(MathUtils.random() - 0.5f) * shakeIntensity;
            position.x += shakeX;
            position.y += shakeY;

            // duration decays over time
            shakeDuration -= delta;
            if (shakeDuration <= 0) {
                shakeIntensity = 0f;
            }
        }

        if (boundsActive) {
            // clamp camera position to world bounds
            // viewportWidth and viewportHeight are the size of the camera view
            float halfWidth = camera.viewportWidth / 2;
            float halfHeight = camera.viewportHeight / 2;

            // clamp forces position x and y to be within the world boundaries
            // ensuring the camera view does not go outside the world
            position.x = MathUtils.clamp(position.x, halfWidth, worldWidth - halfWidth);
            position.y = MathUtils.clamp(position.y, halfHeight, worldHeight - halfHeight);
        }

        // calls OrthographicCamera's update to apply changes
        camera.update();
    }

    // shake the camera with given intensity and duration
    public void shake(float intensity, float duration) {
        this.shakeIntensity = intensity;
        this.shakeDuration = duration;
    }

    public void resize(int width, int height) {
        // update camera viewport on window resize
        camera.setToOrtho(false, width, height);
        camera.update();
    }

    // returns the camera instance
    public OrthographicCamera getCamera() {
        return camera;
    }
}
