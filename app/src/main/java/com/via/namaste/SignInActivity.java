package com.via.namaste;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.via.namaste.databasehelper.AuthRepository;

public class SignInActivity extends AppCompatActivity {
    // Facebook
    private CallbackManager facebookCallbackManager;
    private LoginButton loginButton;
    private static final String TAG = "FacebookAuthentication";
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        FirebaseApp.initializeApp(this);
        authRepository = new AuthRepository(getApplication());

        facebookCallbackManager = authRepository.getFacebookCallbackManager();

        authRepository.getCurrentUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    goToMain();
                }
            }
        });

        loginButton = findViewById(R.id.facebook_login_button);
        loginButton.registerCallback(authRepository.getFacebookCallbackManager(), new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                authRepository.handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}