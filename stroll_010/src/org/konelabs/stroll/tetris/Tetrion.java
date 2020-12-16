package org.konelabs.stroll.tetris;

import java.util.Arrays;

public class Tetrion extends ATetrion {
    private int clearCounter, gravityCounter;
    private final int[] gravityThreshold;
    private boolean fastDrop;

    private final static int CLEAR_THRESHOLD = 93, DEFAULT_GRAVITY_THRESHOLD = 53;

    private final IGenerator generator;

    public void initializeTetrion() {
        super.initializeTetrion();

        this.fastDrop = false;
        this.clearCounter = 0;
        this.gravityCounter = 0;
    }

    public Tetrion(PieceTemplate[] pieces, int[] gravityThreshold) {
        super(10, 22);

        generator = new BagGenerator(5);
        generator.setPieces(pieces);
        this.gravityThreshold = gravityThreshold;

        initializeTetrion();
    }

    public IGenerator getGenerator() {
        return this.generator;
    }

    public TetrionState advanceFrame() {
        switch (getState()) {
            case play:
                gravityCounter++;
                int gThreshold;

                // set the gravity threshold
                gThreshold = calculateGravityThreshold();

                // apply gravity when the gravity counter reaches the threshold
                if (gravityCounter >= gThreshold) {
                    applyTetronimoGravity();
                    gravityCounter = 0;
                }
                break;
            case clearing:
                if (clearCounter++ > CLEAR_THRESHOLD) {
                    performClearingDrop();
                    clearCounter = 0;
                    this.state = TetrionState.spawn;
                }
                break;
            case start:
                this.state = TetrionState.play;
                spawnPiece();
                break;
            case spawn:
                spawnPiece();
                break;
            case end:
                break;
        }

        return this.state;
    }

    private int calculateGravityThreshold() {
        int gThreshold;
        if (fastDrop) {
            gThreshold = Math.min(2, getGravityThreshold());
        } else {
            gThreshold = getGravityThreshold();
        }
        return gThreshold;
    }

    public void move(int x) {
        if (x > 1 || x < -1 || this.state != TetrionState.play)
            return;

        if (!collides(x, 0))
            this.piece.move(x, 0);
    }

    public void rotate(int dir) {

        if (dir > 1 || dir < -1 || state != TetrionState.play)
            return;

        this.piece.rotate(dir);
        if (!collides(0, 0))
            return;

        if (attemptWallKicks()) return;

        // rotation was unsuccessful so return to original direction
        this.piece.rotate(-dir);
    }

    private boolean attemptWallKicks() {
        for (int i = 0; i < 3; i++) {
            if (!collides(i, 0)) {
                this.piece.move(i, 0);
                return true;
            }
            if (!collides(-i, 0)) {
                this.piece.move(-i, 0);
                return true;
            }
        }
        return false;
    }

    public void setFastDrop(boolean b) {
        this.fastDrop = b;
    }


    private void spawnPiece() {
        piece = this.generator.getNext();

        if (collides(0, 0)) {
            this.state = TetrionState.end;
        } else {
            this.state = TetrionState.play;
        }

        fastDrop = false;
    }

    private int getGravityThreshold() {

        if (gravityThreshold.length == 0)
            return DEFAULT_GRAVITY_THRESHOLD;

        if (gravityThreshold.length < getLevel())
            return gravityThreshold[gravityThreshold.length - 1];
        else
            return gravityThreshold[getLevel()];
    }

    /**
     * drops tetronimo by one block, locks if necessary
     */
    private void applyTetronimoGravity() {
        if (collides(0, -1)) {
            // lock
            for (int x = 0; x < piece.getWidth(); x++) {
                for (int y = 0; y < piece.getHeight(); y++) {
                    int px = x + piece.x;
                    int py = y + piece.y;
                    boolean isBlock = piece.getBlocks()[x + y * piece.getWidth()];
                    boolean outBounds = px < 0 | px >= 10 | py < 0 | py >= 18;
                    if (isBlock & !outBounds) {
                        playfield[px + py * getWidth()] = piece.getBlock(x, y);
                    }
                }
            }

            piece = null;

            // clear lines if any are completed
            clearLines();
            this.state = TetrionState.spawn;
            for (boolean clearingRow : clearingRows) {
                if (clearingRow) {
                    this.state = TetrionState.clearing;
                    break;
                }
            }
        } else {
            piece.move(0, -1);
        }
    }

    /**
     * performs row removal / dropping after clearing rows
     */
    private void performClearingDrop() {
        int[] offset = new int[clearingRows.length];

        int counter = 0;
        for (int i = 0; i < offset.length; i++) {
            if (!clearingRows[i]) {
                offset[counter] = i - counter;
                counter++;
            }
        }
        for (; counter < offset.length; counter++)
            offset[counter] = offset[counter - 1];

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                int offsety = y + offset[y];
                if (offsety >= getHeight())
                    playfield[x + y * getWidth()] = null;
                else
                    playfield[x + y * getWidth()] = playfield[x + (offsety * getWidth())];
            }
        }

        Arrays.fill(clearingRows, false);
    }

}
