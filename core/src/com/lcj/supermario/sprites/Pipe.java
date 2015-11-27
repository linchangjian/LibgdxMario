package com.lcj.supermario.sprites;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.lcj.supermario.SuperMario;
import com.lcj.supermario.screen.PlayScreen;

/**
 * Created by aniu on 15/11/26.
 */
public class Pipe extends InteractiveTileObject {


    public Pipe(PlayScreen screen, MapObject mapObject) {
        super(screen, mapObject);
        fixture.setUserData(this);
        setCategoryFilter(SuperMario.OBJECT_BIT);
    }

    @Override
    public void onHeadHit() {

    }
}
