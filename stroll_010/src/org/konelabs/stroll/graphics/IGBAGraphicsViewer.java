package org.konelabs.stroll.graphics;

/**
 * IScreenViewers will be notified of changes to the screen and will be provided
 * with the screen's buffer to associate with itself
 */
public interface IGBAGraphicsViewer {
    /**
     * instructs the screen viewer to register the buffer associated with the
     * screen. When the screen is updated it is this buffer that will be updated
     *
     * @param buffer
     */
    void registerBuffer(int[] buffer);

    /**
     * Is called when the screen has been updated and is ready to be drawn
     */
    void screenUpdated();
}
