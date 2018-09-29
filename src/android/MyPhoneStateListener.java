package org.apache.cordova.aspolarsignalstrength;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;



public class MyPhoneStateListener extends PhoneStateListener {
    int signalLevel;

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);

        signalLevel = signalStrength.getGsmSignalStrength();
    }

}


/*public class MyPhoneStateListener extends PhoneStateListener
{
    private Context context;
    public  MyPhoneStateListener(Context context)
    {
        this.context = context;
    }
    private Integer signalLevel = -1;
    
    
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
                signalLevel = Integer.parseInt(parts[11]);
            } else
            {
                signalLevel = (signalStrength.getGsmSignalStrength() * 2) - 113;
            }
        } catch (Exception e)
        {
            signalLevel = -1;
        }
    }
    
    
    public  Integer getSignalNum()
    {
        return signalLevel;
    }
}*/