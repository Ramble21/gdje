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
import java.util.ArrayList;

public class LevelMechanics extends JPanel {

    public final Level level;
    private final Player player;
    private boolean firstFrame = true;
    private int ixKillerObject = -1;
    public boolean p1Pressed = false;
    public double cameraX;
    public double cameraY;

    private final ArrayList<Timer> activeTimers = new ArrayList<>();

    private final AudioPlayer songPlayer;
    private final AudioPlayer deathSFXPlayer;
    private final AudioPlayer newBestSFXPlayer;

    public static final int SCREEN_WIDTH = 1920;
    public static final int SCREEN_HEIGHT = 1080;

    private volatile boolean running = true;
    private volatile boolean gamePaused = false;

    private static Thread gameThread;
    public static final double PHYSICS_TPS = 240;
    public static final double NS_PER_TICK = 1_000_000_000 / PHYSICS_TPS;

    public static final int RESPAWN_TIME = 500;
    public static final int GROUND_Y = 900;
    public static final double X_VELOCITY = 2.15;
    public static final int COYOTE_PIXELS = 10;
    public static final int SHOD_OUTLINE = 4;

    private void handleCollisions() {
        for (int i = 0; i < level.objects.size(); i++) {
            GameObject obj = level.objects.get(i);
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
                        player.isDead = true;
                        obj.isDead = true;
                        ixKillerObject = i;
                        return;
                    }
                }
            }
            else if (obj instanceof Hazard hazard) {
                if (player.physics.getHazardHitbox(player.getX(), player.getY(), cameraX, cameraY).intersects(hazard.getHazardHitbox(cameraX, cameraY))) {
                    player.isDead = true;
                    obj.isDead = true;
                    ixKillerObject = i;
                    return;
                }
            }
        }
    }

    @SuppressWarnings("NonAtomicOperationOnVolatileField")
    public LevelMechanics(int levelId) throws IOException {
        level = new Level(levelId);
        player = new Player(level.spawnX, level.spawnY, level.spawnPhysics);
        level.objects.addFirst(new Ground(GROUND_Y, this, false));
        level.objects.addFirst(player);
        cameraX = 0;
        cameraY = 0;

        songPlayer = new AudioPlayer("audio/" + level.songName + ".wav");
        deathSFXPlayer = new AudioPlayer("audio/explode_11.wav");
        newBestSFXPlayer = new AudioPlayer("audio/magicExplosion.wav");
        songPlayer.play(false);

        setBackground(level.bgColor);
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_SPACE) {
                    p1Pressed = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    player.isDead = true;
                    ixKillerObject = -1;
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    gamePaused = !gamePaused;
                    if (gamePaused) {
                        for (Timer t : activeTimers) {
                            t.stop();
                        }
                        songPlayer.pause();
                    }
                    else {
                        for (Timer t : activeTimers) {
                            t.start();
                        }
                        songPlayer.resume();
                    }
                    repaint();
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
        startGameLoop();
    }

    private void startGameLoop() {
        running = true;
        gameThread = new Thread(this::runGameLoop, "game-loop");
        gameThread.setDaemon(true);
        gameThread.start();
    }

    @SuppressWarnings("BusyWait")
    private void runGameLoop() {
        long previousTime = System.nanoTime();
        double lag = 0.0;

        while (running) {
            long currentTime = System.nanoTime();
            if (gamePaused) {
                previousTime = currentTime;
            }
            else {
                lag += currentTime - previousTime;
                previousTime = currentTime;
                while (lag >= NS_PER_TICK) {
                    updatePhysics();
                    lag -= NS_PER_TICK;
                }
                repaint();
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                running = false;
            }
        }
    }
    private void updatePhysics() {
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
        if (player.isDead) {
            running = false;
            deathSFXPlayer.play(false);
            songPlayer.stop();
            SwingUtilities.invokeLater(() -> {
                paintImmediately(0, 0, getWidth(), getHeight());
                Timer respawnTimer = getRespawnTimer();
                activeTimers.add(respawnTimer);
                respawnTimer.start();
            });
        }
    }

    private Timer getRespawnTimer() {
        Timer respawnTimer = new Timer(RESPAWN_TIME, e -> {
            player.isDead = false;
            if (ixKillerObject != -1) {
                level.objects.get(ixKillerObject).isDead = false;
            }
            player.setX(level.spawnX);
            player.setY(level.spawnY);
            player.untouchGround();
            player.setVelocityY(0);
            cameraX = 0;
            cameraY = 0;
            ixKillerObject = -1;
            activeTimers.remove((Timer) e.getSource());
            songPlayer.play(false);
            startGameLoop();
        });
        respawnTimer.setRepeats(false);
        return respawnTimer;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < level.objects.size(); i++) {
            GameObject obj = level.objects.get(i);
            if (player.isDead && i == ixKillerObject) {
                obj.draw(g, cameraX, cameraY);
                obj.drawHitbox(g, cameraX, cameraY);
            }
            else if (!(obj instanceof Player && player.isDead)) {
                obj.draw(g, cameraX, cameraY);
            }
        }
        for (GameObject obj : level.deco) {
            obj.draw(g, cameraX, cameraY);
        }
        if (player.isDead) {
            player.drawHitbox(g, cameraX, cameraY);
        }
        if (gamePaused) {
            paintText(g, "Pusab", Color.WHITE, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        }
    }

    public static void paintText(Graphics g, String font, Color color, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(color);
        g.setFont(new Font(font, Font.PLAIN, 72));
        String text = "Game Paused";

        FontMetrics metrics = g2.getFontMetrics();

        int textWidth = metrics.stringWidth(text);
        int drawX = x - (textWidth / 2);
        int textHeight = metrics.getAscent();
        int drawY = y + (textHeight / 2);

        g.drawString("Game Paused", drawX, drawY);
    }
}
