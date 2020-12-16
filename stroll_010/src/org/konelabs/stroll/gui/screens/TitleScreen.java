package org.konelabs.stroll.gui.screens;

import org.konelabs.stroll.graphics.GBABitmap;
import org.konelabs.stroll.graphics.GBAGraphics;
import org.konelabs.stroll.graphics.GBALayer;
import org.konelabs.stroll.gui.GuiManager;
import org.konelabs.stroll.gui.GuiMessage;
import org.konelabs.stroll.gui.Screen;
import org.konelabs.stroll.system.ConfigLoader;
import org.konelabs.stroll.system.Input;

public class TitleScreen implements Screen {
    private final GuiManager guiManager;

    public TitleScreen(GuiManager guiManager) {
        this.guiManager = guiManager;
        GBALayer background;

        background = GBAGraphics.getInstance().getLayer(0);

        background.setMode(GBALayer.Mode.BITMAP_MODE, GBAGraphics.DEFAULT_WIDTH,
                GBAGraphics.DEFAULT_HEIGHT);

        background.setVisible(true);

        try {
            GBABitmap bitmap = ConfigLoader
                    .loadBitmap("090907-stroll-title-screen.png");
            System.arraycopy(bitmap.buffer, 0, background.screenBuffer, 0, bitmap.buffer.length);
        } catch (Exception ignored) {
        }
    }

    public boolean handleMessage(GuiMessage message) {
        if (message.messageType == GuiMessage.MessageType.ON_BUTTON_DOWN) {
            if (0 != (Input.getPressedButtons() & Input.BUTTON_START)) {
                guiManager.openScreen(new PlayGameScreen());
                return true;
            }
        }

        return false;
    }

    public void closeScreen() {
        GBAGraphics.getInstance().getLayer(0).setVisible(false);
    }

    @Override
    public void closePage() {
    }

    @Override
    public void handleFrame() {

    }
}
