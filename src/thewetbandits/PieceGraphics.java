package thewetbandits;

// import graphics packages

public class PieceGraphics {

    // private attributes
    // image, animated image, size, point, xy locations


    // Constructors

    // constructor with just x, y, and size

    // constructor with x, y, size, and color


    // Methods

    // function to check if pieces are animating
        // for each piece in pieces:
            // if piece exists and is animating, return true
        // return false

    // function to reposition the piece
    // attributes: coordinates and size
        // set coordinates to those that were passed in
        // resize image and animated image
        // update to match

    // function to initialize the image file with the correct color
    // no attributes
        // get the color attribute
        // get the correct image files from that color
        // set the location of the image

    // function to update the animation state of the image
        // check to see if the animated boolean is set
        // set the corresponding image

    // function to clear all visuals
        // clear all images

    // getter for animation state
        // check if the piece is moving
        // return true, else return false

    // function to update a piece's location
        // get the difference between the x and y positions of the current location and the future location
        // set the movement speed of the piece based on the direction it will be moving
        // if the movement speed is 0:
            // get the next place for it to move
        // else:
            // move the piece into position

    // function to get the next place for the piece to go
        // check if there are any locations in the queue
            // pop the next location off the queue in return it
        // else
            // return null

    // function to set the piece's target location when moving
        // create a new point where the target is
        // if there is no current location:
            // set the current point to the target point
        // else:
            // add the target to the movement queue

}
