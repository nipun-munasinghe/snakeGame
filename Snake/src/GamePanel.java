import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    Image background;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        background = new ImageIcon("Snake/background.jpg").getImage();
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        //Draw background
        g.drawImage(background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, this);

        if (running) {
            //Draw apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            //Draw snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green); // Head
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(34, 139, 34)); // Body
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            //Display the score
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 35));
            String scoreText = "Score: " + applesEaten;
            g.drawString(scoreText, 10, 40);

        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        //Check if head collides with body
        for(int i = bodyParts; i > 0; i--) {
            if((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        //Check if head touches borders
        if(x[0] < 0 || x[0] >= SCREEN_WIDTH || y[0] < 0 || y[0] >= SCREEN_HEIGHT) {
            running = false;
        }

        if(!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        //Game Over background
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        //Game Over Text
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        String gameOverText = "Game Over!";
        g.drawString(gameOverText, (SCREEN_WIDTH - g.getFontMetrics().stringWidth(gameOverText)) / 2, SCREEN_HEIGHT / 3);

        //Display final score
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        String scoreText = "Your Total Score: " + applesEaten;
        g.drawString(scoreText, (SCREEN_WIDTH - g.getFontMetrics().stringWidth(scoreText)) / 2, SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> {
                    if(direction != 'R') {
                        direction = 'L';
                    }
                }
                case KeyEvent.VK_RIGHT -> {
                    if(direction != 'L') {
                        direction = 'R';
                    }
                }
                case KeyEvent.VK_UP -> {
                    if(direction != 'D') {
                        direction = 'U';
                    }
                }
                case KeyEvent.VK_DOWN -> {
                    if(direction != 'U') {
                        direction = 'D';
                    }
                }
            }
        }
    }
}