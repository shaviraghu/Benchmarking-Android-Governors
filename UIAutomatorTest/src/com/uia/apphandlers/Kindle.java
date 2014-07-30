package com.uia.apphandlers;

import android.util.Log;

import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;


import com.uia.apphandlers.AppDetails.APPS_ON_DEVICE;
import com.uia.common.*;


public class Kindle extends UIEventHandler {
	
	public Kindle(){
		this.packageName=APPS_ON_DEVICE.AMAZON_KINDLE.getPackageName();
	}
	
	public void openBook() throws UiObjectNotFoundException {
		clickTxtViewTxtConWait("%",0);
	}

	public void readBook() throws UiObjectNotFoundException {
		UiObject pageFrame=new UiObject(new UiSelector().packageName(packageName)
				.className("android.widget.FrameLayout"));
		if(pageFrame.exists()){
			Log.i("ROOT","pageswipe");
			for(int i=0;i<5;i++){	
				pageFrame.swipeRight(0);
				Controller.instance.sleep(3000);
			}
		}
	}

	public void manageSettings() throws UiObjectNotFoundException {
		if(clickTxtViewDesc("View Options")){
			
			clickButton("Black");
			clickButton("White");
			clickButton("Sepia");
						
			Controller.device.pressBack();
		}
		
		/*UiObject seekbar=new UiObject(new UiSelector().packageName("com.amazon.kindle")
				.className("android.widget.SeekBar"));
		if(seekbar.exists()){
			seekbar.swipeRight(100);
		}*/
		
	}

}
