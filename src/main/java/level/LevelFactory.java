package level;

import model.*;
import java.util.ArrayList;
import java.util.List;

public class LevelFactory {

    public static List<LevelData> createLevels() {
        List<LevelData> levels = new ArrayList<>();

        // 1. Entrada al Laberinto: muro en L + pasillo superior
        levels.add(new LevelData(
                1, "Compuerta de Acero",
                "Pista: Sube por la izquierda, bordea el muro en L hasta la moneda y baja a la zona final.",
                120, 410,
                new GoalZone(705, 375, 65, 85),
                false,
                List.of(new KeyItem(635, 135, 18)),
                List.of(
                        new Enemy(295, 130, 20, 0, 3.6, 100, 780, 110, 470),
                        new Enemy(465, 445, 20, 0, -3.6, 100, 780, 110, 470),
                        Enemy.oscillating(575, 290, 20, 0, 115, 0.045)
                ),
                List.of(
                        new Wall(220, 180, 24, 210),
                        new Wall(220, 180, 140, 24),
                        new Wall(410, 110, 24, 130),
                        new Wall(410, 330, 24, 140),
                        new Wall(520, 200, 24, 140)
                )
        ));

        // 2. Cámara de Satélites: dos núcleos orbitales y muros centrales
        levels.add(new LevelData(
                2, "Núcleo Doble",
                "Pista: Dos monedas — abajo a la izquierda y arriba al centro. Cuídate de las órbitas.",
                120, 280,
                new GoalZone(705, 130, 65, 85),
                false,
                List.of(
                        new KeyItem(145, 430, 18),
                        new KeyItem(440, 135, 18)
                ),
                List.of(
                        Enemy.orbiting(335, 290, 20, 68, 0, 0.05),
                        Enemy.orbiting(335, 290, 20, 68, Math.PI, 0.05),
                        Enemy.orbiting(555, 290, 20, 68, 0.6, -0.05),
                        Enemy.orbiting(555, 290, 20, 68, 0.6 + Math.PI, -0.05)
                ),
                List.of(
                        new Wall(215, 150, 24, 110),
                        new Wall(215, 320, 24, 110),
                        new Wall(432, 220, 24, 140),
                        new Wall(635, 190, 24, 160)
                )
        ));

        // 3. Troll #1: Devolverse al inicio tras recoger la moneda
        levels.add(new LevelData(
                3, "No Way Out: Vuelta al Origen",
                "Pista: Toma la moneda arriba-derecha y REGRESA a tu zona de inicio para avanzar.",
                120, 280,
                new GoalZone(705, 245, 65, 85),
                true,
                List.of(new KeyItem(640, 135, 18)),
                List.of(
                        new Enemy(280, 130, 20, 0, 4.8, 100, 780, 110, 470),
                        Enemy.oscillating(440, 290, 20, 0, 135, 0.055),
                        new Enemy(585, 445, 20, 0, -4.8, 100, 780, 110, 470)
                ),
                List.of(
                        new Wall(215, 160, 24, 230),
                        new Wall(355, 110, 24, 145),
                        new Wall(355, 345, 24, 125),
                        new Wall(510, 190, 24, 180)
                )
        ));

        // 4. Corredores en Escalera
        levels.add(new LevelData(
                4, "Escalera de Muros",
                "Pista: Avanza por los escalones hasta la moneda central y desciende a la zona final.",
                120, 140,
                new GoalZone(705, 380, 65, 85),
                false,
                List.of(new KeyItem(440, 280, 18)),
                List.of(
                        new Enemy(295, 130, 20, 4.6, 0, 240, 660, 110, 470),
                        new Enemy(610, 225, 20, -4.6, 0, 240, 660, 110, 470),
                        new Enemy(295, 365, 20, 4.6, 0, 240, 660, 110, 470)
                ),
                List.of(
                        new Wall(215, 175, 220, 22),
                        new Wall(345, 310, 240, 22),
                        new Wall(215, 415, 180, 22)
                )
        ));

        // 5. Bastión de Tres Monedas
        levels.add(new LevelData(
                5, "Bastión Dorado",
                "Pista: Recoge las 3 monedas (arriba-centro, abajo-izquierda y abajo-derecha).",
                120, 140,
                new GoalZone(705, 130, 65, 85),
                false,
                List.of(
                        new KeyItem(440, 135, 18),
                        new KeyItem(260, 430, 18),
                        new KeyItem(635, 430, 18)
                ),
                List.of(
                        Enemy.orbiting(440, 290, 20, 82, 0, 0.055),
                        Enemy.orbiting(440, 290, 20, 82, Math.PI, 0.055),
                        new Enemy(565, 130, 20, 0, 5.2, 100, 780, 110, 470)
                ),
                List.of(
                        new Wall(205, 190, 24, 175),
                        new Wall(340, 190, 200, 22),
                        new Wall(515, 250, 24, 130)
                )
        ));

        // 6. Troll #2: Doble moneda y regreso al inicio
        levels.add(new LevelData(
                6, "No Way Out: Espejismo",
                "Pista: Toma ambas monedas y VUELVE a tu punto de inicio para pasar el nivel.",
                120, 410,
                new GoalZone(705, 130, 65, 85),
                true,
                List.of(
                        new KeyItem(310, 135, 18),
                        new KeyItem(645, 430, 18)
                ),
                List.of(
                        new Enemy(250, 130, 20, 0, 5.6, 100, 780, 110, 470),
                        Enemy.oscillating(440, 290, 20, 105, 0, 0.065),
                        new Enemy(575, 445, 20, 0, -5.6, 100, 780, 110, 470)
                ),
                List.of(
                        new Wall(195, 150, 24, 210),
                        new Wall(375, 110, 24, 135),
                        new Wall(375, 340, 24, 130),
                        new Wall(515, 240, 24, 140)
                )
        ));

        // 7 a 12: Escenarios detallados con muros estructurados (el 10 es Troll #3)
        for (int lvl = 7; lvl <= 12; lvl++) {
            boolean trollLevel = (lvl == 10);
            double speed = 5.2 + (lvl - 6) * 0.6;
            int keyCount = (lvl >= 9) ? 3 : 2;

            List<KeyItem> keys = new ArrayList<>();
            keys.add(new KeyItem(260, 135, 18));
            keys.add(new KeyItem(640, 430, 18));
            if (keyCount == 3) {
                keys.add(new KeyItem(440, 280, 18));
            }

            List<Enemy> enemies = new ArrayList<>();
            enemies.add(Enemy.orbiting(350, 290, 20, 64, 0, 0.05 + (lvl - 6) * 0.008));
            enemies.add(Enemy.orbiting(535, 290, 20, 64, Math.PI, -(0.05 + (lvl - 6) * 0.008)));
            enemies.add(Enemy.oscillating(440, 145, 20, 120, 0, 0.06));
            enemies.add(new Enemy(295, 440, 20, 0, -speed, 100, 780, 110, 470));
            enemies.add(new Enemy(615, 130, 20, 0, speed, 100, 780, 110, 470));

            List<Wall> walls = List.of(
                    new Wall(200, 180, 24, 180),
                    new Wall(200, 180, 110, 22),
                    new Wall(375, 110, 24, 115),
                    new Wall(515, 330, 24, 140),
                    new Wall(515, 330, 110, 22)
            );

            String hint = trollLevel
                    ? "Pista TROLL: ¡Recoge las 3 monedas y REGRESA a tu zona de inicio!"
                    : (keyCount == 3)
                    ? "Pista: 3 monedas — arriba-izquierda, centro y abajo-derecha. ¡Atento a los muros!"
                    : "Pista: 2 monedas — navega entre los bloques y las órbitas.";

            String name = trollLevel
                    ? "No Way Out: Retorno Abisal"
                    : "No Way Out: Ciudadela #" + lvl;

            levels.add(new LevelData(
                    lvl, name, hint,
                    120, 280,
                    new GoalZone(705, 245, 65, 85),
                    trollLevel,
                    keys, enemies, walls
            ));
        }

        return levels;
    }
}