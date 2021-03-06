package thewetbandits;

import thewetbandits.screens.Screen;
import thewetbandits.screens.Screens;
import thewetbandits.utils.GraphicsApplication;
import thewetbandits.utils.GraphicsPane;

import java.awt.event.MouseEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Jacob Faulk Created Mar 28, 2018
 */

public class MatchThreeGame extends GraphicsApplication {
	public static final int WINDOW_WIDTH = 1000;
	public static final int WINDOW_HEIGHT = 700;
	public static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	private static final long serialVersionUID = 5086449662569173546L;

	/**
	 * initializes MatchThreeGame according to the specified width and height
	 */
	public void init() {
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		Screens.initialize(this);
	}

	/**
	 * sets testScreen as the main screen and draws it
	 */
	public void run() {
		switchToScreen(Screens.MENU_SCREEN);
	}

	/**
	 * default mousePressed() method. it doesn't do anything yet (as of Mar 31,
	 * 2018)
	 *
	 * @param e the event specifying the details of the click
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		super.mouseClicked(e);
	}


	public <T extends Screen> T getCurrentScreen(Class<T> clazz) {
		GraphicsPane p = this.getCurrentPane();
		if (p.getClass().isAssignableFrom(clazz)) {
			return clazz.cast(p);
		} else {
			return null;
		}
	}
}
