package com.uia.appsessions.clock;

import android.os.RemoteException;

import com.android.uiautomator.core.UiObjectNotFoundException;
import com.uia.apphandlers.Clock;
import com.uia.common.Controller;

public class ClockSession1 {
	
	public void runSession() throws UiObjectNotFoundException,RemoteException
	{
		Clock clk = new Clock();
		Controller.utilities.execute(Controller.utilities.cmdBroadcastIntent("S-ClockSession1"));
		clk.alarmHandler();
		Controller.utilities.execute(Controller.utilities.cmdBroadcastIntent("E-ClockSession1"));
	}
	
}
