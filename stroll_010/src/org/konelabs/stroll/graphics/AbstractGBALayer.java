package org.konelabs.stroll.graphics;

/**
 * <p>
 * Represents a screen layer. Each screen layer will have their own protocols
 * and methods for data which is best tied in with different implementations of
 * the abstract Screen. So each Screen would be associated with a particular
 * AbstractLayer implementation.
 * </p>
 *
 * <p>
 * Each layer contains an affine transformation matrix in PA, PB, PC and PD; in
 * affine mode these control the mapping of the screen to the background data.
 * Scx and scy are integers, while PA-D, xRef and yRef are in 16:16 fixed point.
 * In affine mode the scx and scy parameters may be omitted depending on the
 * behaviour of the implementing layer.
 * </p>
 */
public abstract class AbstractGBALayer {
    /**
     * priority of the layer, the highest priority is 0, high priorities are drawn
     * first
     */
    private int priority;
    /**
     * stores whether or not the layer is visible
     */
    private boolean isVisible;
    /**
     * stores whether or not the layer is affine
     */
    private boolean isAffine;


    /**
     * scroll parameters for scrolling backgrounds
     */
    public int scx, scy;

    /**
     * fixed point 16.16
     */
    public int PA, PB, PC, PD;

    /**
     * Internal texture reference point, 16:16
     */
    public int xRef, yRef;


    /**
     * maps the screen coordinates (x,y) to the texture x coordinate using either
     * the scroll registers or the affine registers
     */
    final protected int getTY(int x, int y) {
        if (isAffine()) {
            // PA-D and x/yRef are 16:16

            long a = PC * ((long) x << 16);
            long b = PD * ((long) y << 16);

            return (int) (((a + b) >> 16) + (long) yRef);
            // i.e. return (PC * x) + (PD * y) + yRef;
        } else {
            return (y + scy) << 16;
        }
    }

    /**
     * maps the screen coordinates (x,y) to the texture x coordinate using either
     * the scroll registers or the affine registers
     */
    final protected int getTX(int x, int y) {
        if (isAffine()) {
            // PA-D and x/yRef are 16:16

            long a = PA * ((long) x << 16);
            long b = PB * ((long) y << 16);

            return (int) (((a + b) >> 16) + (long) xRef);
            // i.e. return (PA * x) + (PB * y) + xRef;
        } else {
            return (x + scx) << 16;
        }
    }


    /**
     * returns whether the screen is visible
     */
    public boolean isVisible() {
        return this.isVisible;
    }

    /**
     * sets whether the layer is visible
     */
    public void setVisible(boolean flag) {
        this.isVisible = flag;
    }

    /**
     * sets the priority of this screen layer, higher layers are drawn last
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * returns the priority of this screen layer
     */
    public int getPriority() {
        return this.priority;
    }

    /**
     * Sets whether the screen layer is affine or not
     */
    public void setAffine(boolean isAffine) {
        this.isAffine = isAffine;
    }

    /**
     * Returns whether the layer is an affine layer or not
     */
    public boolean isAffine() {
        return isAffine;
    }


    /**
     * requests a given line to be rendered, sending the entire buffer from
     * IScreen
     */
    public abstract void renderLine(int[] buffer, int y, int width);
}
