package com.example.courierapp.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.courierapp.auth.AuthActivity;
import com.example.courierapp.ui.order.TableAdapter;
import com.google.android.material.navigation.NavigationView;

import com.example.courierapp.R;
import com.example.courierapp.databinding.FragmentOrderBinding;
import com.example.courierapp.databinding.FragmentHomeBinding;
import com.example.courierapp.ui.order.OrderFragment;
import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    Bundle bundle;
    Bundle bundleHome;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;

        // Получение данных из активности
        bundle = getArguments();
        if (bundle != null) {
            // String login = bundle.getString("login");
            // Используйте значение login по мере необходимости


            bundleHome = new Bundle();
            bundleHome.putString("login", bundle.getString("login"));
            bundleHome.putString("count", bundle.getString("count"));

            homeViewModel.setData(bundleHome);
        }

        // homeViewModel.getData().observe(getViewLifecycleOwner(), textView::setText);
        homeViewModel.getData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String newData) {
                textView.setText(newData);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}