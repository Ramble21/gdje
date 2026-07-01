package gd.Objects;

import gd.ObjRotationInfo;

import java.awt.geom.Rectangle2D;

public abstract class Hazard extends GameObject {

    public Hazard(double x, double y, ObjRotationInfo objRotationInfo) {
        super(x, y, objRotationInfo);
    }

    public final Rectangle2D.Double getHazardHitboxFinal(double cameraX, double cameraY) {
        return getHazardHitbox(cameraX, cameraY);
    }

    public abstract Rectangle2D.Double getHazardHitbox(double cameraX, double cameraY);
}
