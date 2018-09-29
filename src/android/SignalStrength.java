package org.apache.cordova.aspolarsignalstrength;

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
import java.util.List;

public class SignalStrength extends CordovaPlugin {

    @Override
    public boolean execute(final String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        TelephonyManager tm = (TelephonyManager)cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        int dBmlevel = 0;
        int asulevel = 0;
        int signalLevel = -1;
        boolean res = false;
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
                        callbackContext.error("Unknown type of cell signal.");
                        res = false;
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
            res = true;
        }
        catch (JSONException ex){
            callbackContext.error("Failed to retrieve signal strength.");
            res = false;
        }
        finally{
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, "Signal strength: " + dBmlevel + " dBm, "+ asulevel + " asu");
            callbackContext.sendPluginResult(pluginResult);
            res = true;
        }

        return res;
    }

} 
