package model;

import lombok.Getter;
import lombok.Setter;
import java.awt.Rectangle;

@Getter
@Setter
public class Player {
    private double x;
    private double y;
    private int size;
    private double speed;
    private int keysCollected;

    public Player(double x, double y, int size, double speed) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;
        this.keysCollected = 0;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, size, size);
    }

    public void reset(double startX, double startY) {
        this.x = startX;
        this.y = startY;
        this.keysCollected = 0;
    }
}