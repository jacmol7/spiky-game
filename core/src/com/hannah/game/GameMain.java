package com.hannah.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.World;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ArrayList;
import java.util.List;

public class GameMain extends Game {
	SpriteBatch batch;

	Texture texture;

	ShapeDrawer drawer;

	Player player;

	int moveSpeed = 100;

	InputHandler inputHandler;

	World<Entity> world;

	List<Block> blocks;

	@Override
	public void create () {
		// Batch
		batch = new SpriteBatch();

		// ShapeDrawer setup
		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.drawPixel(0, 0);
		texture = new Texture(pixmap);
		pixmap.dispose();
		TextureRegion region = new TextureRegion(texture, 0, 0, 1, 1);
		drawer = new ShapeDrawer(batch, region);

		// Input setup
		inputHandler = new InputHandler();
		Gdx.input.setInputProcessor(inputHandler);

		// Physics setup
		world = new World<>();

		// Player setup
//		playerShape = new Rectangle(0, 0, 50, 50);
		player = new Player(drawer, world, inputHandler);

		blocks = new ArrayList<>();


		// fallable thingy...
//		blocks.add(new Block(drawer, world, 185, 300, 10, 60));
//		blocks.add(new Block(drawer, world, 350, 200, 10, 100));
//		blocks.add(new Block(drawer, world, 200, 200, 50, 10));
//		blocks.add(new Block(drawer, world, 300, 200, 50, 10));
//		blocks.add(new Block(drawer, world, 200, 250, 50, 10));


		// SQUARE
		blocks.add(new Block(drawer, world, 10, 10, 250, 10)); // bottom
		blocks.add(new Block(drawer, world, 10, 250, 250, 10)); // top
		blocks.add(new Block(drawer, world, 10, 10, 10, 250)); // left
		blocks.add(new Block(drawer, world, 250, 10, 10, 250)); // right
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);


//		Vector2 moveDirection = inputHandler.getMoveDirection();
//		playerShape.setX(playerShape.getX() + ((moveDirection.x * moveSpeed) * Gdx.graphics.getDeltaTime()));
//		playerShape.setY(playerShape.getY() + ((moveDirection.y * moveSpeed) * Gdx.graphics.getDeltaTime()));

		batch.begin();

		float delta = Gdx.graphics.getDeltaTime();
		player.act(delta);
		player.draw();

		for (Entity b : blocks) {
			b.draw();
		}

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		texture.dispose();
	}
}
