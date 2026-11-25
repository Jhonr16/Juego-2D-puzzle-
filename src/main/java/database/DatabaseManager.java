package database;

import model.TimeRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String DEFAULT_HOST = "ep-morning-base-bd9e3ypb-pooler.c-8.us-east-2.aws.neon.tech";
    private static final String DEFAULT_PORT = "5432";
    private static final String DEFAULT_DB = "neondb";
    private static final String DEFAULT_USER = "neondb_owner";
    private static final String DEFAULT_PASSWORD = "npg_BW5XVwOH0Sfb";
    private static final String DEFAULT_SSLMODE = "require";
    private static final String DEFAULT_CHANNEL_BINDING = "require";

    private final String jdbcUrl;
    private final String dbUser;
    private final String dbPassword;

    public DatabaseManager() {
        String host = getEnvOrDefault("PGHOST", DEFAULT_HOST);
        String port = getEnvOrDefault("PGPORT", DEFAULT_PORT);
        String dbName = getEnvOrDefault("PGDATABASE", DEFAULT_DB);
        String sslMode = getEnvOrDefault("PGSSLMODE", DEFAULT_SSLMODE);
        String channelBinding = getEnvOrDefault("PGCHANNELBINDING", DEFAULT_CHANNEL_BINDING);

        this.jdbcUrl = String.format(
                "jdbc:postgresql://%s:%s/%s?sslmode=%s&channelBinding=%s",
                host, port, dbName, sslMode, channelBinding
        );
        this.dbUser = getEnvOrDefault("PGUSER", DEFAULT_USER);
        this.dbPassword = getEnvOrDefault("PGPASSWORD", DEFAULT_PASSWORD);

        initTable();
    }

    private String getEnvOrDefault(String key, String fallback) {
        String val = System.getenv(key);
        return (val != null && !val.isBlank()) ? val.trim() : fallback;
    }

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL no encontrado en el classpath", e);
        }
        return DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
    }

    @SuppressWarnings({"SqlNoDataSourceInspection", "SqlDialectInspection"})
    private void initTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS no_way_out_records (
                    id SERIAL PRIMARY KEY,
                    player_name VARCHAR(60) NOT NULL,
                    levels_completed INT NOT NULL,
                    elapsed_seconds DOUBLE PRECISION NOT NULL,
                    deaths INT NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
                """;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("No se pudo inicializar la tabla en Neon: " + e.getMessage());
        }
    }

    public boolean saveRecord(TimeRecord record) {
        if (record == null) {
            return false;
        }
        //noinspection SqlResolve
        String sql = """
                INSERT INTO no_way_out_records (player_name, levels_completed, elapsed_seconds, deaths)
                VALUES (?, ?, ?, ?);
                """;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, record.getPlayerName());
            pstmt.setInt(2, record.getLevelsCompleted());
            pstmt.setDouble(3, record.getElapsedSeconds());
            pstmt.setInt(4, record.getDeaths());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error guardando récord en Neon: " + e.getMessage());
            return false;
        }
    }

    public List<TimeRecord> getTopRecords(int limit) {
        List<TimeRecord> records = new ArrayList<>();
        int safeLimit = Math.max(1, limit);
        //noinspection SqlResolve
        String sql = """
                SELECT player_name, levels_completed, elapsed_seconds, deaths
                FROM no_way_out_records
                ORDER BY elapsed_seconds, deaths, id
                LIMIT ?;
                """;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, safeLimit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    records.add(new TimeRecord(
                            rs.getString("player_name"),
                            rs.getInt("levels_completed"),
                            rs.getDouble("elapsed_seconds"),
                            rs.getInt("deaths")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error consultando récords en Neon: " + e.getMessage());
        }
        return records;
    }
}