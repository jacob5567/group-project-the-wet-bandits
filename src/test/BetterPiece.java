package test;

import acm.graphics.GCompound;
import acm.graphics.GImage;
import acm.graphics.GOval;
import thewetbandits.animation.Pose;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class BetterPiece extends GCompound {

	private static final int MOVEMENT_SPEED = 5;
	private static final int MOVEMENT_FREQUENCY = 13;
	private static final Random random = new Random();

	// Store a list of weak references (so the pieces can be garbage collected
	// correctly) of pieces to update
	private static final ArrayList<WeakReference<BetterPiece>> pieces = new ArrayList<>();
	private static Timer updateTimer;

	static {
		updateTimer = new Timer(MOVEMENT_FREQUENCY, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Timers are async so synchronize access to the array list so we don't break
				// everything with CMEs
				synchronized (pieces) {
					Iterator<WeakReference<BetterPiece>> iterator = pieces.iterator();
					while (iterator.hasNext()) {
						WeakReference<BetterPiece> pieceReference = iterator.next();
						BetterPiece piece = pieceReference.get();
						if (piece == null) {
							// The piece has been garbage collected, remove it from the list
							iterator.remove();
						} else {
							// The piece is still in memory, tell it to update its location

							if (piece.targetPose != null && piece.targetPose.reachedSize(piece.image)
									&& piece.targetPose.reachedPos(piece)) {
								if (!piece.poses.isEmpty()) {
									piece.targetPose = piece.poses.pop();
									piece.targetPose.start();
								}else {
									piece.targetPose = null;
								}
							}
							piece.updatePose();
						}
					}
				}
			}
		});
		updateTimer.start();
	}

	private Color color;

	private GImage image;
	private GImage imageAnimated;
	private GOval testOval;

	private int x, y, size, r, c;

	private boolean active = false;


	private Pose targetPose;

	private LinkedList<Pose> poses = new LinkedList<>();

	/**
	 * Constructor where color and image are randomly chosen out of a predefined set
	 *
	 * @param x    the x position of the piece
	 * @param y    the y position of the piece
	 * @param size the width and height of the piece
	 */
	public BetterPiece(int x, int y, int size, int r, int c) {
		this(x, y, size, getRandomColor(), r, c);
	}

	public BetterPiece(int spaceSize, int size, int r, int c) {
		this(spaceSize * (r + 1), spaceSize * (c + 1), size, r, c);
	}

	public BetterPiece(int x, int y, int size, Color color, int r, int c) {
		this.color = color;
		this.x = x;
		this.y = y;
		this.size = size;
		this.initImage();
		this.targetPose = null;
		this.r = r;
		this.c = c;
		// Add the piece to the list of pieces to update
		synchronized (pieces) {
			pieces.add(new WeakReference<>(this));
		}
	}

	private static Color getRandomColor() {
		return Color.values()[random.nextInt(Color.values().length)];
	}

	public Color setGemColor(BetterPiece[][] b) {
		return Color.values()[random.nextInt(Color.values().length)];
	}

	public void reposition(int x, int y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.image.setSize(size, size);
		this.imageAnimated.setSize(size, size);
		this.updateImage();
		this.setLocation(x, y);
	}

	public void updateRowCol(int r, int c) {
		this.r = r;
		this.c = c;
	}

	public int getR() {
		return r;
	}

	public int getC() {
		return c;
	}

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

	private void updateImage() {
		remove(this.image);
		remove(this.imageAnimated);
		add(active ? this.imageAnimated : this.image);
	}

	public void clearPiece() {
		remove(this.image);
		remove(this.imageAnimated);
		add(testOval);
	}

	public void toggleActive() {
		this.active = !this.active;
		this.updateImage();
	}

	public Color getColorType() {
		return color;
	}

	public void setPose(Pose pose) {
		if (targetPose == null) {
			this.targetPose = pose;
			this.targetPose.start();
		} else {
			this.poses.add(pose);
		}
	}

	public boolean animating() {
		return !this.poses.isEmpty();
	}

	private void updatePose() {
		if (this.targetPose == null || !this.targetPose.shouldAnimate())
			return;
		double deltaX = Pose.clamp(this.targetPose.getX() - this.getX(), -MOVEMENT_SPEED, MOVEMENT_SPEED);
		double deltaY = Pose.clamp(this.targetPose.getY() - this.getY(), -MOVEMENT_SPEED, MOVEMENT_SPEED);

		double deltaWidth = Pose.clamp(this.targetPose.getWidth() - this.getSize().getWidth(), -MOVEMENT_SPEED, MOVEMENT_SPEED);
		double deltaHeight = Pose.clamp(this.targetPose.getHeight() - this.getSize().getHeight(), -MOVEMENT_SPEED, MOVEMENT_SPEED);

		this.move(deltaX, deltaY);
		double newHeight = image.getSize().getHeight() + deltaHeight;
		double newWidth = image.getSize().getWidth() + deltaWidth;
		this.image.setSize(newWidth, newHeight);
		this.imageAnimated.setSize(newHeight, newWidth);
		this.x = (int) this.getX();
		this.y = (int) this.getY();
	}

	public enum Color {
		YELLOW(new java.awt.Color(250, 240, 66)), GREEN(new java.awt.Color(67, 153, 58)), BLUE(
				new java.awt.Color(24, 30, 219)), RED(java.awt.Color.RED);

		private java.awt.Color color;

		private Color(java.awt.Color color) {
			this.color = color;
		}

		public java.awt.Color getColor() {
			return this.color;
		}
	}

	public void setTargetLocation(int x, int y) {
		this.setPose(new Pose(x, y, (int) this.getSize().getWidth(), (int) this.getSize().getHeight()));
	}
}
