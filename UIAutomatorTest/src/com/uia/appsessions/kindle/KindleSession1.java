package com.uia.appsessions.kindle;

import com.uia.apphandlers.Kindle;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.uia.common.Controller;

public class KindleSession1 {
	
	public void runSession() throws UiObjectNotFoundException
	{
		
		Kindle k = new Kindle();
		Controller.utilities.execute(Controller.utilities.cmdBroadcastIntent("S-KindleSession1"));
		k.openBook();
		k.manageSettings();
		k.readBook();
		Controller.utilities.execute(Controller.utilities.cmdBroadcastIntent("E-Kindlesession1"));
	}

}
