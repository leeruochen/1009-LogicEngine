package github.com_1009project.abstractEngine;

// The IUpdatable interface defines a contract for any entity that can be updated in the game. 
// It requires implementing classes to provide an update method that takes a deltaTime parameter, which represents the time elapsed since the last update. 
// This allows entities to perform time-based updates, such as moving, animating, or processing game logic based on the passage of time.

public interface IUpdatable {
    // method to be called to update the entity
    void update (float deltaTime);
}
