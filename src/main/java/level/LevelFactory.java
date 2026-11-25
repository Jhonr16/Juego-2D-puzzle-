package level;

import model.*;
import java.util.ArrayList;
import java.util.List;

public class LevelFactory {

    public static List<LevelData> createLevels() {
        List<LevelData> levels = new ArrayList<>();

        // =========================================================================
        // NIVEL 1: Arco de Entrada (Más rápido y vigilado desde el primer segundo)
        // =========================================================================
        levels.add(new LevelData(
                1, "Arco de Entrada",
                "Pista: Sube por la izquierda, cruza bajo las patrullas, toma la moneda arriba-derecha y ve a la salida.",
                120, 410,
                new GoalZone(705, 375, 65, 85),
                false,
                List.of(
                        new KeyItem(652, 130, 18)
                ),
                List.of(
                        Enemy.vertical(280, 220, 20, 4.5),
                        Enemy.horizontal(340, 145, 20, 4.4),
                        Enemy.horizontal(430, 250, 20, -4.3),
                        Enemy.vertical(580, 430, 20, -4.6)
                ),
                List.of(
                        new Wall(215, 190, 22, 280),
                        new Wall(215, 190, 430, 22),
                        new Wall(623, 190, 22, 175)
                )
        ));

        // =========================================================================
        // NIVEL 2: Doble Órbita (Órbitas rápidas + patrullas cruzadas + 2 monedas)
        // =========================================================================
        levels.add(new LevelData(
                2, "Doble Órbita",
                "Pista: Dos monedas en zonas críticas — abajo-izquierda y al centro. ¡No te confíes!",
                120, 280,
                new GoalZone(705, 130, 65, 85),
                false,
                List.of(
                        new KeyItem(122, 442, 18),
                        new KeyItem(432, 280, 18)
                ),
                List.of(
                        Enemy.circle(315, 290, 20, 50, 0, 0.062),
                        Enemy.circle(315, 290, 20, 50, Math.PI, 0.062),
                        Enemy.horizontal(260, 425, 20, 4.9),
                        Enemy.vertical(565, 130, 20, 4.8),
                        Enemy.horizontal(520, 280, 20, -4.7)
                ),
                List.of(
                        new Wall(205, 110, 22, 130),
                        new Wall(205, 340, 22, 130),
                        new Wall(430, 110, 22, 125),
                        new Wall(430, 345, 22, 125)
                )
        ));

        // =========================================================================
        // NIVEL 3: Escalera de Acero (Pisos estrechos con ritmo alto y 2 monedas)
        // =========================================================================
        levels.add(new LevelData(
                3, "Escalera de Acero",
                "Pista: Cruza cada piso a buen ritmo para tomar las monedas en los extremos.",
                120, 135,
                new GoalZone(705, 380, 65, 85),
                false,
                List.of(
                        new KeyItem(655, 135, 18),
                        new KeyItem(120, 322, 18)
                ),
                List.of(
                        Enemy.horizontal(240, 140, 20, 5.1),
                        Enemy.horizontal(560, 140, 20, -5.0),
                        Enemy.horizontal(560, 232, 20, -5.3),
                        Enemy.horizontal(240, 322, 20, 5.4),
                        Enemy.horizontal(560, 415, 20, -5.2)
                ),
                List.of(
                        new Wall(100, 185, 560, 22),
                        new Wall(220, 275, 560, 22),
                        new Wall(100, 365, 560, 22)
                )
        ));

        // =========================================================================
        // NIVEL 4 (SEÑUELO / TROLL #1): Laberinto de Columnas Alternas
        // =========================================================================
        levels.add(new LevelData(
                4, "No Way Out: Vuelta al Origen",
                "Pista: Toma ambas monedas entre las columnas y REGRESA a tu zona de inicio.",
                120, 280,
                new GoalZone(705, 245, 65, 85),
                true,
                List.of(
                        new KeyItem(325, 122, 18),
                        new KeyItem(655, 442, 18)
                ),
                List.of(
                        Enemy.vertical(275, 130, 20, 5.5),
                        Enemy.vertical(435, 440, 20, -5.5),
                        Enemy.vertical(585, 130, 20, 5.7),
                        Enemy.horizontal(390, 280, 20, 5.3),
                        Enemy.horizontal(545, 280, 20, -5.4)
                ),
                List.of(
                        new Wall(205, 110, 22, 240),
                        new Wall(355, 220, 22, 250),
                        new Wall(510, 110, 22, 240)
                )
        ));

        // =========================================================================
        // NIVEL 5: Cuatro Cuadrantes (Monedas y enemigos fuera de los muros)
        // =========================================================================
        levels.add(new LevelData(
                5, "Cuatro Cuadrantes",
                "Pista: Busca las 3 monedas en las esquinas lejanas y el pasillo inferior.",
                120, 135,
                new GoalZone(705, 130, 65, 85),
                false,
                List.of(
                        new KeyItem(120, 442, 18),
                        new KeyItem(468, 442, 18),
                        new KeyItem(655, 442, 18)
                ),
                List.of(
                        Enemy.circle(285, 195, 20, 48, 0, 0.056),
                        Enemy.circle(585, 375, 20, 48, Math.PI, -0.056),
                        Enemy.horizontal(260, 240, 20, 5.0),
                        Enemy.vertical(490, 130, 20, 4.9)
                ),
                List.of(
                        new Wall(430, 110, 22, 120),
                        new Wall(430, 350, 22, 120),
                        new Wall(185, 280, 165, 22),
                        new Wall(530, 280, 165, 22)
                )
        ));

        // =========================================================================
        // NIVEL 6: El Embudo (Monedas tras el cuello central y en esquinas altas/bajas)
        // =========================================================================
        levels.add(new LevelData(
                6, "El Embudo",
                "Pista: Cruza el cuello, toma las 3 monedas entre patrullas rápidas y sal a la derecha.",
                120, 280,
                new GoalZone(705, 245, 65, 85),
                false,
                List.of(
                        new KeyItem(340, 122, 18),
                        new KeyItem(340, 442, 18),
                        new KeyItem(652, 130, 18)
                ),
                List.of(
                        Enemy.vertical(265, 130, 20, 5.3),
                        Enemy.vertical(440, 130, 20, 5.5),
                        Enemy.vertical(575, 440, 20, -5.3),
                        Enemy.horizontal(410, 280, 20, 5.2)
                ),
                List.of(
                        new Wall(200, 110, 22, 115),
                        new Wall(200, 355, 22, 115),
                        new Wall(375, 110, 22, 145),
                        new Wall(375, 325, 22, 145),
                        new Wall(515, 170, 22, 240)
                )
        ));

        // =========================================================================
        // NIVEL 7: Galería de Bloques (Monedas entre bloques y bordes estrechos)
        // =========================================================================
        levels.add(new LevelData(
                7, "Galería de Bloques",
                "Pista: Filtra por los huecos entre bloques para recoger las 3 monedas.",
                120, 415,
                new GoalZone(705, 130, 65, 85),
                false,
                List.of(
                        new KeyItem(120, 125, 18),
                        new KeyItem(432, 442, 18),
                        new KeyItem(652, 442, 18)
                ),
                List.of(
                        Enemy.horizontal(220, 225, 20, 5.6),
                        Enemy.horizontal(580, 320, 20, -5.6),
                        Enemy.vertical(355, 130, 20, 5.7),
                        Enemy.vertical(525, 440, 20, -5.7)
                ),
                List.of(
                        new Wall(210, 260, 115, 55),
                        new Wall(390, 155, 105, 55),
                        new Wall(390, 365, 105, 55),
                        new Wall(560, 190, 110, 55)
                )
        ));

        // =========================================================================
        // NIVEL 8 (SEÑUELO / TROLL #2): Espiral Falsa
        // =========================================================================
        levels.add(new LevelData(
                8, "No Way Out: Espiral Falsa",
                "Pista: Toma las monedas profundas (centro del espiral y arriba-derecha) y VUELVE al inicio.",
                120, 135,
                new GoalZone(705, 380, 65, 85),
                true,
                List.of(
                        new KeyItem(345, 280, 18),
                        new KeyItem(655, 125, 18)
                ),
                List.of(
                        Enemy.vertical(245, 220, 20, 5.9),
                        Enemy.horizontal(320, 135, 20, 5.9),
                        Enemy.circle(575, 290, 20, 52, 0, 0.062),
                        Enemy.circle(575, 290, 20, 52, Math.PI, 0.062),
                        Enemy.horizontal(340, 425, 20, -5.8)
                ),
                List.of(
                        new Wall(195, 110, 22, 275),
                        new Wall(195, 363, 310, 22),
                        new Wall(483, 195, 22, 190),
                        new Wall(300, 195, 205, 22),
                        new Wall(300, 195, 22, 105)
                )
        ));

        // =========================================================================
        // NIVEL 9: Búnker Central (Moneda dentro del búnker + extremos vigilados)
        // =========================================================================
        levels.add(new LevelData(
                9, "Búnker Central",
                "Pista: Entra al búnker por la moneda central y toma las otras 2 en las esquinas.",
                120, 280,
                new GoalZone(705, 245, 65, 85),
                false,
                List.of(
                        new KeyItem(432, 280, 18),
                        new KeyItem(120, 125, 18),
                        new KeyItem(655, 442, 18)
                ),
                List.of(
                        Enemy.vertical(270, 130, 20, 6.2),
                        Enemy.vertical(600, 440, 20, -6.2),
                        Enemy.horizontal(260, 140, 20, 6.1),
                        Enemy.horizontal(580, 425, 20, -6.1),
                        Enemy.circle(440, 290, 20, 46, 0.5, 0.064)
                ),
                List.of(
                        new Wall(340, 195, 200, 22),
                        new Wall(340, 360, 200, 22),
                        new Wall(340, 195, 22, 55),
                        new Wall(340, 325, 22, 57),
                        new Wall(518, 195, 22, 55),
                        new Wall(518, 325, 22, 57)
                )
        ));

        // =========================================================================
        // NIVEL 10: Zigzag de Alta Velocidad (Monedas en el fondo de los canales)
        // =========================================================================
        levels.add(new LevelData(
                10, "Zigzag de Alta Velocidad",
                "Pista: Baja hasta el fondo de los canales para las 3 monedas entre patrullas veloces.",
                120, 135,
                new GoalZone(705, 380, 65, 85),
                false,
                List.of(
                        new KeyItem(242, 442, 18),
                        new KeyItem(432, 125, 18),
                        new KeyItem(618, 442, 18)
                ),
                List.of(
                        Enemy.vertical(245, 130, 20, 6.5),
                        Enemy.vertical(340, 440, 20, -6.5),
                        Enemy.vertical(435, 130, 20, 6.7),
                        Enemy.vertical(530, 440, 20, -6.7),
                        Enemy.vertical(620, 130, 20, 6.9)
                ),
                List.of(
                        new Wall(195, 110, 22, 265),
                        new Wall(290, 205, 22, 265),
                        new Wall(385, 110, 22, 265),
                        new Wall(480, 205, 22, 265),
                        new Wall(575, 110, 22, 265)
                )
        ));


        // =========================================================================
        // NIVEL 11: El Núcleo Final (4 monedas en las 4 esquinas críticas del mapa)
        // =========================================================================
        levels.add(new LevelData(
                11, "El Núcleo Final",
                "Pista: Último reto — recoge las 4 monedas en las esquinas y escapa de No Way Out.",
                120, 280,
                new GoalZone(705, 245, 65, 85),
                false,
                List.of(
                        new KeyItem(120, 125, 18),
                        new KeyItem(120, 442, 18),
                        new KeyItem(655, 125, 18),
                        new KeyItem(655, 442, 18)
                ),
                List.of(
                        Enemy.vertical(275, 130, 20, 7.2),
                        Enemy.vertical(595, 440, 20, -7.2),
                        Enemy.circle(440, 290, 20, 52, 0, 0.074),
                        Enemy.circle(440, 290, 20, 52, Math.PI, 0.074),
                        Enemy.horizontal(260, 135, 20, 7.1),
                        Enemy.horizontal(580, 425, 20, -7.1)
                ),
                List.of(
                        new Wall(195, 180, 22, 210),
                        new Wall(325, 180, 230, 22),
                        new Wall(325, 365, 230, 22),
                        new Wall(655, 180, 22, 210)
                )

        ));

        // =========================================================================
        // NIVEL 12 (SEÑUELO / TROLL #3): Fortaleza Engañosa
        // =========================================================================
        levels.add(new LevelData(
                12, "No Way Out: Fortaleza Engañosa",
                "Pista: Toma las 3 monedas en las zonas más comprometidas y REGRESA a tu inicio.",
                120, 415,
                new GoalZone(705, 130, 65, 85),
                true,
                List.of(
                        new KeyItem(120, 125, 18),
                        new KeyItem(432, 280, 18),
                        new KeyItem(655, 442, 18)
                ),
                List.of(
                        Enemy.vertical(270, 130, 20, 6.9),
                        Enemy.circle(440, 290, 20, 54, 0, 0.070),
                        Enemy.circle(440, 290, 20, 54, Math.PI, 0.070),
                        Enemy.vertical(600, 440, 20, -6.9),
                        Enemy.horizontal(260, 425, 20, 6.8)
                ),
                List.of(
                        new Wall(185, 195, 22, 275),
                        new Wall(185, 195, 130, 22),
                        new Wall(360, 110, 22, 115),
                        new Wall(360, 365, 22, 105),
                        new Wall(510, 195, 130, 22),
                        new Wall(510, 345, 22, 125)
                )
        ));

        return levels;
    }
}