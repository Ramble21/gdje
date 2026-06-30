package gd.Physics;
import gd.Player;

import java.awt.*;
import java.awt.geom.Area;

public abstract class Physics {
    public abstract void apply(Player player);
    public abstract void jump(Player player);
    public abstract void flipGravity();
    public abstract boolean isFlippedGravity();
    public abstract int getHitboxHeight();

    public abstract Rectangle getHazardHitbox(int x, int y, int cameraX, int cameraY);
    public abstract Rectangle getSolidHitbox(int x, int y, int cameraX, int cameraY);
    public abstract Area getP1Area(int x, int y, int cameraX, int cameraY);
    public abstract Area getP2Area(int x, int y, int cameraX, int cameraY);
    public abstract Area getBlackArea(int x, int y, int cameraX, int cameraY);
    public abstract void draw(Graphics g, int x, int y, int cameraX, int cameraY);


    public static Physics getPhysics(String gamemode) {
        return switch (gamemode) {
            case "BigCube" -> new BigCubePhysics();
            default -> null;
        };
    }
}
