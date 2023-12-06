package com.hannah.game;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Rect;
import com.dongbat.jbump.World;
import space.earlygrey.shapedrawer.ShapeDrawer;

public abstract class Entity {
    public float x;
    public float y;
    public float bboxX;
    public float bboxY;
    public float bboxWidth;
    public float bboxHeight;
    public float rotation;
    public float deltaX;
    public float deltaY;
    public boolean flipX;
    public boolean flipY;
    public float gravityX;
    public float gravityY;
    public Item<Entity> item;

    protected final ShapeDrawer shapeDrawer;
    protected final World<Entity> world;

    public Entity(ShapeDrawer shapeDrawer, World<Entity> world) {
        this.shapeDrawer = shapeDrawer;
        this.world = world;
    }

    public abstract void act(float delta);

    public abstract void draw();

    public void drawCollider() {
        Rect collRect = world.getRect(item);
        shapeDrawer.rectangle(new Rectangle(collRect.x, collRect.y, collRect.w, collRect.h), Color.CYAN);
    }
}
