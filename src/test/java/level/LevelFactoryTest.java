package level;

import model.LevelData;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LevelFactoryTest {

    @Test
    void shouldCreateTwelveValidLevelsInSequence() {
        List<LevelData> levels = LevelFactory.createLevels();

        assertEquals(12, levels.size());

        for (int i = 0; i < levels.size(); i++) {
            LevelData level = levels.get(i);
            assertEquals(i + 1, level.getLevelNumber());
            assertNotNull(level.getName());
            assertFalse(level.getName().isBlank());
            assertNotNull(level.getHint());
            assertNotNull(level.getGoal());
            assertFalse(level.getKeys().isEmpty(), "El nivel " + (i + 1) + " debe tener al menos 1 moneda");
            assertFalse(level.getEnemies().isEmpty(), "El nivel " + (i + 1) + " debe tener al menos 1 enemigo");

            // Verificar que el jugador inicia dentro de la arena jugable (X: 100..780, Y: 110..470)
            assertTrue(level.getStartX() >= 100 && level.getStartX() <= 758);
            assertTrue(level.getStartY() >= 110 && level.getStartY() <= 448);
        }
    }

    @Test
    void shouldMarkTrollLevelsAsReturnToStartGoal() {
        List<LevelData> levels = LevelFactory.createLevels();

        // Niveles 4, 8 y 12 son niveles de retorno al inicio
        assertTrue(levels.get(3).isReturnToStartGoal());
        assertTrue(levels.get(7).isReturnToStartGoal());
        assertTrue(levels.get(11).isReturnToStartGoal());

        // El nivel 1 es salida normal
        assertFalse(levels.get(0).isReturnToStartGoal());
    }
}