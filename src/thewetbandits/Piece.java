package thewetbandits;

import PieceGraphics
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
    private static final int MOVEMENT_SPEED = 5;
    private static final int MOVEMENT_FREQUENCY = 13;
    private static final Random random = new Random();

    private static PieceGraphics graphics;

    private static final ArrayList<WeakReference<GamePiece>> pieces = new ArrayList<>();

    static {
        Timer updateTimer = new Timer(MOVEMENT_FREQUENCY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Timers are async so synchronize access to the array list so we don't break
                // everything with CMEs
                synchronized (pieces) {
                    Iterator<WeakReference<GamePiece>> iterator = pieces.iterator();
                    while (iterator.hasNext()) {
                        WeakReference<GamePiece> pieceReference = iterator.next();
                        GamePiece piece = pieceReference.get();
                        if (piece == null) {
                            // The piece has been garbage collected, remove it from the list
                            iterator.remove();
                        } else {
                            if (piece.currentPoint != null)
                                piece.updateLocation();
                        }
                    }
                }
            }
        });
        updateTimer.start();
    }

    // color in color enum
    private GamePiece.Color color;

    // PieceGraphics object to correspond to this piece
    // row and column
    private int r, c;

    // animation active boolean
    private boolean active = false;

    private ArrayList<GPoint> locations = new ArrayList<>();

    // Constructors

    public Piece(int x, int y, int size, int r, int c) {
        this(x, y, size, getRandomColor(), r, c);
    }

    public Piece(int spaceSize, int size, int r, int c) {
        this(spaceSize * (r + 1), spaceSize * (c + 1), size, r, c);
    }

    public Piece(int x, int y, int size, GamePiece.Color color, int r, int c) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.size = size;
        this.initImage();
        this.r = r;
        this.c = c;
        // Add the piece to the list of pieces to update
        synchronized (pieces) {
            pieces.add(new WeakReference<>(this));
        }
    }


    // Methods

    // function to get a random color
    private static Piece.Color getRandomColor() {
        // choose a random value from all the available colors from the color enum
        return Piece.Color.values()[random.nextInt(GamePiece.Color.values().length)];
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
                java.awt.Color.ORANGE), PINK(java.awt.Color.PINK), WHITE(java.awt.Color.WHITE);

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
