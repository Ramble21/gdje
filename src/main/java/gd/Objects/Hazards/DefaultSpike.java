package gd.Objects.Hazards;

import gd.Objects.Hazard;

import java.awt.*;

public class DefaultSpike extends Hazard {

    protected static int LEN = 50;
    protected static int HITBOX_LEN = 10;
    protected static int HITBOX_HEIGHT = 25;
    protected static int TOP_HEIGHT = 10; // the height of the little top peak of the spike that doesnt have a hitbox
    protected static int OUTLINE_LEN = 6;

    public DefaultSpike(int x, int y) {
        super(x, y);
    }

    private Polygon getTriangle(int cameraX, int cameraY) {
        int[] xPoints = {
                x + cameraX,
                x + LEN / 2 + cameraX,
                x + LEN + cameraX
        };
        int[] yPoints = {
                y + LEN + cameraY,
                y + cameraY,
                y + LEN + cameraY
        };
        return new Polygon(xPoints, yPoints, xPoints.length);
    }

    @Override
    public Rectangle getHazardHitbox(int cameraX, int cameraY) {
        int topLeftX = x + cameraX + ((LEN - HITBOX_LEN) / 2);
        int topLeftY = y + cameraY + TOP_HEIGHT;
        return new Rectangle(topLeftX, topLeftY, HITBOX_LEN, HITBOX_HEIGHT);
    }

    @Override
    public void draw(Graphics g, int cameraX, int cameraY) {
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
