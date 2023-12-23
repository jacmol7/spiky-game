package com.hannah.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.*;
import lombok.Setter;
import space.earlygrey.shapedrawer.ShapeDrawer;

import javax.swing.text.ViewFactory;

public class Arm extends Entity {

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    };

    private static final float MAX_ARM_LENGTH = 50;

    private static final CollisionFilter ARM_COLLISION_FILTER = new ArmCollisionFilter();

    private final Player player;
    private final Direction direction;

    @Setter
    private boolean active;

    private int xOffset;
    private int yOffset;

    private boolean visible = false;

    public Arm(ShapeDrawer shapeDrawer, World<Entity> world, Player player, Direction direction) {
        super(shapeDrawer, world);
        this.player = player;
        this.direction = direction;

        bboxHeight = 10;
        bboxWidth = 10;

        active = false;

        switch (direction) {
            case UP:
                xOffset = 10;
                yOffset = 30;
                break;
            case DOWN:
                xOffset = 10;
                yOffset = 0;
                break;
            case LEFT:
                xOffset = 0;
                yOffset = 10;
                break;
            case RIGHT:
                xOffset = 30;
                yOffset = 10;
                break;
        }

        item = new Item<Entity>(this);
        world.add(item, player.x + xOffset, player.y + yOffset, bboxWidth, bboxHeight);
    }

    @Override
    public void act(float delta) {
        x = player.x + xOffset;
        y = player.y + yOffset;


        if (!active) {
            visible = !resetSize(delta);
            world.move(item, x + bboxWidth - 10, y + bboxHeight - 10, ARM_COLLISION_FILTER);
            return;
        }
        visible = true;

        Vector2 distanceToExtend = getDistanceToExtend(delta);

        if (!distanceToExtend.isZero()) {
            Response.Result result = world.check(item, x + bboxWidth + distanceToExtend.x, y + bboxHeight + distanceToExtend.y, ARM_COLLISION_FILTER);
            if (!result.projectedCollisions.isEmpty()) {
                Collision collision = result.projectedCollisions.get(0);
                if (collision.normal.equals(getPushDirection())) player.push(new Vector2(collision.normal.x * 50, collision.normal.y * 50));
            }
        }

        float goalX = x + bboxWidth + distanceToExtend.x - 10;
        float goalY = y + bboxHeight + distanceToExtend.y - 10;

        world.move(item, goalX, goalY, ARM_COLLISION_FILTER);
    }

    public void updateAnimation() {
        if (!active) return;
        Rect armRect = world.getRect(item);
        Rect playerRect = world.getRect(player.item);

        float distFloat =  new Vector2(armRect.x, armRect.y).dst(playerRect.x + xOffset - 10, playerRect.y + yOffset - 10);

        switch (direction) {
            case UP:
                bboxHeight = distFloat;
                break;
            case DOWN:
                bboxHeight = -distFloat;
                break;
            case RIGHT:
                bboxWidth = distFloat;
                break;
            case LEFT:
                bboxWidth = -distFloat;
                break;
        }
    }

    private IntPoint getPushDirection() {
        switch (direction) {
            case UP:
                return new IntPoint(0, -1);
            case DOWN:
                return new IntPoint(0, 1);
            case LEFT:
                return new IntPoint(1, 0);
            default:
                return new IntPoint(-1, 0);
        }
    }

    private boolean resetSize(float delta) {
        switch (direction) {
            case UP:
                bboxHeight -= 500 * delta;
                if (bboxHeight - 500 * delta < -10) {
                    bboxHeight = -10;
                    return true;
                }
                break;
            case DOWN:
                bboxHeight += 500 * delta;
                if (bboxHeight > 10) {
                    bboxHeight = 10;
                    return true;
                }
                break;
            case LEFT:
                bboxWidth += 500 * delta;
                if (bboxWidth > 10) {
                    bboxWidth = 10;
                    return true;
                }
                break;
            case RIGHT:
                bboxWidth -= 500 * delta;
                if (bboxWidth < -10) {
                    bboxWidth = -10;
                    return true;
                }
                break;
        }
        return false;
    }

    private void extendAnimation(Vector2 distance) {
        bboxHeight += distance.y;
        bboxWidth += distance.x;
    }

    private Vector2 getDistanceToExtend(float delta) {
        switch (direction) {
            case UP:
                if (bboxHeight + 750 * delta > MAX_ARM_LENGTH) {
                    return new Vector2(0, MAX_ARM_LENGTH - bboxHeight);
                }
                return new Vector2(0, 750 * delta);
            case DOWN:
                if (bboxHeight - 750 * delta < -MAX_ARM_LENGTH) {
                    return new Vector2(0, -MAX_ARM_LENGTH - bboxHeight);
                }
                return new Vector2(0, -750 * delta);
            case LEFT:
                if (bboxWidth - 750 * delta < -MAX_ARM_LENGTH) {
                    return new Vector2(-MAX_ARM_LENGTH - bboxWidth, 0);
                }
                return new Vector2(-750 * delta, 0);
            case RIGHT:
                if (bboxWidth + 750 * delta > MAX_ARM_LENGTH) {
                    return new Vector2(MAX_ARM_LENGTH - bboxWidth, 0);
                }
                return new Vector2(750 * delta, 0);
        }
        return new Vector2(0, 0);
    }

    @Override
    public void draw() {
        if (!visible) return;
        shapeDrawer.rectangle(x, y, bboxWidth, bboxHeight, Color.GREEN);
        drawCollider();
    }

    public static class ArmCollisionFilter implements CollisionFilter {

        @Override
        public Response filter(Item item, Item other) {
            if (other.userData instanceof Player) {
                return null;
            }
            if (other.userData instanceof Block) {
                return Response.slide;
            }
            return null;
        }
    }
}
