package github.com_1009project.abstractEngine;

import com.badlogic.gdx.maps.MapObject;

// The MapEntity interface defines a contract for creating entities from map objects. 
// It provides a method for creating an entity based on a MapObject and a map scale factor. 
// This allows for dynamic creation of entities based on the data defined in a map, such as positions, sizes, and other properties specified in the map editor. 
// Implementing classes can use the information from the MapObject to initialize the entity's attributes accordingly.

public interface MapEntity {
    public <T extends Entity> T createEntity(MapObject object, float map_scale);
}
