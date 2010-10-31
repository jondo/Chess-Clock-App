package com.app.chessclock;

import java.security.InvalidParameterException;
import java.util.HashMap;

import com.app.chessclock.enums.MenuId;
import com.app.chessclock.menus.ActivityMenu;
import com.app.chessclock.menus.OptionsMenu;
import com.app.chessclock.menus.TimersMenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends Activity {
	/* ===========================================================
	 * Member Variables
	 * =========================================================== */
	/** The current layout */
	private MenuId mCurrentMenuId = null;
	private final HashMap<MenuId, ActivityMenu> mAllMenus =
		new HashMap<MenuId, ActivityMenu>();
	
	/* ===========================================================
	 * Overrides
	 * =========================================================== */
    /**
     * Called when the activity is first created.
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
    	// Do whatever is in the super class first
        super.onCreate(savedInstanceState);
        
        // Update all constant variables
        this.getWindowManager().getDefaultDisplay().getMetrics(Global.DISPLAY);
        Global.OPTIONS.setSavedState(savedInstanceState);
        
        // Create all the layouts
        mAllMenus.put(MenuId.TIMER, new TimersMenu(this));
        mAllMenus.put(MenuId.OPTIONS, new OptionsMenu(this));
        
        // Set the default layout to main
        this.setCurrentMenuId(MenuId.TIMER);
    }
    
    /**
     * Creates the initial menu layout
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
    	// Generate the menu
        final MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        
        // Return true
        return true;
    }
    
    /**
     * Returns true only if the layout is currently set to timer
     * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
    	boolean toReturn = false;
    	
    	// Check if the layout is currently set to Timer
    	if(mCurrentMenuId == MenuId.TIMER) {
    		toReturn = true;
    	}
    	
    	return toReturn;
    }

	/**
	 * Switches the layout based on the item selected
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		boolean toReturn = true;
			
		// Handle item selection
		switch(item.getItemId()) {
			// If options is clicked, go to options menu
			case R.id.menuOptions:
				this.setCurrentMenuId(MenuId.OPTIONS);
				break;
			    
			// If reset is clicked, restart the timers menu
			case R.id.menuReset:
				this.setCurrentMenuId(MenuId.TIMER);
				break;
			
			// Otherwise, let the super class figure it out
			default:
				toReturn = super.onOptionsItemSelected(item);
		}
		return toReturn;
	}
    
	/* ===========================================================
	 * Public Methods
	 * =========================================================== */
	/**
	 * Gets a menu based on ID
	 * @param menuId Menu to get
	 * @throws InvalidParameterException if <b>menuId</b> is null
	 */
	public ActivityMenu getMenu(final MenuId menuId) {
		ActivityMenu toReturn = null;
		
		if(mAllMenus.containsKey(menuId)) {
			toReturn = mAllMenus.get(menuId);
		}
		
		return toReturn;
	}
	
	/**
	 * Switches this activity's layout
	 * @param layout Layout to switch to
	 */
	public ActivityMenu getCurrentMenu() {
		return this.getMenu(mCurrentMenuId);
	}

	/* ===========================================================
	 * Getters
	 * =========================================================== */
	/**
	 * @return {@link mCurrentMenuId}
	 */
	public MenuId getCurrentMenuId() {
		return mCurrentMenuId;
	}

	/* ===========================================================
	 * Setters
	 * =========================================================== */
	/**
	 * @param menuId sets {@link mCurrentMenuId}
	 * @throws InvalidParameterException if <b>currentLayoutId</b> is null
	 */
	public void setCurrentMenuId(final MenuId menuId) {
		// Check parameter if it's null
		if(menuId == null) {
			throw new InvalidParameterException("Menu ID cannot be null");
		}
		
		// Exit the current layout
		ActivityMenu tempLayout = this.getCurrentMenu();
		tempLayout.exitLayout();
		
		// Update the layout ID
		mCurrentMenuId = menuId;
		
		// Setup the new layout
		tempLayout = this.getCurrentMenu();
		tempLayout.setupLayout();
	}
}
