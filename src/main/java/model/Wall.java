package model;

import lombok.Getter;
import lombok.Setter;
import java.awt.Rectangle;

@Getter
@Setter
public class Wall {
    private int x;
    private int y;
    private int width;
    private int height;

    public Wall(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}