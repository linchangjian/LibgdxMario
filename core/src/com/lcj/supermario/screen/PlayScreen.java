package com.lcj.supermario.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
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
import com.lcj.supermario.sprites.Mario;
import com.lcj.supermario.tools.B2WorldCreator;

import java.awt.Polygon;

/**
 * Created by aniu on 15/11/23.
*/
public class PlayScreen implements Screen {

    private SuperMario game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;

    private OrthogonalTiledMapRenderer renderer;
    private TiledMap tiledMap;
    private TmxMapLoader tmxMapLoader;

    private World world;
    private Box2DDebugRenderer b2dr;

    private  Mario palyer;
    private TextureAtlas atlas;
    public PlayScreen(SuperMario game) {
        atlas = new TextureAtlas("Mario_and_Enemies.pack");
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(SuperMario.V_WIDTH/ SuperMario.PPM,SuperMario.V_HEIGHT/SuperMario.PPM,gameCam);
        hud = new Hud(game.batch);
        tmxMapLoader = new TmxMapLoader();
        tiledMap = tmxMapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap, 1/SuperMario.PPM);
        gameCam.position.set(gamePort.getScreenWidth()/2,gamePort.getScreenHeight()/2,0);


        world = new World(new Vector2(0,-10), true);

        b2dr = new Box2DDebugRenderer();

        palyer = new Mario(world,this);

        new B2WorldCreator(world,tiledMap);
    }
    public TextureAtlas getAtlas(){
        return this.atlas;
    }

    @Override
    public void show() {
    }
    public void handleInput(float dt){
       if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
           palyer.b2body.applyLinearImpulse(new Vector2(0,4f),palyer.b2body.getWorldCenter(),true);
       }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && (palyer.b2body.getLinearVelocity().x <= 2)){
            palyer.b2body.applyLinearImpulse(new Vector2(0.1f,0f),palyer.b2body.getWorldCenter(),true);

        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && (palyer.b2body.getLinearVelocity().x >= -2)){
            palyer.b2body.applyLinearImpulse(new Vector2(-0.1f,0f),palyer.b2body.getWorldCenter(),true);

        }
    }

    public void update(float dt){
        handleInput(dt);

        world.step(1 / 60f, 6, 2);

        palyer.update(dt);
        gameCam.position.x = palyer.b2body.getPosition().x;
        gameCam.update();
        renderer.setView(gameCam);
    }
    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();

        b2dr.render(world, gameCam.combined);
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        palyer.draw(game.batch);
        game.batch.end();

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
        tiledMap.dispose();
        b2dr.dispose();
        world.dispose();
        renderer.dispose();
        hud.dispose();
    }
}
