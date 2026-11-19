package model;

import lombok.Getter;
import lombok.Setter;
import java.awt.Rectangle;

@Getter
@Setter
public class KeyItem {
    private int x;
    private int y;
    private int size;
    private boolean collected;

    public KeyItem(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.collected = false;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }
}