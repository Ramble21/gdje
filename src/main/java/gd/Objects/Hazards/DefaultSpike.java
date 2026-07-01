package gd.Objects.Hazards;

import gd.LevelMechanics;
import gd.Main;
import gd.ObjRotationInfo;
import gd.Objects.Hazard;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static gd.LevelMechanics.GRID_SIZE;

public class DefaultSpike extends Hazard {

    protected static int LEN = GRID_SIZE;
    protected static int HITBOX_LEN = 12;
    protected static int HITBOX_HEIGHT = 24;
    protected static int TOP_HEIGHT = 15; // the height of the little top peak of the spike that doesn't have a hitbox
    protected static int OUTLINE_LEN = 6;

    public DefaultSpike(double x, double y, ObjRotationInfo objRotationInfo) {
        super(x, y, objRotationInfo);
    }

    private Polygon getTriangle(double cameraX, double cameraY) {
        int[] xPoints = {
                (int) (x + cameraX),
                (int) (x + (double) LEN / 2 + cameraX),
                (int) (x + LEN + cameraX)
        };
        int[] yPoints = {
                (int) (y + LEN + cameraY),
                (int) (y + cameraY),
                (int) (y + LEN + cameraY)
        };
        return new Polygon(xPoints, yPoints, xPoints.length);
    }

    @Override
    public Rectangle2D.Double getHazardHitbox(double cameraX, double cameraY) {
        double topLeftX = x + cameraX + ((double) (LEN - HITBOX_LEN) / 2);
        double topLeftY = y + cameraY + TOP_HEIGHT;
        return new Rectangle2D.Double(topLeftX, topLeftY, HITBOX_LEN, HITBOX_HEIGHT);
    }

    public void drawHitbox(Graphics g, double cameraX, double cameraY) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Main.HAZARD_COLOR);
        g2.fill(Main.toOutline(getHazardHitbox(cameraX, cameraY), LevelMechanics.SHOD_OUTLINE));
    }

    @Override
    public void draw(Graphics g, double cameraX, double cameraY) {
        Graphics2D g2 = (Graphics2D) g;

        Shape solid = getTriangle(cameraX, cameraY);
        Rectangle bounds = solid.getBounds();

        Color top = new Color(0, 0, 0, 255);
        Color bottom = new Color(0, 0, 0, 128);
        float[] fractions = {0f, 1f};
        Color[] colors = {top, bottom};
        LinearGradientPaint gradient = new LinearGradientPaint(bounds.x, bounds.y, bounds.x, bounds.y + bounds.height, fractions, colors);
        g2.setPaint(gradient);
        g2.fill(solid);

        g2.setClip(solid);
        g2.setStroke(new BasicStroke(OUTLINE_LEN));
        g2.setColor(Color.WHITE);
        g2.draw(solid);
        g2.setClip(null);
    }

    @Override
    public void update() {
        return;
    }

}
