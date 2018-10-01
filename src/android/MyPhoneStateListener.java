package com.aspolar.plugin;

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