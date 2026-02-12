package gd.Objects.Solids;

import gd.Objects.Solid;

import java.awt.*;

public class DefaultBlock extends Solid {

    protected static int LEN = 50;
    protected static int OUTLINE_LEN = 6;

    public DefaultBlock(int x, int y) {
        super(x, y);
    }

    @Override
    public Rectangle getSolidHitbox(int cameraX, int cameraY) {
        return new Rectangle(x + cameraX, y + cameraY, LEN, LEN);
    }

    @Override
    public int getHitboxHeight() {
        return LEN;
    }

    @Override
    public int getHitboxLength() {
        return LEN;
    }

    @Override
    public void draw(Graphics g, int cameraX, int cameraY) {
        Graphics2D g2 = (Graphics2D) g;

        Shape solid = getSolidHitbox(cameraX, cameraY);
        Rectangle bounds = solid.getBounds();

        // weird shit in order to get a gradient, I have no idea how this works cus I copied it from chatgpt
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
