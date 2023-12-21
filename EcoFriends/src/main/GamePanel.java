package main;

import entity.Player;
import object.SuperObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 16;                                                                                    //16
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;                                                               //48x48 tile
    public final int maxScreenCol = 25;                                                                                        //16
    public final int maxScreenRow = 12;                                                                                        //12
    public final int screenWidth = tileSize * maxScreenCol;                                                                    //768 pixels
    public final int screenHeight = tileSize * maxScreenRow;                                                                   //576 pixels

    private BufferedImage backgroundImage;

    //FPS
    int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;                                                                                                  //keeps program running until you stop it
    Player player = new Player(this, keyH);

    //OBJECTS
    public AssetSetter aSetter = new AssetSetter (this);
    public SuperObject obj[] = new SuperObject[3];                                                                      //means we can display up to 10 objects at the same time
    JButton backButton;
    private boolean isBackButtonHovered = false;
    public GamePanel () {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/player/images/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        aSetter.setObject();
        // Initialize the backButton with proper bounds and make it visible
        ImageIcon backIcon = new ImageIcon(getClass().getResource("/player/images/backbutton.png"));
        backButton = new JButton(backIcon);
        backButton.addActionListener(e -> switchToTitleScreen());
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);

        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                isBackButtonHovered = true;
                repaint();
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                isBackButtonHovered = false;
                repaint();
            }
        });

        this.setLayout(null);
        this.add(backButton);
        this.setVisible(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        // Draw player
        player.draw(g2);

        // Draw corn objects
        for (SuperObject corn : obj) {
            if (corn != null) {
                double scaleFactor = player.getScaleFactor();
                int scaledWidth = (int) (tileSize * scaleFactor);
                int scaledHeight = (int) (tileSize * scaleFactor);
                g2.drawImage(corn.image, corn.worldX, corn.worldY, scaledWidth, scaledHeight, this);
            }
        }

        // Draw backButton
        ImageIcon backIcon = new ImageIcon(getClass().getResource("/player/images/backbutton.png"));
        if (isBackButtonHovered) {
            // Draw the button with hover effect
            g2.drawImage(backIcon.getImage(), 20, 0, backIcon.getIconWidth(), backIcon.getIconHeight(), this);
        } else {
            // Draw the button without hover effect
            backButton.setBounds(20, 0, backIcon.getIconWidth(), backIcon.getIconHeight());
            backIcon.paintIcon(this, g2, 20, 0);  // Use paintIcon method
        }

        g2.dispose();

    }

    private void switchToTitleScreen() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.getContentPane().removeAll(); // Remove all components

        TitleScreen titleScreen = new TitleScreen(frame);
        titleScreen.setPreferredSize(new Dimension(screenWidth, screenHeight));


        frame.setLayout(new BorderLayout());
        frame.add(titleScreen, BorderLayout.CENTER);

        frame.validate();
        frame.repaint();
    }

    public void startGameThread () {                                                                                    //instantiating the thread
        gameThread = new Thread(this);
        gameThread.start ();
    }

    @Override
    public void run () {                                                                                                //the game loop, core of the game; sleep method
        double drawInterval = (double) 100000000 /FPS;                                                                  //1 sec / 60 FPS; we draw the screen 60x / sec
        double nextDrawTime = System.nanoTime() + drawInterval;

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);                             //replaces the while loop

        executor.scheduleAtFixedRate(() -> {
            update();
            repaint();
        }, 0, (long) (1000.0 / FPS), TimeUnit.MILLISECONDS);
    }                                                                                                                   //for everytime we run a game thread, it automatically calls the run method

    public void update () {                                                                                             //animal movement
        player.update();
        updateCornObjects ();
    }

    private void updateCornObjects() {
        for (SuperObject corn: obj) {
            if (corn != null) {
                corn.worldY += 3;                                                                                       //falling speed
                if (corn.worldY > screenHeight) {                                                                       //reset corn position when off the screen
                    corn.worldX = (int) (Math.random() * screenWidth);
                    corn.worldY = -tileSize - (int) (Math.random() * tileSize * 3) - 1 * 50;
                }
            }
        }
    }

}
