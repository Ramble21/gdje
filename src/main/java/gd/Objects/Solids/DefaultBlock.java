package gd.Objects.Solids;

import gd.LevelMechanics;
import gd.Main;
import gd.Objects.Solid;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class DefaultBlock extends Block {

    public DefaultBlock(double x, double y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics g, double cameraX, double cameraY) {
        Graphics2D g2 = (Graphics2D) g;

        Shape solid = getSolidHitbox(cameraX, cameraY);
        Rectangle bounds = solid.getBounds();

        // weird shit in order to get a gradient, I have no idea how this works cus I copied it from ChatGPT
        Color top = new Color(0, 0, 0, 255);
        Color bottom = new Color(0, 0, 0, 96);
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
}
