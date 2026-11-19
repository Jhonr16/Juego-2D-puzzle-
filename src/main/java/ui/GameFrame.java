package ui;

import database.DatabaseManager;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private static final String START_VIEW = "START";
    private static final String GAME_VIEW = "GAME";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel containerPanel = new JPanel(cardLayout);
    private final DatabaseManager dbManager = new DatabaseManager();
    private final StartPanel startPanel;

    public GameFrame() {
        setTitle("No Way Out");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        startPanel = new StartPanel(dbManager, this::showGame);
        containerPanel.add(startPanel, START_VIEW);

        add(containerPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void showGame() {
        GamePanel gamePanel = new GamePanel(dbManager, this::showStart);
        containerPanel.add(gamePanel, GAME_VIEW);
        cardLayout.show(containerPanel, GAME_VIEW);
        gamePanel.requestFocusInWindow();
    }

    private void showStart() {
        startPanel.refreshRecords();
        cardLayout.show(containerPanel, START_VIEW);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameFrame::new);
    }
}