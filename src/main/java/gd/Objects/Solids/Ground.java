package gd.Objects.Solids;

import gd.LevelMechanics;
import gd.Objects.Solid;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class Ground extends Solid {

    protected static int OUTLINE_LEN = 3;
    protected boolean isCeiling;
    protected LevelMechanics levMec;

    public Ground(int y, LevelMechanics levMec, boolean isCeiling) {
        super(0, y);
        this.isCeiling = isCeiling;
        this.levMec = levMec;
    }

    private Area getOutline(int cameraX, int cameraY) {
        Rectangle2D outer = getSolidHitbox(cameraX, cameraY);
        Rectangle2D inner = new Rectangle(x, y + OUTLINE_LEN, LevelMechanics.SCREEN_WIDTH, LevelMechanics.SCREEN_HEIGHT - y - OUTLINE_LEN);
        Area cube = new Area(outer);
        cube.subtract(new Area(inner));
        return cube;
    }

    @Override
    public Rectangle getSolidHitbox(int cameraX, int cameraY) {
        return new Rectangle(0, y + cameraY, LevelMechanics.SCREEN_WIDTH, LevelMechanics.SCREEN_HEIGHT - y - cameraY);
    }

    @Override
    public int getHitboxHeight() {
        return isCeiling ? y : LevelMechanics.SCREEN_HEIGHT - y;
    }

    @Override
    public int getHitboxLength() {
        return LevelMechanics.SCREEN_WIDTH;
    }

    @Override
    public void draw(Graphics g, int cameraX, int cameraY) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(levMec.level.bgColor.darker());
        g2.fill(getSolidHitbox(cameraX, cameraY));
        g2.setColor(Color.WHITE);
        g2.fill(getOutline(cameraX, cameraY));
    }

    @Override
    public void update() {
        return;
    }
}
