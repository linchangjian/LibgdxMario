package com.lcj.supermario.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.lcj.supermario.screen.PlayScreen;


/**
 * Created by aniu on 15/11/24.
 */
public abstract class Enemy extends Sprite {
    protected World world;
    protected PlayScreen screen;
    protected Body b2body;

    public Enemy(PlayScreen screen, float x, float y){
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x,y);
        defineEnemy();
    }

    protected abstract void defineEnemy();
    public abstract void hitOnHead();
}
