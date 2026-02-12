package gd.Objects;

import java.awt.*;
import java.util.Objects;

public abstract class GameObject {
    public int x;
    public int y;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }

    public abstract void draw(Graphics g, int cameraX, int cameraY);
    public abstract void update();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        GameObject other = (GameObject) obj;
        return this.x == other.x && this.y == other.y;
    }
    @Override
    public int hashCode() {
        return Objects.hash(getClass().hashCode(), x, y);
    }
}
