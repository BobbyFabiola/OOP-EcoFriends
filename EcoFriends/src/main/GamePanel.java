package main;

import entity.Player;

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
    }

    public void startGameThread () {                                                                                    //instantiating the thread
        gameThread = new Thread(this);
        gameThread.start ();
    }

    @Override
    public void run () {                                                                                                //the game loop, core of the game; sleep method
//        long currentTime = System.nanoTime();                                                                         //checks current system time
        double drawInterval = (double) 100000000 /FPS;                                                                  //1 sec / 60 FPS; we draw the screen 60x / sec
        double nextDrawTime = System.nanoTime() + drawInterval;

//        while (gameThread != null) {
//            update();
//            repaint();
//
//            try {
//                double remainingTime = nextDrawTime - System.nanoTime();
//                remainingTime = remainingTime /1000000;
//
//                if (remainingTime < 0) {
//                    remainingTime = 0;
//                }
//                Thread.sleep((long) remainingTime);
//
//                nextDrawTime += drawInterval;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);                             //replaces the while loop

        executor.scheduleAtFixedRate(() -> {
            update();
            repaint();
        }, 0, (long) (1000.0 / FPS), TimeUnit.MILLISECONDS);
    }                                                                                                                   //for everytime we run a game thread, it automatically calls the run method

    public void update () {                                                                                             //animal movement
        player.update();
    }

    public void paintComponent (Graphics g) {                                                                           //built in method
        super.paintComponent(g);

        Graphics g2 = (Graphics2D)g;

        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        player.draw(g2);
        g2.dispose();
    }
}
