package model;

import lombok.Getter;
import lombok.Setter;
import java.awt.Rectangle;
import java.util.List;

@Getter
@Setter
public class Enemy {
    private static final int ARENA_MIN_X = 100;
    private static final int ARENA_MAX_X = 780;
    private static final int ARENA_MIN_Y = 110;
    private static final int ARENA_MAX_Y = 470;

    private double x;
    private double y;
    private int size;
    private double dx;
    private double dy;
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    private MovementPattern pattern;
    private double centerX;
    private double centerY;
    private double radius;
    private double angle;
    private double angularSpeed;

    private double safeSpawnX;
    private double safeSpawnY;
    private double safeSpawnRadius = 48.0;
    private boolean hasSafeSpawn = false;

    public Enemy(double x, double y, int size, double dx, double dy,
                 double minX, double maxX, double minY, double maxY) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.minX = Math.max(ARENA_MIN_X, minX);
        this.maxX = Math.min(ARENA_MAX_X, maxX);
        this.minY = Math.max(ARENA_MIN_Y, minY);
        this.maxY = Math.min(ARENA_MAX_Y, maxY);

        if (Math.abs(dx) >= Math.abs(dy)) {
            this.pattern = MovementPattern.HORIZONTAL;
            this.dx = (Math.abs(dx) < 2.2) ? 3.5 : dx;
            this.dy = 0;
        } else {
            this.pattern = MovementPattern.VERTICAL;
            this.dx = 0;
            this.dy = (Math.abs(dy) < 2.2) ? 3.5 : dy;
        }
    }

    public static Enemy horizontal(double x, double y, int size, double speed) {
        double validSpeed = (Math.abs(speed) < 2.2) ? 3.5 : speed;
        return new Enemy(x, y, size, validSpeed, 0,
                ARENA_MIN_X, ARENA_MAX_X, ARENA_MIN_Y, ARENA_MAX_Y);
    }

    public static Enemy vertical(double x, double y, int size, double speed) {
        double validSpeed = (Math.abs(speed) < 2.2) ? 3.5 : speed;
        return new Enemy(x, y, size, 0, validSpeed,
                ARENA_MIN_X, ARENA_MAX_X, ARENA_MIN_Y, ARENA_MAX_Y);
    }

    public static Enemy circle(double centerX, double centerY, int size,
                               double radius, double startAngle, double angularSpeed) {
        double safeRadius = Math.max(24, radius);
        double validAngular = (Math.abs(angularSpeed) < 0.02) ? 0.05 : angularSpeed;
        double startX = centerX + Math.cos(startAngle) * safeRadius - size / 2.0;
        double startY = centerY + Math.sin(startAngle) * safeRadius - size / 2.0;

        Enemy e = new Enemy(startX, startY, size, 1, 0,
                ARENA_MIN_X, ARENA_MAX_X, ARENA_MIN_Y, ARENA_MAX_Y);
        e.pattern = MovementPattern.CIRCLE;
        e.dx = 0;
        e.dy = 0;
        e.centerX = centerX;
        e.centerY = centerY;
        e.radius = safeRadius;
        e.angle = startAngle;
        e.angularSpeed = validAngular;
        e.x = Math.max(ARENA_MIN_X, Math.min(ARENA_MAX_X - size, startX));
        e.y = Math.max(ARENA_MIN_Y, Math.min(ARENA_MAX_Y - size, startY));
        return e;
    }

    public void setSafeSpawn(double spawnX, double spawnY, double radius) {
        this.safeSpawnX = spawnX;
        this.safeSpawnY = spawnY;
        this.safeSpawnRadius = Math.max(24.0, radius);
        this.hasSafeSpawn = true;
    }

    private boolean isInsideSafeSpawn(double testX, double testY) {
        if (!hasSafeSpawn) return false;
        double enemyCenterX = testX + size / 2.0;
        double enemyCenterY = testY + size / 2.0;
        double spawnCenterX = safeSpawnX + 11.0;
        double spawnCenterY = safeSpawnY + 11.0;
        double minDist = safeSpawnRadius + size / 2.0;
        return Math.hypot(enemyCenterX - spawnCenterX, enemyCenterY - spawnCenterY) < minDist;
    }

    public void update(Player player, List<Wall> walls) {
        MovementPattern current = (pattern == null) ? MovementPattern.VERTICAL : pattern;

        switch (current) {
            case HORIZONTAL -> {
                if (Math.abs(dx) < 2.2) {
                    dx = 3.5;
                }
                double prevX = x;
                double nextX = x + dx;

                if (nextX < minX) {
                    x = minX;
                    dx = Math.abs(dx);
                    return;
                }
                if (nextX + size > maxX) {
                    x = maxX - size;
                    dx = -Math.abs(dx);
                    return;
                }

                if (!collidesWithWalls(nextX, y, walls) && !isInsideSafeSpawn(nextX, y)) {
                    x = nextX;
                } else {
                    dx = -dx;
                    double stepBackX = prevX + (dx > 0 ? 2.0 : -2.0);
                    stepBackX = Math.max(minX, Math.min(maxX - size, stepBackX));
                    if (!collidesWithWalls(stepBackX, y, walls) && !isInsideSafeSpawn(stepBackX, y)) {
                        x = stepBackX;
                    }
                }
            }
            case VERTICAL -> {
                if (Math.abs(dy) < 2.2) {
                    dy = 3.5;
                }
                double prevY = y;
                double nextY = y + dy;

                if (nextY < minY) {
                    y = minY;
                    dy = Math.abs(dy);
                    return;
                }
                if (nextY + size > maxY) {
                    y = maxY - size;
                    dy = -Math.abs(dy);
                    return;
                }

                if (!collidesWithWalls(x, nextY, walls) && !isInsideSafeSpawn(x, nextY)) {
                    y = nextY;
                } else {
                    dy = -dy;
                    double stepBackY = prevY + (dy > 0 ? 2.0 : -2.0);
                    stepBackY = Math.max(minY, Math.min(maxY - size, stepBackY));
                    if (!collidesWithWalls(x, stepBackY, walls) && !isInsideSafeSpawn(x, stepBackY)) {
                        y = stepBackY;
                    }
                }
            }
            case CIRCLE -> {
                if (Math.abs(angularSpeed) < 0.02) {
                    angularSpeed = 0.05;
                }
                double prevX = x;
                double prevY = y;
                double prevAngle = angle;

                double nextAngle = angle + angularSpeed;
                double nextX = centerX + Math.cos(nextAngle) * radius - size / 2.0;
                double nextY = centerY + Math.sin(nextAngle) * radius - size / 2.0;

                if (nextX < minX || nextX + size > maxX || nextY < minY || nextY + size > maxY
                        || collidesWithWalls(nextX, nextY, walls) || isInsideSafeSpawn(nextX, nextY)) {
                    angularSpeed = -angularSpeed;
                    double altAngle = prevAngle + angularSpeed;
                    double altX = Math.max(minX, Math.min(maxX - size,
                            centerX + Math.cos(altAngle) * radius - size / 2.0));
                    double altY = Math.max(minY, Math.min(maxY - size,
                            centerY + Math.sin(altAngle) * radius - size / 2.0));
                    if (!collidesWithWalls(altX, altY, walls) && !isInsideSafeSpawn(altX, altY)) {
                        angle = altAngle;
                        x = altX;
                        y = altY;
                    } else {
                        x = prevX;
                        y = prevY;
                    }
                } else {
                    angle = nextAngle;
                    x = nextX;
                    y = nextY;
                }
            }
        }
    }

    private boolean collidesWithWalls(double testX, double testY, List<Wall> walls) {
        if (walls == null || walls.isEmpty()) return false;
        Rectangle box = new Rectangle((int) Math.round(testX), (int) Math.round(testY), size, size);
        for (Wall w : walls) {
            if (box.intersects(w.getBounds())) {
                return true;
            }
        }
        return false;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) Math.round(x), (int) Math.round(y), size, size);
    }
}