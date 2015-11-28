package com.lcj.supermario.item;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.lcj.supermario.SuperMario;
import com.lcj.supermario.screen.PlayScreen;
import com.lcj.supermario.sprites.Mario;

/**
 * Created by aniu on 15/11/26.
 */
public class Mushroom extends Item {
    public Mushroom(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("mushroom"),0, 0, 16, 16);
        velocity = new Vector2(0.7f,0f);
    }

    @Override
    protected void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7/ SuperMario.PPM);

        fdef.filter.categoryBits = SuperMario.ITEM_BIT;
        fdef.filter.maskBits = SuperMario.BRICK_BIT|
        SuperMario.ENEMY_BIT|
        SuperMario.GROUND_BIT|
        SuperMario.OBJECT_BIT|
        SuperMario.MARIO_BIT|
        SuperMario.COIN_BIT;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth()/2,body.getPosition().y - getHeight()/2);
        velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);


    }

    @Override
    public void use(Mario mario) {
        destroy();
        mario.grow();
    }
}
