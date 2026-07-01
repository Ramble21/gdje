package gd.Objects;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Hazard extends GameObject {

    public Hazard(double x, double y) {
        super(x, y);
    }

    public abstract Rectangle2D.Double getHazardHitbox(double cameraX, double cameraY);
}
