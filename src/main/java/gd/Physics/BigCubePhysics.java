package gd.Physics;

import gd.Player;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class BigCubePhysics implements Physics {
    private static final double GRAVITY = 1.2; // pixels per frame squared
    private static double JUMP_VELOCITY = -16; // // negative value = up (regular), positive value = reverse gravity

    protected static int OUTLINE_LEN = 1;
    protected static int HAZARD_LEN = 50;
    protected static int OUTER_SQ_LEN = 30;
    protected static int SOLID_LEN = 15;

    @Override
    public void apply(Player player) {
        if (!player.isTouchingGround()) {
            double v = player.getVelocityY();
            v += GRAVITY;
            player.setVelocityY(v);
            player.setY((int)(player.getY() + v));
        }
    }

    public Rectangle getSolidHitbox(int x, int y, int cameraX, int cameraY) {
        int distFromOuter = (HAZARD_LEN - SOLID_LEN) / 2;
        return new Rectangle(x + distFromOuter, y + distFromOuter, SOLID_LEN, SOLID_LEN);
    }

    public Rectangle getHazardHitbox(int x, int y, int cameraX, int cameraY) {
        return new Rectangle(x, y, HAZARD_LEN, HAZARD_LEN);
    }

    public Area getP2Area(int x, int y, int cameraX, int cameraY) {
        return new Area(getSolidHitbox(x, y, cameraX, cameraY));
    }

    public Area getP1Area(int x, int y, int cameraX, int cameraY) {
        Rectangle2D outer = getHazardHitbox(x, y, cameraX, cameraY);
        int distFromOuter = (HAZARD_LEN - OUTER_SQ_LEN) / 2;
        Rectangle2D inner = new Rectangle(x + distFromOuter, y + distFromOuter, OUTER_SQ_LEN, OUTER_SQ_LEN);
        Area cube = new Area(outer);
        cube.subtract(new Area(inner));
        return cube;
    }

    public Area getBlackArea(int x, int y, int cameraX, int cameraY) {
        int innerMaskLen = HAZARD_LEN - 2 * OUTLINE_LEN;
        int solDistFromOuter = (HAZARD_LEN - OUTER_SQ_LEN) / 2;
        int hazDistFromOuter = (HAZARD_LEN - SOLID_LEN) / 2;


        Rectangle2D outer1 = getHazardHitbox(x, y, cameraX, cameraY);
        Rectangle2D inner1 = new Rectangle(x + OUTLINE_LEN, y + OUTLINE_LEN, innerMaskLen, innerMaskLen);
        Area area = new Area(outer1);
        area.subtract(new Area(inner1));

        Rectangle2D outer2 = new Rectangle(x + solDistFromOuter - OUTLINE_LEN, y + solDistFromOuter - OUTLINE_LEN, OUTER_SQ_LEN + 2 * OUTLINE_LEN, OUTER_SQ_LEN + 2 * OUTLINE_LEN);
        Rectangle2D inner2 = new Rectangle(x + solDistFromOuter, y + solDistFromOuter, OUTER_SQ_LEN, OUTER_SQ_LEN);
        Area area1 = new Area(outer2);
        area1.subtract(new Area(inner2));
        area.add(area1);

        Rectangle2D outer3 = getSolidHitbox(x, y, cameraX, cameraY);
        Rectangle2D inner3 = new Rectangle(x + hazDistFromOuter + OUTLINE_LEN, y + hazDistFromOuter + OUTLINE_LEN, SOLID_LEN - 2 * OUTLINE_LEN, SOLID_LEN - 2 * OUTLINE_LEN);
        Area area2 = new Area(outer3);
        area2.subtract(new Area(inner3));
        area.add(area2);
        return area;
    }

    public void flipGravity() {
        JUMP_VELOCITY *= -1;
    }
    public boolean isFlippedGravity() {
        return JUMP_VELOCITY > 0;
    }
    public int getHitboxHeight() {
        return HAZARD_LEN;
    }

    public void jump(Player player) {
        if (player.isTouchingGround()) {
            player.setVelocityY(JUMP_VELOCITY);
        }
    }
}
