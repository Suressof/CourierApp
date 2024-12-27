package com.example.courierapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<String> data = new MutableLiveData<>();


    public void setData(Bundle bundle) {
        String login = bundle.getString("login");
        String count = bundle.getString("count");

        data.setValue("Добро пожаловать, " + login + "\n У вас " + count + " текущих заказов");
    }

    public LiveData<String> getData() {
        return data;
    }
}