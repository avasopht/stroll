package org.konelabs.stroll.gui.screens;

import org.konelabs.stroll.graphics.GBABitmap;
import org.konelabs.stroll.graphics.GBAGraphics;
import org.konelabs.stroll.graphics.GBALayer;
import org.konelabs.stroll.gui.GuiManager;
import org.konelabs.stroll.gui.GuiMessage;
import org.konelabs.stroll.gui.Screen;
import org.konelabs.stroll.system.ConfigLoader;
import org.konelabs.stroll.system.Input;

public class TitleScreen extends Screen {

  public TitleScreen(GuiManager guiManager) {
    super(guiManager);

    GBALayer background;

    background = GBAGraphics.getInstance().getLayer(0);

    background.setMode(GBALayer.Mode.BITMAP_MODE, GBAGraphics.DEFAULT_WIDTH,
        GBAGraphics.DEFAULT_HEIGHT);

    background.setVisible(true);

    try {
      GBABitmap bitmap = ConfigLoader
          .loadBitmap("090907-stroll-title-screen.png");
      for (int i = 0; i < bitmap.buffer.length; ++i) {
        background.screenBuffer[i] = bitmap.buffer[i];
      }
    } catch (Exception e) {
    }
  }

  public boolean handleMessage(GuiMessage message) {
    switch (message.messageType) {
    case BUTTON_DOWN:
      if (0 != (Input.getPressedButtons() & Input.BUTTON_START)) {
        getGuiManager().openScreen(new PlayGameScreen(getGuiManager()));
        return true;
      }
      break;
    }

    return false;
  }

  public void closeScreen() {
    GBAGraphics.getInstance().getLayer(0).setVisible(false);
  }

}
