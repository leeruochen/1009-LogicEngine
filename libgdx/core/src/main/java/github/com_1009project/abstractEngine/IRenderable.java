package github.com_1009project.abstractEngine;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// The IRenderable interface defines a contract for any entity that can be rendered in the game. 
// It requires implementing classes to provide a render method that takes a SpriteBatch as a parameter, which is used for drawing the entity on the screen. 
// Additionally, it includes a getY method that returns the Y position of the entity, which can be used for sorting entities by their vertical position to ensure correct rendering order (entities with higher Y values are rendered behind those with lower Y values).

public interface IRenderable {
    // method to be called to render the entity
    void render(SpriteBatch batch);
    float getY();
}
