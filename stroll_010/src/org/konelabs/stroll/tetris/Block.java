package org.konelabs.stroll.tetris;

public final class Block {
    public final BlockType blockType;
    public final Piece piece;

    public Block(Piece piece, BlockType blockType) {
        this.piece = piece;
        this.blockType = blockType;
    }

    public char getChar() {
        return piece.getChar();
    }
}
