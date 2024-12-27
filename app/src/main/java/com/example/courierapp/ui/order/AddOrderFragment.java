package com.example.courierapp.ui.order;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.toolbox.HttpResponse;
import com.example.courierapp.R;
import com.example.courierapp.databinding.FragmentAddOrderBinding;
import com.example.courierapp.databinding.FragmentDetailBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddOrderFragment extends Fragment {
    private EditText ed;

    public int id;
    String goodsName;
    HashMap goodsMap;
    private FragmentAddOrderBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddOrderFragment newInstance(String param1, String param2) {
        AddOrderFragment fragment = new AddOrderFragment();
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

        binding = FragmentAddOrderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Spinner spinner = root.findViewById(R.id.spinner);
        ed = root.findViewById(R.id.number);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDate = formatter.format(new Date());
        List<String> data = new ArrayList<>();
        data.add("Наличные");
        data.add("Карта");

        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, data);
        binding.spinner.setAdapter(spinnerAdapter);
        createMap();
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);


        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int pos, long arg3) {
                goodsName = spinner.getSelectedItem().toString();
                id = (int) goodsMap.get(goodsName);
                System.out.println("API Token: " + goodsName + id + formattedDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(view, "Заказ был добавлен", Snackbar.LENGTH_LONG);
                // Почта недействительна
                View snackbarView = snackbar.getView();
                snackbarView.setBackgroundColor(getResources().getColor(R.color.green));
                snackbar.show();

                navController.navigate(R.id.nav_orders, null);

//                JSONObject userJson = new JSONObject();
//                try {
//                    userJson.put("id", 100);
//                    userJson.put("creationDate", formattedDate);
//                    userJson.put("customerPhone", ed.getText().toString());
//                    userJson.put("paymentWayId", id);
//                    userJson.put("isFinished", false);
//                } catch (JSONException e) {}

                CreateOrder user = new CreateOrder();
                user.setId(0);
                user.setCreationDate("2023-12-20T00:00:00");
                user.setcustomerPhone("88005553535");
                user.setPaymentWayId(1);
                user.setisFinished(false);
                Gson gson = new Gson();

//                String json = gson.toJson(user);
//                System.out.println("API Token: " + json);

//                new SendRequestTask(getContext(), "http://pe4bridge.ddns.net/api/Orders?token=buA%2FjDTWzJscaZmo%2BH4EcEf%2B%2FlUpyFQ8UlsmegZfFfM%3D", json).execute();
//                String json = "{\"name\":\"John\", \"surname\":\"Doe\"}";
//
//
//                // Создаем HttpClient
//                CloseableHttpClient httpClient = HttpClients.createDefault();
//
//                try {
//                    // Создаем POST запрос
//
//                    HttpPost request = new HttpPost("http://pe4bridge.ddns.net/api/Orders");
//
//                    // Добавляем данные в виде строки JSON
//                    try {
//                        request.setEntity(new StringEntity(json));
//                    } catch (UnsupportedEncodingException e) {
//                        throw new RuntimeException(e);
//                    }
//
//                    // Отправляем запрос и получаем ответ
//                    CloseableHttpResponse response = null;
//                    try {
//                        response = httpClient.execute(request);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//
//                    System.out.println("Response: " + response.getStatusLine());
//
//                    if (response.getEntity() != null) {
//                        System.out.println(response.getEntity().getContent());
//                    }
//
//                } finally {
//                    httpClient.close();
//                }




            }
        });

        return root;
    }

    public interface ApiInterface {
        @Headers({
                "Content-Type: application/json",
                "X-API-Token: Gr/fXobq+0HInCzYUQmkTMIs1/zedVpE+OvQyHXHHPU="
        })
        @POST("/api/Orders")
        Call<ResponseBody> postOrders(@Header("X-API-Key") String token, @Body RequestBody body);
    }
    void createMap () {
        goodsMap = new HashMap();
        goodsMap.put("Наличные", 1);
        goodsMap.put("Карта", 2);
    }
    public class SendRequestTask extends AsyncTask<String, Void, JSONObject> {
        private String url;
        private String json;
        private Context context;

        public SendRequestTask(Context context, String url, String json) {
            this.url = url;
            this.json = json;
            this.context = context;
        }

        @Override
        protected JSONObject doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setConnectTimeout(10000);
                OutputStream outputStream = urlConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.write(json);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = urlConnection.getInputStream();

            } catch (Exception e) {
                Log.e("SendRequestTask", e.getMessage(), e);
            }

            return null;
        }


        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                // Обработка ответа
            } else {
                Toast.makeText(context, "Ошибка запроса", Toast.LENGTH_SHORT).show();
            }
        }
    }
}