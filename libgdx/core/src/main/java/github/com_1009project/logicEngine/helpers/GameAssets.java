package github.com_1009project.logicEngine.helpers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import github.com_1009project.logicEngine.FoodQueueSystem;

public final class GameAssets {
    public static void loadAll(AssetManager assetManager){
        // load textures
        for(int i=0; i<52; i++){
            assetManager.load(String.format("imgs/bg_frames/tile%03d.png", i), Texture.class);
        }
        assetManager.load("imgs/background.png", Texture.class);
        assetManager.load("foodstations/rubbishBin.png", Texture.class);
        assetManager.load("foodstations/counter.png", Texture.class);
        assetManager.load("foodstations/counter_submission.png", Texture.class);
        assetManager.load("foodstations/counter_choppingboard.png", Texture.class);
        assetManager.load("foodstations/stove_cooking.png", Texture.class);
        assetManager.load("foodstations/stove.png", Texture.class);
        assetManager.load("foodstations/plate_box.png", Texture.class);
        assetManager.load("foodstations/patty_box.png", Texture.class);
        assetManager.load("foodstations/bun_box.png", Texture.class);
        assetManager.load("foodstations/lettuce_box.png", Texture.class);
        assetManager.load("foodstations/cheese_box.png", Texture.class);
        assetManager.load("foodstations/tomato_box.png", Texture.class);

        assetManager.load("character/human_idle.png", Texture.class);
        assetManager.load("character/human_run.png", Texture.class);
        assetManager.load("character/human_death.png", Texture.class);
        assetManager.load("character/human_chop1.png", Texture.class);
        assetManager.load("character/human_chop2.png", Texture.class);

        // Raw ingredient textures (used when spawning from boxes)
        assetManager.load("food/bun.png", Texture.class);
        assetManager.load("food/lettuce.png", Texture.class);
        assetManager.load("food/tomato.png", Texture.class);
        assetManager.load("food/cheese.png", Texture.class);
        assetManager.load("food/patty.png", Texture.class);

        // Chopped / cooked ingredient textures
        assetManager.load("food/lettuce_chopped.png", Texture.class);
        assetManager.load("food/tomato_chopped.png", Texture.class);
        assetManager.load("food/cheese_chopped.png", Texture.class);
        assetManager.load("food/patty_cooked.png", Texture.class);
        assetManager.load("food/patty_burnt.png", Texture.class);

        // Plate / dish texture
        assetManager.load("food/dish.png", Texture.class);
        
        // Food textures + UI skin (for the order queue HUD)
        FoodQueueSystem.queueAssets(assetManager);

        // load music
        assetManager.load("sounds/MainMenuMusic.mp3", Music.class);
        assetManager.load("sounds/LevelMusic.mp3", Music.class);

        // load sounds
        assetManager.load("sounds/Chop.mp3", Sound.class);
        assetManager.load("sounds/SubmissionCorrect.mp3", Sound.class);
        assetManager.load("sounds/SubmissionWrong.mp3", Sound.class);
        assetManager.load("sounds/IngredientTake.mp3", Sound.class);
        assetManager.load("sounds/Place.mp3", Sound.class);
        assetManager.load("sounds/Bin.mp3", Sound.class);

        // load tmx maps
        assetManager.setLoader(TiledMap.class, new TmxMapLoader());
        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.projectFilePath = "maps/test.tiled-project";
        assetManager.load("maps/kitchen.tmx", TiledMap.class, params);
        assetManager.update();
        assetManager.finishLoading();
    }
}