package com.uia.apphandlers;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import junit.framework.Assert;

import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.uia.apphandlers.AppDetails.APPS_ON_DEVICE;
import com.uia.common.*;

public class Clock extends UIEventHandler {
	
	public Clock(){
		packageName=APPS_ON_DEVICE.CLOCK.getPackageName();
	}
	/**
	 * Helper function to set an alarm
	 * 
	 * @param minutesFromNow
	 * @throws UiObjectNotFoundException
	 */
	public void setAlarm(int minutesFromNow) throws UiObjectNotFoundException {
		
		
		UiObject setAlarm = new UiObject(
				new UiSelector().description("Alarms"));
		if (!setAlarm.exists())
			setAlarm = new UiObject(
					new UiSelector().textStartsWith("Set alarm"));
		setAlarm.click();

		UtilityFunctions uti=Controller.utilities;
		// let's add an alarm
		uti.clickByDescription("Add alarm");
		// let's set the time
		//clickByText("Time");
		
		Calendar c=Calendar.getInstance();
		SimpleDateFormat format=new SimpleDateFormat("HHmm");
		c.add(Calendar.MINUTE, 1);
		String now=format.format(c.getTime());
		
		Log.i("ROOT","alarm time: "+now);
		new UiObject(new UiSelector().packageName(packageName).className("android.widget.Button")
				.text(""+now.charAt(0))).click();
		new UiObject(new UiSelector().packageName(packageName).className("android.widget.Button")
		.text(""+now.charAt(1))).click();
		new UiObject(new UiSelector().packageName(packageName).className("android.widget.Button")
		.text(""+now.charAt(2))).click();
		new UiObject(new UiSelector().packageName(packageName).className("android.widget.Button")
		.text(""+now.charAt(3))).click();
		
		
		//clickByText("Ok");

		// few confirmations to click thru
		UiObject doneButton = new UiObject(new UiSelector().text("Done"));
		UiObject okButton = new UiObject(new UiSelector().text("OK"));
		// working around some inconsistencies in phone vs tablet UI
		if (doneButton.exists()) {
			doneButton.click();
		} else {
			okButton.click(); // let it fail if neither exists
		}

		// we're done. Let's return to home screen
		//clickByText("Done");
		uti.goToHomeScreen();
	}
	public void alarmHandler() throws UiObjectNotFoundException,
	RemoteException 
	{
		Controller.utilities.execute(Controller.utilities.cmdBroadcastIntent("S-Alarm Setting"));
		setAlarm(1);
		Controller.device.sleep();
		Controller.utilities.execute(Controller.utilities.cmdBroadcastIntent("E-Alarm Setting"));
		// wait for the alarm alert dialog
		Controller.utilities.execute(Controller.utilities.cmdBroadcastIntent("S-Alarm Wait And Ring"));
		UiObject alarmAlert = new UiObject(new UiSelector()
				.packageName("com.google.android.deskclock")
				.className(TextView.class.getName()).text("Alarm"));

		Assert.assertTrue("Timeout while waiting for alarm to go off",
				alarmAlert.waitForExists(2 * 60 * 1000));

		Controller.instance.sleep(5000);
		
		Controller.device.swipe(380, 870, 750, 870, 50);
		Controller.utilities.execute(Controller.utilities.cmdBroadcastIntent("E-Alarm Wait And Ring"));
		
	}

}
