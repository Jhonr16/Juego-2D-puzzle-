package model;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EnemyTest {

    @Test
    void shouldEnforceMinimumSpeedOnHorizontalAndVerticalFactories() {
        Enemy horizontal = Enemy.horizontal(250, 200, 20, 1.0);
        Enemy vertical = Enemy.vertical(300, 200, 20, 0.5);

        assertEquals(MovementPattern.HORIZONTAL, horizontal.getPattern());
        assertEquals(3.5, horizontal.getDx());
        assertEquals(0.0, horizontal.getDy());

        assertEquals(MovementPattern.VERTICAL, vertical.getPattern());
        assertEquals(0.0, vertical.getDx());
        assertEquals(3.5, vertical.getDy());
    }

    @Test
    void shouldMoveHorizontallyAndBounceOffWall() {
        Player dummyPlayer = new Player(120, 400, 22, 4.5);
        Enemy enemy = Enemy.horizontal(200, 200, 20, 5.0);
        List<Wall> walls = List.of(new Wall(222, 180, 20, 60));

        // Primer paso: avanza hacia la derecha sin tocar muro (200 -> 205)
        enemy.update(dummyPlayer, walls);
        assertEquals(205.0, enemy.getX());
        assertEquals(5.0, enemy.getDx());

        // Segundo paso: intentaría llegar a 210 (210 + 20 = 230 > 222) -> colisiona y rebota
        enemy.update(dummyPlayer, walls);
        assertEquals(-5.0, enemy.getDx());
    }

    @Test
    void shouldBounceAtArenaBoundaries() {
        Player dummyPlayer = new Player(500, 400, 22, 4.5);
        Enemy enemy = Enemy.vertical(300, 112, 20, -5.0);

        enemy.update(dummyPlayer, List.of());

        assertEquals(110.0, enemy.getY());
        assertEquals(5.0, enemy.getDy());
    }

    @Test
    void shouldAvoidSafeSpawnZone() {
        Player dummyPlayer = new Player(120, 280, 22, 4.5);
        Enemy enemy = Enemy.horizontal(182, 280, 20, -5.0);
        enemy.setSafeSpawn(120, 280, 48.0);

        enemy.update(dummyPlayer, List.of());

        // Al intentar entrar en el radio de spawn seguro, invierte dirección
        assertTrue(enemy.getDx() > 0);
    }

    @Test
    void shouldOrbitInCirclePattern() {
        Player dummyPlayer = new Player(120, 120, 22, 4.5);
        Enemy orbiter = Enemy.circle(440, 290, 20, 50, 0.0, 0.07);
        double initialX = orbiter.getX();
        double initialY = orbiter.getY();

        orbiter.update(dummyPlayer, List.of());

        assertEquals(MovementPattern.CIRCLE, orbiter.getPattern());
        assertNotEquals(initialX, orbiter.getX());
        assertNotEquals(initialY, orbiter.getY());
    }
}