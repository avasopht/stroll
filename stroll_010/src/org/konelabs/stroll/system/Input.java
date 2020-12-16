package org.konelabs.stroll.system;

public final class Input {
    private static int lastInput;
    private static int currentInput;
    protected static int inputRegister;

    public final static int KEY_UP = 0x001, KEY_RIGHT = 0x002, KEY_DOWN = 0x004,
            KEY_LEFT = 0x008, BUTTON_A = 0x010, BUTTON_B = 0x020, BUTTON_X = 0x040,
            BUTTON_Y = 0x080, BUTTON_L = 0x100, BUTTON_R = 0x200,
            BUTTON_START = 0x400, BUTTON_SELECT = 0x800;

    public final static int ALL_KEYS = KEY_UP | KEY_RIGHT | KEY_DOWN | KEY_LEFT;
    public final static int ALL_BUTTONS = BUTTON_A | BUTTON_B | BUTTON_X
            | BUTTON_Y | BUTTON_L | BUTTON_R | BUTTON_START | BUTTON_SELECT;

    public static void initialize() {
        lastInput = 0;
        currentInput = 0;
        inputRegister = 0;
    }

    public static int getPressedKeys() {
        return ((~lastInput & currentInput) & ALL_KEYS);
    }

    public static int getHeldKeys() {
        return ((lastInput & currentInput) & ALL_KEYS);
    }

    public static int getReleasedKeys() {
        return ((lastInput & ~currentInput) & ALL_KEYS);
    }

    public static int getPressedButtons() {
        return ((~lastInput & currentInput) & ALL_BUTTONS);
    }

    public static int getReleasedButtons() {
        return ((lastInput & ~currentInput) & ALL_BUTTONS);
    }

    public static int getHeldButtons() {
        return ((lastInput & currentInput) & ALL_BUTTONS);
    }

    public static void updateKeys() {
        lastInput = currentInput;
        currentInput = inputRegister;
    }
}
