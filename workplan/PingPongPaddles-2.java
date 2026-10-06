import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class PingPongPaddles extends JPanel {

    private final int PANEL_WIDTH = 800;
    private final int PANEL_HEIGHT = 500;

    private final int PADDLE_WIDTH = 15;
    private final int PADDLE_HEIGHT = 100;
    private final int PADDLE_SPEED = 6;
    private final int AI_PADDLE_SPEED = 4;

    private int p1X = 30;
    private int p1Y = PANEL_HEIGHT / 2 - PADDLE_HEIGHT / 2;

    private int p2X = PANEL_WIDTH - 30 - PADDLE_WIDTH;
    private int p2Y = PANEL_HEIGHT / 2 - PADDLE_HEIGHT / 2;

    private final int BALL_SIZE = 15;
    private int ballX = PANEL_WIDTH / 2 - BALL_SIZE / 2;
    private int ballY = PANEL_HEIGHT / 2 - BALL_SIZE / 2;
    private int ballSpeedX = 5;
    private int ballSpeedY = 4;

    private int player1Score = 0;
    private int player2Score = 0;
    private boolean isPaused = false;
    private boolean gameOver = false;
    private final int WINNING_SCORE = 10;

    private final Set<Integer> pressedKeys = new HashSet<>();

    public PingPongPaddles() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(new Color(20, 24, 30));
        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();

                if (code == KeyEvent.VK_SPACE && !gameOver) {
                    isPaused = !isPaused;
                }

                if (code == KeyEvent.VK_R) {
                    restartGame();
                }

                pressedKeys.add(code);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
            }
        });

        Timer timer = new Timer(14, e -> {
            try {
                if (!isPaused && !gameOver) {
                    updatePaddlePositions();
                    updateAIPaddle();
                    updateBallPosition();
                }
                repaint();
            } catch (Exception ex) {
                restartGame();
            }
        });
        timer.start();
    }

    private void updatePaddlePositions() {
        if (pressedKeys.contains(KeyEvent.VK_W)) {
            p1Y -= PADDLE_SPEED;
        }
        if (pressedKeys.contains(KeyEvent.VK_S)) {
            p1Y += PADDLE_SPEED;
        }

        p1Y = clamp(p1Y, 0, PANEL_HEIGHT - PADDLE_HEIGHT);
    }

    private void updateAIPaddle() {
        int paddleCenter = p2Y + (PADDLE_HEIGHT / 2);
        int ballCenter = ballY + (BALL_SIZE / 2);

        if (ballSpeedX > 0) {
            if (paddleCenter < ballCenter - 10) {
                p2Y += AI_PADDLE_SPEED;
            } else if (paddleCenter > ballCenter + 10) {
                p2Y -= AI_PADDLE_SPEED;
            }
        }
        p2Y = clamp(p2Y, 0, PANEL_HEIGHT - PADDLE_HEIGHT);
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private void updateBallPosition() {
        ballX += ballSpeedX;
        ballY += ballSpeedY;

        if (ballY <= 0) {
            ballY = 0;
            ballSpeedY = -ballSpeedY;
        } else if (ballY >= PANEL_HEIGHT - BALL_SIZE) {
            ballY = PANEL_HEIGHT - BALL_SIZE;
            ballSpeedY = -ballSpeedY;
        }

        Rectangle ballRect = new Rectangle(ballX, ballY, BALL_SIZE, BALL_SIZE);
        Rectangle p1Rect = new Rectangle(p1X, p1Y, PADDLE_WIDTH, PADDLE_HEIGHT);
        Rectangle p2Rect = new Rectangle(p2X, p2Y, PADDLE_WIDTH, PADDLE_HEIGHT);

        if (ballRect.intersects(p1Rect) && ballSpeedX < 0) {
            ballX = p1X + PADDLE_WIDTH;
            ballSpeedX = -ballSpeedX;
            ballSpeedY += bounceAngleAdjust(ballY, p1Y);
        }

        if (ballRect.intersects(p2Rect) && ballSpeedX > 0) {
            ballX = p2X - BALL_SIZE;
            ballSpeedX = -ballSpeedX;
            ballSpeedY += bounceAngleAdjust(ballY, p2Y);
        }

        if (ballX < 0) {
            player2Score++;
            checkWinCondition();
            resetBall(1);
        } else if (ballX > PANEL_WIDTH - BALL_SIZE) {
            player1Score++;
            checkWinCondition();
            resetBall(-1);
        }
    }

    private void checkWinCondition() {
        if (player1Score >= WINNING_SCORE || player2Score >= WINNING_SCORE) {
            gameOver = true;
        }
    }

    private int bounceAngleAdjust(int ballY, int paddleY) {
        int hitPosition = ballY - paddleY;
        if (hitPosition < PADDLE_HEIGHT / 3) {
            return -1;
        } else if (hitPosition > (2 * PADDLE_HEIGHT) / 3) {
            return 1;
        }
        return 0;
    }

    private void resetBall(int directionX) {
        ballX = PANEL_WIDTH / 2 - BALL_SIZE / 2;
        ballY = PANEL_HEIGHT / 2 - BALL_SIZE / 2;
        ballSpeedX = directionX * 5;
        ballSpeedY = 3;
    }

    private void restartGame() {
        player1Score = 0;
        player2Score = 0;
        isPaused = false;
        gameOver = false;
        p1Y = PANEL_HEIGHT / 2 - PADDLE_HEIGHT / 2;
        p2Y = PANEL_HEIGHT / 2 - PADDLE_HEIGHT / 2;
        resetBall(1);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(new Color(255, 255, 255, 60));
        for (int y = 0; y < PANEL_HEIGHT; y += 20) {
            g2d.fillRect(PANEL_WIDTH / 2 - 1, y, 2, 10);
        }

        g2d.setColor(new Color(52, 152, 219));
        g2d.fillRect(p1X, p1Y, PADDLE_WIDTH, PADDLE_HEIGHT);

        g2d.setColor(new Color(231, 76, 60));
        g2d.fillRect(p2X, p2Y, PADDLE_WIDTH, PADDLE_HEIGHT);

        g2d.setColor(new Color(241, 196, 15));
        g2d.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

        g2d.setFont(new Font("Consolas", Font.BOLD, 36));
        g2d.setColor(new Color(52, 152, 219));
        g2d.drawString(String.valueOf(player1Score), PANEL_WIDTH / 4, 50);

        g2d.setColor(new Color(231, 76, 60));
        g2d.drawString(String.valueOf(player2Score), (3 * PANEL_WIDTH) / 4, 50);

        if (gameOver) {
            g2d.setFont(new Font("Arial", Font.BOLD, 40));
            String resultText = (player1Score >= WINNING_SCORE) ? "You Win!" : "Game Over! (AI Wins)";
            g2d.setColor((player1Score >= WINNING_SCORE) ? new Color(52, 152, 219) : new Color(231, 76, 60));
            
            FontMetrics metrics = g2d.getFontMetrics();
            int x = (PANEL_WIDTH - metrics.stringWidth(resultText)) / 2;
            g2d.drawString(resultText, x, PANEL_HEIGHT / 2 - 20);

            g2d.setFont(new Font("Arial", Font.PLAIN, 20));
            g2d.setColor(Color.WHITE);
            String restartText = "Press 'R' to Restart";
            metrics = g2d.getFontMetrics();
            int rx = (PANEL_WIDTH - metrics.stringWidth(restartText)) / 2;
            g2d.drawString(restartText, rx, PANEL_HEIGHT / 2 + 30);
        } else if (isPaused) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 40));
            String pauseText = "PAUSED";
            FontMetrics metrics = g2d.getFontMetrics();
            int x = (PANEL_WIDTH - metrics.stringWidth(pauseText)) / 2;
            g2d.drawString(pauseText, x, PANEL_HEIGHT / 2);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Ping Pong Game");
            PingPongPaddles gamePanel = new PingPongPaddles();

            frame.add(gamePanel);
            frame.pack();
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}