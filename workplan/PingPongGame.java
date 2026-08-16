import javax.swing.*;
import java.awt.*;

public class PingPongGame extends JFrame {

    public PingPongGame() {
        setTitle("Ping Pong Game - Week 3 Environment Test");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        GamePanel gamePanel = new GamePanel();
        add(gamePanel);
    }

    private static class GamePanel extends JPanel {
        public GamePanel() {
            setBackground(Color.BLACK);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(Color.GREEN);
            g.setFont(new Font("Consolas", Font.BOLD, 22));
            g.drawString("Ping Pong Game Environment Setup", 200, 180);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString("Status: Environment Test Successfully Passed!", 220, 230);
            g.drawString("JDK, Swing, AWT & Window Initialization Ready.", 210, 270);

            g.setColor(Color.WHITE);

            for (int i = 0; i < getHeight(); i += 30) {
                g.fillRect(395, i, 10, 15);
            }

            g.fillRect(30, 240, 15, 100);
            g.fillRect(740, 240, 15, 100);
            g.fillOval(388, 280, 20, 20);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PingPongGame game = new PingPongGame();
            game.setVisible(true);
        });
    }
}
