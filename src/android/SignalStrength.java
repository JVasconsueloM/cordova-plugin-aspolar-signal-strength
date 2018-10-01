package com.aspolar.plugin;
 
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
import android.util.Log;




public class SignalStrength extends CordovaPlugin {
    int dBmlevel = 0;
    int asulevel = 0;
    int signalLevel = -1;
    TelephonyManager tm;

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Log.v(action);
        Log.v(!action.equals("getInfo"));

        if (!action.equals("getInfo")) {
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
                        // callbackContext.error("Unknown type of cell signal.");
                        Log.v("Unknown type of cell signal.");
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
            // callbackContext.error("Failed to retrieve signal strength.");
            Log.v("Failed to retrieve signal strength.");
            return false;
        }

        String message = "Signal strength: " + this.dBmlevel + " dBm, "+ this.asulevel + " asu";
        callbackContext.success(message);
        return true;
        
    }
} 
