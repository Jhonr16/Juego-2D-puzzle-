package database;

import model.TimeRecord;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL =
            "jdbc:postgresql://ep-ep-morning-base-bd9e3ypb-pooler.c-8.us-east-2.aws.neon.tech.aws.neon.tech:5432/neondb?sslmode=require";
    private static final String USER = "neondb_owner";
    private static final String PASSWORD = "npg_BW5XVwOH0Sfb";

    public boolean saveRecord(TimeRecord record) {
        String sql = "INSERT INTO time_records (player_name, total_levels, elapsed_seconds, deaths) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://ep-ep-morning-base-bd9e3ypb-pooler.c-8.us-east-2.aws.neon.tech.aws.neon.tech:5432/neondb?sslmode=require", "neondb_owner", "npg_BW5XVwOH0Sfb");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, record.getPlayerName());
            stmt.setInt(2, record.getTotalLevels());
            stmt.setDouble(3, record.getElapsedSeconds());
            stmt.setInt(4, record.getDeaths());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error guardando récord en Neon: " + e.getMessage());
            return false;
        }
    }

    public List<TimeRecord> getTopRecords(int limit) {
        List<TimeRecord> records = new ArrayList<>();
        String sql = "SELECT player_name, total_levels, elapsed_seconds, deaths FROM time_records ORDER BY elapsed_seconds ASC LIMIT ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TimeRecord r = new TimeRecord(
                            rs.getString("player_name"),
                            rs.getInt("total_levels"),
                            rs.getDouble("elapsed_seconds"),
                            rs.getInt("deaths")
                    );
                    records.add(r);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error consultando récords en Neon: " + e.getMessage());
        }
        return records;
    }
}