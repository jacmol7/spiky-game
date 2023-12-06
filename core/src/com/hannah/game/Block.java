package com.hannah.game;

import com.badlogic.gdx.graphics.Color;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.World;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Block extends Entity {

    public Block(ShapeDrawer shapeDrawer, World<Entity> world, float x, float y, float width, float height) {
        super(shapeDrawer, world);

        this.x = x;
        this.y = y;

        this.bboxX = 0;
        this.bboxY = 0;

        this.bboxWidth = width;
        this.bboxHeight = height;


        item = new Item<Entity>(this);
        world.add(item, x + bboxX, y + bboxY, bboxWidth, bboxHeight);
    }

    @Override
    public void act(float delta) {

    }

    @Override
    public void draw() {
        shapeDrawer.rectangle(x, y,  bboxWidth, bboxHeight, Color.LIGHT_GRAY);
    }
}
