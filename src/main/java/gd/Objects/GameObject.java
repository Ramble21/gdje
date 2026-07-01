package gd.Objects;

import java.awt.*;
import java.util.Objects;

public abstract class GameObject {
    public double x;
    public double y;
    public boolean isDead = false;

    public GameObject(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }

    public abstract void draw(Graphics g, double cameraX, double cameraY);
    public abstract void drawHitbox(Graphics g, double cameraX, double cameraY);
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
