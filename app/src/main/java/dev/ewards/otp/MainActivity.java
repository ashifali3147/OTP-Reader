package dev.ewards.otp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_USER_CONSENT = 200;
    SMSBroadcastReciver smsBroadcastReciver;
    EditText edtOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtOTP = (EditText) findViewById(R.id.edt_otp);
        startSmartUserConsent();
    }

    private void startSmartUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        client.startSmsUserConsent(null); //number
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_USER_CONSENT){
            if ((resultCode == RESULT_OK) && (data != null)){
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                getOTPfromMessage(message);
            }
        }
    }

    private void getOTPfromMessage(String message) {
        Pattern otpPattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = otpPattern.matcher(message);
        if (matcher.find()){
            edtOTP.setText(matcher.group());
        }
    }

    private void registerBroadcastReceiver(){
        smsBroadcastReciver = new SMSBroadcastReciver();
        smsBroadcastReciver.smsBroadcastReciverListner = new SMSBroadcastReciver.SMSBroadcastReciverListner() {
            @Override
            public void onSuccess(Intent intent) {
                startActivityForResult(intent, REQ_USER_CONSENT);
            }

            @Override
            public void onFailure() {

            }
        };

        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReciver, intentFilter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReciver);
    }
}