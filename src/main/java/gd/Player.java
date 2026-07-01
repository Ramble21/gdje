package gd;

import gd.Objects.GameObject;
import gd.Objects.Solid;
import gd.Physics.Physics;

import java.awt.*;

public class Player extends GameObject {

    protected double velocityY = 0;
    public double previousY;
    private boolean touchingGround = false;
    private Solid groundBeingTouched = null;
    public Physics physics;

    public Player(double x, double y, Physics physics) {
        super(x, y, new ObjRotationInfo(false, false, 0));
        this.previousY = y;
        this.physics = physics;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public void touchGround(Solid solid) {
        touchingGround = true;
        groundBeingTouched = solid;
    }
    public void untouchGround() {
        touchingGround = false;
        groundBeingTouched = null;
    }
    public boolean isTouchingGround() {
        return touchingGround;
    }
    public boolean isTouchingGround(Solid solid) {
        return touchingGround && groundBeingTouched.equals(solid);
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void jump() {
        physics.jump(this);
        untouchGround();
    }

    public void drawHitbox(Graphics g, double cameraX, double cameraY) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Main.HAZARD_COLOR);
        g2.fill(Main.toOutline(physics.getHazardHitbox(x, y, cameraX, cameraY), LevelMechanics.SHOD_OUTLINE));
        g2.setColor(Main.SOLID_COLOR);
        g2.fill(Main.toOutline(physics.getSolidHitbox(x, y, cameraX, cameraY), LevelMechanics.SHOD_OUTLINE));
    }

    @Override
    public void draw(Graphics g, double cameraX, double cameraY) {
        physics.draw(g, x, y, cameraX, cameraY);
    }

    @Override
    public void update() {
        physics.apply(this);
    }
}
