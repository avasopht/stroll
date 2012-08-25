package org.konelabs.stroll.system;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.net.URL;
import java.util.ArrayList;

import org.konelabs.stroll.graphics.GBABitmap;
import org.konelabs.stroll.tetris.PieceTemplate;

public final class ConfigLoader {

  public static ClassLoader CLASS_LOADER = ConfigLoader.class.getClassLoader();

  public static enum BufferType {
    BITMAP, TILE
  }

  public static InputStream loadStream(String path) {
    return CLASS_LOADER.getResourceAsStream(path);
  }

  public static URL loadURL(String path) {
    return CLASS_LOADER.getResource(path);
  }

  public static GBABitmap loadBitmap(String fileName) throws IOException {
    BufferedImage image;
    GBABitmap gbaBitmap;
    URL url;

    url = CLASS_LOADER.getResource(fileName);
    image = javax.imageio.ImageIO.read(url);
    gbaBitmap = new GBABitmap(image.getWidth(), image.getHeight());

    for (int y = 0; y < image.getHeight(); ++y) {
      for (int x = 0; x < image.getWidth(); ++x) {
        gbaBitmap.setPixel(x, y, image.getRGB(x, y));
      }
    }

    return gbaBitmap;
  }

  /**
   * Will load the given image in the config file into buffer.
   * 
   * @param fileName
   * @param buffer
   *          : image will be copied into tile buffer
   * @throws IOException
   */
  public static void loadTileMemory(String fileName, int[] buffer)
      throws IOException {
    BufferedImage image;

    URL url = CLASS_LOADER.getResource(fileName);
    image = javax.imageio.ImageIO.read(url);

    int tileWidth = image.getWidth() / 8;
    int tileHeight = image.getHeight() / 8;
    int tiles = tileWidth * tileHeight;
    for (int i = 0; i < tiles; i++) {
      // copy tile _i to buffer
      int xOffset = (i % tileWidth) * 8;
      int yOffset = (i / tileWidth) * 8;
      for (int y = 0; y < 8; ++y) {
        for (int x = 0; x < 8; ++x) {
          int sourcePixel = image.getRGB(xOffset + x, yOffset + y);
          buffer[(i * 64) + x + (y * 8)] = sourcePixel;
        }
      }
    }
  }

  /**
   * loads fonts using 'font.txt' as a guide
   */
  public static GBFont[] loadFonts() throws IOException {
    InputStream stream;
    StreamTokenizer st;
    GBFont fonts[];
    int fontCount;

    stream = CLASS_LOADER.getResourceAsStream("font.txt");
    st = new StreamTokenizer(new InputStreamReader(stream));
    st.wordChars('.', '.');

    fontCount = readInt(st);
    fonts = new GBFont[fontCount];

    for (int i = 0; i < fontCount; i++) {
      String fileName;
      int offset; // offset
      int tiles[]; // tiles
      int cWidth, cHeight; // character width/height

      fileName = readString(st);
      cWidth = readInt(st);
      cHeight = readInt(st);
      offset = readInt(st);

      tiles = new int[8 * 8 * cWidth * cHeight];
      loadTileMemory(fileName, tiles);

      fonts[i] = new GBFont(tiles, offset);
    }

    // close streams
    stream.close();

    return fonts;
  }

  /**
   * Loads the piece templates from 'blocks.txt'
   * 
   * @return
   * @throws IOException
   */
  public static PieceTemplate[] getTemplates() throws IOException {
    StreamTokenizer st;
    ArrayList<PieceTemplate> tempTemplateList = new ArrayList<PieceTemplate>();

    InputStream stream = CLASS_LOADER.getResourceAsStream("blocks.txt");
    st = new StreamTokenizer(new InputStreamReader(stream));

    int numShapes = readInt(st);

    for (int i = 0; i < numShapes; i++) {
      char pieceChar = readString(st).charAt(0);
      int width = readInt(st);
      int height = readInt(st);
      int variations = readInt(st);
      int offsetX = readInt(st);
      int offsetY = readInt(st);
      boolean[][] map = new boolean[variations][width * height];
      for (int j = 0; j < variations; j++) {
        for (int k = 0; k < width * height; k++) {
          int x = k % width;
          int y = k / height;
          y = height - 1 - y; // flip y
          map[j][x + y * width] = readInt(st) != 0;
        }
      }
      PieceTemplate template = new PieceTemplate(map, offsetX, offsetY, width,
          height, pieceChar);
      tempTemplateList.add(template);
    }

    PieceTemplate[] templates = new PieceTemplate[tempTemplateList.size()];
    for (int i = 0; i < templates.length; ++i)
      templates[i] = tempTemplateList.get(i);
    return templates;
  }

  /**
   * Loads gravity thresholds from 'levels.txt'
   */
  public static int[] loadGravityThresholds() throws IOException {
    StreamTokenizer st;
    ArrayList<Integer> thresholdList = new ArrayList<Integer>();

    InputStream stream = CLASS_LOADER.getResourceAsStream("levels.txt");
    st = new StreamTokenizer(new InputStreamReader(stream));

    int i;
    while ((i = readInt(st)) >= 0) {
      thresholdList.add(new Integer(i));
    }

    int[] list = new int[thresholdList.size()];
    for (i = 0; i < list.length; ++i) {
      list[i] = thresholdList.get(i);
    }

    // close streams
    stream.close();

    return list;
  }

  private static int readInt(StreamTokenizer st) throws IOException {
    while (st.nextToken() != StreamTokenizer.TT_NUMBER) {
      if (st.ttype == StreamTokenizer.TT_EOF) {
        return -1;
      }
    }
    return (int) st.nval;
  }

  private static String readString(StreamTokenizer st) throws IOException {
    while (st.nextToken() != StreamTokenizer.TT_WORD) {
      if (st.ttype == StreamTokenizer.TT_EOF) {
        return null;
      }
    }
    return st.sval;
  }
}
