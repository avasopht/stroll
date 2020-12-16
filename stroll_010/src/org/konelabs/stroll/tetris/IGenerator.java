package org.konelabs.stroll.tetris;

public interface IGenerator {
    /**
     * sets the available pieces
     */
    void setPieces(PieceTemplate[] pieces);

    /**
     * returns a preview of the pieces to come
     */
    PieceTemplate[] getPreview();

    /**
     * returns next piece
     */
    Piece getNext();

    /**
     * returns the set of pieces
     */
    PieceTemplate[] getPieces();
}
