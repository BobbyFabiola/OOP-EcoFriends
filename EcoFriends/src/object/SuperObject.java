package object;

import entity.Player;
import main.GamePanel;

import java.awt.image.BufferedImage;

public class SuperObject {
    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    private boolean falling;                                                                                            //property to indicate whether the object is falling
    private final int scoreValue;                                                                                             //associated with corn

    public SuperObject (int scoreValue) {
        this.scoreValue = scoreValue;
    }

    public int getScoreValue() {
        return scoreValue;
    }

    public void setFalling(boolean falling, GamePanel gp) {
        this.falling = falling;

        if (falling) {
            worldX = (int) (Math.random() * gp.getScreenWidth());
            worldY = -gp.tileSize - (int) (Math.random() * gp.tileSize * 5);
        }
    }

    public void resetPosition(GamePanel gp) {
        double screenWidth;
        worldX = (int) (Math.random() * gp.getScreenWidth());
        worldY = -gp.tileSize - (int) (Math.random() * gp.tileSize * 5);
        setFalling (false, gp);
    }

    public boolean checkCollision(Player player, int yCoordinate) {

    }

    //GETTERS FOR OBJECT BOUNDS
    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public int getX() {
        return worldX;
    }

    public int getY() {
        return worldY;
    }
}
