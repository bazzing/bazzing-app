package com.eandr.bazzing.verification.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.eandr.bazzing.R;
import com.eandr.bazzing.verification.PhoneNumberInputActivity;
import com.eandr.bazzing.verification.PhoneVerificationCallBack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PhoneCodeVerificationFragment extends Fragment implements View.OnClickListener {

    private PhoneVerificationCallBack mCallbacks;

    private Button sendCodeBtn;
    private PinView verifyCodeET;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCallbacks = ((PhoneNumberInputActivity)getActivity()).getPhoneVerificationCallBack();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.phone_code_layout, container, false);

        sendCodeBtn = view.findViewById(R.id.send_code_btn);
        verifyCodeET = view.findViewById(R.id.pinView);

        sendCodeBtn.setOnClickListener(this);


        return view;
    }

    public void showSmsCode(String smsCode){
        verifyCodeET.setText(smsCode);
    }


    @Override
    public void onClick(View view) {
        String verificationCode = verifyCodeET.getText().toString();
        if(verificationCode.isEmpty()){
            Toast.makeText(getActivity(),"Enter verification code",Toast.LENGTH_SHORT).show();
        }else {

           mCallbacks.showVerifyingDialog();
           mCallbacks.signInWithUserCodeInput(verificationCode);
        }
    }
}
