package org.konelabs.stroll.gui.screens;

import org.konelabs.stroll.game.Camera;
import org.konelabs.stroll.game.StrollDriver;
import org.konelabs.stroll.graphics.GBAGraphics;
import org.konelabs.stroll.graphics.GBALayer;
import org.konelabs.stroll.gui.GuiMessage;
import org.konelabs.stroll.gui.Screen;
import org.konelabs.stroll.system.ConfigLoader;
import org.konelabs.stroll.system.GBFont;
import org.konelabs.stroll.tetris.*;

import java.io.IOException;

public class PlayGameScreen implements Screen {

    StrollDriver driver;
    Camera camera;
    GBALayer textLayer, bgLayer;
    Tetrion tetrion;

    protected PlayGameScreen() {

        GBAGraphics screen = GBAGraphics.getInstance();
        bgLayer = screen.getLayer(0);
        textLayer = screen.getLayer(1);
        driver = new StrollDriver();

        camera = new Camera();
        camera.setBGLayer(bgLayer);

        int[] gravityThreshold;

        PieceTemplate[] templates;

        // load details
        try {
            loadTiles();

            loadFonts();

            loadPiecesAndGravityThreshold();

            calibrateCamera();

            playGame();
        } catch (IOException ignored) {
        }
    }

    private void playGame() {
        bgLayer.displayWraparound = false;
    }

    private void calibrateCamera() {
        camera.ox = 8 * ((tetrion.getWidth() + 2) / 2);
        camera.oy = 8 * 10;
    }

    private void loadPiecesAndGravityThreshold() throws IOException {
        int[] gravityThreshold;
        PieceTemplate[] templates;
        templates = ConfigLoader.getTemplates();
        gravityThreshold = ConfigLoader.loadGravityThresholds();
        tetrion = new Tetrion(templates, gravityThreshold);
    }

    private void loadTiles() throws IOException {
        ConfigLoader.loadTileMemory("tileset.gif", bgLayer.tiles);
        bgLayer.setVisible(true);
        bgLayer.setAffine(true);
        bgLayer.setMode(GBALayer.Mode.TILE_MODE, 128, 128);
    }

    private void loadFonts() throws IOException {
        GBFont font = ConfigLoader.loadFonts()[0];
        System.arraycopy(font.tiles, 0, textLayer.tiles, 0, font.tiles.length);
        textLayer.setVisible(true);
    }

    @Override
    public boolean handleMessage(GuiMessage message) {
        return driver.handleMessage(message);
    }

    @Override
    public void closePage() {

    }

    public void handleFrame() {
        driver.advanceFrame(tetrion);

        int bgWidth = bgLayer.getWidth() / 8;

        // clear screen
        clearScreen(bgWidth);

        drawFrame(bgWidth);

        drawTetrion(bgWidth);

        drawPiece(bgWidth, tetrion.getPiece());

        camera.applyEffects();

        printGameState();

        if (tetrion.getState() == TetrionState.end) {
            System.out.println("ENDED");
        }
    }

    private void printGameState() {
        print("LEVEL:" + tetrion.getLevel(), textLayer, 0, 0);
        print("LINES:" + tetrion.getLines(), textLayer, 0, 1);
        print("SCORE:" + tetrion.getScore(), textLayer, 0, 2);
    }

    private void clearScreen(int bgWidth) {
        for (int i = 0; i < bgWidth * bgWidth; i++) {
            bgLayer.map[i] = 0;
        }
    }

    private void drawPiece(int bgWidth, Piece piece) {
        if (piece != null) {
            for (int x = 0; x < piece.getWidth(); x++) {
                int cx = 1 + x + piece.x;
                for (int y = 0; y < piece.getHeight(); ++y) {
                    boolean isBlock = piece.getBlocks()[x + y * piece.getWidth()];

                    int cy = 18 - (y + piece.y);
                    if (cy < 0 || cy > 18 || cx < 0 || cx > 10 || !isBlock) {
                        continue;
                    }

                    bgLayer.map[cx + cy * bgWidth] = getPieceTypeOffset(piece.getChar()) + 1;
                }
            }
        }
    }

    private void drawTetrion(int bgWidth) {
        for (int x = 0; x < tetrion.getWidth(); x++) {
            for (int y = 0; y < tetrion.getHeight(); y++) {
                int cx = x + 1;
                int cy = 18 - y;

                Block block = tetrion.getPlayfield()[x + y * tetrion.getWidth()];

                if (block == null) {
                    continue;
                }

                bgLayer.map[cx + cy * bgWidth] = getBlockIndex(block);
            }
        }
    }

    private void drawFrame(int bgWidth) {
        final int blockNS = 2, blockNE = 3, blockEW = 4, blockNW = 5;
        for (int i = 0; i < 19; i++) {
            int cx, cy;
            cx = 0;
            cy = i;
            bgLayer.map[cx + cy * bgWidth] = blockNS;
            cx = 11;
            bgLayer.map[cx + cy * bgWidth] = blockNS;
        }
        bgLayer.map[19 * bgWidth] = blockNE;
        bgLayer.map[11 + 19 * bgWidth] = blockNW;
        for (int i = 1; i < 11; ++i) {
            bgLayer.map[i + 19 * bgWidth] = blockEW;
        }
    }

    private int getPieceTypeOffset(char pieceChar) {
        return 24 + (12 * getPieceTypeIndex(pieceChar));
    }

    private int getPieceTypeIndex(char pieceChar) {
        return switch (Character.toUpperCase(pieceChar)) {
            case 'S' -> 0;
            case 'Z' -> 1;
            case 'I' -> 2;
            case 'T' -> 3;
            case 'O' -> 4;
            case 'L' -> 5;
            case 'J' -> 6;
            default -> -1;
        };
    }

    private int getBlockTypeOffset(BlockType blockType) {

        return switch (blockType) {
            case EMPTY -> -1;
            case CENTRE -> 1;
            case NS -> 2;
            case EW -> 3;
            case SE -> 4;
            case SW -> 5;
            case NE -> 6;
            case NW -> 7;
            case E -> 8;
            case S -> 9;
            case N -> 10;
            case W -> 11;
        };
    }

    private int getBlockIndex(Block block) {
        int index = getPieceTypeIndex(block.getChar());

        if (index < 0) {
            return 0;
        } else {
            int pieceTypeOffset = getPieceTypeOffset(block.getChar());
            int blockTypeOffset = getBlockTypeOffset(block.blockType);

            if (block.piece.erased) {
                blockTypeOffset = 0;
            }

            return pieceTypeOffset + blockTypeOffset;
        }
    }

    /**
     * Will write text on a particular layer
     */
    private static void print(String text, GBALayer bgLayer, int x, int y) {
        int bgWidth;
        int mapPos;

        bgWidth = bgLayer.getWidth() / 8;
        char[] cText = text.toCharArray();

        mapPos = x + (y * bgWidth);

        for (int i = 0; i < cText.length; i++) {
            bgLayer.map[i + mapPos] = cText[i] - 32;
        }
    }
}
