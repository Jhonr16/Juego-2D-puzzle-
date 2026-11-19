package ui;

import database.DatabaseManager;
import model.TimeRecord;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StartPanel extends JPanel {

    private final DatabaseManager dbManager;
    private final Runnable onStartGame;
    private final DefaultTableModel tableModel;
    private final JLabel bestRecordLabel;

    public StartPanel(DatabaseManager dbManager, Runnable onStartGame) {
        this.dbManager = dbManager;
        this.onStartGame = onStartGame;

        setPreferredSize(new Dimension(900, 560));
        setBackground(new Color(24, 26, 33));
        setLayout(new BorderLayout(16, 16));
        setBorder(new EmptyBorder(24, 28, 24, 28));

        // Encabezado
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 6));
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("THE WORLD'S HARDEST GAME - JAVA EDITION", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(new Color(92, 175, 255));

        bestRecordLabel = new JLabel("Cargando mejor récord...", SwingConstants.CENTER);
        bestRecordLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        bestRecordLabel.setForeground(new Color(255, 210, 75));

        headerPanel.add(titleLabel);
        headerPanel.add(bestRecordLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Centro: Reglas (izquierda) + Tabla de Récords (derecha)
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setOpaque(false);

        // Panel de Reglas
        JPanel rulesCard = new JPanel(new BorderLayout(0, 10));
        rulesCard.setBackground(new Color(35, 39, 50));
        rulesCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(75, 85, 110)),
                new EmptyBorder(16, 18, 16, 18)
        ));

        JLabel rulesTitle = new JLabel("REGLAS DEL JUEGO");
        rulesTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        rulesTitle.setForeground(Color.WHITE);

        JTextArea rulesArea = new JTextArea(
                "1. Objetivo: Llega a la zona META en cada nivel superando la dificultad progresiva.\n\n" +
                        "2. Llaves y Pistas: Cada nivel tiene llaves doradas. Lee la pista superior para ubicarlas; la META se desbloquea al recogerlas todas.\n\n" +
                        "3. Enemigos: Si tocas un enemigo rojo, vuelves al inicio del nivel y se suma +1 muerte.\n\n" +
                        "4. Controles: Muévete con WASD o Flechas. Presiona R para reiniciar el nivel actual.\n\n" +
                        "5. Récord de Tiempo: Al completar todos los niveles se registra tu tiempo total y muertes en Neon. El mejor récord es el menor tiempo."
        );
        rulesArea.setEditable(false);
        rulesArea.setFocusable(false);
        rulesArea.setLineWrap(true);
        rulesArea.setWrapStyleWord(true);
        rulesArea.setBackground(new Color(35, 39, 50));
        rulesArea.setForeground(new Color(220, 224, 235));
        rulesArea.setFont(new Font("SansSerif", Font.PLAIN, 13));

        rulesCard.add(rulesTitle, BorderLayout.NORTH);
        rulesCard.add(rulesArea, BorderLayout.CENTER);

        // Panel de Récords desde Neon
        JPanel recordsCard = new JPanel(new BorderLayout(0, 10));
        recordsCard.setBackground(new Color(35, 39, 50));
        recordsCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(75, 85, 110)),
                new EmptyBorder(16, 18, 16, 18)
        ));

        JLabel recordsTitle = new JLabel("TOP 5 MEJORES TIEMPOS (NEON)");
        recordsTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        recordsTitle.setForeground(Color.WHITE);

        String[] columns = {"#", "Jugador", "Tiempo (s)", "Muertes"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable recordsTable = new JTable(tableModel);
        recordsTable.setBackground(new Color(45, 50, 64));
        recordsTable.setForeground(Color.WHITE);
        recordsTable.setRowHeight(28);
        recordsTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        recordsTable.getTableHeader().setBackground(new Color(28, 31, 40));
        recordsTable.getTableHeader().setForeground(new Color(255, 210, 75));
        recordsTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(recordsTable);
        scrollPane.getViewport().setBackground(new Color(45, 50, 64));

        recordsCard.add(recordsTitle, BorderLayout.NORTH);
        recordsCard.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(rulesCard);
        centerPanel.add(recordsCard);
        add(centerPanel, BorderLayout.CENTER);

        // Pie: Botones de acción
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 4));
        bottomPanel.setOpaque(false);

        JButton refreshButton = new JButton("Actualizar Récords");
        refreshButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        refreshButton.setBackground(new Color(65, 72, 92));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> refreshRecords());

        JButton playButton = new JButton("JUGAR AHORA");
        playButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        playButton.setBackground(new Color(72, 209, 120));
        playButton.setForeground(new Color(20, 24, 30));
        playButton.setFocusPainted(false);
        playButton.setPreferredSize(new Dimension(180, 40));
        playButton.addActionListener(e -> onStartGame.run());

        bottomPanel.add(refreshButton);
        bottomPanel.add(playButton);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshRecords();
    }

    public final void refreshRecords() {
        tableModel.setRowCount(0);
        List<TimeRecord> topRecords = dbManager.getTopRecords(5);

        if (topRecords.isEmpty()) {
            bestRecordLabel.setText("MEJOR RÉCORD: Aún no hay registros en Neon (o revisa la conexión)");
            return;
        }

        TimeRecord best = topRecords.get(0);
        bestRecordLabel.setText(String.format(
                "MEJOR RÉCORD: %s — %.2fs (%d muertes)",
                best.getPlayerName(), best.getElapsedSeconds(), best.getDeaths()
        ));

        int rank = 1;
        for (TimeRecord r : topRecords) {
            tableModel.addRow(new Object[]{
                    rank++,
                    r.getPlayerName(),
                    String.format("%.2f", r.getElapsedSeconds()),
                    r.getDeaths()
            });
        }
    }
}