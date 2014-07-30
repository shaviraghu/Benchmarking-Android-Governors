package com.uia.apphandlers;

import android.util.Log;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;

public class UIEventHandler {
	
	public String packageName;
	
	public boolean txtViewTxtExists(String text){
		UiObject search=new UiObject(new UiSelector().packageName(packageName)
				.className("android.widget.TextView").text(text));
		return search.exists();
	}
	public boolean txtViewDescExists(String desc){
		UiObject search=new UiObject(new UiSelector().packageName(packageName)
				.className("android.widget.TextView").description(desc));
		return search.exists();
	}
	public boolean editTxtViewDescExists(String desc){
		UiObject search=new UiObject(new UiSelector().packageName(packageName)
				.className("android.widget.TextView").description(desc));
		return search.exists();
	}
	public boolean editTxtViewTxtExists(String text){
		UiObject edtTxt=new UiObject(new UiSelector().packageName(packageName)
				.className("android.widget.EditText").text(text));
		return edtTxt.exists();
	}
	public boolean clickTxtViewTxt(String text) throws UiObjectNotFoundException{
		UiObject txt=new UiObject(new UiSelector().packageName(packageName)
				.className("android.widget.TextView").text(text));
		if(txt.exists())
				return txt.click();
		return false;
	}
	public boolean clickTxtViewTxtWait(String text) throws UiObjectNotFoundException{
		UiObject obj=new UiObject(new UiSelector().packageName(packageName)
				.className("android.widget.TextView").text(text));
		if(obj.exists())
		{
			return obj.clickAndWaitForNewWindow();
		}
		return false;
	}
	public boolean clickTxtViewTxtWaitTime(String text,long timeOut) throws UiObjectNotFoundException{
		UiObject txt=new UiObject(new UiSelector().packageName(packageName)
				.className("android.widget.TextView").text(text));
		if(txt.exists())
				return txt.clickAndWaitForNewWindow(timeOut);
		return false;
	}
	public boolean clickTxtViewTxtConWait(String text,long timeOut) throws UiObjectNotFoundException{
		UiObject txt=new UiObject(new UiSelector().packageName(packageName)
				.className("android.widget.TextView").textContains(text));
		if(txt.exists())
				return txt.clickAndWaitForNewWindow(timeOut);	
		return false;
	}
	public boolean clickTxtViewDesc(String desc) throws UiObjectNotFoundException{
		UiObject txt=new UiObject(new UiSelector().packageName(packageName)
				.className("android.widget.TextView").description(desc));
		if(txt.exists())
			return txt.click();
		return false;
	}
	public boolean clickTxtViewDescWait(String desc) throws UiObjectNotFoundException{
		UiObject obj=new UiObject(new UiSelector().packageName(packageName)
				.className("android.widget.TextView").description(desc));
		if(obj.exists())
			return obj.clickAndWaitForNewWindow();
		return false;
	}
	public boolean clickTxtViewDescConWait(String desc,long timeOut) throws UiObjectNotFoundException{
		UiObject txt=new UiObject(new UiSelector().packageName(packageName)
				.className("android.widget.TextView").descriptionContains(desc));
		if(txt.exists())
				return txt.clickAndWaitForNewWindow(timeOut);	
		return false;
	}
	public boolean clickButton(String text) throws UiObjectNotFoundException{
		UiObject btn=new UiObject(new UiSelector().packageName(packageName)
				.className("android.widget.Button").text(text));
		if(btn.exists())
				return btn.click();
		return false;
	}
	public boolean clickButtonWait(String text) throws UiObjectNotFoundException{
		UiObject btn=new UiObject(new UiSelector().packageName(packageName)
				.className("android.widget.Button").text(text));
		if(btn.exists())
				return btn.clickAndWaitForNewWindow();
		return false;
	}
	public boolean clickButtonWaitFor(String text,long timeout) throws UiObjectNotFoundException{
		UiObject btn=new UiObject(new UiSelector().packageName(packageName)
				.className("android.widget.Button").text(text));
		if(btn.exists())
				return btn.clickAndWaitForNewWindow(timeout);
		return false;
	}
	public boolean clickButtonWaitForExists(String text,long timeout) throws UiObjectNotFoundException{
		UiObject btn=new UiObject(new UiSelector().packageName(packageName)
				.className("android.widget.Button").text(text));
		if(btn.waitForExists(timeout))
				return btn.clickAndWaitForNewWindow();
		return false;
	}
	public boolean clickImageButtonDesc(String text) throws UiObjectNotFoundException{
		UiObject btn=new UiObject(new UiSelector().packageName(packageName)
				.className("android.widget.ImageButton").description(text));
		if(btn.exists())
				return btn.click();
		return false;
	}
	public boolean clickImageButtonDescWaitFor(String text,long timeOut) throws UiObjectNotFoundException{
		UiObject btn=new UiObject(new UiSelector().packageName(packageName)
				.className("android.widget.ImageButton").description(text));
		if(btn.exists())
				return btn.clickAndWaitForNewWindow(timeOut);
		return false;
	}
	public boolean clickImageButtonDescWait(String text) throws UiObjectNotFoundException{
		UiObject btn=new UiObject(new UiSelector().packageName(packageName)
				.className("android.widget.ImageButton").description(text));
		if(btn.exists())
				return btn.clickAndWaitForNewWindow();
		return false;
	}
	public boolean clickChildAtHierarchialIndex(UiObject parent,long timeOut,int...i) throws UiObjectNotFoundException{
		UiObject obj=parent;
		for(int j:i)
			obj=obj.getChild(new UiSelector().index(j));
		if(obj.exists())
				return obj.clickAndWaitForNewWindow(timeOut);
		return false;
	}
	public boolean clickChildAtHierarchialIndexWait(UiObject parent,int...i) throws UiObjectNotFoundException{
		UiObject obj=parent;
		for(int j:i)
			obj=obj.getChild(new UiSelector().index(j));
		if(obj.exists())
				return obj.clickAndWaitForNewWindow();
		return false;
	}
	
	/*private UiObject getUiobj(String pkg, String cls, String desc, String txt, boolean txtContains, boolean descContains){
    	UiSelector sel=new UiSelector().packageName(pkg).className(cls);
    	
    	if(txtContains)
    		sel.textContains(txt);
    	else
    		if(!txt.isEmpty())
    			sel.text(txt);
    	if(descContains)
    		sel.descriptionContains(txt);
    	else
    		if(!txt.isEmpty())
    			sel.text(txt);
		
		new UiObject(new UiSelector().packageName(packageName)
				.className("android.widget.TextView").text(text));
    }*/
	
}
