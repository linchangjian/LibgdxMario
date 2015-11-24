package com.lcj.supermario.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lcj.supermario.SuperMario;

/**
 * Created by aniu on 15/11/23.
 */
public class Hud implements Disposable{
    public Stage stage;
    private Viewport viewport;
    private Integer worldTime = 300;
    private float timeCount;
    private static Integer score = 0;

    protected Label countdownLabel;
    protected static Label scoreLabel;
    protected Label timeLabel;
    protected Label levelLabel;
    protected Label worldLabel;
    protected Label marioLabel;

    public Hud(SpriteBatch sb){

        viewport = new FitViewport(SuperMario.V_WIDTH, SuperMario.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%03d", worldTime), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%03d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        marioLabel = new Label("MARIO", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        table.add(marioLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);

        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();
        stage.addActor(table);


    }
    public void update(float dt){
        timeCount += dt;
        if(timeCount >= 1){
            worldTime--;
            countdownLabel.setText(String.format("%03d", worldTime));
            timeCount = 0;
        }
    }
    public static void addScore(int value){
        score+=value;
        scoreLabel.setText(String.format("%06d", score));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
