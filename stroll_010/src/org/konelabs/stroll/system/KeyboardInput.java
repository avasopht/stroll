package org.konelabs.stroll.system;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {

  public void keyReleased(KeyEvent e) {
    switch (e.getKeyCode()) {
    case KeyEvent.VK_X:
      Input.inputRegister = (Input.inputRegister & ~Input.BUTTON_A);
      break;
    case KeyEvent.VK_Z:
      Input.inputRegister = (Input.inputRegister & ~Input.BUTTON_B);
      break;
    case KeyEvent.VK_ENTER:
      Input.inputRegister = (Input.inputRegister & ~Input.BUTTON_START);
      break;
    case KeyEvent.VK_UP:
      Input.inputRegister = (Input.inputRegister & ~Input.KEY_UP);
      break;
    case KeyEvent.VK_RIGHT:
      Input.inputRegister = (Input.inputRegister & ~Input.KEY_RIGHT);
      break;
    case KeyEvent.VK_DOWN:
      Input.inputRegister = (Input.inputRegister & ~Input.KEY_DOWN);
      break;
    case KeyEvent.VK_LEFT:
      Input.inputRegister = (Input.inputRegister & ~Input.KEY_LEFT);
      break;
    }
  }

  public void keyPressed(KeyEvent e) {
    switch (e.getKeyCode()) {
    case KeyEvent.VK_X:
      Input.inputRegister = (Input.inputRegister | Input.BUTTON_A);
      break;
    case KeyEvent.VK_Z:
      Input.inputRegister = (Input.inputRegister | Input.BUTTON_B);
      break;
    case KeyEvent.VK_ENTER:
      Input.inputRegister = (Input.inputRegister | Input.BUTTON_START);
      break;
    case KeyEvent.VK_UP:
      Input.inputRegister = (Input.inputRegister | Input.KEY_UP);
      break;
    case KeyEvent.VK_RIGHT:
      Input.inputRegister = (Input.inputRegister | Input.KEY_RIGHT);
      break;
    case KeyEvent.VK_DOWN:
      Input.inputRegister = (Input.inputRegister | Input.KEY_DOWN);
      break;
    case KeyEvent.VK_LEFT:
      Input.inputRegister = (Input.inputRegister | Input.KEY_LEFT);
      break;
    }
  }

  public void keyTyped(KeyEvent e) {
  }

}
