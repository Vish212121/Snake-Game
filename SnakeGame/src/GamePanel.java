import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel implements KeyListener, Runnable {
    private final int TILE_SIZE = 20;
    private final int GAME_WIDTH = 800 / TILE_SIZE;
    private final int GAME_HEIGHT = 600 / TILE_SIZE;
    private final List<Point> snake = new ArrayList<>();
    private Point food;
    private int direction = KeyEvent.VK_RIGHT;
    private boolean running = false;
    private boolean paused = false;
    private final Random random = new Random();
    private int score = 0;
    private int gameSpeed = 150;

    public GamePanel() {
        this.setFocusable(true);
        this.addKeyListener(this);
        setDifficulty();
        initGame();
    }

    private void setDifficulty() {
        String[] options = {"Easy", "Medium", "Hard"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Select Difficulty Level:",
                "Snake Game",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 1) gameSpeed = 100;
        if (choice == 2) gameSpeed = 50;
    }

    private void initGame() {
        snake.clear();
        snake.add(new Point(5, 5));
        spawnFood();
        score = 0;
        running = true;
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    private void spawnFood() {
        int x = random.nextInt(GAME_WIDTH);
        int y = random.nextInt(GAME_HEIGHT);
        food = new Point(x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.GREEN);
        for (Point segment : snake) {
            g.fillRect(segment.x * TILE_SIZE, segment.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
        g.setColor(Color.RED);
        g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 20);
        if (!running) {
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Game Over!", getWidth() / 2 - 150, getHeight() / 2);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Final Score: " + score, getWidth() / 2 - 70, getHeight() / 2 + 40);
        }
        if (paused && running) {
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Paused", getWidth() / 2 - 100, getHeight() / 2);
        }
    }

    private void move() {
        if (!running || paused) return;

        Point head = snake.get(0);
        Point newHead = new Point(head);

        switch (direction) {
            case KeyEvent.VK_UP -> newHead.y -= 1;
            case KeyEvent.VK_DOWN -> newHead.y += 1;
            case KeyEvent.VK_LEFT -> newHead.x -= 1;
            case KeyEvent.VK_RIGHT -> newHead.x += 1;
        }

        if (newHead.x < 0 || newHead.x >= GAME_WIDTH || newHead.y < 0 || newHead.y >= GAME_HEIGHT) {
            running = false;
            return;
        }

        if (snake.contains(newHead)) {
            running = false;
            return;
        }

        if (newHead.equals(food)) {
            snake.add(0, newHead);
            spawnFood();
            score += 10;
            if (gameSpeed > 50) gameSpeed -= 5;
        } else {
            snake.add(0, newHead);
            snake.remove(snake.size() - 1);
        }
    }

    @Override
    public void run() {
        while (running) {
            move();
            repaint();
            try {
                Thread.sleep(gameSpeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if ((key == KeyEvent.VK_UP) && direction != KeyEvent.VK_DOWN) {
            direction = KeyEvent.VK_UP;
        }
        if ((key == KeyEvent.VK_DOWN) && direction != KeyEvent.VK_UP) {
            direction = KeyEvent.VK_DOWN;
        }
        if ((key == KeyEvent.VK_LEFT) && direction != KeyEvent.VK_RIGHT) {
            direction = KeyEvent.VK_LEFT;
        }
        if ((key == KeyEvent.VK_RIGHT) && direction != KeyEvent.VK_LEFT) {
            direction = KeyEvent.VK_RIGHT;
        }
        if (key == KeyEvent.VK_P) {
            paused = !paused;
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
