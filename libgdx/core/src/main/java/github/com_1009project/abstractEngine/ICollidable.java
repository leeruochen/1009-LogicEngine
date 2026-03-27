package github.com_1009project.abstractEngine;

// The ICollidable interface defines a contract for any entity that can participate in collision detection.

public interface ICollidable {
    // method to be called when a collision occurs with another entity
    void onCollision(Entity other);
}
