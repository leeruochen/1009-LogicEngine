package github.com_1009project.abstractEngine;

public interface ICollidable {
    // method to be called when a collision occurs with another entity
    void onCollision(Entity other);
}
