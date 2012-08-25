package org.konelabs.stroll.graphics;

public class GBABitmap {
  public final int width;
  public final int height;
  public final int[] buffer;

  public GBABitmap(int newWidth, int newHeight) {
    width = newWidth;
    height = newHeight;
    buffer = new int[width * height];
  }

  public GBABitmap(int newWidth, int newHeight, int[] newBuffer) {
    width = newWidth;
    height = newHeight;
    buffer = newBuffer;
  }

  public GBABitmap(GBABitmap bitmap) {
    width = bitmap.width;
    height = bitmap.height;
    buffer = bitmap.buffer;
  }

  public void setPixel(int x, int y, int colour) {
    if (0 > x || 0 > y || width <= x || height <= y) {
      return;
    }

    buffer[x + y * width] = colour;
  }
}
