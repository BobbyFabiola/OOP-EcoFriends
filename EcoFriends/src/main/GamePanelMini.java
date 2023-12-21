package main;

import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;

public class GamePanelMini extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 1300;
    static final int SCREEN_HEIGHT = 750;
    private BufferedImage backgroundImage;
    private BufferedImage front;
    private BufferedImage player;
    private BufferedImage babeh;
    static final int UNIT_SIZE = 50;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    static final int DELAY = 175;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 1;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    Font minecraft;
    JButton resetButton;
    JButton backButton;

    GamePanelMini() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();

        try {
            backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/images/town.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            front = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/images/trash.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            player = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/images/bara_front.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            babeh = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/images/bara_left.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            InputStream is = getClass().getResourceAsStream("/player/images/Minecraftia-Regular.ttf");
            assert is != null;
            minecraft = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(minecraft);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        resetButton = new JButton(new ImageIcon(getClass().getResource("/player/images/resetbutton.png")));
        resetButton.addActionListener(e -> resetGame());
        resetButton.setFocusable(false);
        resetButton.setVisible(false);

        resetButton.setBounds((SCREEN_WIDTH - 200) / 2, SCREEN_HEIGHT / 2 + 100, 200, 40);

        resetButton.setContentAreaFilled(false);
        resetButton.setBorderPainted(false);

        this.setLayout(null);
        this.add(resetButton);

        backButton = new JButton(new ImageIcon(getClass().getResource("/player/images/backbutton.png")));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Switch to the title screen
                switchToTitleScreen();
            }
        });


        backButton.setFocusable(false);
        backButton.setVisible(true);

        // Set the bounds for the back button to be in the upper left corner
        backButton.setBounds(10, 20, 200, 40); // x, y, width, height

        // Customize the button appearance
        backButton.setContentAreaFilled(false); // Make the button transparent
        backButton.setBorderPainted(false); // Remove the border

        this.setLayout(null); // Use null layout to manually position components
        this.add(backButton);
    }
    private void switchToTitleScreen() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.getContentPane().removeAll();

        TitleScreen titleScreen = new TitleScreen(frame);  // Pass the existing JFrame instance
        titleScreen.switchToTitleScreen();  // Update the switchToTitleScreen method

        frame.setLayout(new BorderLayout());
        frame.add(titleScreen, BorderLayout.CENTER);

        frame.validate();
        frame.repaint();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        draw(g);
    }
    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.red);
            g.drawImage(front, appleX, appleY, UNIT_SIZE, UNIT_SIZE, this);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.drawImage(player, x[i], y[i], UNIT_SIZE, UNIT_SIZE, this);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.drawImage(babeh, x[i], y[i], UNIT_SIZE, UNIT_SIZE, this);
                }
            }
            g.setColor(Color.black);
            g.setFont(new Font("Minecraftia", Font.BOLD, 30));

            String trashCollectedText = "Trash Collected: " + applesEaten;
            g.drawString(trashCollectedText, 920, 60);

        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        while (true) {
            appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
            appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

            boolean onSnake = false;
            for (int i = 0; i < bodyParts; i++) {
                if (x[i] == appleX && y[i] == appleY) {
                    onSnake = true;
                    break;
                }
            }
            if (!onSnake) break;
        }
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
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }

        if (x[0] < 0 || x[0] >= SCREEN_WIDTH || y[0] < 0 || y[0] >= SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
            resetButton.setVisible(true);
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Minecraftia", Font.BOLD, 40));
        String scoreText = "Score: " + applesEaten;
        FontMetrics scoreMetrics = getFontMetrics(g.getFont());
        int scoreX = (SCREEN_WIDTH - scoreMetrics.stringWidth(scoreText)) / 2;
        int scoreY = SCREEN_HEIGHT / 2 - scoreMetrics.getHeight();
        g.drawString(scoreText, scoreX, scoreY);

        g.setColor(Color.red);
        g.setFont(new Font("Minecraftia", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        int gameOverX = (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2;
        int gameOverY = SCREEN_HEIGHT / 2 + metrics2.getHeight();
        g.drawString("Game Over", gameOverX, gameOverY);

        resetButton.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (timer != null && running) {
            move();
            checkApple();
            checkCollisions();
            repaint();
        }
    }

    public void resetGame() {
        running = false;
        bodyParts = 1;
        applesEaten = 0;
        direction = 'R';
        newApple();
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }

        // Stop the existing timer if it is running
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        startGame();

        // Show the reset button only when the game is over
        resetButton.setVisible(!running);
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
