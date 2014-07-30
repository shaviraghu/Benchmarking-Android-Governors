/*  cpufreq-bench CPUFreq microbenchmark
 *
 *  Copyright (C) 2008 Christian Kornacker <ckornacker@suse.de>
 *  Copyright (C) 2013 Raghavendra Shavi <shaviraghu@gmail.com>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.example.benchmarktool;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

import android.content.*;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.util.Log;

public class BenchmarkActivity extends Activity  {

	Handler mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_benchmark);
		mHandler=new Handler();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_benchmark, menu);
		return true;
	}
	
	
	public void getCPUFreqInfo(View view) 
	{
		/*ExecuteAsRootBase exroot=new ExecuteAsRootBase(){
			@Override
			protected ArrayList<String> getCommandsToExecute(){
				ArrayList<String> cmds= new ArrayList<String>();
				cmds.add("chmod 646 /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor");
				return cmds;
			}
		};
		if(exroot.execute())
		{
			//startTrepnService();
			//startTrepnProfiling();
			
			Spinner cSpinner=(Spinner) findViewById(R.id.spinner4);
			int cpu= Integer.valueOf(cSpinner.getSelectedItem().toString());
			
			
			broadcastIntent(1001, "start Benchmark");
						
			int result=runBenchmark("hello",cpu);
			AlertDialog.Builder alertDialog= new AlertDialog.Builder(this).setCancelable(true);
			alertDialog.setMessage(Integer.toString(result));
			alertDialog.show();
			broadcastIntent(1003, "End Benchmark");
			//stopTrepnProfiling();
		}*/
		
		class Thrd implements Runnable{
			String scl_gov=null;
			Thrd(String gov){ scl_gov=gov;}
			public void run() {
							
				broadcastIntent(1001, "start Benchmark");
	    		runBenchmark("hello",0,scl_gov);
				broadcastIntent(1003, "End Benchmark");
				//Button btn=(Button)findViewById(R.id.button2);
				//btn.setEnabled(true);
				showNotification();
				mHandler.post(new Runnable(){
					public void run() {
					AlertDialog.Builder alertDialog= new AlertDialog.Builder(BenchmarkActivity.this).setCancelable(true);
					alertDialog.setMessage("Done!!!");
					alertDialog.show();
					}
				}
				);
				
			}
		}
		
		Button btn=(Button)findViewById(R.id.button2);
		btn.setEnabled(false);
		String gov=null;
		try{
		 String getGovCmd="cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor";
		 gov=execCommand(getGovCmd);
		}
		catch(IOException ex){
			Log.i("BenchmarkActivity.c", ex.getMessage());
		}
		Log.i("BenchmarkActivity.c","hello:"+gov);
		Thread mythread = new Thread(new Thrd(gov));
		mythread.setPriority(mythread.getThreadGroup().getMaxPriority());
		mythread.start();
	}
	
	protected String execCommand(String command) throws IOException {
	    String line = "";
	    if (command.length() > 0) {
	        Process child = Runtime.getRuntime().exec(command);
	        InputStream lsOut = child.getInputStream();
	        InputStreamReader r = new InputStreamReader(lsOut);
	        BufferedReader in = new BufferedReader(r);

	        String readline = null;
	        while ((readline = in.readLine()) != null) {
	            line = line + readline;
	        }
	    }

	    return line;
	}
	
	public void startTrepnService()
	{
		Intent trepn= new Intent();
		trepn.setClassName("com.quicinc.trepn", "com.quicinc.trepn.TrepnService");
		startService(trepn);
		try{
		Thread.sleep(1000);
		}catch(InterruptedException ex){
			
		}
	}
	public void startTrepnProfiling()
	{
		Intent startProfiling = new Intent("com.quicinc.trepn.start_profiling");
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy#h:mm:ss");
		String db_filename= sdf.format(new Date());
		startProfiling.putExtra("com.quicinc.trepn.database_file", db_filename);
		sendBroadcast(startProfiling);
		try{
			Thread.sleep(10000);
			}catch(InterruptedException ex){
				
			}
	}
	public void stopTrepnProfiling()
	{
		try{
			Thread.sleep(1000);
		}
		catch(InterruptedException ex){
			
		}
		Intent stopProfiling = new Intent("com.quicinc.trepn.stop_profiling");
		sendBroadcast(stopProfiling);
	}
	public void broadcastIntent(int intentNum, String desc)
	{
		Intent stateUpdate = new Intent("com.quicinc.Trepn.UpdateAppState");
		//stateUpdate.putExtra("com.quicinc.Trepn.UpdateAppState.Value",intentNum);
		stateUpdate.putExtra("com.quicinc.Trepn.UpdateAppState.Value.Desc", desc);
		sendBroadcast(stateUpdate); 
	    //Log.i("benchmarkactivity","JNI callback to java: in java method broadcastIntent");
		return;
	}
	
	public void runBenchmarkClick(View view){
		
		
		String gov="";
		
		Intent trepnProf=new Intent();
		trepnProf.setClassName("com.quicinc.trepn", "com.quicinc.trepn.TrepnService");
		startService(trepnProf);
		
		Intent startProf=new Intent("com.quicinc.trepn.start_profiling");
		String mydate = java.text.DateFormat.getDateTimeInstance()
				.format(Calendar.getInstance().getTime());
		
		sendBroadcast(startProf);
		
		
		Intent stateUpdate = new Intent("com.quicinc.Trepn.UpdateAppState");
		stateUpdate.putExtra("com.quicinc.Trepn.UpdateAppState.Value",100);
		stateUpdate.putExtra("com.quicinc.Trepn.UpdateAppState.Desc", "start Benchmark 4 "+gov);
		
		sendBroadcast(stateUpdate); 
		
		
		ExecuteAsRootBase exroot=new ExecuteAsRootBase(){
			@Override
			protected ArrayList<String> getCommandsToExecute(){
				ArrayList<String> cmds= new ArrayList<String>();
				cmds.add("chmod 646 /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor");
				return cmds;
			}
		};
		if(exroot.execute())
		{
			int res=setScalingGovernor(gov);
			ExecuteAsRootBase exroot2=new ExecuteAsRootBase(){
				@Override
				protected ArrayList<String> getCommandsToExecute(){
					ArrayList<String> cmds= new ArrayList<String>();
					cmds.add("adb shell\n");
					cmds.add("uiautomator runtest UIAutomatorTest.jar -c com.uia.common.Controller#startRun");
					return cmds;
				}
			};
			if(exroot2.execute())
				Log.i("ROOT","Simulation running");
		}
		
		
		
	}
	
	private native void CPUfreqInfo();
	private native int runBenchmark(String greet, int onlineCPUCount, String scl_gov);
	private native int setScalingGovernor(String gov);
	
	static {
		System.loadLibrary("BenchmarkTool");
	}
	public void cancelNotificationClick(View view){
		
	}
	public void showNotification(){
		Log.i("benchmarkactivity","in notification");
        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // intent triggered, you can add other intent for other actions
        Intent intent = new Intent(BenchmarkActivity.this, BenchmarkActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(BenchmarkActivity.this, 0, intent, 0);

        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0
        Notification mNotification = new Notification.Builder(this)

            .setContentTitle("New Post!")
            .setContentText("Here's an awesome update for you!")
            .setSmallIcon(0)
            .setContentIntent(pIntent)
            .setSound(soundUri)

            .addAction(0, "View", pIntent)
            .addAction(0, "Remind", pIntent)

            .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // If you want to hide the notification after it was selected, do the code below
        // myNotification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, mNotification);
    }

    public void cancelNotification(int notificationId){

        if (Context.NOTIFICATION_SERVICE!=null) {
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
            nMgr.cancel(notificationId);
        }
    }

	
}

