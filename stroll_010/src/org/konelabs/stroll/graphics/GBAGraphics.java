package org.konelabs.stroll.graphics;

/**
 * <p>
 * Representation of the GBA screen hardware. The <b>getInstance</b> method must
 * be used to instantiate the class, which provides only singleton access.
 * </p>
 * <p>
 * Use getLayer(layerNumber) to gain access to a layer
 * </p>
 * 
 * @author Keldon
 * 
 */
public final class GBAGraphics extends AbstractGraphics<GBALayer> {
  // ********************************* PRIVATE VARIABLES ********************
  // ************************************************************************
  /** Stores all layers that the Screen will display */
  private GBALayer layers[];

  /** Sets the total number of backgrounds for the layer */
  private final static int NUM_BACKGROUNDS = 4;

  // ********************************** PUBLIC VARIABLES ********************
  // ************************************************************************
  /** Settings of the screen */
  public final static int DEFAULT_WIDTH = 240, DEFAULT_HEIGHT = 160,
      MAX_PRIORITY = 4, DEFAULT_MODE = 0;

  /** will return the singleton instance of the Screen */
  public static GBAGraphics getInstance() {
    return SingletonScreen.INSTANCE;
  }

  // ********************************* PRIVATE METHODS **********************
  // ************************************************************************
  // constructor
  private GBAGraphics() {
    super(DEFAULT_WIDTH, DEFAULT_HEIGHT);

    this.layers = new GBALayer[NUM_BACKGROUNDS];

    // create backgrounds
    for (int i = 0; i < NUM_BACKGROUNDS; ++i) {
      GBALayer bg = new GBALayer();
      this.layers[i] = bg;
      bg.setPriority(0);
      bg.setVisible(false);
    }
  }

  // Singleton Action
  /**
   * provides "initialization on demand holder idiom" access to singleton screen
   */
  private static class SingletonScreen {
    private final static GBAGraphics INSTANCE = new GBAGraphics();
  }

  // ******************************* PROTECTED METHODS **********************
  // ************************************************************************
  /**
   * will draw all of the Screen's layers to the specified buffer on the current
   * line.
   */
  protected final void drawLayers(int buffer[]) {
    for (int i = 0; i < getNumLayers(); ++i) {
      if (getLayer(i).isVisible()) {
        getLayer(i).renderLine(buffer, getVCount(), getWidth());
      }
    }
  }

  // ********************************** PUBLIC METHODS **********************
  // ************************************************************************
  // Screen public methods

  /** returns the maximum priority that can be given to a layer */
  public int getMaxPriority() {
    return MAX_PRIORITY;
  }

  // **************************** INHERITED METHODS *************************

  /** returns the number of screen layers */
  public int getNumLayers() {
    return NUM_BACKGROUNDS;
  }

  /** returns the given layer; null is returned if the layer is not loaded */
  public GBALayer getLayer(int num) {
    return layers[num];
  }
}
