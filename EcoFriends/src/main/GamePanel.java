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
    final int maxScreenCol = 25;                                                                                        //16
    final int maxScreenRow = 12;                                                                                        //12
    final int screenWidth = tileSize * maxScreenCol;                                                                    //768 pixels
    final int screenHeight = tileSize * maxScreenRow;                                                                   //576 pixels

    private BufferedImage backgroundImage;

    //FPS
    int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;                                                                                                  //keeps program running until you stop it
    Player player = new Player(this, keyH);

    private int scoreValue;
    private  int cornsCaught = 0;
    private  int cornsMissed = 0;

    //OBJECTS
    public AssetSetter aSetter = new AssetSetter (this);
    public SuperObject obj[] = new SuperObject[3];                                                                      //means we can display up to 10 objects at the same time

    public int getScreenWidth() {
        return screenWidth;
    }

    public GamePanel () {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/player/images/background.png"));      //background setting
        } catch (IOException e) {
            e.printStackTrace();
        }

        aSetter.setObject();                                                                                            //call to initialize objects
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

                int collisionYCoordinate = player.getY() + player.getHeight();;                                                                         // Change this to the desired y-coordinate
                if (corn.checkCollision(player, collisionYCoordinate)) {                                               // Check if corn collides with player at a specific y-coordinate
                    corn.setFalling(false, this);
                    setCornsCaught(corn);
                }

                if (corn.worldY > screenHeight) {
                    corn.resetPosition(this);                                                                       // Reset corn position when off the screen
                    setCornsMissed();
                }
            }
        }
    }

    private void setCornsCaught (SuperObject corn) {
        cornsCaught = cornsCaught + 1;
        System.out.println("Corns Caught: " + cornsCaught);
    }

    private void setCornsMissed () {
        cornsMissed = cornsMissed + 1;
        System.out.println("Corns Missed: " + cornsMissed);
    }

    public void paintComponent (Graphics g) {                                                                           //built in method
        super.paintComponent(g);

        Graphics g2 = (Graphics2D)g;

        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        player.draw(g2);

        //DRAW CORN OBJECTS W/ SCALING
        for (SuperObject corn: obj) {
            if (corn != null) {
                int scaledWidth = tileSize;                                                                             // Adjust the scaling factor as needed
                int scaledHeight = tileSize;                                                                            // Adjust the scaling factor as needed
                g2.drawImage(corn.image, corn.worldX, corn.worldY,scaledWidth, scaledHeight, null);
            }
        }

        g2.dispose();
    }
}
