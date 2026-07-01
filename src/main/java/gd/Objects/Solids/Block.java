package gd.Objects.Solids;

import gd.Objects.Solid;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Block extends Solid {

    protected static int LEN = 50;

    public Block(double x, double y) {
        super(x, y);
    }

    @Override
    public Rectangle2D.Double getSolidHitbox(double cameraX, double cameraY) {
        return new Rectangle2D.Double(x + cameraX, y + cameraY, LEN, LEN);
    }

    @Override
    public abstract void drawHitbox(Graphics g, double cameraX, double cameraY);

    @Override
    public double getHitboxHeight() {
        return LEN;
    }

    @Override
    public double getHitboxLength() {
        return LEN;
    }

    @Override
    public abstract void draw(Graphics g, double cameraX, double cameraY);

    @Override
    public void update() {
        return;
    }
}
