package com.lcj.supermario.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.lcj.supermario.SuperMario;
import com.lcj.supermario.item.Item;
import com.lcj.supermario.sprites.Enemy;
import com.lcj.supermario.sprites.InteractiveTileObject;
import com.lcj.supermario.sprites.Mario;

/**
 * Created by aniu on 15/11/24.
 */
public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){
            case SuperMario.MARIO_HEAD_BIT | SuperMario.BRICK_BIT:
            case SuperMario.MARIO_HEAD_BIT | SuperMario.COIN_BIT:
                if (fixA.getFilterData().categoryBits == SuperMario.MARIO_HEAD_BIT)
                    ((InteractiveTileObject)fixB.getUserData()).onHeadHit(((Mario)fixA.getUserData()));
                else
                    ((InteractiveTileObject)fixA.getUserData()).onHeadHit(((Mario)fixB.getUserData()));
                break;
            case SuperMario.MARIO_BIT | SuperMario.ENEMY_HEAD_BIT:
                if (fixA.getFilterData().categoryBits == SuperMario.ENEMY_HEAD_BIT)
                    ((Enemy)fixA.getUserData()).hitOnHead();
                else if (fixB.getFilterData().categoryBits == SuperMario.ENEMY_HEAD_BIT)
                    ((Enemy)fixB.getUserData()).hitOnHead();
                break;
            case SuperMario.ENEMY_BIT | SuperMario.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == SuperMario.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true,false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true,false);
                break;
            case SuperMario.MARIO_BIT | SuperMario.ENEMY_BIT:
                Gdx.app.log("mario died","died");
                break;
            case SuperMario.ITEM_BIT | SuperMario.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == SuperMario.ITEM_BIT)
                    ((Item)fixA.getUserData()).reverseVelocity(true,false);
                else
                    ((Item)fixB.getUserData()).reverseVelocity(true,false);
                break;
            case SuperMario.ITEM_BIT | SuperMario.MARIO_BIT:
                if (fixA.getFilterData().categoryBits == SuperMario.ITEM_BIT)
                    ((Item)fixA.getUserData()).use(((Mario)fixB.getUserData()));
                else
                    ((Item)fixB.getUserData()).use(((Mario)fixA.getUserData()));
                break;

        }

    }

    @Override
    public void endContact(Contact contact) {
        Gdx.app.log("end contact","");

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
