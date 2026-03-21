package github.com_1009project.abstractEngine;

import com.badlogic.gdx.maps.MapObject;

public interface MapEntity {
    public <T extends Entity> T createEntity(MapObject object, float map_scale);
}
