package com.eandr.bazzing.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.eandr.bazzing.MainActivity;
import com.eandr.bazzing.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hmomeni.progresscircula.ProgressCircula;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {
    private static int REQUEST_EXTERNAL_STORAGE = 1;

    private CircleImageView profileImage;
    private Uri profileImageUri;
    private TextInputLayout  userNameTextInputLayout;
    private Button saveProfileBtn;

    private FirebaseAuth firebaseAuth;
    private StorageReference firebaseStorage;
    private ProgressCircula progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profileImage = findViewById(R.id.profile_image);
        userNameTextInputLayout = findViewById(R.id.user_name_input_text);
        saveProfileBtn = findViewById(R.id.save_user_profile_btn);
        progressBar = findViewById(R.id.user_profile_progress_bar);

        profileImage.setOnClickListener(view -> {
                if (ContextCompat.checkSelfPermission(UserProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    //aks user for permission
                    ActivityCompat.requestPermissions(UserProfile.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_EXTERNAL_STORAGE);
                } else{
                    //user already gave permission
                    chooseProfileImage();
                }
        });

        saveProfileBtn.setOnClickListener(v->saveUserProfileToServer());
         firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance().getReference();


    }

    private void saveUserProfileToServer() {
        String userName = userNameTextInputLayout.getEditText().getText().toString().trim();
        String userId = firebaseAuth.getCurrentUser().getUid();
        if (profileImageUri == null){
            profileImageUri = Uri.EMPTY;
        }
        //show progress bar
        progressBar.setVisibility(View.VISIBLE);
        firebaseStorage.child("profile_images")
                       .child(userId+".jpg")
                       .putFile(profileImageUri)
                       .addOnCompleteListener(task -> {
                           progressBar.setVisibility(View.INVISIBLE);
                           if(task.isSuccessful()){
                               Toast.makeText(UserProfile.this,"profile saved successfully",Toast.LENGTH_SHORT).show();
                               //go to main activity
                               Intent intent = new Intent(UserProfile.this, MainActivity.class);
                               startActivity(intent);
                               finish();
                           }else{
                               String error = task.getException().getMessage();
                               showErrorDialog(error);
                           }
                       });


    }

    private void showErrorDialog(String error) {

        new TTFancyGifDialog.Builder(this)
                .setMessage(error)
                .setPositiveBtnText("Ok")
                .setPositiveBtnBackground("#22b573")
                .setGifResource(R.drawable.error)      //pass your gif, png or jpg
                .isCancellable(false)
                .build();
    }


    private void chooseProfileImage(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                profileImageUri = result.getUri();
                profileImage.setImageURI(profileImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_EXTERNAL_STORAGE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                chooseProfileImage();
            }
        }
    }
}
