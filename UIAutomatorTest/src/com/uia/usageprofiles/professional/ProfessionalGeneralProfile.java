package com.uia.usageprofiles.professional;

import android.os.RemoteException;

import com.android.uiautomator.core.UiObjectNotFoundException;

import com.uia.common.Controller;
import com.uia.usagesessions.Session1;
import com.uia.usagesessions.Session2;
import com.uia.usagesessions.Session3;

public class ProfessionalGeneralProfile {
	
	public void runProfile()throws UiObjectNotFoundException, RemoteException{
		
		Controller.device.sleep();
		Session1.run();
		Controller.instance.sleep(2000);
		Session2.run();
		Controller.instance.sleep(2000);
		Session3.run();
		
	}
	

}
