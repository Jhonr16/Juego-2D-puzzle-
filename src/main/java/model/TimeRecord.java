package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeRecord {
    private String playerName;
    private int levelsCompleted;
    private double elapsedSeconds;
    private int deaths;
}