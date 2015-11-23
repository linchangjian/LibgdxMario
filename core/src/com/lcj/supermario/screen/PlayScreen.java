package com.lcj.supermario.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lcj.supermario.SuperMario;
import com.lcj.supermario.scenes.Hud;

import java.awt.Polygon;

/**
 * Created by aniu on 15/11/23.
*/
public class PlayScreen implements Screen {

    private SuperMario game;
    private Texture texture;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;

    private OrthoCachedTiledMapRenderer renderer;
    private TiledMap tiledMap;
    private TmxMapLoader tmxMapLoader;

    private World world;
    private Box2DDebugRenderer b2dr;

    public PlayScreen(SuperMario game) {
        this.game = game;
        texture = new Texture("tileset_gutter.png");
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(SuperMario.V_WIDTH,SuperMario.V_HEIGHT,gameCam);
        hud = new Hud(game.batch);

        tmxMapLoader = new TmxMapLoader();
        tiledMap = tmxMapLoader.load("level2.tmx");
        renderer = new OrthoCachedTiledMapRenderer(tiledMap);

        gameCam.position.set(gamePort.getScreenWidth() / 2 , gamePort.getScreenHeight() / 2,0);

        world = new World(new Vector2(0,0), true);
        b2dr = new Box2DDebugRenderer();

        BodyDef bded = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        for(MapObject object : tiledMap.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();

            bded.type = BodyDef.BodyType.StaticBody;
            bded.position.set(rect.getX()+rect.getWidth() / 2 ,rect.getY()+ rect.getHeight());

            body = world.createBody(bded);
            shape.setAsBox(rect.getWidth() / 2 ,rect.getHeight() / 2);
            fdef.shape = shape;
            body.createFixture(fdef);
        }
    }

    @Override
    public void show() {
    }
    public void handleInput(float dt){
        if(Gdx.input.isTouched()){
            gameCam.position.x += 100*dt;
        }
    }

    public void update(float dt){
        handleInput(dt);
        gameCam.update();
        renderer.setView(gameCam);
    }
    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();

        b2dr.render(world,gameCam.combined);
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
