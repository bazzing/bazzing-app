package com.eandr.bazzing.verification.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.eandr.bazzing.R;
import com.eandr.bazzing.verification.PhoneNumberInputActivity;
import com.eandr.bazzing.verification.PhoneVerificationCallBack;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import br.com.joinersa.oooalertdialog.Animation;
import br.com.joinersa.oooalertdialog.OoOAlertDialog;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static com.eandr.bazzing.verification.PhoneNumberInputActivity.CALLBACK_KEY;

public class PhoneNumberInputFragment extends Fragment {

    private EditText phoneNumberEditText;
    private CountryCodePicker countryCodePicker;
    private Button sendNumberBtn;

    private Disposable disposable;

    private PhoneVerificationCallBack mCallbacks;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallbacks = (PhoneVerificationCallBack) savedInstanceState.getSerializable(CALLBACK_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.phone_number_input_layout, container, false);

        phoneNumberEditText = view.findViewById(R.id.phone_number_edit_text);
        countryCodePicker = view.findViewById(R.id.ccp);
        sendNumberBtn = view.findViewById(R.id.send_number_btn);

        countryCodePicker.registerCarrierNumberEditText(phoneNumberEditText);

        disposable =  RxView.clicks(sendNumberBtn)
                            .flatMap(o-> {
                                        if (!countryCodePicker.isValidFullNumber()) {
                                            return Observable.error(new IllegalArgumentException("number is not valid"));
                                        } else {
                                            return Observable.just(countryCodePicker.getFullNumberWithPlus());
                                        }
                                    }
                                )
                            .subscribe(this::sendVerifyPhoneNumber, this::showErrorDialog);


        return view;
    }

    private void showErrorDialog(Throwable e) {
        new OoOAlertDialog.Builder(getActivity())
                .setTitle("Error")
                .setMessage(e.getMessage())
                .setImage(R.drawable.error_triangle)
                .setAnimation(Animation.POP)
                .setPositiveButton("OK", null)
                .build();
    }

    private void sendVerifyPhoneNumber(String phoneNumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,              // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),      // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        //move to next fragment
        ((PhoneNumberInputActivity)getActivity()).setViewPager(1);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(disposable!=null){
            disposable.dispose();
        }
    }

    public static PhoneNumberInputFragment newInstance(PhoneVerificationCallBack phoneVerificationCallBack) {

        Bundle args = new Bundle();
        args.putSerializable(CALLBACK_KEY,phoneVerificationCallBack);

        PhoneNumberInputFragment fragment = new PhoneNumberInputFragment();
        fragment.setArguments(args);
        return fragment;
    }



}
