package com.lcj.supermario.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.lcj.supermario.SuperMario;
import com.lcj.supermario.screen.PlayScreen;

/**
 * Created by aniu on 15/11/23.
 */
public class Mario extends Sprite {
    public World world;
    public Body b2body;
    public TextureRegion marioStand;

    public Mario(World world, PlayScreen screen){
        super(screen.getAtlas().findRegion("little_mario"));
        this.world = world;
        marioStand = new TextureRegion(getTexture(),0,0,16,16);
        setBounds(0, 0, 16 / SuperMario.PPM, 16/SuperMario.PPM);
        setRegion(marioStand);

        BodyDef bdef = new BodyDef();
        bdef.position.set(32/ SuperMario.PPM, 32/ SuperMario.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7/ SuperMario.PPM);
        fdef.shape = shape;
        b2body.createFixture(fdef);
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2);
    }
}
