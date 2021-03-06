/**
 * Chess Clock App
 * https://github.com/japtar10101/Chess-Clock-App
 * 
 * The MIT License
 * 
 * Copyright (c) 2011 Taro Omiya
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.japtar.chessclock.models;

import java.security.InvalidParameterException;

import android.content.SharedPreferences;

/**
 * Helper class representing time.
 * @author japtar10101
 */
public class TimeModel implements Comparable<TimeModel> {
	/* ===========================================================
	 * Static functions
	 * =========================================================== */
	/**
	 * @return Converts <b>integer</b> into a byte
	 */
	public final static byte intToByte(final int integer) {
		return (integer <= Byte.MAX_VALUE ? (byte) integer : Byte.MAX_VALUE);
	}
	public final static int totalSeconds(final TimeModel time) {
		return time.mSeconds;
	}
	private final static StringBuffer mStringGenerator = new StringBuffer();
	
	/* ===========================================================
	 * Members
	 * =========================================================== */
	/** Seconds */
	private int mSeconds;
	/** If true, negative values will be allowed */
	private boolean mAllowNegatives = false;
	
	/* ===========================================================
	 * Constructors
	 * =========================================================== */
	/**
	 * Constructor
	 */
	public TimeModel(final int minutes, final int seconds) throws
			InvalidParameterException {
		this.setSeconds(seconds);
		this.setMinutes(minutes);
	}
	
	/**
	 * Constructor for saved values.
	 */
	public TimeModel(final int storedValue) throws InvalidParameterException {
		// Convert the stored value into seconds
		mSeconds = storedValue;
	}
	
	/**
	 * Default constructor.  Sets the time to 0.
	 */
	public TimeModel() {
		mSeconds = 0;
	}

	/* ===========================================================
	 * Override
	 * =========================================================== */
	/**
	 * Compares which time model is greater, and by how many seconds.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final TimeModel another) {
		return mSeconds - totalSeconds(another);
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// Generate a string
		mStringGenerator.delete(0, mStringGenerator.length());
		
		// If negative, add in a minus sign
		if(mSeconds < 0) {
			mStringGenerator.append('-');
		}
		
		// Add in the minutes and colon
		mStringGenerator.append(this.getMinutes());
		mStringGenerator.append(':');
		
		// Append the seconds
		final int seconds = this.getSeconds();
		if(seconds < 10) {
			// If seconds is less than 10, append a 0
			mStringGenerator.append('0');
		}
		mStringGenerator.append(seconds);
		
		return mStringGenerator.toString();
	}
	
	/* ===========================================================
	 * Public Methods
	 * =========================================================== */
	/**
	 * Sets the time.
	 * @param minutes sets {@link mMinutes}
	 * @param seconds sets {@link mSeconds}
	 * @throws InvalidParameterException if <b>minutes</b> is negative
	 * @throws InvalidParameterException if <b>seconds</b> is negative,
	 * or greater than 59
	 */
	public void setTime(final int minutes, final int seconds) throws
			InvalidParameterException {
		// Make sure the parameter are correct
		if(minutes < 0) {
			throw new InvalidParameterException("Minutes must be positive value");
		} else if(seconds < 0) {
			throw new InvalidParameterException("Seconds must be positive value");
		} else if(seconds > 59) {
			throw new InvalidParameterException("Seconds must be less than 60");
		}
		
		// Set the time
		mSeconds = seconds + minutes * 60;
	}
	
	/**
	 * Sets the time.
	 * @param model time to match
	 */
	public void setTime(final TimeModel model) {
		if(model != null) {
			mSeconds = totalSeconds(model);
		}
	}
	
	/**
	 * Sets the time based on the saved state's value
	 * @param savedState
	 * @param key
	 * @param defaultValue
	 * @throws InvalidParameterException 
	 * @throws InternalError 
	 */
	public void recallTime(final SharedPreferences savedState,
			final String key, final int defaultValue) throws
			InvalidParameterException, InternalError {
		// Make sure the parameters are valid
		if(savedState == null) {
			throw new InvalidParameterException("SharedPreference is null");
		} else if(key == null) {
			throw new InvalidParameterException("String is null");
		}
		
		// Recall the attributes from the bundle
		final int savedValue = savedState.getInt(key, defaultValue);
		
		// Make sure this value is valid
		if(savedValue < 0) {
			throw new InternalError(
					"The stored value must be a non-negative value");
		}
		
		// Convert the stored value into seconds
		mSeconds = savedValue;
	}
	
	/**
	 * Stores the time
	 * @param saveEditor
	 * @param key
	 * @throws InvalidParameterException
	 */
	public void saveTime(final SharedPreferences.Editor saveEditor,
			final String key) throws InvalidParameterException {
		// Make sure the parameters are valid
		if(saveEditor == null) {
			throw new InvalidParameterException("SharedPreference is null");
		} else if(key == null) {
			throw new InvalidParameterException("String is null");
		}
	
		// Save the attributes to the bundle
		saveEditor.putInt(key, totalSeconds(this));
	}
	
	/**
	 * @return True if both the minutes and seconds are 0
	 */
	public boolean isTimeZero() {
		return mSeconds == 0;
	}
	
	/**
	 * @return True if the time is negative
	 */
	public boolean isTimeNegative() {
		return mSeconds < 0;
	}
	
	/**
	 * Increment the time
	 */
	public void incrementTime(final TimeModel model) {
		mSeconds += totalSeconds(model);
	}
	
	/**
	 * Decreases the time by a second
	 * @return False if both minutes and seconds are 0
	 * @see #isTimeZero()
	 */
	public boolean decrementASecond() {
		// Check if we're at zero, first
		boolean toReturn = !this.isTimeZero();
		if(mAllowNegatives || toReturn) {
			// Decrement the seconds 
			--mSeconds;
		}
		
		return toReturn;
	}
	
	/**
	 * Increases the time by a second, if not negative
	 * @return False if time is negative
	 * @see #isTimeZero()
	 */
	public boolean incrementASecond() {
		// Check if we're negative, first
		boolean toReturn = !isTimeNegative();
		if(toReturn) {
			// Increment the seconds 
			++mSeconds;
		}
		return toReturn;
	}

	/* ===========================================================
	 * Getters
	 * =========================================================== */
	/**
	 * @return Minutes. Always positive.
	 */
	public int getMinutes() {
		// Get the total seconds
		int toReturn = mSeconds;
		
		// If negative, make it positive
		if(toReturn < 0) {
			toReturn *= -1;
		}
		
		// Divide by 60
		toReturn /= 60;
		
		return toReturn;
	}
	/**
	 * @return Seconds. Always positive.
	 */
	public int getSeconds() {
		// Get the total seconds
		int toReturn = mSeconds;
		
		// If negative, make it positive
		if(toReturn < 0) {
			toReturn *= -1;
		}
		
		// Modulo by 60
		toReturn %= 60;
		
		return toReturn;
	}

	/* ===========================================================
	 * Setters
	 * =========================================================== */
	/**
	 * @param minutes sets {@link mMinutes}
	 * @throws InvalidParameterException if <b>minutes</b> is negative
	 */
	public void setMinutes(final int minutes) {
		// Make sure the parameter is correct
		if(minutes < 0) {
			throw new InvalidParameterException("Minutes must be positive value");
		}
		
		// Set the time to positive seconds
		mSeconds = this.getSeconds();
		
		// Add the minutes
		mSeconds += minutes * 60;
	}
	
	/**
	 * @param seconds sets {@link mSeconds}
	 * @throws InvalidParameterException if <b>seconds</b> is negative,
	 * or greater than 59
	 */
	public void setSeconds(final int seconds) throws InvalidParameterException {
		// Make sure the parameter is correct
		if(seconds < 0) {
			throw new InvalidParameterException("Seconds must be positive value");
		} else if(seconds > 59) {
			throw new InvalidParameterException("Seconds must be less than 60");
		}
		
		// First, get the minutes
		int minutes = this.getMinutes();
		
		// Set the time to positive seconds
		mSeconds = seconds;
		
		// Add the minutes
		mSeconds += minutes * 60;
	}
}
