package com.hannah.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.*;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.HashMap;
import java.util.Map;

public class Player extends Entity {

    private static final CollisionFilter PLAYER_COLLISION_FILTER = new PlayerCollisionFilter();

    private final InputHandler inputHandler;
    private boolean onGround;

    Map<Arm.Direction, Arm> arms;


    public Player(ShapeDrawer shapeDrawer, World<Entity> world, InputHandler inputHandler) {
        super(shapeDrawer, world);

        this.inputHandler = inputHandler;

        this.x = 200;
        this.y = 100;

        bboxX = 0;
        bboxY = 0;

        bboxWidth = 30;
        bboxHeight = 30;

        item = new Item<Entity>(this);
        world.add(item, x + bboxX, y + bboxY, bboxWidth, bboxHeight);

        arms = new HashMap<>();
        arms.put(Arm.Direction.UP, new Arm(shapeDrawer, world, this, Arm.Direction.UP));
        arms.put(Arm.Direction.DOWN, new Arm(shapeDrawer, world, this, Arm.Direction.DOWN));
        arms.put(Arm.Direction.LEFT, new Arm(shapeDrawer, world, this, Arm.Direction.LEFT));
        arms.put(Arm.Direction.RIGHT, new Arm(shapeDrawer, world, this, Arm.Direction.RIGHT));
    }

    public void push(Vector2 pushForce) {
        deltaY += pushForce.y;
        deltaX += pushForce.x;
    }

    @Override
    public void act(float delta) {
        Vector2 moveDirection = inputHandler.getMoveDirection();

        arms.get(Arm.Direction.UP).setActive(moveDirection.y > 0);
        arms.get(Arm.Direction.DOWN).setActive(moveDirection.y < 0);
        arms.get(Arm.Direction.LEFT).setActive(moveDirection.x < 0);
        arms.get(Arm.Direction.RIGHT).setActive(moveDirection.x > 0);

        for (Arm arm : arms.values()) {
            arm.act(delta);
        }

        x += delta * deltaX;
        y += delta * deltaY;


        Response.Result result = world.move(item, x + bboxX, y + bboxY, PLAYER_COLLISION_FILTER);

        Collisions collisions = result.projectedCollisions;
        onGround = false;
        for (int i=0; i < collisions.size(); i++) {
            Collision collision = collisions.get(i);
            if (collision.normal.y == 1) {
                onGround = true;
                deltaY = 0;
                break;
            } else if (collision.normal.y == -1) {
                deltaY = 0;
            } else if (collision.normal.x != 0) {
                deltaX = 0;
            }
        }
        if (!onGround) {
            deltaY += delta * -350;
        }
        Rect rect = world.getRect(item);
        if (rect != null) {
            x = rect.x - bboxX;
            y = rect.y - bboxY;
        }

        for (Arm arm : arms.values()) {
            arm.updateAnimation();
            arm.draw();
        }
    }

    @Override
    public void draw() {
        shapeDrawer.rectangle(x, y, bboxWidth, bboxHeight, Color.WHITE);
    }

    public static class PlayerCollisionFilter implements CollisionFilter {

        @Override
        public Response filter(Item item, Item other) {
            if (other.userData instanceof Arm) {
                return null;
            }
            return Response.slide;
        }
    }
}
