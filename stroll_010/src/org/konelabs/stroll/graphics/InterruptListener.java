package org.konelabs.stroll.graphics;

/**
 * Interface for a Screen's interrupt listener. Vertical blank and Horizontal
 * blank events will call the listener and notify them of the event
 */
public interface InterruptListener {
  /** states for interruptType */
  public final int VBL_INTERRUPT = 0x01, HBL_INTERRUPT = 0x02,
      VCOUNT_INTERRUPT = 0x03;

  /**
   * informs an interrupt listener of an interrupt and the type
   * 
   * @param: interruptType
   * @see: org.konelabs.gamelib.graphics.InterruptListener.VBL_INTERRUPT
   *       org.konelabs.gamelib.graphics.InterruptListener.HBL_INTERRUPT
   *       org.konelabs.gamelib.graphics.InterruptListener.VCOUNT_INTERRUPT
   */
  public void screenInterrupt(int interruptType);
}
