package com.eandr.bazzing.verification;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.eandr.bazzing.R;

public class PhoneNumberInputActivity extends AppCompatActivity {

    public static final String CALLBACK_KEY = "phoneCallback";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_input);

    }

}
