package ui;

import database.DatabaseManager;
import level.LevelFactory;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GamePanel extends JPanel implements ActionListener {

    private static final int ARENA_X = 100;
    private static final int ARENA_Y = 110;
    private static final int ARENA_WIDTH = 680;
    private static final int ARENA_HEIGHT = 360;

    private final List<LevelData> levels;
    private final DatabaseManager dbManager;
    private final Runnable onBackToMenu;
    private final Set<Integer> pressedKeys = new HashSet<>();

    private int currentLevelIndex = 0;
    private final Player player;
    private LevelData currentLevel;
    private int deaths = 0;
    private final long startTimeMillis;
    private boolean finishedAllLevels = false;
    private final Timer timer;

    public GamePanel(DatabaseManager dbManager, Runnable onBackToMenu) {
        this.dbManager = dbManager;
        this.onBackToMenu = onBackToMenu;

        setPreferredSize(new Dimension(900, 560));
        setBackground(new Color(24, 26, 33));
        setFocusable(true);

        this.levels = LevelFactory.createLevels();
        this.player = new Player(120, 280, 22, 4.5);

        loadLevel(0);
        setupInput();

        this.startTimeMillis = System.currentTimeMillis();
        this.timer = new Timer(16, this);
        this.timer.start();
    }

    private void loadLevel(int index) {
        this.currentLevelIndex = index;
        this.currentLevel = levels.get(index);
        for (KeyItem k : currentLevel.getKeys()) {
            k.setCollected(false);
        }
        player.reset(currentLevel.getStartX(), currentLevel.getStartY());
    }

    private void setupInput() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
                if (e.getKeyCode() == KeyEvent.VK_R && !finishedAllLevels) {
                    loadLevel(currentLevelIndex);
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE && !finishedAllLevels) {
                    timer.stop();
                    if (onBackToMenu != null) {
                        onBackToMenu.run();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!finishedAllLevels) {
            updatePlayer();
            updateEnemies();
            checkCollisions();
        }
        repaint();
    }

    private void updatePlayer() {
        double moveX = 0, moveY = 0;
        if (pressedKeys.contains(KeyEvent.VK_W) || pressedKeys.contains(KeyEvent.VK_UP)) moveY -= 1;
        if (pressedKeys.contains(KeyEvent.VK_S) || pressedKeys.contains(KeyEvent.VK_DOWN)) moveY += 1;
        if (pressedKeys.contains(KeyEvent.VK_A) || pressedKeys.contains(KeyEvent.VK_LEFT)) moveX -= 1;
        if (pressedKeys.contains(KeyEvent.VK_D) || pressedKeys.contains(KeyEvent.VK_RIGHT)) moveX += 1;

        if (moveX != 0 && moveY != 0) {
            moveX *= 0.7071;
            moveY *= 0.7071;
        }

        // Movimiento horizontal con colisión contra paredes
        double nextX = Math.max(
                ARENA_X,
                Math.min(ARENA_X + ARENA_WIDTH - player.getSize(), player.getX() + moveX * player.getSpeed())
        );
        player.setX(nextX);
        Rectangle pBoundsX = player.getBounds();
        for (Wall wall : currentLevel.getWalls()) {
            if (pBoundsX.intersects(wall.getBounds())) {
                if (moveX > 0) {
                    player.setX(wall.getX() - player.getSize());
                } else if (moveX < 0) {
                    player.setX(wall.getX() + wall.getWidth());
                }
                pBoundsX = player.getBounds();
            }
        }

        // Movimiento vertical con colisión contra paredes
        double nextY = Math.max(
                ARENA_Y,
                Math.min(ARENA_Y + ARENA_HEIGHT - player.getSize(), player.getY() + moveY * player.getSpeed())
        );
        player.setY(nextY);
        Rectangle pBoundsY = player.getBounds();
        for (Wall wall : currentLevel.getWalls()) {
            if (pBoundsY.intersects(wall.getBounds())) {
                if (moveY > 0) {
                    player.setY(wall.getY() - player.getSize());
                } else if (moveY < 0) {
                    player.setY(wall.getY() + wall.getHeight());
                }
                pBoundsY = player.getBounds();
            }
        }
    }

    private void updateEnemies() {
        for (Enemy enemy : currentLevel.getEnemies()) {
            double oldX = enemy.getX();
            double oldY = enemy.getY();

            enemy.update();

            if (enemy.getPattern() == null || enemy.getPattern() == MovementPattern.LINEAR) {
                // Rebote contra paredes en X
                for (Wall wall : currentLevel.getWalls()) {
                    if (enemy.getBounds().intersects(wall.getBounds())) {
                        if (enemy.getX() > oldX) {
                            enemy.setX(wall.getX() - enemy.getSize() - 1);
                            enemy.setDx(-Math.abs(enemy.getDx()));
                        } else if (enemy.getX() < oldX) {
                            enemy.setX(wall.getX() + wall.getWidth() + 1);
                            enemy.setDx(Math.abs(enemy.getDx()));
                        }
                    }
                }

                // Rebote contra paredes en Y
                for (Wall wall : currentLevel.getWalls()) {
                    if (enemy.getBounds().intersects(wall.getBounds())) {
                        if (enemy.getY() > oldY) {
                            enemy.setY(wall.getY() - enemy.getSize() - 1);
                            enemy.setDy(-Math.abs(enemy.getDy()));
                        } else if (enemy.getY() < oldY) {
                            enemy.setY(wall.getY() + wall.getHeight() + 1);
                            enemy.setDy(Math.abs(enemy.getDy()));
                        }
                    }
                }
            }
        }
    }

    private Rectangle getStartZoneBounds() {
        return new Rectangle(
                (int) currentLevel.getStartX() - 14,
                (int) currentLevel.getStartY() - 14,
                player.getSize() + 28,
                player.getSize() + 28
        );
    }

    private void checkCollisions() {
        Rectangle pBounds = player.getBounds();

        // Recoger monedas / llaves
        for (KeyItem key : currentLevel.getKeys()) {
            if (!key.isCollected() && pBounds.intersects(key.getBounds())) {
                key.setCollected(true);
                player.setKeysCollected(player.getKeysCollected() + 1);
            }
        }

        // Chocar con enemigos -> reiniciar nivel y sumar muerte
        for (Enemy enemy : currentLevel.getEnemies()) {
            if (pBounds.intersects(enemy.getBounds())) {
                deaths++;
                loadLevel(currentLevelIndex);
                return;
            }
        }

        boolean allKeysCollected = player.getKeysCollected() == currentLevel.getKeys().size();

        if (currentLevel.isReturnToStartGoal()) {
            if (allKeysCollected && pBounds.intersects(getStartZoneBounds())) {
                advanceLevelOrWin();
            }
        } else {
            if (allKeysCollected && pBounds.intersects(currentLevel.getGoal().getBounds())) {
                advanceLevelOrWin();
            }
        }
    }

    private void advanceLevelOrWin() {
        if (currentLevelIndex + 1 < levels.size()) {
            loadLevel(currentLevelIndex + 1);
        } else {
            finishedAllLevels = true;
            timer.stop();
            handleVictory();
        }
    }

    private void handleVictory() {
        double elapsedSeconds = (System.currentTimeMillis() - startTimeMillis) / 1000.0;
        String name = JOptionPane.showInputDialog(
                this,
                String.format("¡Escapaste de No Way Out (%d niveles) en %.2f s con %d muertes!\nIngresa tu nombre para guardar el récord:",
                        levels.size(), elapsedSeconds, deaths),
                "No Way Out - ¡Victoria!",
                JOptionPane.PLAIN_MESSAGE
        );

        if (name != null && !name.isBlank()) {
            TimeRecord record = new TimeRecord(name.trim(), levels.size(), elapsedSeconds, deaths);
            boolean saved = dbManager.saveRecord(record);
            List<TimeRecord> top = dbManager.getTopRecords(5);

            StringBuilder sb = new StringBuilder(
                    saved ? "¡Récord guardado en Neon!\n\n" : "No se pudo guardar en BD (revisa Neon).\n\n"
            );
            sb.append("TOP 5 TIEMPOS - NO WAY OUT:\n");
            int pos = 1;
            for (TimeRecord r : top) {
                sb.append(String.format("%d. %s - %.2fs (%d muertes)\n",
                        pos++, r.getPlayerName(), r.getElapsedSeconds(), r.getDeaths()));
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        }

        if (onBackToMenu != null) {
            onBackToMenu.run();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double elapsed = (System.currentTimeMillis() - startTimeMillis) / 1000.0;

        // HUD superior
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 15));
        g2.drawString(String.format("No Way Out | Nivel %d/%d: %s",
                currentLevel.getLevelNumber(), levels.size(), currentLevel.getName()), ARENA_X, 35);
        g2.drawString(String.format("Tiempo: %.1fs   |   Muertes: %d   |   Monedas: %d/%d",
                elapsed, deaths, player.getKeysCollected(), currentLevel.getKeys().size()), 450, 35);

        // Pista del nivel
        g2.setColor(new Color(255, 215, 90));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 13));
        g2.drawString(currentLevel.getHint(), ARENA_X, 62);

        // Fondo de la arena y cuadrícula
        g2.setColor(new Color(38, 42, 54));
        g2.fillRect(ARENA_X, ARENA_Y, ARENA_WIDTH, ARENA_HEIGHT);

        g2.setColor(new Color(47, 52, 66));
        int cellSize = 20;
        for (int gx = ARENA_X + cellSize; gx < ARENA_X + ARENA_WIDTH; gx += cellSize) {
            g2.drawLine(gx, ARENA_Y, gx, ARENA_Y + ARENA_HEIGHT);
        }
        for (int gy = ARENA_Y + cellSize; gy < ARENA_Y + ARENA_HEIGHT; gy += cellSize) {
            g2.drawLine(ARENA_X, gy, ARENA_X + ARENA_WIDTH, gy);
        }

        // Borde exterior doble de la arena
        g2.setColor(new Color(120, 132, 165));
        g2.drawRect(ARENA_X, ARENA_Y, ARENA_WIDTH, ARENA_HEIGHT);
        g2.setColor(new Color(75, 84, 108));
        g2.drawRect(ARENA_X + 2, ARENA_Y + 2, ARENA_WIDTH - 4, ARENA_HEIGHT - 4);

        boolean allKeysCollected = player.getKeysCollected() == currentLevel.getKeys().size();

        // Zona de inicio (tono neutro fijo con borde)
        Rectangle startZone = getStartZoneBounds();
        g2.setColor(new Color(68, 80, 108));
        g2.fillRect(startZone.x, startZone.y, startZone.width, startZone.height);
        g2.setColor(new Color(125, 145, 185));
        g2.drawRect(startZone.x, startZone.y, startZone.width, startZone.height);

        // Paredes con relieve y borde
        for (Wall wall : currentLevel.getWalls()) {
            g2.setColor(new Color(96, 106, 132));
            g2.fillRect(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
            g2.setColor(new Color(130, 142, 172));
            g2.drawRect(wall.getX() + 2, wall.getY() + 2,
                    Math.max(1, wall.getWidth() - 4), Math.max(1, wall.getHeight() - 4));
            g2.setColor(new Color(165, 178, 210));
            g2.drawRect(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
        }

        // Zona final: verde opaco antes de recoger todas las monedas, verde más clarito al recogerlas todas
        GoalZone goal = currentLevel.getGoal();
        Color dullGoalGreen = new Color(45, 95, 65);
        Color brightGoalGreen = new Color(135, 245, 165);

        g2.setColor(allKeysCollected ? brightGoalGreen : dullGoalGreen);
        g2.fillRect(goal.getX(), goal.getY(), goal.getWidth(), goal.getHeight());
        g2.setColor(allKeysCollected ? new Color(200, 255, 215) : new Color(75, 130, 95));
        g2.drawRect(goal.getX(), goal.getY(), goal.getWidth(), goal.getHeight());
        g2.drawRect(goal.getX() + 3, goal.getY() + 3, goal.getWidth() - 6, goal.getHeight() - 6);

        // Monedas / llaves con centro y borde
        for (KeyItem key : currentLevel.getKeys()) {
            if (!key.isCollected()) {
                g2.setColor(new Color(255, 200, 40));
                g2.fillOval(key.getX(), key.getY(), key.getSize(), key.getSize());
                g2.setColor(new Color(255, 238, 140));
                g2.drawOval(key.getX() + 1, key.getY() + 1, key.getSize() - 2, key.getSize() - 2);
                g2.fillOval(key.getX() + key.getSize() / 3, key.getY() + key.getSize() / 3,
                        key.getSize() / 3, key.getSize() / 3);
            }
        }

        // Enemigos con borde oscuro para destacar
        for (Enemy enemy : currentLevel.getEnemies()) {
            int ex = (int) Math.round(enemy.getX());
            int ey = (int) Math.round(enemy.getY());
            g2.setColor(new Color(235, 68, 90));
            g2.fillOval(ex, ey, enemy.getSize(), enemy.getSize());
            g2.setColor(new Color(140, 25, 42));
            g2.drawOval(ex, ey, enemy.getSize(), enemy.getSize());
        }

        // Jugador con borde brillante
        int px = (int) Math.round(player.getX());
        int py = (int) Math.round(player.getY());
        g2.setColor(new Color(66, 155, 255));
        g2.fillRect(px, py, player.getSize(), player.getSize());
        g2.setColor(new Color(165, 212, 255));
        g2.drawRect(px, py, player.getSize(), player.getSize());

        // Controles abajo
        g2.setColor(new Color(180, 185, 200));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2.drawString("No Way Out  |  Mover: WASD o Flechas  |  Reiniciar nivel: R  |  Menú: ESC", ARENA_X, 505);
    }
}