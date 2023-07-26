package com.dbtapps.chatroom.authentication;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.dbtapps.chatroom.activities.LoginPage;
import com.dbtapps.chatroom.activities.OTPVerificationPage;
import com.dbtapps.chatroom.activities.RegisterPage;
import com.dbtapps.chatroom.constants.Constants;
import com.dbtapps.chatroom.utilities.FinishCurrentActivity;
import com.dbtapps.chatroom.utilities.LoadingAnimationController;
import com.dbtapps.chatroom.utilities.MakeToast;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class FirebaseAuthentication {

    private static String mVerificationId;
    private static PhoneAuthProvider.ForceResendingToken mResendToken;
    private static PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private static FirebaseAuth mAuth;
    private static int LOGIN_REGISTER_FLAG;
    private static String phno;

    public static void sendOTP(Activity activity, String phoneNumber, TextView appName, MaterialButton button, LottieAnimationView loadingAnimation, int lrFlag){
        LOGIN_REGISTER_FLAG = lrFlag;
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

                Log.d("Debug", "FirebaseAuthentication : onVerificationCompleted:" + credential);
                LoadingAnimationController.animationStop(loadingAnimation);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Log.d("Debug", "onVerificationFailed", e);
                LoadingAnimationController.animationStop(loadingAnimation);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    MakeToast.makeLongToast(activity.getApplicationContext(), e.getMessage());
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    MakeToast.makeLongToast(activity.getApplicationContext(), "SMS Quota for ChatRoom has been exceeded due to huge number of users at the moment. Please wait till next day.");
                } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
                    MakeToast.makeLongToast(activity.getApplicationContext(), "reCAPTCHA verification unsuccessfull/ not done due to null activity");
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                MakeToast.makeToast(activity.getApplicationContext(), "Code sent to your phone number");
                LoadingAnimationController.animationStop(loadingAnimation);
                phno = phoneNumber;

                Pair pairs[] = new Pair[2];
                pairs[0] = new Pair<View,String>(appName, "appNameTransition");
                pairs[1] = new Pair<View,String>(button, "verifyBtnTransition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, pairs);
                Intent intent = new Intent(activity, OTPVerificationPage.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent, options.toBundle());
                //finishActivity(activity);
                FinishCurrentActivity.finish(activity);

                mVerificationId = verificationId;
                mResendToken = token;
            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(activity)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        LoadingAnimationController.animationStart(loadingAnimation);
    }

    public static void verifyOTP(Activity activity, String code, TextView appName, LottieAnimationView loadingAnimation, MaterialButton button){
        LoadingAnimationController.animationStart(loadingAnimation);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(activity, credential, appName, loadingAnimation, button);
    }


    private static void signInWithPhoneAuthCredential(Activity activity, PhoneAuthCredential credential,TextView appName, LottieAnimationView loadingAnimation, MaterialButton button) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    LoadingAnimationController.animationStop(loadingAnimation);
                    Constants.setKeyUserid(task.getResult().getUser().getUid());
                    Constants.setKeyPhone(phno);
                    Log.d("Debug", "signInWithCredential:success --> UID : " + Constants.getKeyUserid());
                    MakeToast.makeToast(activity.getApplicationContext(), "OTP Verification successful");
                    checkUserRegistered(activity, appName, button);
                } else {
                    LoadingAnimationController.animationStop(loadingAnimation);
                    Log.d("Debug", "signInWithCredential:failure", task.getException());
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        MakeToast.makeToast(activity.getApplicationContext(), "Wrong OTP Entered");
                        //finishActivity(activity);
                        FinishCurrentActivity.finish(activity);
                    }
                }
            });
    }

    private static void checkUserRegistered(Activity activity,TextView appName, MaterialButton button) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(Constants.getKeyUserid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(LOGIN_REGISTER_FLAG == 0 && document.exists()) {
                            Log.d("Debug", "DocumentSnapshot data: " + document.getData());
                            Constants.setKeyPassword(document.get("password").toString());
                            Constants.setKeyName(document.get("name").toString());
                            Constants.setKeyProfilePicture(document.get("profile_picture").toString());
                            Pair pairs[] = new Pair[2];
                            pairs[0] = new Pair<View, String>(appName, "appNameTransition");
                            pairs[1] = new Pair<View, String>(button, "loginBtnTransition");
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, pairs);
                            Intent intent = new Intent(activity, LoginPage.class);
                            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent, options.toBundle());
                            //finishActivity(activity);
                            FinishCurrentActivity.finish(activity);

                        }
                        else if(LOGIN_REGISTER_FLAG == 1 && !document.exists()){

                            Pair pairs[] = new Pair[2];
                            pairs[0] = new Pair<View, String>(appName, "appNameTransition");
                            pairs[1] = new Pair<View, String>(button, "registerBtnTransition");
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, pairs);
                            Intent intent = new Intent(activity, RegisterPage.class);
                            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent, options.toBundle());
                            //finishActivity(activity);
                            FinishCurrentActivity.finish(activity);

                        }
                        else if(LOGIN_REGISTER_FLAG == 0 && !document.exists()){
                            MakeToast.makeLongToast(activity.getApplicationContext(), "You are not registered. Please register first to Login.");
                            //finishActivity(activity);
                            FinishCurrentActivity.finish(activity);

                        }
                        else if(LOGIN_REGISTER_FLAG == 1 && document.exists()){
                            MakeToast.makeLongToast(activity.getApplicationContext(), "You are already registered. You can login.");
                            //finishActivity(activity);
                            FinishCurrentActivity.finish(activity);
                        }

                    }
                });
    }


}
