package gd;

import gd.Objects.GameObject;
import gd.Objects.Hazard;
import gd.Objects.Solid;
import gd.Objects.Solids.Ground;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class LevelMechanics extends JPanel {

    public final Level level;
    private final Player player;
    private boolean firstFrame = true;
    public boolean p1Pressed = false;
    public int cameraX;
    public int cameraY;

    private final Timer logicTimer;
    private boolean gamePaused;

    public static int SCREEN_WIDTH = 1920;
    public static int SCREEN_HEIGHT = 1080;
    public static int GROUND_Y = 900;
    public static int X_VELOCITY = 9;
    public static int COYOTE_PIXELS = 10;

    private void handleCollisions() {
        for (GameObject obj : level.objects) {
            if (obj instanceof Solid block) {
                if (player.isTouchingGround(block)) {
                    if (player.x + COYOTE_PIXELS > block.x + cameraX + block.getHitboxLength()) {
                        player.untouchGround();
                        player.setVelocityY(0);
                    }
                }
                else if (player.physics.getHazardHitbox(player.getX(), player.getY(), cameraX, cameraY).intersects(block.getSolidHitbox(cameraX, cameraY))) {
                    if (player.y <= player.previousY && player.physics.isFlippedGravity() && player.previousY >= block.y + block.getHitboxHeight()) {
                        player.y = block.y + block.getHitboxHeight();
                        player.touchGround(block);
                    }
                    else if (player.y >= player.previousY && !player.physics.isFlippedGravity() && player.previousY + player.physics.getHitboxHeight() <= block.y) {
                        player.y = block.y - player.physics.getHitboxHeight();
                        player.touchGround(block);
                    }
                    if (player.physics.getSolidHitbox(player.getX(), player.getY(), cameraX, cameraY).intersects(block.getSolidHitbox(cameraX, cameraY))) {
                        killPlayer();
                    }
                }
            }
            else if (obj instanceof Hazard hazard) {
                if (player.physics.getHazardHitbox(player.getX(), player.getY(), cameraX, cameraY).intersects(hazard.getHazardHitbox(cameraX, cameraY))) {
                    killPlayer();
                }
            }
        }
    }

    private void killPlayer() {
        player.untouchGround();
        player.setX(level.spawnX);
        player.setY(level.spawnY);
        player.setVelocityY(0);
        cameraX = 0;
        cameraY = 0;
    }

    public LevelMechanics(int levelId) throws IOException {
        level = new Level(levelId);
        gamePaused = false;
        player = new Player(level.spawnX, level.spawnY, level.spawnPhysics);
        level.objects.addFirst(new Ground(GROUND_Y, this, false));
        level.objects.addFirst(player);
        cameraX = 0;
        cameraY = 0;

        setBackground(Color.BLUE);
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_SPACE) {
                    p1Pressed = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    killPlayer();
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    gamePaused = !gamePaused;
                    if (gamePaused) {
                        logicTimer.stop();
                    }
                    else {
                        logicTimer.start();
                    }
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_SPACE) {
                    p1Pressed = false;
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    p1Pressed = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    p1Pressed = false;
                }
            }
        });

        logicTimer = getLogicTimer();
        logicTimer.start();
    }

    private Timer getLogicTimer() {
        return new Timer(1000 / Player.FPS, _ -> {
            if (firstFrame) {
                handleCollisions();
                firstFrame = false;
            }
            player.previousY = player.y;
            if (p1Pressed) {
                player.jump();
            }
            cameraX -= X_VELOCITY;
            for (GameObject obj : level.objects) {
                obj.update();
            }
            handleCollisions();
            repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (GameObject obj : level.objects) {
            obj.draw(g, cameraX, cameraY);
        }
        for (GameObject obj : level.deco) {
            obj.draw(g, cameraX, cameraY);
        }
    }
}
