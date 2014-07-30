package com.uia.apphandlers;

import java.util.Calendar;

import android.util.Log;
import android.view.KeyEvent;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.uia.apphandlers.AppDetails.APPS_ON_DEVICE;
import com.uia.common.Controller;


public class Facebook extends UIEventHandler {
	
	public Facebook(){
		this.packageName=APPS_ON_DEVICE.FACEBOOK.getPackageName();
	}
	
	public void uploadPic() throws UiObjectNotFoundException
	{
		if(clickTxtViewTxtWait("Photo")){
			if(clickImageButtonDescWait("Open camera"))
			{
				UiObject prnt=new UiObject(new UiSelector().packageName(packageName)
						.className("android.widget.RelativeLayout"));
				clickChildAtHierarchialIndexWait(prnt, 2,2);//flip camera
				clickChildAtHierarchialIndexWait(prnt,3,1,0);//click camera
				Controller.device.pressBack();
				UiObject grdViw=new UiObject(new UiSelector().packageName(packageName)
						.className("android.widget.GridView"));
				if(grdViw.exists()){
					Log.i("ROOT", "GridView");
					clickChildAtHierarchialIndexWait(grdViw,0,1);//select first pic
					clickImageButtonDescWait("Post photos");
				}
				/*UiObject edttxt=new UiObject(new UiSelector().packageName(packageName)
						.className("android.widget.EditText"));*/
				
				if(editTxtViewTxtExists("Say something about this photo")){
										
					String mydate = java.text.DateFormat.getDateTimeInstance()
							.format(Calendar.getInstance().getTime());
					
					for(char c : mydate.toCharArray()){
						if(c==',')
							Controller.device.pressKeyCode(KeyEvent.KEYCODE_COMMA);
						else
							if(c==' ')
								Controller.device.pressKeyCode(KeyEvent.KEYCODE_SPACE);
							else
								if(c==':')
									Controller.device.pressKeyCode(KeyEvent.KEYCODE_SEMICOLON);
								Controller.device.pressKeyCode(KeyEvent.keyCodeFromString("KEYCODE_"+c));
					}
					UiObject post=new UiObject(new UiSelector().packageName(packageName)
							.className("android.view.View").description("Post"));
					if(post.exists())
						post.clickAndWaitForNewWindow();
				}
			}
		}
	}
	public void uploadVideo() throws UiObjectNotFoundException
	{
		
	}
	public void browseMyProfile() throws UiObjectNotFoundException
	{
		
	}
	public void browsefriendProfile() throws UiObjectNotFoundException
	{
		
	}
	public void watchVideo() throws UiObjectNotFoundException
	{
		
	}
	public void browsePics() throws UiObjectNotFoundException
	{
		
	}

}
