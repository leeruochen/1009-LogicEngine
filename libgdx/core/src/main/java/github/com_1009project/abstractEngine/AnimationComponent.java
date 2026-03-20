package github.com_1009project.abstractEngine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;
import java.util.Map;

public class AnimationComponent {
    private Map<String, Animation<TextureRegion>> animations;
    private String currentState;
    private float stateTime;

    public AnimationComponent() {
        this.animations = new HashMap<>();
        this.stateTime = 0f;
        this.currentState = "IDLE"; // Default state
    }

    public void addAnimation(String stateName, Texture sheet, int cols, int rows, float frameDuration) {
        int frameWidth = sheet.getWidth() / cols;
        int frameHeight = sheet.getHeight() / rows;
        TextureRegion[][] tmp = TextureRegion.split(sheet, frameWidth, frameHeight);

        TextureRegion[] frames = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        animations.put(stateName, new Animation<>(frameDuration, frames));
    }

    public void setState(String newState) {
        if (!newState.equals(currentState) && animations.containsKey(newState)) {
            this.currentState = newState;
            this.stateTime = 0f; 
        }
    }

    public String getCurrentState() {
        return this.currentState;
    }

    public TextureRegion getCurrentFrame(float deltaTime, boolean looping) {
        this.stateTime += deltaTime;
        Animation<TextureRegion> currentAnim = animations.get(currentState);
        
        if (currentAnim != null) {
            return currentAnim.getKeyFrame(stateTime, looping);
        }
        return null;
    }
}