package com.uia.apphandlers;

import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;

import com.uia.apphandlers.AppDetails.APPS_ON_DEVICE;
import com.uia.apphandlers.AppDetails.APPS_ON_PLAY;
import com.uia.common.Controller;

import android.util.Log;
import android.view.KeyEvent;


public class GooglePlay extends UIEventHandler{
	
	public GooglePlay(){
		packageName=APPS_ON_DEVICE.GOOGLE_PLAY_STORE.getPackageName();
	}
	
	
	public boolean checkConnection() throws UiObjectNotFoundException{

		if(txtViewTxtExists("No connection")){
			clickButtonWait("Retry");
			if(txtViewTxtExists("No connection"))		
				return false;
		}
		
		return true;
	}
	
	public void goToGooglePlayHomeScreen(){
		do{
			if(txtViewTxtExists("Google Play"))
				return;
			else
				Controller.device.pressBack();
		}while(true);
	}
	
	public void search(String searchText, String category) throws UiObjectNotFoundException{
		
//		UiObject obj=new UiObject(new UiSelector().packageName(packageName)
//				.className("android.widget.TextView").text("GAMES"));
//		if(obj.exists())
//			obj.clickAndWaitForNewWindow();
//		{
		boolean test=clickTxtViewTxt("GAMES");
		Log.i("ROOT", test==true?"success":"failure");
		if(test){
			Log.i("ROOT","In Games");
			UiObject obj=new UiObject(new UiSelector().packageName(packageName)
				.className("android.widget.TextView").description("Search Google Play"));
			if(obj.exists())
				{obj.clickAndWaitForNewWindow();
				//if(clickTxtViewDescWait("Search Google Play")){
					searchText=searchText.toUpperCase();
					for(char c : searchText.toCharArray()){
						if(c==' ')
							Controller.device.pressKeyCode(KeyEvent.KEYCODE_SPACE);
						else
							Controller.device.pressKeyCode(KeyEvent.keyCodeFromString("KEYCODE_"+c));
					}					
					Controller.device.pressKeyCode(KeyEvent.KEYCODE_ENTER);	
				
			}	
		}
		
	}
	public void installAndOpen(APPS_ON_PLAY app)throws UiObjectNotFoundException{
		
		search(app.getSearchString(),app.getCategory());
		
		// Clicking the Settings
        UiScrollable searchResults = new UiScrollable(new UiSelector().scrollable(true));
        searchResults.setAsVerticalList();

		UiObject inst = searchResults.getChildByDescription(new UiSelector().packageName("com.android.vending")
				.className("android.widget.RelativeLayout"), app.getDescription());				
		
		if(inst.exists()){
			inst.clickAndWaitForNewWindow();
			if(clickButton("INSTALL"))
				if(clickButton("ACCEPT"))
					clickButtonWaitForExists("OPEN",2*60*1000);					
		}
	}
	
}
