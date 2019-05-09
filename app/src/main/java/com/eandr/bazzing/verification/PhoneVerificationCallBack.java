package com.eandr.bazzing.verification;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.eandr.bazzing.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class PhoneVerificationCallBack extends PhoneAuthProvider.OnVerificationStateChangedCallbacks {

    private String phoneVeficationId;
    private final Context context;
    private AlertDialog dialogVerifying;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    PhoneVerificationCallBack(Context context) {
        this.context = context;
    }

    @Override
    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        showVaryingDialog();
        //signInWithPhoneAuthCredential(phoneAuthCredential);

    }


    @Override
    public void onVerificationFailed(FirebaseException e) {

    }

    @Override
    public void onCodeSent(String verificationCode, PhoneAuthProvider.ForceResendingToken token) {
        this.phoneVeficationId = verificationCode;
        this.resendToken = token;
    }

    public String getPhoneVeficationId() {
        return phoneVeficationId;
    }

    public PhoneAuthProvider.ForceResendingToken getResendToken() {
        return resendToken;
    }


    private void showVaryingDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View alertLayout= inflater.inflate(R.layout.processing_dialog,null);
        AlertDialog.Builder show = new AlertDialog.Builder(context);

        show.setView(alertLayout);
        show.setCancelable(false);
        dialogVerifying = show.create();
        dialogVerifying.show();
    }
}
