package org.konelabs.stroll.gui;

public interface Screen extends MessageHandler {

    /**
     * Called when the page needs to be closed. The function is expected to close
     * all resources by the end of this method.
     */
    void closePage();

    /**
     * Called once per frame at the beginning of update.
     */
    void handleFrame();
}
