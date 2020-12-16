package org.konelabs.stroll.system;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_X -> Input.inputRegister = (Input.inputRegister & ~Input.BUTTON_A);
            case KeyEvent.VK_Z -> Input.inputRegister = (Input.inputRegister & ~Input.BUTTON_B);
            case KeyEvent.VK_ENTER -> Input.inputRegister = (Input.inputRegister & ~Input.BUTTON_START);
            case KeyEvent.VK_UP -> Input.inputRegister = (Input.inputRegister & ~Input.KEY_UP);
            case KeyEvent.VK_RIGHT -> Input.inputRegister = (Input.inputRegister & ~Input.KEY_RIGHT);
            case KeyEvent.VK_DOWN -> Input.inputRegister = (Input.inputRegister & ~Input.KEY_DOWN);
            case KeyEvent.VK_LEFT -> Input.inputRegister = (Input.inputRegister & ~Input.KEY_LEFT);
        }
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_X -> Input.inputRegister = (Input.inputRegister | Input.BUTTON_A);
            case KeyEvent.VK_Z -> Input.inputRegister = (Input.inputRegister | Input.BUTTON_B);
            case KeyEvent.VK_ENTER -> Input.inputRegister = (Input.inputRegister | Input.BUTTON_START);
            case KeyEvent.VK_UP -> Input.inputRegister = (Input.inputRegister | Input.KEY_UP);
            case KeyEvent.VK_RIGHT -> Input.inputRegister = (Input.inputRegister | Input.KEY_RIGHT);
            case KeyEvent.VK_DOWN -> Input.inputRegister = (Input.inputRegister | Input.KEY_DOWN);
            case KeyEvent.VK_LEFT -> Input.inputRegister = (Input.inputRegister | Input.KEY_LEFT);
        }
    }

    public void keyTyped(KeyEvent e) {
    }

}
