package com.example.courierapp.ui.completedorder;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courierapp.R;
import com.example.courierapp.databinding.FragmentCompletedorderBinding;
import com.example.courierapp.ui.order.OrderFragment;
import com.example.courierapp.ui.order.TableAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CompletedOrderFragment extends Fragment {
    private TableAdapter tableAdapter;
    private RecyclerView recyclerView;
    private boolean check = false;
    List<String> data = new ArrayList<>();
    List<String> data1 = new ArrayList<>();
    List<String> data2 = new ArrayList<>();
    private FragmentCompletedorderBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CompletedOrderViewModel completedOrderViewModel =
                new ViewModelProvider(this).get(CompletedOrderViewModel.class);

        binding = FragmentCompletedorderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerView;

        new FetchDataAsyncTask().execute("http://pe4bridge.ddns.net/api/Orders");


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
                connection.setReadTimeout(5000);

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

                // Создайте объект JsonReader с использованием jsonString
                JsonReader jsonReader = new JsonReader(new StringReader(result));

                int id = 0;
                String creationDate = "";
                String customerPhone = "";
                String paymentWayId;

                try {
                    // Начало чтения массива JSON
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        // Начало чтения объекта
                        jsonReader.beginObject();

                        // Чтение полей объекта
                        while (jsonReader.hasNext()) {
                            String fieldName = jsonReader.nextName();
                            if (fieldName.equals("id") && !check) {
                                id = jsonReader.nextInt();
                            } else if (fieldName.equals("creationDate") && !check) {
                                creationDate = jsonReader.nextString();

                            } else if (fieldName.equals("customerPhone")) {
                                customerPhone = jsonReader.nextString();

                            } else if (fieldName.equals("paymentWayId")) {
                                paymentWayId = jsonReader.nextString();

                            } else if (fieldName.equals("isFinished") && !check) {
                                boolean isFinished = jsonReader.nextBoolean();
                                if (isFinished) {
                                    data.add("№ " + id);
                                    data1.add("Дата: " + creationDate);
                                    data2.add("Номер: " + customerPhone);
                                }
                            } else {
                                jsonReader.skipValue(); // Пропустить неизвестные поля
                            }
                        }

                        // Конец чтения объекта
                        jsonReader.endObject();
                    }
                    check = true;

                    // Конец чтения массива JSON
                    jsonReader.endArray();
                    tableAdapter = new TableAdapter(data, data1, data2);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(tableAdapter);

                    int itemCount = recyclerView.getAdapter().getItemCount();

                    System.out.println("API Token: " + itemCount);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {
            }
        }
    }
}