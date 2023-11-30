package entity;                                                                                                         //superclass for all entities

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public int x, y;
    public int speed;

    public BufferedImage left, right, front;
    public String direction;

    public Rectangle solidArea;
    public boolean collisionOn = false;
}
