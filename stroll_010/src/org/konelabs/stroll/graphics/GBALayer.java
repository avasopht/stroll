package org.konelabs.stroll.graphics;

/**
 * <p>
 * Represents a background layer emulating the GBA backgrounds. Backgrounds are
 * either tiled or bit mapped. When they are tiled the screen is represented
 * with the two arrays, <b>tiles</b> and <b>map</b>. <b>Tiles</b> stores the
 * pixel data in 8x8 tiles, so the first 64 integer values represents the bitmap
 * information for tile 0. <b>Map</b> stores the tile references for the screen,
 * such that <b>map[0]</b> will store a reference to the first tile displayed on
 * the screen.
 * </p>
 * <p>
 * A BGLayer instance defaults at a width of 128x128 tiles, in TILE_MODE. A call
 * to setMode(mode,width,height) will set the screen size. Note that the width
 * and height specify the number of pixels when the mode is BITMAP_MODE. Width
 * and height specifies the number of tiles in TILE_MODE.
 * </p>
 * <p>
 * In BITMAP_MODE the screen is accessed via <b>screenBuffer</b>;
 * <b>displayWraparound</b> determines whether the layer's bitmaps wrap around
 * and tile.
 * </p>
 */
public final class GBALayer extends AbstractGBALayer {
  // ***************************** PRIVATE VARIABLES ***********************
  // ************************************************************************
  /** default number of characters to store in the map */
  private final static int DEFAULT_MAP_WIDTH = 128, DEFAULT_MAP_HEIGHT = 128;
  /** default number of 8x8 tiles to store */
  private final static int DEFAULT_TILE_SIZE = 1024;
  /** default bitmap buffer size */
  private final static int DEFAULT_BITMAP_WIDTH = 640,
      DEFAULT_BITMAP_HEIGHT = 480;

  /** bitmap width and height */
  private int bWidth, bHeight;
  /** map width and height */
  private int mWidth, mHeight;
  /** size of the tile memory */
  private int numTiles;

  /** Mode of the BGLayer, either Tile mode or Bitmap mode */
  private Mode mode;

  // ****************************** PUBLIC VARIABLES ************************
  // ************************************************************************
  /** BGLayer mode type */
  public static enum Mode {
    TILE_MODE, BITMAP_MODE
  }

  /** width and height of a character tile */
  public final static int TILE_WIDTH = 8, TILE_HEIGHT = 8;

  /** tile and map arrays. Bitmaps are stored in the tile data in bitmap modes */
  public int tiles[], map[], screenBuffer[];

  /** determines whether backgrounds display wrap around */
  public boolean displayWraparound;

  // ********************************* PRIVATE METHODS **********************
  // ************************************************************************

  /**
   * Will render a line to the buffer from the screen, it can draw to any line
   * possible but it supposed to write to screen.fvCount. This method will draw
   * in text mode
   * 
   * @param buffer
   */
  private void renderAffineTileLine(int buffer[], int y, int sWidth) {
    if (!isAffine()) {
      renderTileLine(buffer, y, sWidth);
      return;
    }

    // width of internal background screen
    int width, height;

    // initial x/y of texture; x/y deltas, current x/y
    int ix, iy, ixDiff, iyDiff, cx, cy;

    // pointer for accessing correct pixel in buffer
    int bufferPtr;

    // get width,height of internal background screen
    width = getWidth();
    height = getHeight();

    // store screen texture position and deltas
    ix = getTX(0, y); // texture x<<16
    iy = getTY(0, y); // texture y<<16

    ixDiff = getTX(1, y) - ix; // difference in getTX(0,y) and getTX(1,y)
    iyDiff = getTY(1, y) - iy; // difference in getTY(0,y) and getTX(1,y)

    bufferPtr = y * sWidth;

    // prepare ix/iy(diff) and cx/cy for reduction of
    // multiplies in managing wraparound (i.e. an optimisation)
    if (displayWraparound) {
      // configure ix and ixDiff
      ixDiff = ixDiff % (width << 16);
      if (ixDiff < 0) {
        ixDiff += width << 16;
      }

      ix = ix % (width << 16);
      if (ix < 0) {
        ix += width << 16;
      }

      // configure ix and ixDiff
      iyDiff = iyDiff % (height << 16);
      if (iyDiff < 0) {
        iyDiff += height << 16;
      }

      iy = iy % (height << 16);
      if (iy < 0) {
        iy += height << 16;
      }
    }

    for (int x = 0; x < sWidth; x++) {
      int mapPtr, tileNum, tilePixelOffset, pixel;

      // perform wrap around if enabled
      if (displayWraparound & ((ix >> 16) >= width)) {
        ix -= width << 16;
      }

      if (displayWraparound & ((iy >> 16) >= height)) {
        iy -= height << 16;
      }

      cx = ix >> 16;
      cy = iy >> 16;

      // if there is no wraparound and we are drawing outside of the
      // texture map then continue as there is nothing else to draw
      if (cx >= 0 & cx < width & cy >= 0 & cy < height) {
        // mapPtr = ( (cx/8) + (cy/8)*(width/8));
        mapPtr = (cx >> 3) + ((cy >> 3) * (width >> 3));
        tileNum = map[mapPtr];

        tilePixelOffset = (cx & 7) + ((cy & 7) << 3);
        pixel = tiles[(tileNum << 6) + tilePixelOffset];

        int alpha = (pixel >>> 24) & 0xff;
        if (alpha > 128) {
          buffer[bufferPtr] = pixel;
        }
      }

      ix += ixDiff;
      iy += iyDiff;
      ++bufferPtr;
    }
  }

  /**
   * Will render a line to the buffer from the screen, it can draw to any line
   * possible but it supposed to write to screen.fvCount. This verion of the
   * method will draw in bitmap mode
   * 
   * @param buffer
   */
  private void renderBitmapLine(int buffer[], int y, int sWidth) {
    // width of internal background screen
    int width, height;

    // initial x/y of texture; x/y deltas, current x/y
    int ix, iy, ixDiff, iyDiff, cx, cy;
    // pointer for accessing correct pixel in buffer
    int bufferPtr;

    // get width,height of internal background screen
    width = getWidth();
    height = getHeight();

    // store screen texture position and deltas
    ix = getTX(0, y); // texture x<<16
    iy = getTY(0, y); // texture y<<16

    ixDiff = getTX(1, y) - ix; // difference in getTX(0,y) and getTX(1,y)
    iyDiff = getTY(1, y) - iy; // difference in getTY(0,y) and getTX(1,y)

    bufferPtr = y * sWidth;

    // prepare ix/iy(diff) and cx/cy for reduction of
    // multiplies in managing wraparound (i.e. an optimisation)
    if (displayWraparound) {
      // configure ix and ixDiff
      ixDiff = ixDiff % (width << 16);
      if (ixDiff < 0) {
        ixDiff += width << 16;
      }

      ix = ix % (width << 16);
      if (ix < 0) {
        ix += width << 16;
      }

      // configure ix and ixDiff
      iyDiff = iyDiff % (height << 16);
      if (iyDiff < 0) {
        iyDiff += height << 16;
      }

      iy = iy % (height << 16);
      if (iy < 0) {
        iy += height << 16;
      }
    }

    // render a line, adjusting layer coordinates accordingly
    for (int x = 0; x < sWidth; x++) {
      int pixel;

      // perform wrap around if enabled
      if (displayWraparound & ((ix >> 16) >= width)) {
        ix -= width << 16;
      }

      if (displayWraparound & ((iy >> 16) >= height)) {
        iy -= height << 16;
      }

      cx = ix >> 16;
      cy = iy >> 16;

      // if there is no wraparound and we are drawing outside of the
      // texture map then continue as there is nothing else to draw
      if (cx >= 0 & cx < width & cy >= 0 & cy < height) {
        pixel = screenBuffer[cx + cy * width];

        int alpha = (pixel >> 24) & 0xff;
        if (alpha > 128) {
          buffer[bufferPtr] = pixel;
        }
      }

      ix += ixDiff;
      iy += iyDiff;
      ++bufferPtr;
    }
  }

  /** renders a non-affine line */
  private void renderTileLine(int buffer[], int y, int sWidth) {
    int cx, cy;
    boolean visible, outBounds;
    int bufferPtr = y * sWidth;
    int mapPtr, tileNum, tilePixelOffset, pixel;
    int width, height;

    width = getWidth();
    height = getHeight();

    cx = -scx;
    cy = y - scy;

    outBounds = (cy < 0 | cy >= height);

    if (!displayWraparound & outBounds) {
      return;
    }

    for (int x = 0; x < sWidth; ++x) {
      visible = displayWraparound | (x < width & y < height);
      if (visible) {
        mapPtr = (cx >> 3) + ((cy >> 3) * (width >> 3));
        tileNum = map[mapPtr];

        tilePixelOffset = (cx & 7) + ((cy & 7) << 3);
        pixel = tiles[(tileNum << 6) + tilePixelOffset];

        int alpha = (pixel >>> 24);
        if (alpha > 128) {
          buffer[bufferPtr] = pixel;
        }
      }

      ++cx;
      ++bufferPtr;
    }
  }

  // ********************************** PUBLIC METHODS **********************
  // ************************************************************************
  /**
   * Creates a new instance of BGLayer; defaults to TILE_MODE with 128x128 tiles
   * and <b>displayWraparound</b> set to false.
   */
  public GBALayer() {
    mWidth = DEFAULT_MAP_WIDTH;
    mHeight = DEFAULT_MAP_HEIGHT;
    bWidth = DEFAULT_BITMAP_WIDTH;
    bHeight = DEFAULT_BITMAP_HEIGHT;
    numTiles = DEFAULT_TILE_SIZE;

    mode = Mode.TILE_MODE;

    map = new int[mWidth * mHeight];
    tiles = new int[numTiles * TILE_WIDTH * TILE_HEIGHT];
    screenBuffer = new int[bWidth * bHeight];

    displayWraparound = false;
    scx = scy = 0;
    PA = PD = 1 << 16;
    PB = PC = 0;
    xRef = yRef = 0;
  }

  /**
   * requests a given line to be rendered, sending the entire buffer from
   * IScreen
   */
  public void renderLine(int[] buffer, int y, int sWidth) {
    if (!isVisible()) {
      return;
    }

    if (isVisible() && mode == Mode.TILE_MODE) {
      renderAffineTileLine(buffer, y, sWidth);
    } else {
      renderBitmapLine(buffer, y, sWidth);
    }

  }

  /**
   * Sets the mode of the BGLayer and its screen size; when the mode is
   * Mode.TILE_MODE, the width and height specify the width and height in the
   * number of tiles in the map. In Mode.BITMAP_MODE, the width and height
   * specify the width and height in pixels.
   */
  public void setMode(Mode mode, int modeWidth, int modeHeight) {
    this.mode = mode;
    switch (mode) {
    case BITMAP_MODE:
      bWidth = modeWidth;
      bHeight = modeHeight;
      break;
    case TILE_MODE:
      mWidth = modeWidth;
      mHeight = modeHeight;
      break;
    }
  }

  /**
   * @return: the current mode of the screen
   */
  public Mode getMode() {
    return mode;
  }

  /** returns width of screen map area size (in pixels) */
  public int getWidth() {
    switch (mode) {
    case BITMAP_MODE:
      return bWidth;
    case TILE_MODE:
      return mWidth * TILE_WIDTH;
    default:
      return 0;
    }
  }

  /** returns height of screen map area size (in pixels) */
  public int getHeight() {
    switch (mode) {
    case BITMAP_MODE:
      return bHeight;
    case TILE_MODE:
      return mHeight * TILE_HEIGHT;
    default:
      return 0;
    }
  }

}
