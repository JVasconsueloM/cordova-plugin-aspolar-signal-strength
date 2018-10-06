package com.aspolar.plugin;
 
import android.content.Context;
import android.content.pm.PackageManager;
import android.Manifest;
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
import java.lang.Exception;
import java.lang.Thread;
import java.util.List;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PermissionHelper;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;


public class SignalStrength extends CordovaPlugin {
    private String networkType;
    public CallbackContext callbackContext;
    public int asulevel = -1;
    public int asulevelmax = 31;
    public int dBmlevel = 0;
    public String result = "";
    public String signalLevel;
    public TelephonyManager tm;

    protected final static String[] permissions = { Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION };
    public static final int CONTINUE = 1;
    public static final int PERMISSION_DENIED_ERROR = 20;

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        boolean isGetdBm = action.equals("getdBm");
        boolean isGetLevel = action.equals("getLevel");
        boolean isGetPercentage = action.equals("getPercentage");

        if (!isGetdBm && !isGetPercentage && !isGetLevel) {
            return false;
        }

        this.callbackContext = callbackContext;
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
            this.callbackContext.error("Failed to retrieve signal strength. (" + ex + ")");
            return false;
        }


        if (isGetPercentage) {
            this.networkType = args.getString(0);
            this.getPercentage(networkType);
        }
        else if (isGetLevel){
            result = signalLevel;
        }
        else {
            result = dBmlevel  + "";
        }

        this.callbackContext.success(result);
        return true;
    }


    /***
    * A listener class for monitoring changes in specific signal strength telephony state on the device.
    */
    public class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onSignalStrengthsChanged(android.telephony.SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            asulevel = signalStrength.getGsmSignalStrength();
        }
    } 

    // SIGNAL STRENGTH INFO
    /* **********************
                 MIN     MAX 
    CDMA (2g)   
        dBm =   -100    -75
        asu =   0       97
    LTE (4g)
        dBm =   -140    -44
        asu =   0(99)   97
    GSM (3g)
        dBm =   -120    -50
        asu =   0(99)   31
    WCDMA(3g)
        dBm =   -115    -50
        asu =   0(99)   31

    LINEAR =        100 * (1 - (((-dBmmax) - (-dBmlevel))/((-dBmmax) - (-dBmmin))));
    NOT LINEAR =    (2.0 * (dBmlevel + 100))/100

    ********************* */

    public void getPercentage(String networkType){
        if(networkType == "wifi"){
            if (dBmlevel <= -100){
                result =  String.format( "%.2f", 0 );
            }
            else if (dBmlevel >= -50){
                result =  String.format( "%.2f", 1 );
            }
            else{
                result = String.format( "%.2f", (2.0 * (dBmlevel + 100))/100 );
            }
        }
        else {
            result = String.format( "%.2f",  1.0 * asulevel / asulevelmax);
        }
    }

    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
        for (int r : grantResults) {
            if (r == PackageManager.PERMISSION_DENIED) {
                this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, PERMISSION_DENIED_ERROR));
                return;
            }
        }
        switch (requestCode) {
            case CONTINUE:
                this.processInfo();
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
                    signalLevel =  cellSignalStrength.getLevel() + "";
                    asulevelmax = 31;
                }
                else if (info instanceof CellInfoCdma) {
                    //CDMA Network
                    CellSignalStrengthCdma cellSignalStrength = ((CellInfoCdma)info).getCellSignalStrength();
                    dBmlevel = cellSignalStrength.getDbm();
                    asulevel = cellSignalStrength.getAsuLevel();
                    signalLevel =  cellSignalStrength.getLevel() + "";
                    asulevelmax = 97;
                }
                else if (info instanceof CellInfoLte) {
                    //LTE Network
                    CellSignalStrengthLte cellSignalStrength = ((CellInfoLte)info).getCellSignalStrength();
                    dBmlevel = cellSignalStrength.getDbm();
                    asulevel = cellSignalStrength.getAsuLevel();
                    signalLevel =  cellSignalStrength.getLevel() + "";
                    asulevelmax = 97;
                }
                else if  (info instanceof CellInfoWcdma) {
                    //WCDMA Network
                    CellSignalStrengthWcdma cellSignalStrength = ((CellInfoWcdma)info).getCellSignalStrength();
                    dBmlevel = cellSignalStrength.getDbm();
                    asulevel = cellSignalStrength.getAsuLevel();
                    signalLevel =  cellSignalStrength.getLevel() + "";
                    asulevelmax = 31;
                }
                else{
                    throw new IllegalArgumentException("Unknown type of cell signal.");
                }
            }
        }
        else{
            //Mostly for Samsung devices, after checking if the list is indeed empty.
            try { 
                MyPhoneStateListener MyListener = new MyPhoneStateListener();
                tm.listen(MyListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
                int cc = 0;
                while ( asulevel == -1){
                    Thread.sleep(200);
                    if (cc++ >= 5)
                    {
                        break;
                    }
                }
                asulevelmax = 31;
                dBmlevel = -113 + 2 * asulevel;
                tm.listen(MyListener, PhoneStateListener.LISTEN_NONE);
                signalLevel = String.format("%.0g%n", 1.0*asulevel/asulevelmax*4);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

}