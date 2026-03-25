package github.com_1009project.abstractEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;

public class OutputManager implements EventObserver{
    private AssetManager assetManager;
    private HashMap<Event, String> soundMap;
    private HashMap<Event, String> musicMap;
    private Music curMusic;

    private void playMusic(Event event){
        String targetMusicPath = musicMap.get(event);
        Music targetMusic = assetManager.get(targetMusicPath, Music.class);

        if (curMusic == targetMusic) {
            if (!curMusic.isPlaying()) {
                curMusic.play();
            }
            return;
        }

        if (curMusic != null) curMusic.stop();
        curMusic = targetMusic;
        curMusic.setLooping(true);
        curMusic.play();
    }

    private void playSound(Event event){
        assetManager.get(soundMap.get(event), Sound.class).play(1.0f);
    }

    public void registerSound(Event event, String filePath){
        soundMap.put(event, filePath);
    }

    public void registerMusic(Event event, String filePath){
        musicMap.put(event, filePath);
    }
    
    public void dispose(){
        soundMap.clear();
        musicMap.clear();
    }

    public OutputManager(AssetManager assetManager){
        soundMap = new HashMap<Event, String>();
        musicMap = new HashMap<Event, String>();
        this.assetManager = assetManager;
    }

    @Override
    public void onNotify(Event event, Boolean up){
        if (up != null && up) return;

        if (soundMap.containsKey(event)) playSound(event);
        if (musicMap.containsKey(event)) playMusic(event);
    }

    @Override
    public void onNotify(Event event, Boolean up, int screenX, int screenY){onNotify(event, up);}
    
}
