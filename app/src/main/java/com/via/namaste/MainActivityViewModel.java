package com.via.namaste;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.via.namaste.repository.AuthRepository;

public class MainActivityViewModel extends AndroidViewModel {
    AuthRepository authRepository;
    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        authRepository=new AuthRepository(application);
    }
    public void signOut(){
        authRepository.signOut();
    }
}
