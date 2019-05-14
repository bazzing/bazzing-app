package com.eandr.bazzing.verification.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.eandr.bazzing.R;
import com.eandr.bazzing.verification.PhoneNumberInputActivity;
import com.eandr.bazzing.verification.PhoneVerificationCallBack;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static com.eandr.bazzing.verification.PhoneNumberInputActivity.INPUT_CODE_FRAGMENT_TAG;


public class PhoneNumberInputFragment extends Fragment {

    private EditText phoneNumberEditText;
    private CountryCodePicker countryCodePicker;
    private Button sendNumberBtn;

    private PhoneVerificationCallBack mCallbacks;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCallbacks = ((PhoneNumberInputActivity) getActivity()).getPhoneVerificationCallBack();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.phone_number_input_layout, container, false);

        phoneNumberEditText = view.findViewById(R.id.phone_number_edit_text);
        countryCodePicker = view.findViewById(R.id.ccp);
        sendNumberBtn = view.findViewById(R.id.send_number_btn);

        countryCodePicker.registerCarrierNumberEditText(phoneNumberEditText);

        sendNumberBtn.setOnClickListener(view1 -> {

            if (!countryCodePicker.isValidFullNumber()) {
                showErrorDialog();
            }else {
                sendVerifyPhoneNumber(countryCodePicker.getFullNumberWithPlus());
            }
        });


        return view;
    }

    private void showErrorDialog() {

        new TTFancyGifDialog.Builder(getActivity())
                .setMessage("Invalid number")
                .setPositiveBtnText("Ok")
                .setPositiveBtnBackground("#22b573")
                .setGifResource(R.drawable.error)      //pass your gif, png or jpg
                .isCancellable(false)
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
        PhoneCodeVerificationFragment phoneCodeVerificationFragment = new PhoneCodeVerificationFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.phone_verification_container, phoneCodeVerificationFragment,INPUT_CODE_FRAGMENT_TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}


