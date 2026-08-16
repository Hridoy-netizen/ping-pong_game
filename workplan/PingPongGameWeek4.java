import javax.swing.*;
import java.awt.*;

public class PingPongGameWeek4 extends JFrame {

    public PingPongGameWeek4() {
        setTitle("Ping Pong Game - Week 4 UI Layout & Window Development");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        GamePanel gamePanel = new GamePanel();
        add(gamePanel);
    }

    private static class GamePanel extends JPanel {
        private int player1Score = 0;
        private int player2Score = 0;
        private String gameStatus = "PRESS SPACE TO START / PAUSE";

        public GamePanel() {
            setBackground(new Color(20, 24, 33));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(new Color(255, 255, 255, 80));
            g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{12}, 0));
            g2d.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());

            g2d.setFont(new Font("Consolas", Font.BOLD, 48));
            g2d.setColor(Color.WHITE);
            g2d.drawString(String.valueOf(player1Score), getWidth() / 2 - 100, 70);
            g2d.drawString(String.valueOf(player2Score), getWidth() / 2 + 70, 70);

            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.setColor(new Color(180, 180, 180));
            g2d.drawString("PLAYER 1", getWidth() / 2 - 115, 25);
            g2d.drawString("PLAYER 2", getWidth() / 2 + 55, 25);

            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.setColor(new Color(241, 196, 15));
            int statusWidth = g2d.getFontMetrics().stringWidth(gameStatus);
            g2d.drawString(gameStatus, (getWidth() - statusWidth) / 2, getHeight() - 30);

            g2d.setColor(new Color(52, 152, 219));
            g2d.fillRoundRect(30, 230, 15, 100, 8, 8);

            g2d.setColor(new Color(231, 76, 60));
            g2d.fillRoundRect(getWidth() - 45, 230, 15, 100, 8, 8);

            g2d.setColor(Color.WHITE);
            g2d.fillOval(getWidth() / 2 - 10, getHeight() / 2 - 10, 20, 20);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PingPongGameWeek4 game = new PingPongGameWeek4();
            game.setVisible(true);
        });
    }
}