package com.uia.usagesessions;

import android.os.RemoteException;

import com.android.uiautomator.core.UiObjectNotFoundException;
import com.uia.apphandlers.AppDetails.APPS_ON_DEVICE;
import com.uia.appsessions.kindle.KindleSession1;
import com.uia.common.Controller;

public class Session2 {
	
	public static void run()throws UiObjectNotFoundException,RemoteException{
		
		Controller.device.wakeUp();
		Controller.utilities.unlockFromPin();
		
		Controller.utilities.goToHomeScreen();
		
		Controller.utilities.launchAppFromAllApps(APPS_ON_DEVICE.AMAZON_KINDLE);
		new KindleSession1().runSession();
		
		Controller.utilities.goToHomeScreen();
		Controller.device.sleep();
			
		}

}
