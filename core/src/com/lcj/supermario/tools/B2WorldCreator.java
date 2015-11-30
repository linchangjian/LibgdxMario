package com.lcj.supermario.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.lcj.supermario.SuperMario;
import com.lcj.supermario.screen.PlayScreen;
import com.lcj.supermario.sprites.Brick;
import com.lcj.supermario.sprites.Coin;
import com.lcj.supermario.sprites.Goomba;
import com.lcj.supermario.sprites.Pipe;
import com.lcj.supermario.sprites.Turtle;

/**
 * Created by aniu on 15/11/24.
 */
public class B2WorldCreator {
    private Array<Goomba> goombas;
    private Array<Turtle> turtles;
    public Array<Goomba> getGoombas() {
        return goombas;
    }

    public Array<Turtle> getTurtles() {
        return turtles;
    }

    public B2WorldCreator(PlayScreen screen){
        TiledMap map = screen.getTiledMap();

        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            new Brick(screen,object);
        }
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            new Brick(screen,object);
        }
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            new Coin(screen,object);
        }
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            new Pipe(screen,object);
        }

        goombas = new Array<Goomba>();
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            goombas.add(new Goomba(screen,rect.getX()/ SuperMario.PPM,rect.getY() / SuperMario.PPM));
        }

        turtles = new Array<Turtle>();
        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            turtles.add(new Turtle(screen,rect.getX()/ SuperMario.PPM,rect.getY() / SuperMario.PPM));
        }

    }
}
