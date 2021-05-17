package com.via.namaste.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.via.namaste.SignInActivity;
import com.via.namaste.callback.OnFacebookLoginListener;

public class AuthRepository {

    private FirebaseAuth mAuth;
    private Application application;
    private CallbackManager facebookCallbackManager;

    public AuthRepository(Application application) {
        mAuth=FirebaseAuth.getInstance();
        this.application=application;
        facebookCallbackManager = CallbackManager.Factory.create();

    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public CallbackManager getFacebookCallbackManager() {
        return facebookCallbackManager;
    }

    public void handleFacebookAccessToken(AccessToken accessToken, OnFacebookLoginListener onFacebookLoginListener) {

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(ContextCompat.getMainExecutor(application), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Facebook", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                         onFacebookLoginListener.OnFacebookLoginSuccessCallback(getCurrentUser());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Facebook", "signInWithCredential:failure", task.getException());
                            Toast.makeText(application, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void signOut(){
        AuthUI.getInstance()
                .signOut(application.getApplicationContext());
    }
}
