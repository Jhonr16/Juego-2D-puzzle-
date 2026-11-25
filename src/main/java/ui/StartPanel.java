package ui;

import database.DatabaseManager;
import model.TimeRecord;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StartPanel extends JPanel {

    private final DatabaseManager dbManager;
    private final Runnable onStartGame;
    private final JTextArea leaderboardArea;

    public StartPanel(DatabaseManager dbManager, Runnable onStartGame) {
        this.dbManager = dbManager;
        this.onStartGame = onStartGame;

        setPreferredSize(new Dimension(900, 560));
        setBackground(new Color(24, 26, 33));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("NO WAY OUT");
        titleLabel.setForeground(new Color(235, 68, 90));
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 44));
        gbc.gridy = 0;
        add(titleLabel, gbc);

        JLabel subtitleLabel = new JLabel("12 niveles • Sin salida fácil");
        subtitleLabel.setForeground(new Color(175, 185, 205));
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        gbc.gridy = 1;
        add(subtitleLabel, gbc);

        JButton playButton = new JButton("Jugar");
        playButton.setPreferredSize(new Dimension(220, 44));
        playButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        playButton.setBackground(new Color(66, 155, 255));
        playButton.setForeground(Color.WHITE);
        playButton.setFocusPainted(false);
        playButton.addActionListener(e -> {
            if (onStartGame != null) {
                onStartGame.run();
            }
        });
        gbc.gridy = 2;
        gbc.insets = new Insets(16, 10, 6, 10);
        add(playButton, gbc);

        JButton recordsButton = new JButton("Actualizar Récords");
        recordsButton.setPreferredSize(new Dimension(220, 38));
        recordsButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        recordsButton.setBackground(new Color(52, 58, 74));
        recordsButton.setForeground(Color.WHITE);
        recordsButton.setFocusPainted(false);
        recordsButton.addActionListener(e -> refreshRecords());
        gbc.gridy = 3;
        gbc.insets = new Insets(4, 10, 12, 10);
        add(recordsButton, gbc);

        leaderboardArea = new JTextArea(6, 36);
        leaderboardArea.setEditable(false);
        leaderboardArea.setBackground(new Color(34, 38, 48));
        leaderboardArea.setForeground(new Color(225, 232, 245));
        leaderboardArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        leaderboardArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(85, 98, 128), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        gbc.gridy = 4;
        gbc.insets = new Insets(4, 10, 10, 10);
        add(leaderboardArea, gbc);

        refreshRecords();
    }

    public void refreshRecords() {
        if (dbManager == null) {
            leaderboardArea.setText("Sin gestor de base de datos configurado.");
            return;
        }
        List<TimeRecord> top = dbManager.getTopRecords(5);
        StringBuilder sb = new StringBuilder("TOP 5 RÉCORDS - NO WAY OUT\n");
        sb.append("----------------------------------------\n");
        if (top.isEmpty()) {
            sb.append("Aún no hay récords en Neon.\nCompleta los 12 niveles para aparecer aquí.");
        } else {
            int pos = 1;
            for (TimeRecord r : top) {
                sb.append(String.format("#%d %-14s %6.2fs  (%d muertes)\n",
                        pos++, r.getPlayerName(), r.getElapsedSeconds(), r.getDeaths()));
            }
        }
        leaderboardArea.setText(sb.toString());
        revalidate();
        repaint();
    }
}