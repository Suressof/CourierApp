package com.example.courierapp.ui.order;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.courierapp.R;
import com.example.courierapp.auth.AuthActivity;
import com.example.courierapp.databinding.ActivityMainBinding;
import com.example.courierapp.databinding.FragmentDetailBinding;
import com.example.courierapp.databinding.FragmentOrderBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {
    private FragmentDetailBinding binding;
    StringBuilder result;
    String rowData4;
    private ActivityMainBinding binding1;

    // Получение переданных данных из аргументов фрагмента
    public static DetailFragment newInstance(String rowData) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString("rowData", rowData);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String rowData2 = getArguments().getString("rowData2");
            new FetchDataAsyncTask().execute("http://pe4bridge.ddns.net/api/Customers/" + rowData2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//        binding1 = ActivityMainBinding.inflate(getLayoutInflater());
//        binding1.appBarMain.toolbar.findViewById(R.id.imageButton7).setVisibility(View.VISIBLE);
                // Получение данных из активности
                Bundle bundle = getArguments();
                if (bundle != null) {
                    String rowData = getArguments().getString("rowData");
                    String rowData2 = getArguments().getString("rowData2");
                    String rowData1 = getArguments().getString("rowData1");
                    binding.dataTextView.setText("№ " + rowData);
                    binding.TextView.setText("Номер - " + rowData2);
                    binding.textView2.setText("Дата - " + rowData1);
                    binding.textView3.setText("Адрес - " + rowData4);
                }

                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);

                binding.button4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navController.navigate(R.id.nav_map, null);
                    }
                });

                binding.button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar snackbar = Snackbar.make(v, "Заказ был выполнен", Snackbar.LENGTH_LONG);
                        // Почта недействительна
                        View snackbarView = snackbar.getView();
                        snackbarView.setBackgroundColor(getResources().getColor(R.color.green));
                        snackbar.show();

                        navController.navigate(R.id.nav_orders, null);
                    }
                });
            }
        }, 100);
        return root;

    }

    public class FetchDataAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                String urlString = urls[0];
                // Создание объекта URL
                URL url = new URL(urlString);
                // Создание объекта HttpURLConnection
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Установка метода запроса и таймаута
                connection.setRequestMethod("GET");
                connection.setReadTimeout(2000);

                // Чтение данных из URL-соединения
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();

                // Возвращение полученных данных в onPostExecute
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Отображение полученных данных в текстовом поле
            if (result != null) {

                System.out.println(result);

                JsonReader jsonReader = new JsonReader(new StringReader(result));
                try {
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        String fieldName = jsonReader.nextName();
                        if (fieldName.equals("address")) {
                            rowData4 = jsonReader.nextString();
                        } else {
                            jsonReader.skipValue(); // Пропустить неизвестные поля
                        }
                    }
                    // Конец чтения объекта
                    jsonReader.endObject();

                    System.out.println(rowData4);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {

            }
        }
    }

//    private String loadAccessToken() {
//        String access_token = "";
//        try {
//            SharedPreferences prefs = this.getSharedPreferences("AuthToken", MODE_PRIVATE);
//            access_token = prefs.getString("auth_token", null);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return access_token;
//    }
}
