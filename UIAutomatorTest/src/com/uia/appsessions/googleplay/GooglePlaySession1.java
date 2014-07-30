package com.uia.appsessions.googleplay;

import com.android.uiautomator.core.UiObjectNotFoundException;
import com.uia.apphandlers.AppDetails.APPS_ON_PLAY;
import com.uia.apphandlers.GooglePlay;
import com.uia.common.Controller;

public class GooglePlaySession1 {
	
	public void runSession() throws UiObjectNotFoundException
	{
		GooglePlay gp = new GooglePlay();
		Controller.utilities.execute(Controller.utilities.cmdBroadcastIntent("S-GooglePlaySession1"));
		if(gp.checkConnection())
			gp.installAndOpen(APPS_ON_PLAY.STUPID_ZOMBIES);
		Controller.utilities.execute(Controller.utilities.cmdBroadcastIntent("E-GooglePlaySession1"));
	}

}
