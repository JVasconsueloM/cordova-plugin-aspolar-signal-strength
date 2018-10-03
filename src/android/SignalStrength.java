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
import android.telephony.TelephonyManager;
import android.util.Log;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PermissionHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.Exception;
import java.lang.Thread;
import java.util.List;


public class SignalStrength extends CordovaPlugin {
    int asulevel = 0;
    int asulevelmax = 31;
    int dBmlevel = 0;
    int signalLevel = -1;
    String message = "";
    double signalpercentage = 0.0;
    TelephonyManager tm;

    public static final int CONTINUE = 1;
    protected final static String[] permissions = { Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION };

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (!action.equals("getbBm") && !action.equals("getPercentage")) {
            return false;
        }

        tm = (TelephonyManager)cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);

        try {

            if(!PermissionHelper.hasPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                PermissionHelper.requestPermission(this, CONTINUE, Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            else{
                this.processInfo();
            }

        }
        catch (Exception ex){
            callbackContext.error("Failed to retrieve signal strength. (" + ex + ")");
            Log.i("tag", "Failed to retrieve signal strength.", ex);
            return false;
        }

        message = dBmlevel  + "";

        if (action.equals("getPercentage")) {
            this.getPercentage();
        }


        callbackContext.success(message);
        return true;
        
    }


    public class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onSignalStrengthsChanged(android.telephony.SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            signalLevel = signalStrength.getGsmSignalStrength();
        }
    }

    //--------------------------------------------------------------------------
    // LOCAL METHODS
    //--------------------------------------------------------------------------

    /**         MIN     MAX 
    CDMA (2g)   
        dBm =   -100    -75
        asu =   1       16
    GSM (3g)
        dBm =   -120    -50
        asu =   0(99)   31
    WCDMA(3g)
        dBm =   -115    -50
        asu =   0(99)   31
    LTE (4g)
        dBm =   -140    -44
        asu =   0(99)   97

    LINEAR =        100 * (1 - (((-dBmmax) - (-dBmlevel))/((-dBmmax) - (-dBmmin))));
    NOT LINEAR =    (2.0 * (dBmlevel + 100))/100

    */
    public void getPercentage(String networkType){
        switch (networkType) {
            case "wifi":
                signalpercentage = (2.0 * (dBmlevel + 100))/100;
                break;
            default:
                signalpercentage = asulevel/asulevelmax * 1.0;
                break;
        }
        message = signalpercentage + "";
        Log.i("tag", message);

    }


    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
        for (int r : grantResults) {
            if (r == PackageManager.PERMISSION_DENIED) {
                this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "PERMISSION_DENIED_ERROR"));
                return;
            }
        }
        switch (requestCode) {
            case CONTINUE:
                this.processInfo(this.srcType, this.destType, this.encodingType);
                break;
        }
    }


    public void processInfo(){
        List<CellInfo> cellInfoList = tm.getAllCellInfo();
        //Checking if list values are not null
        if (cellInfoList != null) {
            for (final CellInfo info : cellInfoList) {
                if (info instanceof CellInfoGsm) {
                    //GSM Network
                    CellSignalStrengthGsm cellSignalStrength = ((CellInfoGsm)info).getCellSignalStrength();
                    dBmlevel = cellSignalStrength.getDbm();
                    asulevel = cellSignalStrength.getAsuLevel();
                    asulevelmax = 31;
                }
                else if (info instanceof CellInfoCdma) {
                    //CDMA Network
                    CellSignalStrengthCdma cellSignalStrength = ((CellInfoCdma)info).getCellSignalStrength();
                    dBmlevel = cellSignalStrength.getDbm();
                    asulevel = cellSignalStrength.getAsuLevel();
                    asulevelmax = 16;
                }
                else if (info instanceof CellInfoLte) {
                    //LTE Network
                    CellSignalStrengthLte cellSignalStrength = ((CellInfoLte)info).getCellSignalStrength();
                    dBmlevel = cellSignalStrength.getDbm();
                    asulevel = cellSignalStrength.getAsuLevel();
                    asulevelmax = 97;
                }
                else if  (info instanceof CellInfoWcdma) {
                    //WCDMA Network
                    CellSignalStrengthWcdma cellSignalStrength = ((CellInfoWcdma)info).getCellSignalStrength();
                    dBmlevel = cellSignalStrength.getDbm();
                    asulevel = cellSignalStrength.getAsuLevel();
                    asulevelmax = 31;
                }
                else{
                    callbackContext.error("Unknown type of cell signal.");
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

} 

