package gd.Objects;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Solid extends GameObject {

    public Solid(double x, double y) {
        super(x, y);
    }

    public abstract Rectangle2D.Double getSolidHitbox(double cameraX, double cameraY);
    public abstract double getHitboxHeight();
    public abstract double getHitboxLength();
}
