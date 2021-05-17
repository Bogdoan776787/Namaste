package com.via.namaste;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.google.firebase.auth.FirebaseUser;
import com.via.namaste.callback.OnFacebookLoginListener;
import com.via.namaste.repository.AuthRepository;

public class SignInViewModel extends AndroidViewModel {
    AuthRepository authRepository;
    public SignInViewModel(@NonNull Application application) {
        super(application);
        authRepository=new AuthRepository(application);
    }

    public FirebaseUser getUser() {
        return authRepository.getCurrentUser();
    }

    public CallbackManager getFacebookCallbackManager() {
        return authRepository.getFacebookCallbackManager();
    }

    public void handleFacebookAccessToken(AccessToken accessToken, OnFacebookLoginListener onFacebookLoginListener) {
        authRepository.handleFacebookAccessToken( accessToken,onFacebookLoginListener);
    }
}
