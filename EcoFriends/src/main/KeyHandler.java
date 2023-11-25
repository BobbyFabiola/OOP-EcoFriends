package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;

public class KeyHandler implements KeyListener {

    public boolean leftPressed, rightPressed;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_A) {                                                                                    //move left
            leftPressed = true;
        }

        if (code == KeyEvent.VK_D) {                                                                                    //move right
            rightPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_A) {                                                                                    //move left
            leftPressed = false;
        }

        if (code == KeyEvent.VK_D) {                                                                                    //move right
            rightPressed = false;
        }
    }
}
