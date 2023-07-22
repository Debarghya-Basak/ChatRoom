package com.dbtapps.chatroom.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.dbtapps.chatroom.R;
import com.dbtapps.chatroom.constants.Constants;
import com.dbtapps.chatroom.databinding.ActivityRegisterPageBinding;
import com.dbtapps.chatroom.utilities.BitmapManipulator;
import com.dbtapps.chatroom.utilities.MakeToast;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterPage extends AppCompatActivity {

    private static final int REQUEST_GET_SINGLE_FILE = 200;
    private ActivityRegisterPageBinding binding;
    Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

        defaultProfilePicMaker();
        registerButtonListener();
        profilePicCivListener();
    }

    private void defaultProfilePicMaker() {
        Bitmap bitmap = BitmapManipulator.getBitmapFromVectorDrawable(this, R.drawable.default_profile_pic);
        String profilePic = BitmapManipulator.bitMapToString(bitmap);
        Constants.setKeyProfilePicture(profilePic);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_GET_SINGLE_FILE) {
                    Uri selectedImageUri = data.getData();
                    encodeImageToString(selectedImageUri);
                    binding.profilePicCiv.setImageURI(selectedImageUri);
                }
            }
        } catch (Exception e) {
            Log.e("FileSelectorActivity", "File select error", e);
        }
    }

    private void profilePicCivListener() {
        binding.profilePicCiv.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_GET_SINGLE_FILE);
        });
    }


    private void encodeImageToString(Uri selectedImageUri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
        String profilePic = BitmapManipulator.bitMapToString(bitmap);
        Constants.setKeyProfilePicture(profilePic);
    }


    private void registerButtonListener() {
        binding.registerBtn.setOnClickListener(v -> {
            //TODO: Add checks for edit text
            Map<String, Object> userData = new HashMap<>();
            userData.put("name", binding.nameEt.getText().toString());
            userData.put("password", binding.passwordEt.getText().toString());
            userData.put("profile_picture", Constants.getKeyProfilePicture());
            userData.put("phone_number", Constants.getKeyPhone());
            userData.put("account_active_status", "false");
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .document(Constants.getUSERID().getUid())
                    .set(userData)
                    .addOnSuccessListener(aVoid -> {
                        MakeToast.makeToast(this, "User registered successfully. Please login once more.");
                        Log.d("Debug", "User Data added");
                        finish();
                    })
                    .addOnFailureListener(exception -> {
                        MakeToast.makeToast(this, "Registration failed : " + exception.getLocalizedMessage());
                        Log.d("Debug", "User Data could not be added --> " + exception.getLocalizedMessage());
                    });


        });
    }
}