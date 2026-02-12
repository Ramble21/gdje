package gd.Objects;

import java.awt.*;

public abstract class Solid extends GameObject {

    public Solid(int x, int y) {
        super(x, y);
    }

    public abstract Rectangle getSolidHitbox(int cameraX, int cameraY);
    public abstract int getHitboxHeight();
    public abstract int getHitboxLength();
}
