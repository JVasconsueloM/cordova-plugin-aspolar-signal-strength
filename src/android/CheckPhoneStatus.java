/**
 * author :  lipan
 * filename :  CheckStatus.java
 * create_time : 2014年4月10日 上午11:26:46
 */
package org.apache.cordova.aspolarsignalstrength;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.telephony.TelephonyManager;

/**
 * @author : lipan
 * @create_time : 2014年8月29日 上午9:56:59
 * @desc : 手机状态检查
 * @update_person:
 * @update_time :
 * @update_desc :
 *
 */
public class CheckPhoneStatus
{
    public static final String UNKNOWN = "unknown";
    public static final String NETWORK_CLASS_2_G = "2G";
    public static final String NETWORK_CLASS_3_G = "3G";
    public static final String NETWORK_CLASS_4_G = "4G";

    public static final String CARRIER_CLASS_CMC = "CMC";
    public static final String CARRIER_CLASS_CUC = "CUC";
    public static final String CARRIER_CLASS_CTC = "CTC";

    /**
     * 判断wifi是否连接
     * 
     * @param context
     * @return
     */
    public static  boolean isWIFIConnection(Context context)
    {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifi && (wifi.getState()==State.CONNECTED || wifi.getState()==State.CONNECTING))
        {
            return true;
        }
        return false;
    }

    /**
     * 检查网络连接状态，Monitor network connections (Wi-Vi, GPRS, UMTS, etc.)
     * 
     * @param context
     * @return
     */
    public static  boolean checkNetWorkStatus(Context context)
    {
        boolean result;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isConnected())
        {
            result = true;
        } else
        {
            result = false;
        }
        return result;
    }

    /**
     * 检查URL
     * 
     * @param url
     * @return
     */
    public static  boolean checkURL(String url)
    {
        boolean value = false;
        try
        {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setReadTimeout(0);
            int code = conn.getResponseCode();
            if (code != 200)
            {
                value = false;
            } else
            {
                value = true;
            }
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获得手机基本信息
     * 
     * @param context
     * @return
     */
    public static  DeviceInfo getDeviceInfo(Context context)
    {
        DeviceInfo deviceInfo = new DeviceInfo();
        try
        {
            final TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String lineNumber = telephonyManager.getLine1Number();
            int networkType = telephonyManager.getNetworkType();
            String subscriberId = telephonyManager.getSubscriberId();

            // 手机号码
            if (lineNumber != null && lineNumber.length() >= 11)
            {
                deviceInfo.phone_nbr = lineNumber.replaceAll("\\+86", "");
            }
            
            // 手机型号
            deviceInfo.phone_type = android.os.Build.MODEL;

            // 运营商类型
            String carrier = UNKNOWN;
            String carrier_name = UNKNOWN;
            if (subscriberId.startsWith("46000") || subscriberId.startsWith("46002")
                    || subscriberId.startsWith("46007"))
            {
                carrier = CARRIER_CLASS_CMC;
                carrier_name = "移动";
            } else if (subscriberId.startsWith("46001") || subscriberId.startsWith("46006")
                    || subscriberId.startsWith("46020"))
            {
                carrier = CARRIER_CLASS_CUC;
                carrier_name = "联通";
            } else if (subscriberId.startsWith("46003") || subscriberId.startsWith("46005"))
            {
                carrier = CARRIER_CLASS_CTC;
                carrier_name = "电信";
            }
            deviceInfo.carrier = carrier;
            deviceInfo.carrier_name = carrier_name;

            // 网络类型
            String netType = UNKNOWN;
            switch (networkType)
            {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    netType = NETWORK_CLASS_2_G;
                    break;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    netType = NETWORK_CLASS_3_G;
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE:
                    netType = NETWORK_CLASS_4_G;
                    break;
                default:
                    break;
            }
            deviceInfo.net_type = netType;
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return deviceInfo;
    }
}