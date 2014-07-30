/*  cpufreq-bench CPUFreq microbenchmark
 *
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;


public abstract class ExecuteAsRootBase
{
   public static boolean canRunRootCommands()
   {
      boolean retval = false;
      Process suProcess;

      try
      {
         suProcess = Runtime.getRuntime().exec("su");

         DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
         DataInputStream osRes = new DataInputStream(suProcess.getInputStream());

         if (null != os && null != osRes)
         {
            // Getting the id of the current user to check if this is root
            os.writeBytes("id\n");
            os.flush();
            
            os.writeBytes("adb -s 04fffa641bdbef03 shell\n");
            os.flush();
            
            os.writeBytes("su\n");
            os.flush();
            
            os.writeBytes("echo ondemand > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor\n");
            os.flush();
            
            os.writeBytes("exit\n");
            os.flush();
            
            String currUid = osRes.readLine();
            boolean exitSu = false;
            if (null == currUid)
            {
               retval = false;
               exitSu = false;
               Log.d("ROOT", "Can't get root access or denied by user");
            }
            else if (true == currUid.contains("uid=0"))
            {
               retval = true;
               exitSu = true;
               Log.d("ROOT", "Root access granted");
            }
            else
            {
               retval = false;
               exitSu = true;
               Log.d("ROOT", "Root access rejected: " + currUid);
            }

            if (exitSu)
            {
               os.writeBytes("exit\n");
               os.flush();
            }
         }
      }
      catch (Exception e)
      {
         // Can't get root !
         // Probably broken pipe exception on trying to write to output stream (os) after su failed, meaning that the device is not rooted

         retval = false;
         Log.d("ROOT", "Root access rejected [" + e.getClass().getName() + "] : " + e.getMessage());
      }

      return retval;
   }
   
   public final boolean execute()
   {
      boolean retval = false;
      
      try
      {
         ArrayList<String> commands = getCommandsToExecute();
         if (null != commands && commands.size() > 0)
         {
            Process suProcess = Runtime.getRuntime().exec("su");
 
            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());

            // Execute commands that require root access
            for (String currCommand : commands)
            {
               os.writeBytes(currCommand + "\n");
               os.flush();
            }

            os.writeBytes("exit\n");
            os.flush();

            try
            {
               int suProcessRetval = suProcess.waitFor();
               if (255 != suProcessRetval)
               {
                  // Root access granted
                  retval = true;
               }
               else
               {
                  // Root access denied
                  retval = false;
               }
            }
            catch (Exception ex)
            {
               Log.e("ROOT", "Error executing root action", ex);
            }
         }
      }
      catch (IOException ex)
      {
         Log.w("ROOT", "Can't get root access", ex);
      }
      catch (SecurityException ex)
      {
         Log.w("ROOT", "Can't get root access", ex);
      }
      catch (Exception ex)
      {
         Log.w("ROOT", "Error executing internal operation", ex);
      }
      
      return retval;
   }
   
   protected abstract ArrayList<String> getCommandsToExecute();
}
