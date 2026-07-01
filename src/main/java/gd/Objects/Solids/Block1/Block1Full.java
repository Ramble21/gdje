package gd.Objects.Solids.Block1;

import gd.ObjRotationInfo;
import gd.Objects.Solids.Block;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Block1Full extends Block {

    protected static final Color c1 = new Color(0, 0, 0, 64);
    protected static final Color c2 = new Color(0, 0, 0, 128);
    protected static final Color c3 = new Color(0, 0, 0, 195);
    protected static final Color c4 = new Color(0, 0, 0, 255);
    protected static final double HALF_LEN_OUTLINE = (double) OUTLINE_LEN / 2;

    public Block1Full(double x, double y, ObjRotationInfo objRotationInfo) {
        super(x, y, objRotationInfo);
    }

    @Override
    public void draw(Graphics g, double cameraX, double cameraY) {
        Graphics2D g2 = (Graphics2D) g;

        Rectangle2D.Double solid = getSolidHitbox(cameraX, cameraY);
        Rectangle2D.Double[] squares = getNineSquares(solid);

        g2.setColor(c4);
        g2.fill(squares[0]);
        g2.fill(squares[2]);
        g2.fill(squares[6]);
        g2.fill(squares[8]);

        g2.setColor(c3);
        g2.fill(squares[1]);
        g2.fill(squares[3]);
        g2.fill(squares[5]);
        g2.fill(squares[7]);

        g2.setColor(c2);
        g2.fill(squares[4]);

        g2.setClip(solid);
        g2.setStroke(new BasicStroke(OUTLINE_LEN));
        g2.setColor(Color.WHITE);
        g2.draw(solid);
        g2.setClip(null);
    }

    public static Rectangle2D.Double[] getNineSquares(Rectangle2D.Double solid) {
        int i = 0;
        Rectangle2D.Double[] ret = new Rectangle2D.Double[9];
        double x = solid.x;
        double y = solid.y;
        double sqLen = (double) (LEN - 2 * OUTLINE_LEN) / 3;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                Rectangle2D.Double box = new Rectangle2D.Double(x + r * (sqLen + OUTLINE_LEN), y + c * (sqLen + OUTLINE_LEN ), sqLen, sqLen);
                ret[i++] = box;
            }
        }
        return ret;
    }
}
