package com.grupo6.buscapets.ui.protectora;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProtectoraViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ProtectoraViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}