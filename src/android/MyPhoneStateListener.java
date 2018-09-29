/**
 * author :  lipan
 * filename :  Snippet.java
 * create_time : 2014年11月12日 下午4:44:22
 */
package org.apache.cordova.aspolarsignalstrength;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;

/**
 * @author : lipan
 * @create_time : 2014年11月12日 下午4:44:22
 * @desc : 手机状态变化监控
 * @update_person:
 * @update_time :
 * @update_desc :
 *
 */
public class MyPhoneStateListener2 extends PhoneStateListener
{
    private Context context;
    public  MyPhoneStateListener2(Context context)
    {
        this.context = context;
    }
    private Integer signalNum = -1; //信号强度
    
    
    @Override
    public  void onSignalStrengthsChanged(SignalStrength signalStrength)
    {
        super.onSignalStrengthsChanged(signalStrength);

        try
        {
            if (CheckPhoneStatus.getDeviceInfo(context).net_type.equals(CheckPhoneStatus.NETWORK_CLASS_4_G))
            {
                String ssignal = signalStrength.toString();
                String[] parts = ssignal.split(" ");
                signalNum = Integer.parseInt(parts[11]);
            } else
            {
                signalNum = (signalStrength.getGsmSignalStrength() * 2) - 113;
            }
        } catch (Exception e)
        {
            signalNum = -1;
        }
    }
    
    
    public  Integer getSignalNum()
    {
        return signalNum;
    }
}