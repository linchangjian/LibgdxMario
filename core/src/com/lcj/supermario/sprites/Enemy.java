package com.lcj.supermario.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.lcj.supermario.screen.PlayScreen;


/**
 * Created by aniu on 15/11/24.
 */
public abstract class Enemy extends Sprite {
    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    protected Vector2 velocity;


    public Enemy(PlayScreen screen, float x, float y){
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x,y);
        defineEnemy();
        velocity = new Vector2(-1, 0);
        b2body.setActive(false);
    }

    protected abstract void defineEnemy();
    public abstract void hitOnHead(Mario mario);
    public abstract void update(float dt);
    public void reverseVelocity(boolean x , boolean y){
        if(x)
            velocity.x = -velocity.x;
        if(y)
            velocity.y = -velocity.y;
    }
}
