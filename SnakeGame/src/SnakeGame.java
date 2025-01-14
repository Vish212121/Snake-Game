import javax.swing.*; // For JFrame and JPanel

public class SnakeGame {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Snake Game");
        GamePanel panel = new GamePanel();
        frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
