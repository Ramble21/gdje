package gd.Objects.Solids.Block1;

import gd.ObjRotationInfo;
import gd.Objects.Solids.Block;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static gd.Objects.Solids.Block1.Block1Full.*;

public class Block1U extends Block {

    public Block1U(double x, double y, ObjRotationInfo objRotationInfo) {
        super(x, y, objRotationInfo);
    }

    @Override
    public void draw(Graphics g, double cameraX, double cameraY) {
        Graphics2D g2 = (Graphics2D) g;

        Rectangle2D.Double solid = getSolidHitbox(cameraX, cameraY);
        Rectangle2D.Double[] squares = getNineSquares(solid);

        g2.setColor(c4);
        g2.fill(squares[0]);
        g2.fill(squares[1]);
        g2.fill(squares[2]);
        g2.fill(squares[3]);
        g2.fill(squares[6]);
        g2.fill(squares[7]);
        g2.fill(squares[8]);

        g2.setColor(c3);
        g2.fill(squares[4]);
        g2.fill(squares[5]);

        g2.setColor(Color.WHITE);
        g2.fill(new Rectangle2D.Double(x + cameraX, y + cameraY, HALF_LEN_OUTLINE, LEN));
        g2.fill(new Rectangle2D.Double(x + cameraX, y + cameraY, LEN, HALF_LEN_OUTLINE));
        g2.fill(new Rectangle2D.Double(x + cameraX + LEN - HALF_LEN_OUTLINE, y + cameraY, HALF_LEN_OUTLINE, LEN));

    }
}
