package com.via.namaste.callback;

import com.google.firebase.auth.FirebaseUser;

public interface OnFacebookLoginListener {
   void OnFacebookLoginSuccessCallback(FirebaseUser user);
    void OnFacebookLoginFailedCallback(FirebaseUser user);
}
