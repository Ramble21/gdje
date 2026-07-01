package gd.Objects;

import gd.LevelMechanics;
import gd.ObjRotationInfo;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Objects;

import static gd.LevelMechanics.GRID_SIZE;

public abstract class GameObject {
    public double x;
    public double y;
    public ObjRotationInfo objRotationInfo;
    public boolean isDead = false;

    public GameObject(double x, double y, ObjRotationInfo objRotationInfo) {
        this.x = x;
        this.y = y;
        this.objRotationInfo = objRotationInfo;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public final void drawFinal(Graphics g, double cameraX, double cameraY) {
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform originalTransform = g2.getTransform();

        double screenX = x + cameraX;
        double screenY = y + cameraY;
        double centerX = screenX + GRID_SIZE / 2.0;
        double centerY = screenY + GRID_SIZE / 2.0;

        g2.translate(centerX, centerY);
        g2.rotate(Math.toRadians(objRotationInfo.degsRotated()));
        g2.scale(objRotationInfo.flippedHoriz() ? -1 : 1, objRotationInfo.flippedVert() ? -1 : 1);
        g2.translate(-centerX, -centerY);

        draw(g2, cameraX, cameraY);

        g2.setTransform(originalTransform);
    }

    public final void drawHitboxFinal(Graphics g, double cameraX, double cameraY) {
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform originalTransform = g2.getTransform();

        double screenX = x + cameraX;
        double screenY = y + cameraY;
        double centerX = screenX + GRID_SIZE / 2.0;
        double centerY = screenY + GRID_SIZE / 2.0;

        g2.translate(centerX, centerY);
        g2.rotate(Math.toRadians(objRotationInfo.degsRotated()));
        g2.scale(objRotationInfo.flippedHoriz() ? -1 : 1, objRotationInfo.flippedVert() ? -1 : 1);
        g2.translate(-centerX, -centerY);

        drawHitbox(g2, cameraX, cameraY);

        g2.setTransform(originalTransform);
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
