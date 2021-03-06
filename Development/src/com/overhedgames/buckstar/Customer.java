package com.overhedgames.buckstar;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.util.Log;
import android.util.Pair;

import com.overhedgames.buckstar.enums.AttributeLevel;
import com.overhedgames.buckstar.enums.CustomerType;
import com.overhedgames.buckstar.enums.Direction;
import com.overhedgames.buckstar.enums.DrinkType;
import com.overhedgames.buckstar.enums.CustomerState;
import com.overhedgames.buckstar.globals.ApplicationData;
import com.overhedgames.buckstar.parameters.Parameters_Customer;

public class Customer extends GameObject {
	private static String TAG = Customer.class.getSimpleName();
	
	private int facingDirection = Direction.Left;
	
	private CustomerType custType;
	
	private AttributeLevel menuSatisfaction;
	private AttributeLevel purchaseSatisfaction;
	private AttributeLevel wealth;
	private ArrayList<Pair<DrinkType, AttributeLevel>> drinkTypeRatings;
	private CustomerFacilityInfo facilityInfo;
	
	private long waitTime;	
	private long maxWaitTime;
	
	private CustomerState currentState;	
	
	public Customer(CustomerType custType, Point location, Boolean isAnimating, CustomerFacilityInfo facilityInfo) {
		
		super(Parameters_Customer.getAnimationBitmaps(custType), location, Parameters_Customer.CUSTOMER_SPEED, 
				isAnimating, Parameters_Customer.CUSTOMER_FPS);
		
		init(custType, facilityInfo);
		
	}			
	private void init(CustomerType custType, CustomerFacilityInfo facilityInfo) {
		try {
			switch(custType) {
				case Adult:
					this.maxWaitTime = Parameters_Customer.CUSTOMER_ADULT_MAX_WAIT_TIME;
					break;
				case Elderly:
					//@todo
					break;
				case Executive:
					//@todo
					break;
				case Kid:
					//@todo
					break;
				case Teenager:
					//@todo
					break;
				default:
					//@todo
					break;
			}			
			this.drinkTypeRatings = Parameters_Customer.getCustDrinkTypeRatings(custType);						
			this.facilityInfo = facilityInfo;
			this.custType = custType;
			this.currentState = Parameters_Customer.CUSTOMER_DEFAULT_STATE;	// default initial state
			this.facingDirection = Parameters_Customer.getCustomerFacingDirection(this.getCurrentLocation());
		}catch(Exception ex) { 
			// @todo
		}
	}
	public void update(CustomerFacilityInfo facilityInfo) {
		switch(currentState) {
			case Browsing:
				//Log.d(Customer.TAG, "Browsing; Current Location: " + this.getCurrentLocation().toString());
				// @todo 
				// check current facility for location of menu, set as targetLoc
				if(this.getTargetLocation() == null) {
					this.setTargetLocation(this.facilityInfo.getMenuLocation());
				} else if(this.getCurrentLocation().equals(this.getTargetLocation())) {
					// once at loc, process menu options
					// hold for a few seconds (5 sec or so)
					//@todo menu options check
					
					this.setTargetLocation(null);			// reset target location
					if(true) {
						this.currentState = CustomerState.Waiting;
						this.waitTime = 0;
						this.setIsAnimating(false);
					}
				}
				
				// change state to Leaving or Waiting depending on menu choices avail
				break;
			case Waiting:				
				waitTime++;
				//Log.d(Customer.TAG, "Waiting. Current Wait Time: " + waitTime);
				if(this.waitTime > this.maxWaitTime) {
					this.currentState = CustomerState.Leaving;
					this.setIsAnimating(true);
				}
				break;
			case Buying:
				break;
			case Leaving:
				if(this.getTargetLocation() == null) {
					this.setTargetLocation(this.facilityInfo.getExitLocation());
					this.setIsAnimating(true);
					this.facingDirection = Direction.Right;
				} else if (this.getTargetLocation().equals(this.getCurrentLocation())) {
					this.setIsAnimating(false);
				}
				break;
		}
		
		super.update();
	}
	
	@Override
	public void render(Canvas canvas) {
		if(animationBitmaps.length > 0) {
			//canvas.drawBitmap(animationBitmaps[currentBitmapIndex],
				//		currentLocation.x - (animationBitmaps[currentBitmapIndex].getWidth() / 2),
					//	currentLocation.y - (animationBitmaps[currentBitmapIndex].getHeight() / 2),
						//null);
			
			if(this.facingDirection == Direction.Right) { 
				canvas.drawBitmap(animationBitmaps[currentBitmapIndex], currentLocation.x,  currentLocation.y, null);
			} else {
				Matrix m = new Matrix();
				m.setScale(-1, 1);
				m.postTranslate(animationBitmaps[currentBitmapIndex].getWidth(), 0);
				m.postTranslate(this.currentLocation.x, this.currentLocation.y);
				canvas.drawBitmap(animationBitmaps[currentBitmapIndex], m, null);
				
			}	
		}
	} 

}
