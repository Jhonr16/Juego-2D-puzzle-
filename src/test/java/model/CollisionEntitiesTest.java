package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CollisionEntitiesTest {

    @Test
    void shouldDetectIntersectionBetweenPlayerAndKeyItem() {
        Player player = new Player(200, 200, 22, 4.5);
        KeyItem key = new KeyItem(210, 210, 18);

        assertFalse(key.isCollected());
        assertTrue(player.getBounds().intersects(key.getBounds()));

        key.setCollected(true);
        assertTrue(key.isCollected());
    }

    @Test
    void shouldDetectIntersectionWithWallAndGoalZone() {
        Player player = new Player(700, 250, 22, 4.5);
        Wall wall = new Wall(500, 200, 22, 100);
        GoalZone goal = new GoalZone(705, 245, 65, 85);

        assertFalse(player.getBounds().intersects(wall.getBounds()));
        assertTrue(player.getBounds().intersects(goal.getBounds()));
    }

    @Test
    void shouldStoreTimeRecordFieldsProperly() {
        TimeRecord record = new TimeRecord("Jhon", 12, 84.35, 3);

        assertEquals("Jhon", record.getPlayerName());
        assertEquals(12, record.getLevelsCompleted());
        assertEquals(84.35, record.getElapsedSeconds());
        assertEquals(3, record.getDeaths());
    }
}