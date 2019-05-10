package com.eandr.bazzing.verification.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.eandr.bazzing.R;
import com.eandr.bazzing.verification.PhoneVerificationCallBack;
import com.google.api.Authentication;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import static com.eandr.bazzing.verification.PhoneNumberInputActivity.CALLBACK_KEY;

public class PhoneCodeVerificationFragment extends Fragment implements View.OnClickListener {

    private PhoneVerificationCallBack mCallbacks;

    private Button sendCodeBtn;
    private PinView verifyCodeET;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallbacks = (PhoneVerificationCallBack) savedInstanceState.getSerializable(CALLBACK_KEY);
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

    public static PhoneCodeVerificationFragment newInstance(PhoneVerificationCallBack phoneVerificationCallBack) {

        Bundle args = new Bundle();
        args.putSerializable(CALLBACK_KEY, phoneVerificationCallBack);

        PhoneCodeVerificationFragment fragment = new PhoneCodeVerificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        String verificationCode = verifyCodeET.getText().toString();
        if(verificationCode.isEmpty()){
            Toast.makeText(getActivity(),"Enter verification code",Toast.LENGTH_SHORT).show();
        }else {

           mCallbacks.showVaryingDialog();
        }
    }
}
