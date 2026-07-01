package gd.Physics;
import gd.Player;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public abstract class Physics {
    public abstract void apply(Player player);
    public abstract void jump(Player player);
    public abstract void flipGravity();
    public abstract boolean isFlippedGravity();
    public abstract int getHitboxHeight();

    public abstract Rectangle2D.Double getHazardHitbox(double x, double y, double cameraX, double cameraY);
    public abstract Rectangle2D.Double getSolidHitbox(double x, double y, double cameraX, double cameraY);
    public abstract Area getP1Area(double x, double y, double cameraX, double cameraY);
    public abstract Area getP2Area(double x, double y, double cameraX, double cameraY);
    public abstract Area getBlackArea(double x, double y, double cameraX, double cameraY);
    public abstract void draw(Graphics g, double x, double y, double cameraX, double cameraY);


    public static Physics getPhysics(String gamemode) {
        return switch (gamemode) {
            case "BigCube" -> new BigCubePhysics();
            default -> null;
        };
    }
}
