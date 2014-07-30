package com.uia.common;

import android.util.Log;
import android.os.RemoteException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

import com.uia.apphandlers.AppDetails.APPS_ON_DEVICE;
import com.uia.common.UtilityFunctions;

import com.uia.usageprofiles.professional.ProfessionalGeneralProfile;

public class Controller extends UiAutomatorTestCase {
	
	public static UtilityFunctions utilities;
	public static UiDevice device;
	public static Controller instance;
	public APPS_ON_DEVICE displayedApp;
	
	
	public void startRun() throws UiObjectNotFoundException, RemoteException {
		
		utilities=new UtilityFunctions();
		instance=this;
		device=getUiDevice();
				
		Log.i("ROOT",""+this.getUiDevice().getCurrentPackageName()+" : "
				+this.getUiDevice().getLastTraversedText());
		
		System.out.println("S-ProfessionalGeneralProfile");
		Controller.utilities.execute(Controller.utilities.cmdBroadcastIntent("S-ProfessionalGeneralProfile"));
		new ProfessionalGeneralProfile().runProfile();
		Controller.utilities.execute(Controller.utilities.cmdBroadcastIntent("E-ProfessionalGeneralProfile"));
		System.out.println("E-ProfessionalGeneralProfile");
		
	}
}
