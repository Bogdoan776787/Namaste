package com.via.namaste.databasehelper;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.via.namaste.SignInActivity;
import com.via.namaste.models.UserLiveData;


public class AuthRepository {
    private final Application application;
    private final UserLiveData currentUser;

    // Firebase instances
    private final FirebaseAuth mAuth;

    // Facebook
    private final CallbackManager facebookCallbackManager;
    public AuthRepository(Application application) {
        this.application=application;
        mAuth=FirebaseAuth.getInstance();
        facebookCallbackManager = CallbackManager.Factory.create();
    currentUser=new UserLiveData();
    }
    public CallbackManager getFacebookCallbackManager() {
        return facebookCallbackManager;
    }
    public LiveData<FirebaseUser> getCurrentUser() {
        return currentUser;
    }
    public void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential);

    }
}
