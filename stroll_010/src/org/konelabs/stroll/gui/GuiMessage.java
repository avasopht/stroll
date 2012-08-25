package org.konelabs.stroll.gui;

/**
 * A gui message is sent around to <b>Screen</b>s and [eventually]
 * <b>GuiObj</b>'s
 */
public class GuiMessage {
  public enum MessageType {
    KEY_DOWN, KEY_UP,

    BUTTON_DOWN, BUTTON_UP,

    CURSOR_UP, CURSOR_DOWN, CURSOR_MOVE
  }

  int param1;
  int param2;

  public final MessageType messageType;

  private GuiMessage(MessageType messageType_, int param1_, int param2_) {
    messageType = messageType_;
    param1 = param1_;
    param2 = param2_;
  }

  public int getX() {
    switch (messageType) {
    case CURSOR_UP:
      /* Fall through */
    case CURSOR_DOWN:
      /* Fall through */
    case CURSOR_MOVE:
      return param1;
    default:
      throw new RuntimeException("Method invalid for given type");
    }
  }

  public int getY() {
    switch (messageType) {
    case CURSOR_UP:
      /* Fall through */
    case CURSOR_DOWN:
      /* Fall through */
    case CURSOR_MOVE:
      return param2;
    default:
      throw new RuntimeException("Method invalid for given type");
    }
  }

  public int getKeys() {
    switch (messageType) {
    case KEY_DOWN:
      /* Fall through */
    case KEY_UP:
      /* Fall through */
    case BUTTON_UP:
      /* Fall through */
    case BUTTON_DOWN:
      return param1;
    default:
      throw new RuntimeException("Method invalid for given type");
    }
  }

  public boolean isAutoRepeat() {
    switch (messageType) {
    case KEY_DOWN:
      if (param2 == 0) {
        return false;
      } else {
        return true;
      }
    default:
      throw new RuntimeException("Method invalid for given type");
    }
  }

  public static GuiMessage createKeyDownMessage(int keys, boolean isAutoRepeat) {
    int param2;

    if (true == isAutoRepeat) {
      param2 = 1;
    } else {
      param2 = 0;
    }
    return new GuiMessage(MessageType.KEY_DOWN, keys, param2);
  }

  public static GuiMessage createKeyUpMessage(int keys) {
    return new GuiMessage(MessageType.KEY_UP, keys, 0);
  }

  public static GuiMessage createButtonUpMessage(int keys) {
    return new GuiMessage(MessageType.BUTTON_UP, keys, 0);
  }

  public static GuiMessage createButtonDownMessage(int keys) {
    return new GuiMessage(MessageType.BUTTON_DOWN, keys, 0);
  }

  public static GuiMessage createCursorDownMessage(int x, int y) {
    return new GuiMessage(MessageType.CURSOR_DOWN, x, y);
  }

  public static GuiMessage createCursorUpMessage(int x, int y) {
    return new GuiMessage(MessageType.CURSOR_UP, x, y);
  }

  public static GuiMessage createCursorMoveMessage(int x, int y) {
    return new GuiMessage(MessageType.CURSOR_MOVE, x, y);
  }
}
