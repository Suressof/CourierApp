package com.example.courierapp.ui.couriers;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.courierapp.R;
import com.example.courierapp.databinding.FragmentCourierBinding;
import com.example.courierapp.databinding.FragmentOrderBinding;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourierFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourierFragment extends Fragment {
    private FragmentCourierBinding binding;
    private RecyclerView recyclerView;
    private TableAdapterCourier tableAdapter;
    List<String> data = new ArrayList<>();
    List<String> data1 = new ArrayList<>();
    List<String> data2 = new ArrayList<>();
    private boolean check = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CourierFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourierFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourierFragment newInstance(String param1, String param2) {
        CourierFragment fragment = new CourierFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCourierBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // final TextView textView = binding.textGallery;

        recyclerView = binding.recyclerView;

        new FetchDataAsyncTask().execute("http://pe4bridge.ddns.net/api/Deliverymen/All");

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

                String email = "";
                String phone = "";
                String login = "";

                try {
                    // Начало чтения массива JSON
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        // Начало чтения объекта
                        jsonReader.beginObject();

                        // Чтение полей объекта
                        while (jsonReader.hasNext()) {
                            String fieldName = jsonReader.nextName();
                            if (fieldName.equals("email") && !check) {
                                email = jsonReader.nextString();
                                data.add("email: " + email);

                            } else if (fieldName.equals("phone") && !check) {
                                phone = jsonReader.nextString();
                                data1.add("phone: " + phone);

                            } else if (fieldName.equals("login")) {
                                login = jsonReader.nextString();
                                data2.add("login: " + login);

                            }  else {
                                jsonReader.skipValue(); // Пропустить неизвестные поля
                            }
                        }

                        // Конец чтения объекта
                        jsonReader.endObject();
                    }
                    check = true;

                    // Конец чтения массива JSON
                    jsonReader.endArray();


                    tableAdapter = new TableAdapterCourier(data, data1, data2);
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