package com.eandr.bazzing.verification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.eandr.bazzing.MainActivity;
import com.eandr.bazzing.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.Serializable;

import br.com.joinersa.oooalertdialog.Animation;
import br.com.joinersa.oooalertdialog.OoOAlertDialog;

public class PhoneVerificationCallBack extends PhoneAuthProvider.OnVerificationStateChangedCallbacks
implements Serializable {


    private String phoneVeficationId;
    private final Activity context;
    private AlertDialog dialogVerifying;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    public PhoneVerificationCallBack(Activity context) {
        this.context = context;
    }

    @Override
    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
        showVaryingDialog();
        signInWithPhoneAuthCredential(phoneAuthCredential);

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


    public void signInWithUserCodeInput(String userCodeInput){

        showVaryingDialog();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVeficationId, userCodeInput);
        signInWithPhoneAuthCredential(credential);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            dialogVerifying.dismiss();
                            //go to main activity
                            gotToMainActivity();
                        } else {
                            dialogVerifying.dismiss();
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid show alert dialog
                                new OoOAlertDialog.Builder(context)
                                        .setTitle("Error")
                                        .setMessage(task.getException().getMessage())
                                        .setImage(R.drawable.error_triangle)
                                        .setAnimation(Animation.POP)
                                        .setPositiveButton("OK", null)
                                        .build();
                            }
                        }
                    }
                });
    }

    private void gotToMainActivity() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }


    public void showVaryingDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View alertLayout= inflater.inflate(R.layout.processing_dialog,null);
        AlertDialog.Builder show = new AlertDialog.Builder(context);

        show.setView(alertLayout);
        show.setCancelable(false);
        dialogVerifying = show.create();
        dialogVerifying.show();
    }
}
