package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{

    private boolean upPressed , downPressed , rightPressed , leftPressed;

    final private int down = 0;
    final private int right = 1;
    final private int left = 2;
    final private int up = 3;

    private String direction;

    public KeyHandler()
    {

    }

    public boolean getUpstate(){return upPressed;};
    public boolean getDownstate(){return downPressed;};
    public boolean getRightstate(){return rightPressed;};
    public boolean getLeftstate(){return leftPressed;};


    public String getDirection(){return direction;};


    @Override
    public void keyTyped(KeyEvent e){

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch(keyCode)
        {
            case KeyEvent.VK_S: downPressed = true; direction = "down"; break;
            case KeyEvent.VK_A: leftPressed = true; direction = "left"; break;
            case KeyEvent.VK_D: rightPressed = true; direction = "right"; break;
            case KeyEvent.VK_W: upPressed = true; direction = "up"; break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode)
        {
            case KeyEvent.VK_S: downPressed = false; break;
            case KeyEvent.VK_A: leftPressed = false; break;
            case KeyEvent.VK_D: rightPressed = false; break;
            case KeyEvent.VK_W: upPressed = false; break;
        }
    }
}

