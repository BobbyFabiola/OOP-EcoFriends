package object;

import java.awt.image.BufferedImage;

public class SuperObject {
    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    private boolean falling;                                                                                            //property to indicate whether the object is falling

    public void setFalling(boolean falling) {
        this.falling = falling;
    }
}
