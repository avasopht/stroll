package org.konelabs.stroll.lang;

/**
 * The IPlayClient interface provides for a mechanism that allows applets to
 * interface with your class in a start/stop safe manor
 */
public interface IPlayClient {
    /**
     * returns true when the client is initialized
     */
    boolean isInitialized();

    /**
     * Informs the client to start/restart. The client may not start if it is not
     * initialized
     */
    boolean start();

    /**
     * Informs the client to stop/pause
     */
    void stop();

    /**
     * Informs the client to cease to exist, system may exit before destroy
     * finishes, but in implementations where the program will not System.exit(0)
     * then it is important that destroy() will close any resources and stop any
     * threads.
     */
    void destroy();
}
