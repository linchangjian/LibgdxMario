package com.lcj.supermario;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lcj.supermario.screen.PlayScreen;

public class SuperMario extends Game {
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	public SpriteBatch batch;
	Texture img;
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		super.render();
	}
}
