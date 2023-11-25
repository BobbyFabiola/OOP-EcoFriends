package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 16;
    final int scale = 3;

    final int tileSize = originalTileSize * scale;                                                                      //48x48 tile
    final int maxScreenCol = 25;                                                                                        //16
    final int maxScreenRow = 12;                                                                                        //12
    final int screenWidth = tileSize * maxScreenCol;                                                                    //768 pixels
    final int screenHeight = tileSize * maxScreenRow;                                                                   //576 pixels

    //FPS
    int FPS = 30;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;                                                                                                  //keeps program running until you stop it

    //set player's default position
    int playerX = (screenWidth - tileSize) / 2;
    int playerY = 450;
    int playerSpeed = 4;

    public GamePanel () {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread () {                                                                                    //instantiating the thread

        gameThread = new Thread(this);
        gameThread.start ();
    }

    @Override
    public void run () {                                                                                                //the game loop, core of the game; sleep method
//        long currentTime = System.nanoTime();                                                                           //checks current system time
        double drawInterval = (double) 100000000 /FPS;                                                                  //1 sec / 60 FPS; we draw the screen 60x / sec
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            update();
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime /1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }                                                                                                                   //for everytime we run a game thread, it automatically calls the run method

    public void update () {                                                                                             //animal movement
        if (keyH.leftPressed) {
            playerX -= playerSpeed;
        } else if (keyH.rightPressed) {
            playerX += playerSpeed;
        }

        playerX = Math.max(0, Math.min(playerX, screenWidth - tileSize));                                               //keeps player within the screen
        playerY = Math.max(0, Math.min(playerY, screenHeight - tileSize));
    }

    public void paintComponent (Graphics g) {                                                                           //built in method
        super.paintComponent(g);

        Graphics g2 = (Graphics2D)g;
        g2.setColor(Color.WHITE);
        g2.fillRect(playerX, playerY,tileSize, tileSize);                                                               //x, y, width, height

        g2.dispose();
    }
}
