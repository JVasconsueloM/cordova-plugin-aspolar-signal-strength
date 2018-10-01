package com.aspolar.plugin;
 
import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.Exception;
import java.lang.Thread;
import java.util.List;


public class SignalStrength extends CordovaPlugin {
    int dBmlevel = 0;
    int asulevel = 0;
    int signalLevel = -1;
    TelephonyManager tm;

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (!action.equals("getInfo")) {
            return false;
        }

        tm = (TelephonyManager)cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);

        try {
            List<CellInfo> cellInfoList = tm.getAllCellInfo();
            //Checking if list values are not null
            if (cellInfoList != null) {
                for (final CellInfo info : cellInfoList) {
                    if (info instanceof CellInfoGsm) {
                        //GSM Network
                        CellSignalStrengthGsm cellSignalStrength = ((CellInfoGsm)info).getCellSignalStrength();
                        dBmlevel = cellSignalStrength.getDbm();
                        asulevel = cellSignalStrength.getAsuLevel();
                    }
                    else if (info instanceof CellInfoCdma) {
                        //CDMA Network
                        CellSignalStrengthCdma cellSignalStrength = ((CellInfoCdma)info).getCellSignalStrength();
                        dBmlevel = cellSignalStrength.getDbm();
                        asulevel = cellSignalStrength.getAsuLevel();
                    }
                    else if (info instanceof CellInfoLte) {
                        //LTE Network
                        CellSignalStrengthLte cellSignalStrength = ((CellInfoLte)info).getCellSignalStrength();
                        dBmlevel = cellSignalStrength.getDbm();
                        asulevel = cellSignalStrength.getAsuLevel();
                    }
                    else if  (info instanceof CellInfoWcdma) {
                        //WCDMA Network
                        CellSignalStrengthWcdma cellSignalStrength = ((CellInfoWcdma)info).getCellSignalStrength();
                        dBmlevel = cellSignalStrength.getDbm();
                        asulevel = cellSignalStrength.getAsuLevel();
                    }
                    else{
                        // callbackContext.error("Unknown type of cell signal.");
                        Log.i("tag", "Unknown type of cell signal.");
                        return false;
                    }
                }
            }
            else{
                //Mostly for Samsung devices, after checking if the list is indeed empty.
                MyPhoneStateListener MyListener = new MyPhoneStateListener();
                tm.listen(MyListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
                int cc = 0;
                while ( signalLevel == -1){
                    Thread.sleep(200);
                    if (cc++ >= 5)
                    {
                        break;
                    }
                }
                asulevel = signalLevel;
                dBmlevel = -113 + 2 * asulevel;
                tm.listen(MyListener, PhoneStateListener.LISTEN_NONE);
                signalLevel = -1;
            }
        }
        catch (Exception ex){
            // callbackContext.error("Failed to retrieve signal strength.");
            Log.i("tag", "Failed to retrieve signal strength.", ex + "");
            return false;
        }

        String message = "Signal strength: " + dBmlevel + " dBm, "+ asulevel + " asu";
        callbackContext.success(message);
        return true;
        
    }


    public class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(SignalStrength);
            int signalLevel = signalStrength.getGsmSignalStrength();
        }

    }
} 

