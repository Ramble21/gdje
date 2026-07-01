package gd.Objects;

import gd.ObjRotationInfo;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Solid extends GameObject {

    public Solid(double x, double y, ObjRotationInfo objRotationInfo) {
        super(x, y, objRotationInfo);
    }

    public final Rectangle2D.Double getSolidHitboxFinal(double cameraX, double cameraY) {
        return getSolidHitbox(cameraX, cameraY);
    }

    public abstract Rectangle2D.Double getSolidHitbox(double cameraX, double cameraY);

    public abstract double getHitboxHeight();

    public abstract double getHitboxLength();
}
