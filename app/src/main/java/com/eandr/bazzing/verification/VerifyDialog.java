package com.eandr.bazzing.verification;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.eandr.bazzing.R;

public class VerifyDialog {

    private Activity activity;
    private Dialog dialog;

    public VerifyDialog(Activity activity){
        this.activity = activity;
    }

    public void showDialog(){

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.verify_user_dialog);

        ImageView gifImageView = dialog.findViewById(R.id.verify_imageView);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gifImageView);
        Glide.with(activity)
             .load(R.drawable.verify)
             .placeholder(R.drawable.verify)
             .centerCrop()
             .crossFade()
             .into(imageViewTarget);

        dialog.show();

    }


    public void hideDialog(){
        dialog.dismiss();
    }



}
