package gd.Objects;

import java.awt.*;

public abstract class Hazard extends GameObject {

    public Hazard(int x, int y) {
        super(x, y);
    }

    public abstract Rectangle getHazardHitbox(int cameraX, int cameraY);
}
