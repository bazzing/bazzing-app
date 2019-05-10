package com.eandr.bazzing.verification;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.eandr.bazzing.R;
import com.eandr.bazzing.verification.adatpter.SectionStatePagerAdapter;
import com.eandr.bazzing.verification.fragments.PhoneCodeVerificationFragment;
import com.eandr.bazzing.verification.fragments.PhoneNumberInputFragment;

public class PhoneNumberInputActivity extends AppCompatActivity {

    public static final String CALLBACK_KEY = "phoneCallback";

    private  PhoneVerificationCallBack phoneVerificationCallBack;

    private  ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_input);

        viewPager = findViewById(R.id.phone_verification_container);

        phoneVerificationCallBack = new PhoneVerificationCallBack(this);
        setUpViewPager();

    }


    private void setUpViewPager(){

        SectionStatePagerAdapter adapter = new SectionStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(PhoneNumberInputFragment.newInstance(phoneVerificationCallBack));
        adapter.addFragment(PhoneCodeVerificationFragment.newInstance(phoneVerificationCallBack));
        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber){
        viewPager.setCurrentItem(fragmentNumber);
    }

}
