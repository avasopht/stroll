package org.konelabs.stroll.tetris;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BagGenerator implements IGenerator {
    private final int previewSize;
    private PieceTemplate[] pieces;
    private List<List<PieceTemplate>> bags;
    private Random rand;

    public BagGenerator(int previewSize) {
        this.previewSize = previewSize;
        init();
    }

    private void init() {
        this.bags = new ArrayList<List<PieceTemplate>>();
        rand = new Random(System.nanoTime());
    }

    public Piece getNext() {
        Piece next = new Piece(bags.get(0).remove(0));
        if (bags.get(0).size() == 0) {
            bags.remove(0);
            bags.add(createBag());
        }
        return next;
    }

    public PieceTemplate[] getPieces() {
        return this.pieces;
    }

    public PieceTemplate[] getPreview() {
        int bagSize = this.bags.get(0).size();
        PieceTemplate[] preview = new PieceTemplate[bagSize];
        for (int i = 0; i < bagSize; i++) {
            preview[i] = this.bags.get(0).get(i);
        }
        return preview;
    }

    public void setPieces(PieceTemplate[] pieces) {
        this.pieces = pieces;

        this.bags.clear();
        for (int i = 0; i < this.previewSize; i++) {
            bags.add(createBag());
        }
    }

    private List<PieceTemplate> createBag() {
        ArrayList<PieceTemplate> bag = new ArrayList<PieceTemplate>();
        Collections.addAll(bag, pieces);

        for (int i = 0; i < pieces.length; i++) {
            int j = rand.nextInt(bag.size());
            PieceTemplate temp = bag.get(i);
            bag.set(i, bag.get(j));
            bag.set(j, temp);
        }

        return bag;
    }

}
