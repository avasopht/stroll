package org.konelabs.stroll.graphics;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;

/**
 * <p>
 * A ScreenCanvas is used to display an instance of <b>Screen</b> and, storing
 * the back buffer's pixels. It registers the size of the component upon
 * initialisation - using the size of the Screen
 * </p>
 */
public final class GBAGraphicsCanvas extends Panel implements
    IGBAGraphicsViewer {

  // ******************************** PRIVATE VARIABLES *********************
  // ************************************************************************
  private static final long serialVersionUID = 1L;

  /** Storage of the image */
  private Image image;
  /** Storage of the ImageSource access to the image */
  private MemoryImageSource imageSource;
  /** Decide whether to use BufferedImage or ImageSource for image creation */
  private static boolean USING_BUFFERED_IMAGE = false;

  // ********************************* PRIVATE METHODS **********************
  // ************************************************************************

  /**
   * Registers the Screen's dimensions, setting the preferred size of the panel.
   */
  private void registerScreen() {
    GBAGraphics screen = GBAGraphics.getInstance();
    screen.setScreenViewer(this);
    Dimension d = new Dimension(screen.getWidth(), screen.getHeight());
    setPreferredSize(d);
    setSize(d);
    setMinimumSize(d);
  }

  // ********************************** PANEL METHODS ***********************
  // ************************************************************************

  /** Called when the screen needs to be redrawn */
  public void update(Graphics g) {
    paint(g);
  }

  /** Called when the screen needs to be redrawn */
  public void paint(Graphics g) {
    int x1, x2, y1, y2;
    if ((g != null) && (image != null)) {
      // sync the display on some systems
      java.awt.Toolkit.getDefaultToolkit().sync();

      // draw relevant portion of the screen
      Rectangle bounds = g.getClipBounds();
      if (bounds == null) {
        g.drawImage(image, 0, 0, null);
      } else {
        x1 = bounds.x;
        x2 = bounds.x + bounds.width;
        y1 = bounds.y;
        y2 = bounds.y + bounds.height;

        g.drawImage(image, x1, y1, x2, y2, x1, y1, x2, y2, null);
      }

    }

  }

  // ********************************** PUBLIC METHODS **********************
  // ************************************************************************

  /** creates a screen frame */
  public GBAGraphicsCanvas() {
    image = null;
    setFocusable(false);
    registerScreen();
  }

  /**
   * Registers the integer bitmap buffer for use with this panel. Editing the
   * array and calling <b>screenUpdated</b> will result in the frame being drawn
   * to the panel.
   */
  public void registerBuffer(int[] buffer) {
    int width, height;
    width = GBAGraphics.getInstance().getWidth();
    height = GBAGraphics.getInstance().getHeight();

    ColorModel colorModel = new DirectColorModel(24, 0xff0000, 0x00ff00,
        0x0000ff);

    SampleModel sampleModel = new SinglePixelPackedSampleModel(
        DataBuffer.TYPE_INT, width, height, new int[] { 0xff0000, 0x00ff00,
            0x0000ff });

    DataBuffer dataBuffer = new DataBufferInt(buffer, width * height);

    WritableRaster raster = Raster.createWritableRaster(sampleModel,
        dataBuffer, new Point(0, 0));

    if (USING_BUFFERED_IMAGE) {
      image = new BufferedImage(colorModel, raster, true,
          new Hashtable<Object, Object>());
    } else {
      imageSource = new MemoryImageSource(width, height, colorModel, buffer, 0,
          width);
      imageSource.setAnimated(true);
      imageSource.setFullBufferUpdates(true);
      image = createImage(imageSource);
    }

  }

  /**
   * Is called when the array sent in <b>registerBuffer</b> has changed and the
   * screen needs to be redrawn.
   */
  public void screenUpdated() {
    if (USING_BUFFERED_IMAGE == false) {
      imageSource.newPixels();
    }

    repaint();

  }
}
