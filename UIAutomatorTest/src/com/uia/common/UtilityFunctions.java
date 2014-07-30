package com.uia.common;

/*import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;*/
import java.util.ArrayList;
import java.util.Arrays;

import android.graphics.Point;


import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;

import android.os.RemoteException;
//import android.util.Log;

import com.uia.common.ExecuteAsRootBase;

import com.uia.apphandlers.AppDetails.APPS_ON_DEVICE;


public class UtilityFunctions extends ExecuteAsRootBase {
	
	public int trepnAppState=100;
	
	private ArrayList<String>cmdsToExec=new ArrayList<String>();
	
	/**
     * For the purpose of this demo, we're declaring the Launcher signatures here.
     * It may be more appropriate to declare signatures and methods related
     * to Launcher in their own reusable Launcher helper file.
     */
    public static class LauncherHelper {
        public static final UiSelector ALL_APPS_BUTTON = new UiSelector().description("Apps");
        public static final UiSelector LAUNCHER_CONTAINER = new UiSelector().scrollable(true);
        public static final UiSelector LAUNCHER_ITEM =
                new UiSelector().className(android.widget.TextView.class.getName());
    }
	 
    public UtilityFunctions() {	}
	
	public void goToHomeScreen() throws UiObjectNotFoundException
	{
		UiObject homescr3=new UiObject(new UiSelector()
		.packageName("com.android.launcher")
		.className(android.view.View.class).description("Home screen 3"));
		while (!homescr3.exists())
		{
			Controller.device.pressHome();
			homescr3=new UiObject(new UiSelector()
			.packageName("com.android.launcher")
			.className(android.view.View.class).description("Home screen 3"));
		}
		
		Controller.instance.displayedApp=APPS_ON_DEVICE.HOME;
	}
	
	public void launchAppFromAllApps(APPS_ON_DEVICE appOnDevice) throws UiObjectNotFoundException
	{
		// open the All Apps view
        UiObject allAppsButton = new UiObject(LauncherHelper.ALL_APPS_BUTTON);
        if(allAppsButton.click())
        	Controller.instance.displayedApp=APPS_ON_DEVICE.ALL_APPS;

        // clicking the APPS tab
        UiSelector appsTabSelector =
                new UiSelector().className(android.widget.TabWidget.class.getName())
                    .childSelector(new UiSelector().text("Apps"));
        UiObject appsTab = new UiObject(appsTabSelector);
        appsTab.click();

        // Clicking the Settings
        UiScrollable allAppsScreen = new UiScrollable(LauncherHelper.LAUNCHER_CONTAINER);
        allAppsScreen.setAsHorizontalList();

		UiObject app = allAppsScreen.getChildByText(LauncherHelper.LAUNCHER_ITEM, appOnDevice.getSearchString());

		if(app.click())
			Controller.instance.displayedApp=appOnDevice;
	}
	public void launchAppsFromRecentApps(String appName) throws RemoteException,UiObjectNotFoundException
	{
		Controller.device.pressRecentApps();

		UiObject appsTab = new UiObject(new UiSelector().text("Apps"));

		appsTab.click();

		UiScrollable appViews = new UiScrollable(
				new UiSelector().scrollable(true));

		appViews.setAsHorizontalList();

		UiObject app = appViews.getChildByText(new UiSelector()
				.className(android.widget.TextView.class.getName()), appName);

		app.click();
	}
	
	public void clickByText(String text) throws UiObjectNotFoundException {
		UiObject obj = new UiObject(new UiSelector().text(text));
		obj.clickAndWaitForNewWindow();
	}

	public void clickByDescription(String text)
			throws UiObjectNotFoundException {
		UiObject obj = new UiObject(new UiSelector().description(text));
		obj.clickAndWaitForNewWindow();
	}
	
	public void swipePattern() throws UiObjectNotFoundException
	{
		
		/*UiObject patternBox = new UiObject(new UiSelector()
		.packageName("android")
		.className(android.view.View.class).description("Pattern area."));
		*/
		Point p1=new Point(83,450);
		Point p2=new Point(383,450);
		Point p3=new Point(383,750);
		Point p4=new Point(383,1050);
		Point p5=new Point(683,1050);
		Point segments[]={p1,p2,p3,p4,p5};
		//assertTrue("pattern swipe",device.swipe(segments, 1));
	}
	
	public void unlockFromPin() throws UiObjectNotFoundException
	{
		new UiObject(new UiSelector().packageName("android").className("android.widget.Button").text("3 DEF")).click();
		new UiObject(new UiSelector().packageName("android").className("android.widget.Button").text("4 GHI")).click();
		new UiObject(new UiSelector().packageName("android").className("android.widget.Button").text("5 JKL")).click();
		new UiObject(new UiSelector().packageName("android").className("android.widget.Button").text("6 MNO")).click();
			
		new UiObject(new UiSelector().description("Enter")).click();
	}
	
	
	// {{ Trepn Profiler functions
	
	public String cmdStartTrepnService()
	{
		// To start Trepn Profiler:
		// 	adb shell am startservice com.quicinc.trepn/.TrepnService
		
		return "am startservice com.quicinc.trepn/.TrepnService\n";
	}
	
	public String cmdStartTrepnProfiling()
	{
		// To start profiling:
		// adb shell am broadcast –a com.quicinc.trepn.start_profiling	
		
		return "am broadcast –a com.quicinc.trepn.start_profiling\n";
	}
	public String cmdStartTrepnProfilingWithName(String runName)
	{
		/*
		 * It is possible to choose a name to give to the run when starting profiling. 
		 * To start profiling with a chosen name:
		 * adb shell am broadcast –a com.quicinc.trepn.start_profiling –e
			com.quicinc.trepn.database_file “<string_value>”
		*/
		
		return "am broadcast –a com.quicinc.trepn.start_profiling –e "
				+"com.quicinc.trepn.database_file '"+runName+"'\n";
	}
	public String stopTrepnProfiling(){
		/*To stop profiling:
			adb shell am broadcast –a com.quicinc.trepn.stop_profiling*/
		
		return "am broadcast –a com.quicinc.trepn.stop_profiling\n";
	}
	
	public String cmdLoadTrepnPreferences(String prefFilename)
	{
		/*
		 * To load preferences:
			adb shell am broadcast -a com.quicinc.trepn.load_preferences –e
			com.quicinc.trepn.load_preferences_file “<string_value>”*/
		
		return "am broadcast -a com.quicinc.trepn.load_preferences –e "
				+"com.quicinc.trepn.load_preferences_file "+prefFilename+"\n";
	}
	public String cmdSaveTrepnPreferences(String prefFilename)
	{
		/*
		 * To save preferences:
			adb shell am broadcast -a com.quicinc.trepn.save_preferences –e
			com.quicinc.trepn.save_preferences_file “<string_value>”*/
		
		return "am broadcast -a com.quicinc.trepn.save_preferences –e "
				+"com.quicinc.trepn.save_preferences_file "+prefFilename+"\n";
	}
	public String cmdBroadcastIntent(String desc)
	{
		return "am broadcast -a com.quicinc.Trepn.UpdateAppState"
		+" -e com.quicinc.Trepn.UpdateAppState.Value "+trepnAppState+10
		+" -e com.quicinc.Trepn.UpdateAppState.Value.Desc '"+desc+"'\n";
	}
	/*public void runExecCmd(String cmd)
	{
		try
        {           
			Log.i("ROOT","exc: "+cmd);
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("adb shell\n");
            DataOutputStream os = new DataOutputStream(proc.getOutputStream());
            
            os.writeBytes("cmd");
            os.flush();
            
            InputStream stderr = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            
            while ( (line = br.readLine()) != null)
            	Log.i("ROOT",line);
           
            int exitVal = proc.waitFor();
            Log.i("ROOT","Process exitValue: " + exitVal);
        } catch (Throwable t)
          {
            t.printStackTrace();
          }
	}*/
	
	public void execute(String...cmds)
	{
		cmdsToExec=new ArrayList<String>(Arrays.asList(cmds));
		execute();
	}

	@Override
	protected ArrayList<String> getCommandsToExecute() {
		// TODO Auto-generated method stub
		return cmdsToExec;
	}
	
	
	public static void swipeDownNotificationBar () {
	    UiDevice deviceInstance = UiDevice.getInstance();
	    int dHeight = deviceInstance.getDisplayHeight();
	    int dWidth = deviceInstance.getDisplayWidth();
	    System.out.println("height =" +dHeight);
	    System.out.println("width =" +dWidth);
	    int xScrollPosition = dWidth/2;
	    int yScrollStop = dHeight/2;
	    UiDevice.getInstance().swipe(
	        xScrollPosition, 
	        0, 
	        xScrollPosition, 
	        yScrollStop, 
	        100
	    );
	}
	/*public void pingToWait(long time)
	{
		String cmd="ping 123.45.67.89 -n 1 -w "+time+" > NUL";
		runExecCmd(cmd);
		return;
	}*/
	
	
}
