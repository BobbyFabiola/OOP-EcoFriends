package entity;                                                                                                         //superclass for all entities

import java.awt.image.BufferedImage;

public class Entity {
    public int x, y;
    public int speed;

    public BufferedImage left, right, front;
    public String direction;
}
