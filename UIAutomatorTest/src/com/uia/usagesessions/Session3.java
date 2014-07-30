package com.uia.usagesessions;

import android.os.RemoteException;

import com.android.uiautomator.core.UiObjectNotFoundException;
import com.uia.apphandlers.AppDetails.APPS_ON_DEVICE;
import com.uia.appsessions.facebook.FacebookSession1;
import com.uia.appsessions.googleplay.GooglePlaySession1;
import com.uia.common.Controller;

public class Session3 {

public static void run()throws UiObjectNotFoundException,RemoteException{
		
		Controller.device.wakeUp();
		Controller.utilities.unlockFromPin();
		
		Controller.utilities.goToHomeScreen();
		
		Controller.utilities.launchAppFromAllApps(APPS_ON_DEVICE.FACEBOOK);
		new FacebookSession1().runSession();
		
		Controller.utilities.goToHomeScreen();
		
		Controller.utilities.launchAppFromAllApps(APPS_ON_DEVICE.GOOGLE_PLAY_STORE);
		new GooglePlaySession1().runSession();
		
		Controller.utilities.goToHomeScreen();
		Controller.device.sleep();
			
	}
}
