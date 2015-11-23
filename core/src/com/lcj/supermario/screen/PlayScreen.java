package com.lcj.supermario.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lcj.supermario.SuperMario;
import com.lcj.supermario.scenes.Hud;

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
