package gd.Physics;
import gd.Player;

import java.awt.*;
import java.awt.geom.Area;

public interface Physics {
    void apply(Player player);
    void jump(Player player);
    void flipGravity();
    boolean isFlippedGravity();
    int getHitboxHeight();

    Rectangle getHazardHitbox(int x, int y, int cameraX, int cameraY);
    Rectangle getSolidHitbox(int x, int y, int cameraX, int cameraY);
    Area getP1Area(int x, int y, int cameraX, int cameraY);
    Area getP2Area(int x, int y, int cameraX, int cameraY);
    Area getBlackArea(int x, int y, int cameraX, int cameraY);

    static Physics getPhysics(String gamemode) {
        return switch (gamemode) {
            case "BigCube" -> new BigCubePhysics();
            default -> null;
        };
    }
}
