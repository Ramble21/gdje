package gd.Objects.Hazards;

import gd.LevelMechanics;
import gd.Main;
import gd.Objects.Hazard;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class HalfSpike extends Hazard {

    protected static int LEN = 50;
    protected static int HITBOX_LEN = 12;
    protected static int HITBOX_HEIGHT = 12;
    protected static int TOP_HEIGHT = 39;
    protected static int OUTLINE_LEN = 6;

    public HalfSpike(double x, double y) {
        super(x, y);
    }

    private Polygon getTriangle(double cameraX, double cameraY) {
        int[] xPoints = {
                (int) (x + cameraX),
                (int) (x + (double) LEN / 2 + cameraX),
                (int) (x + LEN + cameraX)
        };
        int[] yPoints = {
                (int) (y + (double) 3 * LEN / 4 + cameraY),
                (int) (y + (double) LEN / 4 + cameraY),
                (int) (y + (double) 3 * LEN / 4 + cameraY)
        };
        return new Polygon(xPoints, yPoints, xPoints.length);
    }

    @Override
    public Rectangle2D.Double getHazardHitbox(double cameraX, double cameraY) {
        double topLeftX = x + cameraX + ((double) (LEN - HITBOX_LEN) / 2);
        double topLeftY = y + cameraY + (double) 3 * HITBOX_HEIGHT / 2;
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
