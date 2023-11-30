package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static javax.imageio.ImageIO.read;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;

    private int playerWidth;
    private int playerHeight;

    public Player (GamePanel gp, KeyHandler keyH) {                                                                     //instantiated in GamePanel class
        this.gp = gp;
        this.keyH = keyH;

        setDefaultValues();
        getPlayerImage();

        playerWidth = gp.tileSize * 2;
        playerHeight = gp.tileSize * 2;

        solidArea = new Rectangle(8, 16, 32, 32);                                                //solid area for player
    }

    public void setDefaultValues () {
        //SET PLAYER DEFAULT VALUES
        x = 560;                                                                                                        //HARD CODED
        y = 430;
        speed = 8;                                                                                                      //originally 4, but just wanted to see it slow down
        direction = "front";                                                                                            //for default direction
    }

    public void getPlayerImage () {
        try {
            left = ImageIO.read(getClass().getResourceAsStream("/player/images/bara_left.png"));
            right = ImageIO.read(getClass().getResourceAsStream("/player/images/bara_right.png"));
            front = ImageIO.read(getClass().getResourceAsStream("/player/images/bara_front.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void update () {                                                                                             //player movement
        if (keyH.leftPressed) {
            direction = "left";
            x -= speed;
        } else if (keyH.rightPressed) {
            direction = "right";
            x += speed;
        } else {
            direction = "front";
        }

        x = Math.max(0, Math.min(x, gp.getWidth() - (gp.tileSize * 2)));                                                //keeps player within the screen
        y = Math.max(0, Math.min(y, gp.getHeight() - (gp.tileSize * 2)));
    }

    public void draw (Graphics g2) {
        BufferedImage image = null;

        switch (direction) {
            case "left":
                image = left;
                break;

            case "right":
                image = right;
                break;

            default:
                image = front;
                break;
        }

        g2.drawImage(image, x, y, playerWidth, playerHeight, null);                                             //only for player
    }
<<<<<<< HEAD

    public Double getScaleFactor() {
        return 1.0;
    }

    //GETTERS FOR OBJECT BOUNDS
    public int getWidth() {
        return playerWidth;
    }

    public int getHeight() {
        return playerHeight;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
=======
>>>>>>> parent of bdd05a8 (Falling corn!)
}
