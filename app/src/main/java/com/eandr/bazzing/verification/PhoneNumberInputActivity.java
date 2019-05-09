package com.eandr.bazzing.verification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.eandr.bazzing.R;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;

public class PhoneNumberInputActivity extends AppCompatActivity {

    private EditText phoneNumberEditText;
    private CountryCodePicker countryCodePicker;
    private Button sendNumberBtn;

    private View phoneNumberInputLayout;


    private Disposable disposable;
    private PhoneVerificationCallBack mCallbacks = new PhoneVerificationCallBack(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_input);

        phoneNumberEditText = findViewById(R.id.phone_number_edit_text);
        countryCodePicker = findViewById(R.id.ccp);
        sendNumberBtn = findViewById(R.id.send_number_btn);
        phoneNumberInputLayout = findViewById(R.id.phone_number_input_layout);

        countryCodePicker.registerCarrierNumberEditText(phoneNumberEditText);

        disposable =  RxView.clicks(sendNumberBtn)
                            .filter(o->countryCodePicker.isValidFullNumber())
                            .map(o->countryCodePicker.getFullNumberWithPlus())
                            .subscribe(this::sendVerifyPhoneNumber);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposable!=null){
            disposable.dispose();
        }
    }


    private void sendVerifyPhoneNumber(String phoneNumber){
        phoneNumberInputLayout.setVisibility(View.GONE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks


    }

}
