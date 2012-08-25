package org.konelabs.stroll.tetris;

public interface IGenerator {
  /** sets the available pieces */
  public void setPieces(PieceTemplate[] pieces);

  /** returns a preview of the pieces to come */
  public PieceTemplate[] getPreview();

  /** returns next piece */
  public Piece getNext();

  /** returns the set of pieces */
  public PieceTemplate[] getPieces();
}
