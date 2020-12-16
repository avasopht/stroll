package org.konelabs.stroll.tetris;

public class Piece {
    private final PieceTemplate template;
    private int direction;

    public int x, y;
    public boolean erased = false;

    public final static int START_X = 5, START_Y = 18;

    public Piece(PieceTemplate template) {
        this.template = template;
        this.x = START_X + template.x;
        this.y = START_Y + template.y;
    }

    public boolean[] getBlocks() {
        return this.template.blocks[direction % getSize()];
    }

    public int getDirection() {
        return this.direction;
    }

    public int getHeight() {
        return this.template.height;
    }

    public int getSize() {
        return this.template.blocks.length;
    }

    public int getWidth() {
        return this.template.width;
    }

    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public void rotate(int dir) {
        dir %= getSize();
        this.direction += getSize();
        this.direction += dir;
        this.direction %= getSize();
    }

    public Block getBlock(int x, int y) {
        return new Block(this, getBlockType(x, y));
    }

    public BlockType getBlockType(int x, int y) {
        boolean n, e, s, w, c;

        n = isOccupied(x, y + 1);
        s = isOccupied(x, y - 1);
        e = isOccupied(x + 1, y);
        w = isOccupied(x - 1, y);
        c = isOccupied(x, y);

        if (!c)
            return BlockType.EMPTY;

        if (n & s)
            return BlockType.NS;
        if (e & w)
            return BlockType.EW;

        if (n & w)
            return BlockType.NW;
        if (n & e)
            return BlockType.NE;
        if (s & w)
            return BlockType.SW;
        if (s & e)
            return BlockType.SE;

        if (e)
            return BlockType.E;
        if (w)
            return BlockType.W;
        if (s)
            return BlockType.S;
        if (n)
            return BlockType.N;

        return BlockType.CENTRE;
    }

    public char getChar() {
        return template.character;
    }

    private boolean isOccupied(int x, int y) {
        if (x < 0 | y < 0 | x >= getWidth() | y >= getHeight())
            return false;

        return getBlocks()[x + y * getWidth()];
    }

}
