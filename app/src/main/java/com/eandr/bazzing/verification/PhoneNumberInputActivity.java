package com.eandr.bazzing.verification;

import android.os.Bundle;

import com.eandr.bazzing.R;
import com.eandr.bazzing.verification.fragments.PhoneCodeVerificationFragment;
import com.eandr.bazzing.verification.fragments.PhoneNumberInputFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class PhoneNumberInputActivity extends AppCompatActivity {

    private  PhoneVerificationCallBack phoneVerificationCallBack;

    public static final String INPUT_NUMBER_FRAGMENT_TAG = "inputNumberFragmentTag";
    public static final String INPUT_CODE_FRAGMENT_TAG = "inputCodeFragmentTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_input);
        setSupportActionBar(findViewById(R.id.activity_phone_number_toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        phoneVerificationCallBack = new PhoneVerificationCallBack(this);

        if (savedInstanceState == null) {
            Fragment newFragment = new PhoneNumberInputFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.phone_verification_container, newFragment, INPUT_NUMBER_FRAGMENT_TAG).commit();
        }

    }


    public void showSmsCode(String smsCode){

        PhoneCodeVerificationFragment phoneCodeVerificationFragment = (PhoneCodeVerificationFragment)getSupportFragmentManager().findFragmentByTag(INPUT_CODE_FRAGMENT_TAG);
        if (phoneCodeVerificationFragment != null && phoneCodeVerificationFragment.isVisible()) {
            // add your code here
            phoneCodeVerificationFragment.showSmsCode(smsCode);
        }

    }

    public PhoneVerificationCallBack getPhoneVerificationCallBack(){
        return phoneVerificationCallBack;
    }

}
