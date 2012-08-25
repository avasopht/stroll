package org.konelabs.stroll.game;

import org.konelabs.stroll.gui.GuiMessage;
import org.konelabs.stroll.gui.MessageHandler;
import org.konelabs.stroll.system.Input;
import org.konelabs.stroll.tetris.ATetrion;

public class StrollDriver implements MessageHandler {

  enum Key {
    up, down, left, right, rotateCCW, rotateCW, softDrop, hardDrop, holdPiece
  }

  private int _rightAutoshift, _leftAutoshift;
  private boolean _rotateCW, _rotateCCW, _keyRight, _keyLeft, _keySoftdrop,
      _softdropRelease;

  private static final int _Autorepeat = 2;
  private static final int _Delay = 10;

  // temporary keystates
  private boolean lastDown, lastLeft, lastRight, lastX, lastZ;
  private boolean keyRight, keyDown, keyLeft, keyX, keyZ;

  public StrollDriver() {
    _rightAutoshift = _leftAutoshift = 0;
  }

  public void advanceFrame(ATetrion tetrion) {

    // deal with keys

    if (!lastLeft & keyLeft)
      pressKey(Key.left);
    if (!lastRight & keyRight)
      pressKey(Key.right);
    if (!lastX & keyX)
      pressKey(Key.rotateCW);
    if (!lastZ & keyZ)
      pressKey(Key.rotateCCW);
    if (!lastDown & keyDown)
      pressKey(Key.softDrop);

    if (lastDown & !keyDown)
      releaseKey(Key.softDrop);
    if (lastLeft & !keyLeft)
      releaseKey(Key.left);
    if (lastRight & !keyRight)
      releaseKey(Key.right);

    lastLeft = keyLeft;
    lastRight = keyRight;
    lastDown = keyDown;
    lastX = keyX;
    lastZ = keyZ;

    // first manage key presses
    if (_rotateCW)
      tetrion.rotate(1);
    if (_rotateCCW)
      tetrion.rotate(-1);
    if (_keyRight && (_rightAutoshift <= 0))
      tetrion.move(1);
    if (_keyLeft && (_leftAutoshift <= 0))
      tetrion.move(-1);

    if (_rightAutoshift < 0)
      _rightAutoshift = _Delay;
    if (_leftAutoshift < 0)
      _leftAutoshift = _Delay;

    if (_rightAutoshift == 0)
      _rightAutoshift = _Autorepeat;
    if (_leftAutoshift == 0)
      _leftAutoshift = _Autorepeat;

    if (_keySoftdrop)
      tetrion.setFastDrop(true);
    if (_softdropRelease)
      tetrion.setFastDrop(false);

    _rightAutoshift--;
    _leftAutoshift--;

    // update game state
    tetrion.advanceFrame();

    // clear keystates
    _keySoftdrop = _softdropRelease = _rotateCW = _rotateCCW = false;
  }

  public void pressKey(Key key) {
    switch (key) {
    case rotateCW:
      _rotateCW = true;
      break;
    case rotateCCW:
      _rotateCCW = true;
      break;
    case left:
      _leftAutoshift = -1;
      _keyLeft = true;
      break;
    case right:
      _rightAutoshift = -1;
      _keyRight = true;
      break;
    case softDrop:
      _keySoftdrop = true;
      break;
    }
  }

  public void releaseKey(Key key) {
    switch (key) {
    case left:
      _keyLeft = false;
      break;
    case right:
      _keyRight = false;
      break;
    case softDrop:
      _softdropRelease = true;
      break;
    }
  }

  public boolean handleMessage(GuiMessage message) {
    switch (message.messageType) {
    case BUTTON_DOWN:
      if (0 != (message.getKeys() & Input.BUTTON_A)) {
        keyX = true;
      }

      if (0 != (message.getKeys() & Input.BUTTON_B)) {
        keyZ = true;
      }

      return true;

    case BUTTON_UP:
      if (0 != (message.getKeys() & Input.BUTTON_A)) {
        keyX = false;
      }

      if (0 != (message.getKeys() & Input.BUTTON_B)) {
        keyZ = false;
      }
      return true;

    case KEY_DOWN:
      if (0 != (message.getKeys() & Input.KEY_DOWN)) {
        keyDown = true;
      }

      if (0 != (message.getKeys() & Input.KEY_RIGHT)) {
        keyRight = true;
      }

      if (0 != (message.getKeys() & Input.KEY_LEFT)) {
        keyLeft = true;
      }
      return true;

    case KEY_UP:
      if (0 != (message.getKeys() & Input.KEY_DOWN)) {
        keyDown = false;
      }

      if (0 != (message.getKeys() & Input.KEY_RIGHT)) {
        keyRight = false;
      }

      if (0 != (message.getKeys() & Input.KEY_LEFT)) {
        keyLeft = false;
      }
      return true;

    default:
      return false;
    }
  }
}
