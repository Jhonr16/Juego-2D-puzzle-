package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeRecord {
    private int id;
    private String playerName;
    private int totalLevels;
    private double elapsedSeconds;
    private int deaths;

    public TimeRecord() {
    }

    public TimeRecord(String playerName, int totalLevels, double elapsedSeconds, int deaths) {
        this.playerName = playerName;
        this.totalLevels = totalLevels;
        this.elapsedSeconds = elapsedSeconds;
        this.deaths = deaths;
    }
}