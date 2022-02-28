package dev.ewards.otp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class SMSBroadcastReciver extends BroadcastReceiver {

    public SMSBroadcastReciverListner smsBroadcastReciverListner;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == SmsRetriever.SMS_RETRIEVED_ACTION){
            Bundle extras = intent.getExtras();
            Status smsRecevireStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
            switch (smsRecevireStatus.getStatusCode()){
                case CommonStatusCodes
                        .SUCCESS:
                    Intent messageIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                smsBroadcastReciverListner.onSuccess(messageIntent);
                break;
                case CommonStatusCodes.TIMEOUT:
                    smsBroadcastReciverListner.onFailure();
                    break;
            }
        }
    }

    public interface SMSBroadcastReciverListner{
        void onSuccess(Intent intent);
        void onFailure();
    }
}
