package com.uia.appsessions.facebook;

import com.android.uiautomator.core.UiObjectNotFoundException;
import com.uia.apphandlers.Facebook;
import com.uia.common.Controller;

public class FacebookSession1 {
	public void runSession() throws UiObjectNotFoundException
	{
		Facebook fb = new Facebook();
		Controller.utilities.execute(Controller.utilities.cmdBroadcastIntent("S-FacebookSession1"));
		//if(fb.checkConnection())
		fb.uploadPic();
		Controller.utilities.execute(Controller.utilities.cmdBroadcastIntent("E-FacebookSession1"));
	}
}
