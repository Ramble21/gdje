package gd;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class Main extends JPanel {

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Geometry Dash Java Edition");
        LevelMechanics panel = new LevelMechanics(1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(LevelMechanics.SCREEN_WIDTH, LevelMechanics.SCREEN_HEIGHT);
        frame.add(panel);
        frame.setVisible(true);
    }
}
