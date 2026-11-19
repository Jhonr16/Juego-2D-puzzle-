package model;

import lombok.Getter;
import lombok.Setter;
import java.awt.Rectangle;

@Getter
@Setter
public class Enemy {
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
    private double amplitudeX;
    private double amplitudeY;
    private double radius;
    private double angle;
    private double angularSpeed;
    private double phase;

    public Enemy(double x, double y, int size, double dx, double dy,
                 double minX, double maxX, double minY, double maxY) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.dx = (dx == 0 && dy == 0) ? 3.0 : dx;
        this.dy = dy;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.pattern = MovementPattern.LINEAR;
    }

    public static Enemy oscillating(double baseX, double baseY, int size,
                                    double ampX, double ampY, double speed) {
        Enemy e = new Enemy(baseX, baseY, size, 0, 0, 100, 780, 110, 470);
        e.pattern = MovementPattern.OSCILLATE;
        e.centerX = baseX;
        e.centerY = baseY;
        e.amplitudeX = ampX;
        e.amplitudeY = ampY;
        e.angularSpeed = (Math.abs(speed) < 0.005) ? 0.05 : speed;
        e.phase = 0;
        return e;
    }

    public static Enemy orbiting(double centerX, double centerY, int size,
                                 double radius, double startAngle, double angularSpeed) {
        double safeRadius = Math.max(20, radius);
        double startX = centerX + Math.cos(startAngle) * safeRadius - size / 2.0;
        double startY = centerY + Math.sin(startAngle) * safeRadius - size / 2.0;
        Enemy e = new Enemy(startX, startY, size, 0, 0, 100, 780, 110, 470);
        e.pattern = MovementPattern.ORBIT;
        e.centerX = centerX;
        e.centerY = centerY;
        e.radius = safeRadius;
        e.angle = startAngle;
        e.angularSpeed = (Math.abs(angularSpeed) < 0.005) ? 0.05 : angularSpeed;
        return e;
    }

    public void update() {
        MovementPattern currentPattern = (pattern == null) ? MovementPattern.LINEAR : pattern;

        switch (currentPattern) {
            case LINEAR -> {
                if (dx == 0 && dy == 0) {
                    dy = 3.5;
                }
                x += dx;
                y += dy;

                if (x < minX) {
                    x = minX;
                    dx = Math.abs(dx);
                } else if (x + size > maxX) {
                    x = maxX - size;
                    dx = -Math.abs(dx);
                }

                if (y < minY) {
                    y = minY;
                    dy = Math.abs(dy);
                } else if (y + size > maxY) {
                    y = maxY - size;
                    dy = -Math.abs(dy);
                }
            }
            case OSCILLATE -> {
                phase += angularSpeed;
                double nextX = centerX + Math.sin(phase) * amplitudeX;
                double nextY = centerY + Math.sin(phase) * amplitudeY;
                x = Math.max(100, Math.min(780 - size, nextX));
                y = Math.max(110, Math.min(470 - size, nextY));
            }
            case ORBIT -> {
                angle += angularSpeed;
                double nextX = centerX + Math.cos(angle) * radius - size / 2.0;
                double nextY = centerY + Math.sin(angle) * radius - size / 2.0;
                x = Math.max(100, Math.min(780 - size, nextX));
                y = Math.max(110, Math.min(470 - size, nextY));
            }
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int) Math.round(x), (int) Math.round(y), size, size);
    }
}