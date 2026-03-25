package github.com_1009project.abstractEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.Preferences;

public class OutputManager implements EventObserver{
    private AssetManager assetManager;
    private HashMap<Event, String> soundMap;
    private HashMap<Event, String> musicMap;
    private Music curMusic;
    private String prefName;
    private Event settingsUpdateEvent;
    private float musicVolume = 1.0f;
    private float soundVolume = 1.0f;

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
        curMusic.setVolume(musicVolume);
        curMusic.play();
    }

    public OutputManager(AssetManager assetManager, String prefName, Event settingsUpdateEvent) {
        soundMap = new HashMap<Event, String>();
        musicMap = new HashMap<Event, String>();
        this.assetManager = assetManager;
        this.prefName = prefName;
        this.settingsUpdateEvent = settingsUpdateEvent;

        loadPreferences();
    }

    public OutputManager(AssetManager assetManager, String name) {
        this(assetManager); 
    }

    public OutputManager(AssetManager assetManager) {
        this(assetManager, null, null);
    }

    private void loadPreferences(){
        if (prefName == null) return;

        Preferences prefs = Gdx.app.getPreferences(prefName);
        musicVolume = prefs.getFloat("musicVolume", 1.0f);
        soundVolume = prefs.getFloat("soundVolume", 1.0f);

        if (curMusic != null) curMusic.setVolume(musicVolume);
    }

    private void playSound(Event event){
        assetManager.get(soundMap.get(event), Sound.class).play(soundVolume);
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

    @Override
    public void onNotify(Event event, Boolean up){
        if (up != null && up) return;

        if (settingsUpdateEvent != null && event == settingsUpdateEvent) {
            loadPreferences();
            return;
        }

        if (soundMap.containsKey(event)) playSound(event);
        if (musicMap.containsKey(event)) playMusic(event);
    }

    @Override
    public void onNotify(Event event, Boolean up, int screenX, int screenY){onNotify(event, up);}
    
}
