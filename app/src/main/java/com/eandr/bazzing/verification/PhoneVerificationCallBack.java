package com.eandr.bazzing.verification;

import android.content.Intent;
import android.util.Log;

import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.eandr.bazzing.MainActivity;
import com.eandr.bazzing.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class PhoneVerificationCallBack extends PhoneAuthProvider.OnVerificationStateChangedCallbacks
implements Serializable {


    private String phoneVeficationId;
    private final PhoneNumberInputActivity phoneNumberInputActivity;
    private VerifyDialog dialogVerifying;

    private Disposable disposable;

    public PhoneVerificationCallBack(PhoneNumberInputActivity phoneNumberInputActivity) {
        this.phoneNumberInputActivity = phoneNumberInputActivity;
    }

    @Override
    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
        String smsCode = phoneAuthCredential.getSmsCode();
        Log.i("SHOWCODE","onVerificationCompleted got sms code" + smsCode);
        if(smsCode != null) {
            phoneNumberInputActivity.showSmsCode(smsCode);
        }
        showVerifyingDialog();
        signInWithPhoneAuthCredential(phoneAuthCredential);

    }


    @Override
    public void onVerificationFailed(FirebaseException e) {
        Log.i("PhoneCallBack",e.getMessage());
    }

    @Override
    public void onCodeSent(String verificationCode, PhoneAuthProvider.ForceResendingToken token) {
        Log.i("SHOWCODE","onCodeSent got verificationCode" + verificationCode);
        this.phoneVeficationId = verificationCode;
        //phoneNumberInputActivity.showSmsCode(verificationCode);

    }

    public void signInWithUserCodeInput(String userCodeInput){

        showVerifyingDialog();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVeficationId, userCodeInput);
        signInWithPhoneAuthCredential(credential);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(phoneNumberInputActivity, task -> {
                    if (task.isSuccessful()) {
                       disposable =  Observable.just("one").delay(2,TimeUnit.SECONDS)
                                  .observeOn(AndroidSchedulers.mainThread())
                                  .subscribe(l->{ dialogVerifying.hideDialog();gotToMainActivity();});

                        // Sign in success, update UI with the signed-in user's information
                        //dialogVerifying.hideDialog();
                        //go to main activity
                        //gotToMainActivity();
                    } else {
                        dialogVerifying.hideDialog();
                        // Sign in failed, display a message and update the UI
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid show alert dialog
                            new TTFancyGifDialog.Builder(phoneNumberInputActivity)
                                    .setTitle("Error")
                                    .setMessage(task.getException().getMessage())
                                    .setPositiveBtnText("Ok")
                                    .setPositiveBtnBackground("#22b573")
                                    .setGifResource(R.drawable.error)      //pass your gif, png or jpg
                                    .isCancellable(true)
                                    .build();

                        }
                    }
                });
    }

    private void gotToMainActivity() {
        Intent intent = new Intent(phoneNumberInputActivity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        phoneNumberInputActivity.startActivity(intent);
        disposable.dispose();

    }


    public void showVerifyingDialog() {

        dialogVerifying = new VerifyDialog(phoneNumberInputActivity);
        dialogVerifying.showDialog();

    }



}
