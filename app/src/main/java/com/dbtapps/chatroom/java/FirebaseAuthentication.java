package com.dbtapps.chatroom.java;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dbtapps.chatroom.LoginPage;
import com.dbtapps.chatroom.MainActivity;
import com.dbtapps.chatroom.OTPVerificationPage;
import com.dbtapps.chatroom.RegisterPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class FirebaseAuthentication {
    public static FirebaseUser USERID;
    public static String mVerificationId;
    public static PhoneAuthProvider.ForceResendingToken mResendToken;
    public static PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    public static FirebaseAuth mAuth;

    public static void sendOTP(Activity context, String phoneNumber, TextView appName, MaterialButton button){

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
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
                    // reCAPTCHA verification attempted with null Activity
                }


            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                Log.d("Debug", "onCodeSent:" + verificationId);

                Pair pairs[] = new Pair[2];
                pairs[0] = new Pair<View,String>(appName, "appNameTransition");
                pairs[1] = new Pair<View,String>(button, "verifyButtonTransition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(context, pairs);
                Intent intent = new Intent(context, OTPVerificationPage.class);
                context.startActivity(intent, options.toBundle());

                mVerificationId = verificationId;
                mResendToken = token;
            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(context)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }

    public static void verifyOTP(Activity context, String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(context, credential);
    }


    private static void signInWithPhoneAuthCredential(Activity context, PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("Debug", "signInWithCredential:success");

                    USERID = task.getResult().getUser();
                    context.onBackPressed();

                } else {

                    Log.d("Debug", "signInWithCredential:failure", task.getException());
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                    }
                }
            }
        });



    }

}
