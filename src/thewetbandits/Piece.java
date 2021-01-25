package thewetbandits;

import thewetbandits.PieceGraphics;
import acm.graphics.GPoint;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Piece {
    // Attributes
    private static final long serialVersionUID = -7593716498021184989L;
    private static final int MOVEMENT_FREQUENCY = 13;
    private static final Random random = new Random();
    private static final ArrayList<WeakReference<Piece>> pieces = new ArrayList<>();

    static {
        Timer updateTimer = new Timer(MOVEMENT_FREQUENCY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Timers are async so synchronize access to the array list so we don't break
                // everything with CMEs
                synchronized (pieces) {
                    Iterator<WeakReference<Piece>> iterator = pieces.iterator();
                    while (iterator.hasNext()) {
                        WeakReference<Piece> pieceReference = iterator.next();
                        Piece piece = pieceReference.get();
                        if (piece == null) {
                            // The piece has been garbage collected, remove it from the list
                            iterator.remove();
                        } else {
                            if (piece.graphics.getCurrentPoint() != null)
                                piece.graphics.updateLocation();
                        }
                    }
                }
            }
        });
        updateTimer.start();
    }

    public PieceGraphics graphics;

    // color in color enum
    private Piece.Color color;

    // PieceGraphics object to correspond to this piece
    // row and column
    private int r, c;

    // Constructors

    public Piece(int x, int y, int size, int r, int c) {
        this(x, y, size, getRandomColor(), r, c);
    }

    public Piece(int spaceSize, int size, int r, int c) {
        this(spaceSize * (r + 1), spaceSize * (c + 1), size, r, c);
    }

    public Piece(int x, int y, int size, Piece.Color color, int r, int c) {
        this.color = color;
        this.r = r;
        this.c = c;
        this.graphics = new PieceGraphics(x, y, size, color, this);
        // Add the piece to the list of pieces to update
        synchronized (pieces) {
            pieces.add(new WeakReference<>(this));
        }
    }


    // Methods

    public boolean animating() {
        return this.graphics.animating();
    }

    // function to check if pieces are animating
    public static boolean arePiecesAnimating() {
        for (WeakReference<Piece> ref : pieces) {
            Piece p = ref.get();
            if (p != null) {
                if (p.animating())
                    return true;
            }
        }
        return false;
    }

    // function to get a random color
    private static Piece.Color getRandomColor() {
        // choose a random value from all the available colors from the color enum
        return Piece.Color.values()[random.nextInt(Piece.Color.values().length)];
    }


    // setter for the row and column values
    public void updateRowCol(int r, int c) {
        this.r = r;
        this.c = c;
    }

    // getters for the row and column values
    public int getR() {
        return r;
    }

    public int getC() {
        return c;
    }

    // getter for color
    public Piece.Color getColorType() {
        return color;
    }

    public enum Color {
        YELLOW(new java.awt.Color(250, 240, 66)), GREEN(new java.awt.Color(67, 153, 58)), BLUE(
                new java.awt.Color(24, 30, 219)), RED(java.awt.Color.RED), ORANGE(
                java.awt.Color.ORANGE), PINK(java.awt.Color.PINK), WHITE(java.awt.Color.WHITE),
                STAR(new java.awt.Color(245, 191, 66));

        private java.awt.Color color;

        /**
         * Constructor that is set based on the actual RGB color
         *
         * @param color the RGB color of the piece
         */
        Color(java.awt.Color color) {
            this.color = color;
        }

        /**
         * returns the RGB color
         *
         * @return the RGB value of the selected color
         */
        public java.awt.Color getColor() {
            return this.color;
        }
    }
}
