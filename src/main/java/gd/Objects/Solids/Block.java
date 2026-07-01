package gd.Objects.Solids;

import gd.LevelMechanics;
import gd.Main;
import gd.ObjRotationInfo;
import gd.Objects.Solid;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static gd.LevelMechanics.GRID_SIZE;

public abstract class Block extends Solid {

    protected static int LEN = GRID_SIZE;
    protected static final int OUTLINE_LEN = 6;

    public Block(double x, double y, ObjRotationInfo objRotationInfo) {
        super(x, y, objRotationInfo);
    }

    @Override
    public Rectangle2D.Double getSolidHitbox(double cameraX, double cameraY) {
        return new Rectangle2D.Double(x + cameraX, y + cameraY, LEN, LEN);
    }

    @Override
    public void drawHitbox(Graphics g, double cameraX, double cameraY) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Main.SOLID_COLOR);
        g2.fill(Main.toOutline(getSolidHitbox(cameraX, cameraY), LevelMechanics.SHOD_OUTLINE));
    }

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
