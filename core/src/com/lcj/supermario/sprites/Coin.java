package com.lcj.supermario.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.lcj.supermario.SuperMario;
import com.lcj.supermario.item.ItemDef;
import com.lcj.supermario.item.Mushroom;
import com.lcj.supermario.screen.PlayScreen;

/**
 * Created by aniu on 15/11/24.
 */
public class Coin extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 70;
    public Coin(PlayScreen screen, MapObject mapObject) {
        super(screen, mapObject);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(SuperMario.COIN_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Coin", "Collision");
        //setCategoryFilter(SuperMario.DESTROYED_BIT);
        if(getCell().getTile().getId() == BLANK_COIN){
            SuperMario.manager.get("audio/sounds/bump.wav",Sound.class).play();
        }else{
            if(mapObject.getProperties().containsKey("mashroom")){
                SuperMario.manager.get("audio/sounds/powerup.wav",Sound.class).play();
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16/SuperMario.PPM), Mushroom.class));

            }else {
                SuperMario.manager.get("audio/sounds/coin.wav",Sound.class).play();
            }
        }
        getCell().setTile(tileSet.getTile(BLANK_COIN));
    }
}
