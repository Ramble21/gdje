package gd.Objects.Solids;

import gd.LevelMechanics;
import gd.Main;
import gd.Objects.Solid;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class DefaultBlock extends Solid {

    protected static int LEN = 50;
    protected static int OUTLINE_LEN = 6;

    public DefaultBlock(double x, double y) {
        super(x, y);
    }

    @Override
    public Rectangle2D.Double getSolidHitbox(double cameraX, double cameraY) {
        return new Rectangle2D.Double(x + cameraX, y + cameraY, LEN, LEN);
    }

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
    public void draw(Graphics g, double cameraX, double cameraY) {
        Graphics2D g2 = (Graphics2D) g;

        Shape solid = getSolidHitbox(cameraX, cameraY);
        Rectangle bounds = solid.getBounds();

        // weird shit in order to get a gradient, I have no idea how this works cus I copied it from ChatGPT
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
