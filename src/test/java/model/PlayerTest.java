package model;

import org.junit.jupiter.api.Test;
import java.awt.Rectangle;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void shouldInitializeWithZeroKeysAndValidBounds() {
        Player player = new Player(120.5, 280.0, 22, 4.5);

        assertEquals(120.5, player.getX());
        assertEquals(280.0, player.getY());
        assertEquals(22, player.getSize());
        assertEquals(4.5, player.getSpeed());
        assertEquals(0, player.getKeysCollected());
        assertEquals(new Rectangle(120, 280, 22, 22), player.getBounds());
    }

    @Test
    void shouldResetPositionAndClearCollectedKeys() {
        Player player = new Player(100, 100, 22, 4.5);
        player.setX(450);
        player.setY(320);
        player.setKeysCollected(3);

        player.reset(120, 280);

        assertEquals(120.0, player.getX());
        assertEquals(280.0, player.getY());
        assertEquals(0, player.getKeysCollected());
    }
}