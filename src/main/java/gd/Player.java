package gd;

import gd.Objects.GameObject;
import gd.Objects.Solid;
import gd.Physics.Physics;

import java.awt.*;

public class Player extends GameObject {

    protected double velocityY = 0;
    public int previousY;
    private boolean touchingGround = false;
    private Solid groundBeingTouched = null;
    public Physics physics;

    protected static int FPS = 165;

    public Player(int x, int y, Physics physics) {
        super(x, y);
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

    @Override
    public void draw(Graphics g, int cameraX, int cameraY) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.GREEN);
        g2.fill(physics.getP1Area(x, y, cameraX, cameraY));
        g2.setColor(Color.CYAN);
        g2.fill(physics.getP2Area(x, y, cameraX, cameraY));
        g2.setColor(Color.BLACK);
        g2.fill(physics.getBlackArea(x, y, cameraX, cameraY));
    }

    @Override
    public void update() {
        physics.apply(this);
    }
}
