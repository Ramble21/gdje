package gd;

import gd.GDPorting.GDLevelPorter;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.IOException;


public class Main extends JPanel {

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Geometry Dash Java Edition");
        LevelMechanics panel = new LevelMechanics(1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(LevelMechanics.SCREEN_WIDTH, LevelMechanics.SCREEN_HEIGHT);
        frame.add(panel);
        frame.setVisible(true);
//        try {
//            GDLevelPorter.createTxt("CCLocalLevels.dat", "Stereo Madness 2");
//        }
//        catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    public static Area toOutline(Rectangle2D.Double rect, int thickness) {
        Area outer = new Area(rect);
        Area inner = new Area(new Rectangle2D.Double(
                rect.x + thickness,
                rect.y + thickness,
                rect.width - thickness * 2,
                rect.height - thickness * 2
        ));
        outer.subtract(inner);
        return outer;
    }

    public static Color SOLID_COLOR = new Color(0, 0, 255);
    public static Color HAZARD_COLOR = new Color(255, 0, 0);
}
