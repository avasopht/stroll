package org.konelabs.stroll.gui;

abstract public class Screen implements MessageHandler {
  private GuiManager guiManager;

  protected Screen(GuiManager gui) {
    guiManager = gui;
  }

  public GuiManager getGuiManager() {
    return guiManager;
  }

  final void update() {
    handleFrame();
  }

  /**
   * Called when the page needs to be closed. The function is expected to close
   * all resources by the end of this method.
   */
  public void closePage() {
  }

  /**
   * Called once per frame at the beginning of update.
   */
  public void handleFrame() {
  }
}
