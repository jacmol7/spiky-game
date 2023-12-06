package com.hannah.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.*;
import lombok.Setter;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Arm extends Entity {

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    };

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

        boolean maxExtended = extend(delta);

        if (!maxExtended) {
            Response.Result result = world.check(item, x + bboxWidth, y + bboxHeight, ARM_COLLISION_FILTER);
            if (!result.projectedCollisions.isEmpty()) {
                Collision collision = result.projectedCollisions.get(0);
                if (collision.normal.equals(getPushDirection())) player.push(new Vector2(collision.normal.x * 50, collision.normal.y * 50));
            }
        }
        world.move(item, x + bboxWidth - 10, y + bboxHeight - 10, ARM_COLLISION_FILTER);
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
                if (bboxHeight < -10) {
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

    private boolean extend(float delta) {
        switch (direction) {
            case UP:
                bboxHeight += 750 * delta;
                if (bboxHeight > 50) {
                    bboxHeight = 50;
                    return true;
                }
                break;
            case DOWN:
                bboxHeight -= 750 * delta;
                if (bboxHeight < -50) {
                    bboxHeight = -50;
                    return true;
                }
                break;
            case LEFT:
                bboxWidth -= 750 * delta;
                if (bboxWidth < -50) {
                    bboxWidth = -50;
                    return true;
                }
                break;
            case RIGHT:
                bboxWidth += 750 * delta;
                if (bboxWidth > 50) {
                    bboxWidth = 50;
                    return true;
                }
                break;
        }
        return false;
    }

    @Override
    public void draw() {
        if (!visible) return;
        shapeDrawer.rectangle(x, y, bboxWidth, bboxHeight, Color.GREEN);
//        drawCollider();
    }

    public static class ArmCollisionFilter implements CollisionFilter {

        @Override
        public Response filter(Item item, Item other) {
            if (other.userData instanceof Player) {
                return null;
            }
            if (other.userData instanceof Block) {
                return Response.touch;
            }
            return null;
        }
    }
}
