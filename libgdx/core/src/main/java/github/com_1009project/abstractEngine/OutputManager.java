package github.com_1009project.abstractEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;

public class OutputManager implements EventObserver{
    private AssetManager resourceManager;
    private HashMap<Event, String> soundMap;
    private HashMap<Event, String> musicMap;
    private Music curMusic;
    private void playMusic(Event event){
        if (curMusic != null) curMusic.stop();
        curMusic = resourceManager.get(musicMap.get(event), Music.class);
        curMusic.setLooping(true);
        curMusic.play();
    }

    private void playSound(Event event){
        resourceManager.get(soundMap.get(event), Sound.class).play(1.0f);
    }

    public void loadSound(Event event, String filePath){
        resourceManager.load(filePath, Sound.class);
        soundMap.put(event, filePath);
    }

    public void loadMusic(Event event, String filePath){
        resourceManager.load(filePath, Music.class);
        musicMap.put(event, filePath);
    }
    
    public void dispose(){
        if (curMusic != null){curMusic.dispose();}
        for (String filename : soundMap.values()) {
            resourceManager.unload(filename);
        }
        for (String filename : musicMap.values()) {
            resourceManager.unload(filename);
        }
        soundMap.clear();
        musicMap.clear();
    }

    public OutputManager(AssetManager resourceManager){
        soundMap = new HashMap<Event, String>();
        musicMap = new HashMap<Event, String>();
    }

    @Override
    public void onNotify(Event event, Boolean up){
        if (soundMap.containsKey(event)){playSound(event);}
        playMusic(event);
    }

    @Override
    public void onNotify(Event event, Boolean up, int screenX, int screenY){onNotify(event, up);}
    
}
