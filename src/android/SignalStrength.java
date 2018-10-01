package org.apache.cordova.aspolarsignalstrength;
/* 
import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellInfoCdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellInfoLte;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.PhoneStateListener;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.Thread;

import java.lang.Exception;
import java.util.List;
*/

import android.widget.Toast;
// Cordova-required packages
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SignalStrength extends CordovaPlugin {
    // int dBmlevel = 0;
    // int asulevel = 0;
    // int signalLevel = -1;
    // TelephonyManager tm;

    private static final String DURATION_LONG = "long";

    @Override
    public boolean execute(final String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {

        /* if (!action.equals("getInfo")) {
            callbackContext.error("\"" + action + "\" is not a recognized action.");
            return false;
        }

        this.tm = (TelephonyManager)cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);

        try {
            List<CellInfo> cellInfoList = tm.getAllCellInfo();
            //Checking if list values are not null
            if (cellInfoList != null) {
                for (final CellInfo info : cellInfoList) {
                    if (info instanceof CellInfoGsm) {
                        //GSM Network
                        CellSignalStrengthGsm cellSignalStrength = ((CellInfoGsm)info).getCellSignalStrength();
                        this.dBmlevel = cellSignalStrength.getDbm();
                        this.asulevel = cellSignalStrength.getAsuLevel();
                    }
                    else if (info instanceof CellInfoCdma) {
                        //CDMA Network
                        CellSignalStrengthCdma cellSignalStrength = ((CellInfoCdma)info).getCellSignalStrength();
                        this.dBmlevel = cellSignalStrength.getDbm();
                        this.asulevel = cellSignalStrength.getAsuLevel();
                    }
                    else if (info instanceof CellInfoLte) {
                        //LTE Network
                        CellSignalStrengthLte cellSignalStrength = ((CellInfoLte)info).getCellSignalStrength();
                        this.dBmlevel = cellSignalStrength.getDbm();
                        this.asulevel = cellSignalStrength.getAsuLevel();
                    }
                    else if  (info instanceof CellInfoWcdma) {
                        //WCDMA Network
                        CellSignalStrengthWcdma cellSignalStrength = ((CellInfoWcdma)info).getCellSignalStrength();
                        this.dBmlevel = cellSignalStrength.getDbm();
                        this.asulevel = cellSignalStrength.getAsuLevel();
                    }
                    else{
                        callbackContext.error("Unknown type of cell signal.");
                        return false;
                    }
                }
            }
            else{
                //Mostly for Samsung devices, after checking if the list is indeed empty.
                MyPhoneStateListener MyListener = new MyPhoneStateListener();
                this.tm.listen(MyListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
                int cc = 0;
                while ( this.signalLevel == -1){
                    Thread.sleep(200);
                    if (cc++ >= 5)
                    {
                        break;
                    }
                }
                this.asulevel = this.signalLevel;
                this.dBmlevel = -113 + 2 * this.asulevel;
                this.tm.listen(MyListener, PhoneStateListener.LISTEN_NONE);
                this.signalLevel = -1;
            }
        }
        catch (Exception ex){
            callbackContext.error("Failed to retrieve signal strength.");
            return false;
        }

        // PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, "Signal strength: " + this.dBmlevel + " dBm, "+ this.asulevel + " asu");
        // callbackContext.sendPluginResult(pluginResult);
        callbackContext.success("Signal strength: " + this.dBmlevel + " dBm, "+ this.asulevel + " asu");
        return true;
        */
        if (!action.equals("show")) {
        callbackContext.error("\"" + action + "\" is not a recognized action.");
        return false;
      }
      String message;
      String duration;
      try {
        JSONObject options = args.getJSONObject(0);
        message = options.getString("message");
        duration = options.getString("duration");
      } catch (JSONException e) {
        callbackContext.error("Error encountered: " + e.getMessage());
        return false;
      }
      // Create the toast
      Toast toast = Toast.makeText(cordova.getActivity(), message,
        DURATION_LONG.equals(duration) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
      // Display toast
      toast.show();
      // Send a positive result to the callbackContext
      PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
      callbackContext.sendPluginResult(pluginResult);
      return true;
        
    }
} 
