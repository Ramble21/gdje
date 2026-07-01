package gd.Objects.Solids;

import gd.LevelMechanics;
import gd.ObjRotationInfo;
import gd.Objects.Solid;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class Ground extends Solid {

    protected static int OUTLINE_LEN = 3;
    protected boolean isCeiling;
    protected LevelMechanics levMec;

    public Ground(double y, LevelMechanics levMec, boolean isCeiling) {
        super(0, y, new ObjRotationInfo(false, isCeiling, 0));
        this.isCeiling = isCeiling;
        this.levMec = levMec;
    }

    private Area getOutline(double cameraX, double cameraY) {
        Rectangle2D.Double outer = getSolidHitbox(cameraX, cameraY);
        Rectangle2D.Double inner = new Rectangle2D.Double(x, y + OUTLINE_LEN, LevelMechanics.SCREEN_WIDTH, LevelMechanics.SCREEN_HEIGHT - y - OUTLINE_LEN);
        Area cube = new Area(outer);
        cube.subtract(new Area(inner));
        return cube;
    }

    @Override
    public Rectangle2D.Double getSolidHitbox(double cameraX, double cameraY) {
        return new Rectangle2D.Double(0, y + cameraY, LevelMechanics.SCREEN_WIDTH, LevelMechanics.SCREEN_HEIGHT - y - cameraY);
    }

    @Override
    public double getHitboxHeight() {
        return isCeiling ? y : LevelMechanics.SCREEN_HEIGHT - y;
    }

    @Override
    public double getHitboxLength() {
        return LevelMechanics.SCREEN_WIDTH;
    }

    @Override
    public void draw(Graphics g, double cameraX, double cameraY) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(levMec.level.bgColor.darker());
        g2.fill(getSolidHitbox(cameraX, cameraY));
        g2.setColor(Color.WHITE);
        g2.fill(getOutline(cameraX, cameraY));
    }

    @Override
    public void drawHitbox(Graphics g, double cameraX, double cameraY) {
        draw(g, cameraX, cameraY);
    }

    @Override
    public void update() {
        return;
    }
}
