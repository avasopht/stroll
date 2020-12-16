package org.konelabs.stroll.tetris;

import java.util.Arrays;

public abstract class ATetrion {
    private final int width;
    private final int height;
    private int score;

    protected boolean[] clearingRows;
    protected Block[] playfield;

    protected Piece piece;
    protected TetrionState state;
    protected int lines;

    /**
     * locates completed lines and clears them, but does not drop the rows above
     */
    protected void clearLines() {

        int linesCleared = 0;

        for (int y = 0; y < getHeight(); y++) {
            boolean lineCleared = true;
            // find lines to clear
            for (int x = 0; x < getWidth(); x++) {
                if (playfield[x + y * getWidth()] == null)
                    lineCleared = false;
            }

            // clear line
            if (lineCleared) {
                linesCleared++;
                clearingRows[y] = true;

                for (int x = 0; x < getWidth(); x++) {
                    playfield[x + y * getWidth()].piece.erased = true;
                    playfield[x + y * getWidth()] = null;
                }
            }
        }
        // Scoring: 40 * (n + 1) 100 * (n + 1) 300 * (n + 1) 1200 * (n + 1)
        int multiplier = switch (linesCleared) {
            case 1 -> 40;
            case 2 -> 100;
            case 3 -> 300;
            case 4 -> 1200;
            default -> 0;
        };
        this.lines += linesCleared;
        this.score += multiplier * (getLevel() + 1);
    }

    /**
     * returns whether collision occurs with block at given offset
     */
    protected boolean collides(int x, int y) {
        boolean[] blocks = this.piece.getBlocks();
        int pwidth = this.piece.getWidth();
        int pheight = this.piece.getHeight();

        for (int cx = 0; cx < pwidth; cx++) {
            for (int cy = 0; cy < pheight; cy++) {
                int px = x + cx + this.piece.x;
                int py = y + cy + this.piece.y;

                boolean isPieceBlock = blocks[cx + cy * pwidth];
                boolean inWall = px < 0 || py < 0 || px >= 10;
                boolean aboveTetrion = py >= 18;

                if (inWall & isPieceBlock)
                    return true;

                if (inWall | aboveTetrion)
                    continue;

                boolean isFieldBlock = this.playfield[px + py * getWidth()] != null;

                if (isPieceBlock && isFieldBlock)
                    return true;
            }
        }

        return false;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLevel() {
        return this.lines / 10;
    }

    public int getLines() {
        return this.lines;
    }

    public Piece getPiece() {
        return this.piece;
    }

    public TetrionState getState() {
        return state;
    }

    public Block[] getPlayfield() {
        return this.playfield;
    }

    public int getScore() {
        return this.score;
    }

    public boolean[] getClearingRows() {
        return this.clearingRows;
    }

    public ATetrion(int width, int height) {
        this.width = width;
        this.height = height;

        initializeTetrion();
    }

    public void initializeTetrion() {

        this.state = TetrionState.start;

        this.clearingRows = new boolean[height];

        playfield = new Block[width * height];
        Arrays.fill(playfield, null);
    }

    public abstract TetrionState advanceFrame();

    public abstract IGenerator getGenerator();

    public abstract void move(int x);

    public abstract void rotate(int dir);

    public abstract void setFastDrop(boolean b);
}
