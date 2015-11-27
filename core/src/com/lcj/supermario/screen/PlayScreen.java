package com.lcj.supermario.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lcj.supermario.SuperMario;
import com.lcj.supermario.item.Item;
import com.lcj.supermario.item.ItemDef;
import com.lcj.supermario.item.Mushroom;
import com.lcj.supermario.scenes.Hud;
import com.lcj.supermario.sprites.Enemy;
import com.lcj.supermario.sprites.Goomba;
import com.lcj.supermario.sprites.Mario;
import com.lcj.supermario.tools.B2WorldCreator;
import com.lcj.supermario.tools.WorldContactListener;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

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
    private Goomba goomba;
    private TextureAtlas atlas;

    private B2WorldCreator creator;

    private Array<Item> items;
    //private PriorityQueue<ItemDef> itemToSpwan;
    private LinkedBlockingQueue<ItemDef> itemsToSpwan;
    public PlayScreen(SuperMario game) {
        atlas = new TextureAtlas("Mario_and_Enemies.pack");
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(SuperMario.V_WIDTH/ SuperMario.PPM,SuperMario.V_HEIGHT/SuperMario.PPM,gameCam);
        hud = new Hud(game.batch);
        tmxMapLoader = new TmxMapLoader();
        tiledMap = tmxMapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / SuperMario.PPM);
        gameCam.position.set(gamePort.getScreenWidth()/2,gamePort.getScreenHeight()/2,0);
        gameCam.position.y = 1;
        world = new World(new Vector2(0,-10), true);

        b2dr = new Box2DDebugRenderer();

        palyer = new Mario(this);
        //goomba= new Goomba(this,.64f,.32f);
        world.setContactListener(new WorldContactListener());

        creator = new B2WorldCreator(this);

        items = new Array<Item>();
        itemsToSpwan = new LinkedBlockingQueue<ItemDef>();

    }
    public void spawnItem(ItemDef idef){
        itemsToSpwan.add(idef);
    }

    public void handleSpawningItems(){
        if(!itemsToSpwan.isEmpty()){
            ItemDef idef = itemsToSpwan.poll();
            if(idef.type == Mushroom.class){
                items.add(new Mushroom(this, idef.position.x,idef.position.y));
            }
        }

    }
    public TextureAtlas getAtlas(){
        return this.atlas;
    }
    public World getWorld(){
        return world;
    }

    public TiledMap getTiledMap(){
        return tiledMap;
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

    public void update(float dt) {
        handleInput(dt);
        handleSpawningItems();
        world.step(1 / 60f, 6, 2);

        palyer.update(dt);
        gameCam.position.x = palyer.b2body.getPosition().x;
        if(palyer.b2body.getPosition().x < gamePort.getWorldWidth() / 2)
            gameCam.position.x = 2;

        Gdx.app.log("mario x : ",palyer.getX()+"");

        for (Enemy goomba : creator.getGoombas()){
            goomba.update(dt);
            if(goomba.getX() < palyer.getX() + 224 / SuperMario.PPM){
                goomba.b2body.setActive(true);
            }
        }
        for (Item item : items){
            item.update(dt);
        }

        hud.update(dt);
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
        for (Enemy goomba : creator.getGoombas())
            goomba.draw(game.batch);
        for (Item item : items){
            item.draw(game.batch);
        }
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
