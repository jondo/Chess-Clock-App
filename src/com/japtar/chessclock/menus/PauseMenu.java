/**
 * 
 */
package com.japtar.chessclock.menus;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.admob.android.ads.AdView;
import com.japtar.chessclock.MainActivity;
import com.japtar.chessclock.R;

/**
 * Menu for the pause layout
 * @author japtar10101
 */
class PauseMenu implements MenuInterface {
	/* ===========================================================
	 * Members
	 * =========================================================== */
	/** The main activities class */
	private final MainActivity mParentActivity;
	/** The button listener! */
	private final TimersMenu mParentMenu;
	
	// == Layouts ==
	/** A pause screen, generally left invisible */
	private RelativeLayout mPauseLayout = null;
	
	// == Buttons ==
	/** The All-purpose (mainly for pausing the game) button */
	private Button mPauseButton = null;
	
	// == Labels ==
	/** Label that appears on the pause screen */
	private TextView mPauseLabel = null;
	
	// == Misc. ==
	/** The visible adds */
	private AdView mAds = null; 
	
	/* ===========================================================
	 * Constructors
	 * =========================================================== */
	/**
	 * @param parent the menu's parent activity
	 */
	public PauseMenu(final MainActivity parent,
			final TimersMenu buttonListener) {
		// Setup variables
		mParentActivity = parent;
		mParentMenu = buttonListener;
	}

	/* ===========================================================
	 * Override Methods
	 * =========================================================== */
	/**
	 * Resumes the timer if paused, or starts it over.
	 */
	public void setupMenu() {
		// == Load up all the member variables ==
		
		// Grab layouts
		mPauseLayout = (RelativeLayout)
			mParentActivity.findViewById(R.id.layoutPause);
		mAds = (AdView) mParentActivity.findViewById(R.id.ad);
		
		// Grab the button
		mPauseButton = mParentMenu.getButton(R.id.buttonPause);
		
		// Grab the label
		mPauseLabel = (TextView)mParentActivity.findViewById(R.id.labelPause);

		// == Setup the member variables ==

		// Update the text size on everything
		mPauseButton.setTextSize(MainActivity.msTextSize * 0.5f);
		mPauseLabel.setTextSize(MainActivity.msTextSize);
	}
	
	/* ===========================================================
	 * Public Methods
	 * =========================================================== */
	/**
	 * @param buttonPressed the button pressed
	 * @return true if the pause button is pressed
	 */
	public boolean isPauseButtonClicked(final View buttonPressed) {
		// By default, return true
		boolean toReturn = false;
		
		// Check if the button clicked is the pause button
		if(buttonPressed != null) {
			toReturn = buttonPressed.equals(mPauseButton);
		}
		return toReturn;
	}
	
	/**
	 * Hides the pause menu
	 */
	public void hideMenu() {
		mPauseLayout.setVisibility(View.INVISIBLE);
		mPauseButton.setText(mParentActivity.getString(
				R.string.pauseButtonText));
	}
		
	/**
	 * Indicate the game just started
	 */
	public void startup() {
		// Update the pause button text
		mPauseButton.setText(
				mParentActivity.getString(R.string.settingsMenuLabel));
		
		// Set both layouts to be invisible
		mPauseLayout.setVisibility(View.INVISIBLE);
		mAds.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * Indicate the game is paused
	 */
	public void paused() {
		// Set the pause layout as visible
		mPauseLayout.setVisibility(View.VISIBLE);
		mPauseLabel.setText(
				mParentActivity.getString(R.string.pauseLabelText));
		
		// Hide the add (as to not cover the delay label)
		mAds.setVisibility(View.INVISIBLE);

		// Update the pause button text
		mPauseButton.setText(
				mParentActivity.getString(R.string.resumeButtonText));
	}
	
	/**
	 * Indicate the time is up
	 */
	public void timesUp() {
		// Set the times-up layout as visible
		mPauseLayout.setVisibility(View.VISIBLE);
		mPauseLabel.setText(
				mParentActivity.getString(R.string.timesUpLabelText));
		mAds.setVisibility(View.VISIBLE);
		
		// Update the pause button text
		mPauseButton.setText(mParentActivity.getString(R.string.newGameButtonText));
	}
}
