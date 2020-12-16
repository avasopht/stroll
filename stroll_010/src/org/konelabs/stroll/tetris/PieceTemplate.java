package org.konelabs.stroll.tetris;

/**
 * Represents a template piece that is used to create a Tetris piece. Pieces
 * must have unique id's
 *
 * @author Keldon
 */
public class PieceTemplate {
    /**
     * stores array of blocks at all possible rotations
     */
    public final boolean[][] blocks;
    /**
     * offset of x and y position, as some blocks are stored at an offset
     */
    public final int x, y;
    /**
     * width and height of blocks
     */
    public final int width, height;
    /**
     * Identifying character for the particular piece
     */
    public final char character;

    public PieceTemplate(boolean[][] blocks, int x, int y, int width, int height,
                         char character) {
        this.blocks = blocks;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.character = character;
    }
}
