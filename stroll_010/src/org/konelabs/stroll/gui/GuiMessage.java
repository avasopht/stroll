package org.konelabs.stroll.gui;

/**
 * A gui message is sent around to <b>Screen</b>s and [eventually]
 * <b>GuiObj</b>'s
 */
public class GuiMessage {
    public enum MessageType {
        ON_KEY_DOWN, ON_KEY_UP,

        ON_BUTTON_DOWN, ON_BUTTON_UP,

        ON_CURSOR_UP, ON_CURSOR_DOWN, ON_CURSOR_MOVE
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
        return switch (messageType) {
            case ON_CURSOR_UP, ON_CURSOR_DOWN, ON_CURSOR_MOVE -> param1;
            default -> throw new RuntimeException("Method invalid for given type");
        };
    }

    public int getY() {
        return switch (messageType) {
            case ON_CURSOR_UP, ON_CURSOR_DOWN, ON_CURSOR_MOVE -> param2;
            default -> throw new RuntimeException("Method invalid for given type");
        };
    }

    public int getKeys() {
        return switch (messageType) {
            case ON_KEY_DOWN, ON_KEY_UP, ON_BUTTON_UP, ON_BUTTON_DOWN -> param1;
            default -> throw new RuntimeException("Method invalid for given type");
        };
    }

    public boolean isAutoRepeat() {
        if (messageType == MessageType.ON_KEY_DOWN) {
            return param2 != 0;
        }
        throw new RuntimeException("Method invalid for given type");
    }

    public static GuiMessage createKeyDownMessage(int keys, boolean isAutoRepeat) {
        int param2;

        if (isAutoRepeat) {
            param2 = 1;
        } else {
            param2 = 0;
        }
        return new GuiMessage(MessageType.ON_KEY_DOWN, keys, param2);
    }

    public static GuiMessage createKeyUpMessage(int keys) {
        return new GuiMessage(MessageType.ON_KEY_UP, keys, 0);
    }

    public static GuiMessage createButtonUpMessage(int keys) {
        return new GuiMessage(MessageType.ON_BUTTON_UP, keys, 0);
    }

    public static GuiMessage createButtonDownMessage(int keys) {
        return new GuiMessage(MessageType.ON_BUTTON_DOWN, keys, 0);
    }

    public static GuiMessage createCursorDownMessage(int x, int y) {
        return new GuiMessage(MessageType.ON_CURSOR_DOWN, x, y);
    }

    public static GuiMessage createCursorUpMessage(int x, int y) {
        return new GuiMessage(MessageType.ON_CURSOR_UP, x, y);
    }

    public static GuiMessage createCursorMoveMessage(int x, int y) {
        return new GuiMessage(MessageType.ON_CURSOR_MOVE, x, y);
    }
}
