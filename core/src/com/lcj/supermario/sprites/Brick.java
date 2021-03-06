package com.lcj.supermario.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.lcj.supermario.SuperMario;
import com.lcj.supermario.scenes.Hud;
import com.lcj.supermario.screen.PlayScreen;

/**
 * Created by aniu on 15/11/24.
 */
public class Brick extends InteractiveTileObject{
    public Brick(PlayScreen screen, MapObject mapObject) {
        super(screen, mapObject);
        fixture.setUserData(this);
        setCategoryFilter(SuperMario.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {
        if(mario.isBig()){
            Gdx.app.log("Brick","Collision");
            setCategoryFilter(SuperMario.DESTROYED_BIT);
            getCell().setTile(null);
            Hud.addScore(200);
            SuperMario.manager.get("audio/sounds/breakblock.wav",Sound.class).play();
        }else{
            SuperMario.manager.get("audio/sounds/bump.wav",Sound.class).play();

        }
    }
}
