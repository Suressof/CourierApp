package com.example.courierapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.courierapp.auth.AuthActivity;
import com.example.courierapp.auth.CheckEmail;
import com.example.courierapp.auth.PasswordConfirm;
import com.example.courierapp.databinding.FragmentOrderBinding;
import com.example.courierapp.databinding.FragmentHomeBinding;
import com.example.courierapp.ui.home.HomeFragment;
import com.example.courierapp.ui.home.HomeViewModel;
import com.example.courierapp.ui.order.OrderFragment;
import com.example.courierapp.ui.order.OrderViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.courierapp.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private String dataToSend;
    private HomeViewModel viewModel;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String login = intent.getStringExtra("login");
        String email = intent.getStringExtra("email");
        String count = intent.getStringExtra("count");

        bundle = new Bundle();
        bundle.putString("login", login);
        bundle.putString("count", count);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setVisibility(View.INVISIBLE);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_orders, R.id.nav_completedorders, R.id.nav_map, R.id.nav_couriers)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

//        new FetchDataAsyncTask(MainActivity.this).execute("http://pe4bridge.ddns.net/api/Orders");

        navController.navigate(R.id.nav_home, bundle);


        NavigationView navView = binding.navView;
        Menu menu = navView.getMenu();
        MenuItem item = menu.findItem(R.id.nav_couriers);
        item.setVisible(false);

        if(Objects.equals(login,"Admin")) {
            binding.appBarMain.fab.setVisibility(View.VISIBLE);

            Menu menu2 = navView.getMenu();
            MenuItem item2 = menu2.findItem(R.id.nav_couriers);
            item2.setVisible(true);

            binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navController.navigate(R.id.nav_add, null);
                    binding.appBarMain.fab.setVisibility(View.INVISIBLE);

//                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                }
            });
        }

        // Раздуть макет navheadermain
        LayoutInflater inflater = LayoutInflater.from(this);
        View headerView = inflater.inflate(R.layout.nav_header_main, null);
        // Получить ссылку на TextView внутри раздутого макета
        TextView textView = headerView.findViewById(R.id.loginHeader);
        TextView textView1 = headerView.findViewById(R.id.textView);
        // Установить желаемый текст
        textView.setText(login);
        textView1.setText(email);
        navigationView.addHeaderView(headerView);

    }

    public void Exit(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Вы уверены, что хотите выйти из аккаунта?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Действие при выборе "Да"
                        SharedPreferences.Editor editor = MainActivity.this.getSharedPreferences("AuthToken", Context.MODE_PRIVATE).edit();
                        editor.putString("auth_token", null);
                        editor.putString("login", null);
                        editor.apply();

                        Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                        startActivity(intent);

                        finish();
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Действие при выборе "Нет"
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void FragmentManager(HomeFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}