package com.dbtapps.chatroom.authentication;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dbtapps.chatroom.activities.LoginPage;
import com.dbtapps.chatroom.activities.OTPVerificationPage;
import com.dbtapps.chatroom.activities.RegisterPage;
import com.dbtapps.chatroom.constants.Constants;
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
import java.util.concurrent.TimeUnit;

public class FirebaseAuthentication {

    private static String mVerificationId;
    private static PhoneAuthProvider.ForceResendingToken mResendToken;
    private static PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private static FirebaseAuth mAuth;
    private static int LOGIN_REGISTER_FLAG;

    public static void sendOTP(Activity activity, String phoneNumber, TextView appName, MaterialButton button, int lrFlag){
        LOGIN_REGISTER_FLAG = lrFlag;
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

                Log.d("Debug", "FirebaseAuthentication : onVerificationCompleted:" + credential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Log.d("Debug", "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    MakeToast.makeLongToast(activity.getApplicationContext(), e.getMessage());
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    MakeToast.makeLongToast(activity.getApplicationContext(), "SMS Quota for ChatRoom has been exceeded due to huge number of users at the moment. Please wait till next day.");
                } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
                    // reCAPTCHA verification attempted with null Activity
                }


            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                MakeToast.makeToast(activity.getApplicationContext(), "Code sent to your phone number");

                Pair pairs[] = new Pair[2];
                pairs[0] = new Pair<View,String>(appName, "appNameTransition");
                pairs[1] = new Pair<View,String>(button, "verifyBtnTransition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, pairs);
                Intent intent = new Intent(activity, OTPVerificationPage.class);
                intent.putExtra("loginRegisterFlag", LOGIN_REGISTER_FLAG);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent, options.toBundle());
                finishActivity(activity);

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

    }

    public static void verifyOTP(Activity activity, String code, TextView appName, MaterialButton button){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(activity, credential, appName, button);
    }


    private static void signInWithPhoneAuthCredential(Activity activity, PhoneAuthCredential credential,TextView appName, MaterialButton button) {

        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("Debug", "signInWithCredential:success");
                    Constants.USERID = task.getResult().getUser();
                    MakeToast.makeToast(activity.getApplicationContext(), "OTP Verification successful");
                    if(LOGIN_REGISTER_FLAG == 0) {
                        Pair pairs[] = new Pair[2];
                        pairs[0] = new Pair<View,String>(appName, "appNameTransition");
                        pairs[1] = new Pair<View,String>(button, "loginBtnTransition");
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, pairs);
                        Intent intent = new Intent(activity, LoginPage.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent,options.toBundle());
                        finishActivity(activity);
                    }
                    else {
                        Pair pairs[] = new Pair[2];
                        pairs[0] = new Pair<View,String>(appName, "appNameTransition");
                        pairs[1] = new Pair<View,String>(button, "registerBtnTransition");
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, pairs);
                        Intent intent = new Intent(activity, RegisterPage.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent,options.toBundle());
                        finishActivity(activity);
                    }
                } else {
                    Log.d("Debug", "signInWithCredential:failure", task.getException());
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        MakeToast.makeToast(activity.getApplicationContext(), "Wrong OTP Entered");
                        finishActivity(activity);
                    }
                }
            });
    }

    private static void finishActivity(Activity activity){
        new Handler().postDelayed(() -> {
            activity.finish();
        },1000);
    }

}
