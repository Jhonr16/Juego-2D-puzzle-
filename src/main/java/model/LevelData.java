package model;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class LevelData {
    private int levelNumber;
    private String name;
    private String hint;
    private double startX;
    private double startY;
    private GoalZone goal;
    private boolean returnToStartGoal;
    private List<KeyItem> keys;
    private List<Enemy> enemies;
    private List<Wall> walls;

    public LevelData(int levelNumber, String name, String hint,
                     double startX, double startY, GoalZone goal,
                     boolean returnToStartGoal,
                     List<KeyItem> keys, List<Enemy> enemies, List<Wall> walls) {
        this.levelNumber = levelNumber;
        this.name = name;
        this.hint = hint;
        this.startX = startX;
        this.startY = startY;
        this.goal = goal;
        this.returnToStartGoal = returnToStartGoal;
        this.keys = keys;
        this.enemies = enemies;
        this.walls = walls;
    }
}