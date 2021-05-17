package com.via.namaste;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.via.namaste.callback.OnFacebookLoginListener;

import java.util.Arrays;
import java.util.List;


public class SignInActivity extends AppCompatActivity {
    // Facebook
    private CallbackManager mCallbackManager;
    private LoginButton loginButton;
    private SignInViewModel signInViewModel;
    private static final String TAG="FacebookAuthentication";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page);
        signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        loginButton = findViewById(R.id.login_button);
checkIfSignedIn();
        // Initialize Facebook Login button
        mCallbackManager=   signInViewModel.getFacebookCallbackManager();
      registerFacebookCallback();

    }

    private void registerFacebookCallback() {
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
               signInViewModel.handleFacebookAccessToken(loginResult.getAccessToken(),onFacebookLoginListener);
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
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void checkIfSignedIn() {
       FirebaseUser user= signInViewModel.getUser();
        if ( user!= null) {
            goToHome(user);
        }
    }



    private void goToHome(FirebaseUser user) {
        if(user!=null){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    OnFacebookLoginListener onFacebookLoginListener=new OnFacebookLoginListener() {
        @Override
        public void OnFacebookLoginSuccessCallback(FirebaseUser user) {
goToHome(user);
        }

        @Override
        public void OnFacebookLoginFailedCallback(FirebaseUser user) {

        }
    };
}
