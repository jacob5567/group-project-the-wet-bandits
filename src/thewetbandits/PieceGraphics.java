package thewetbandits;

import acm.graphics.GCompound;
import acm.graphics.GImage;
import acm.graphics.GOval;
import acm.graphics.GPoint;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class PieceGraphics extends GCompound {

    // private attributes
    // image, animated image, size, point, xy locations
    private GImage image;
    private GImage imageAnimated;
    private GOval testOval;
    private Piece.Color color;
    // animation active boolean
    private boolean active = false;

    private int x, y, size;
    private static final int MOVEMENT_SPEED = 5;
    private static final int MOVEMENT_FREQUENCY = 13;
    private ArrayList<GPoint> locations = new ArrayList<>();

    private GPoint currentPoint;

    // Constructors

    // constructor with x, y, size, and color
    public PieceGraphics(int x, int y, int size, Piece.Color color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
        this.initImage();
    }

    // Methods

    // function to reposition the piece
    public void reposition(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.image.setSize(size, size);
        this.imageAnimated.setSize(size, size);
        this.updateImage();
        this.setLocation(x, y);
    }


    // function to initialize the image file with the correct color
    private void initImage() {
        this.image = new GImage(this.color.toString().toLowerCase() + "_gem.png");
        this.imageAnimated = new GImage(this.color.toString().toLowerCase() + "_gem_animated.gif");
        this.imageAnimated.setSize(this.size, this.size);
        this.image.setSize(this.size, this.size);
        this.testOval = new GOval(size, size);
        this.testOval.setColor(this.color.getColor());
        updateImage();
        this.setLocation(this.x, this.y);
    }


    // function to update the animation state of the image
    private void updateImage() {
        remove(this.image);
        remove(this.imageAnimated);
        add(active ? this.imageAnimated : this.image);
    }

    // function to clear all visuals
    public void clearPiece() {
        remove(this.image);
        remove(this.imageAnimated);
        add(testOval);
    }

    // getter for animation state
    public boolean animating() {
        return !this.locations.isEmpty() || this.currentPoint != null;
    }

    public void toggleActive() {
        this.active = !this.active;
        this.updateImage();
    }

    // function to update a piece's location
    private void updateLocation() {
        double dx = this.currentPoint.getX() - this.getX();
        double dy = this.currentPoint.getY() - this.getY();

        if (dx < -MOVEMENT_SPEED)
            dx = -MOVEMENT_SPEED;
        else if (dx > MOVEMENT_SPEED)
            dx = MOVEMENT_SPEED;

        if (dy < -MOVEMENT_SPEED)
            dy = -MOVEMENT_SPEED;
        else if (dy > MOVEMENT_SPEED)
            dy = MOVEMENT_SPEED;

        if (dx == 0 && dy == 0) {
            this.currentPoint = this.getNextPoint();
        } else {
            this.move(dx, dy);
        }
    }

    // function to get the next place for the piece to go
    private GPoint getNextPoint() {
        if (!this.locations.isEmpty())
            return this.locations.remove(0);
        else
            return null;
    }

    // function to set the piece's target location when moving
    public void setTargetLocation(int x, int y, boolean queue) {
        GPoint target = new GPoint(x, y);
        if (this.currentPoint == null) {
            this.currentPoint = target;
        } else {
            if (queue)
                this.locations.add(target);
            else
                this.currentPoint = target;
        }
    }

    public void setTargetLocation(int x, int y) {
        this.setTargetLocation(x, y, false);
    }
}
