package gd.Objects.Solids;

import gd.ObjRotationInfo;
import gd.Objects.Solid;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static gd.LevelMechanics.GRID_SIZE;

public abstract class Slab extends Solid {

    protected static int LEN = GRID_SIZE;

    public Slab(double x, double y, ObjRotationInfo objRotationInfo) {
        super(x, y, objRotationInfo);
    }

    @Override
    public Rectangle2D.Double getSolidHitbox(double cameraX, double cameraY) {
        return new Rectangle2D.Double(x + cameraX, y + cameraY, LEN, (double) LEN / 2);
    }

    @Override
    public abstract void drawHitbox(Graphics g, double cameraX, double cameraY);

    @Override
    public double getHitboxHeight() {
        return (double) LEN / 2;
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
