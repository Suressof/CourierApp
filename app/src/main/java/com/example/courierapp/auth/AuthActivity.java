package com.example.courierapp.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.util.JsonReader;
import android.util.Log;
import android.widget.CheckBox;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.text.InputType;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.courierapp.FetchDataAsyncTask;
import com.example.courierapp.MainActivity;
import com.example.courierapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Objects;


public class AuthActivity extends AppCompatActivity {
    private int count = 0;
    private EditText ed;
    private EditText ed2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Button btn = findViewById(R.id.btn_auth);
        ed = findViewById(R.id.login);
        ed2 = findViewById(R.id.password);

        String accessToken = loadAccessToken();
        String Accesslogin = loadAccesslogin();
        String count = loadAccesscount();

        if (accessToken != null) {

            try {
                try {
                    System.out.println(Accesslogin);

                    accessToken = URLEncoder.encode(accessToken, "UTF-8");
                    try {

                        if(Objects.equals("Admin", Accesslogin)) {
                            Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                            intent.putExtra("login", Accesslogin);
                            intent.putExtra("count", count);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            // Вызов AsyncTask для выполнения запроса на сервер и получения данных
                            new AuthDataAsyncTask(AuthActivity.this).execute("http://pe4bridge.ddns.net/api/Deliverymen?token=" + accessToken);
                            new countDataAsyncTask(AuthActivity.this).execute("http://pe4bridge.ddns.net/api/Orders");

                            String Accessemail = loadAccessemail();

                            System.out.println(Accessemail);

                            Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                            intent.putExtra("login", Accesslogin);
                            intent.putExtra("email", Accessemail);
                            intent.putExtra("count", count);
                            startActivity(intent);

                            finish();
                        }
                    }
                    catch (Exception e) {
                        Toast.makeText(AuthActivity.this, "Сервер недоступен", Toast.LENGTH_LONG).show();
                    }

                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
            catch (Exception e) {
                Toast.makeText(AuthActivity.this, "Сервер недоступен", Toast.LENGTH_LONG).show();
            }

        }


        TextView myTextView = findViewById(R.id.textView2);
        String text = "CourierApp";
        float[] positions = new float[]{0.5f, 1.0f};
        SpannableString spannableString = new SpannableString(text);
        LinearGradient gradient = new LinearGradient(
                0, 0, 0, myTextView.getTextSize(),
                new int[]{Color.rgb(65, 105,225), Color.argb(85,150, 225,245)},
                positions,
                Shader.TileMode.CLAMP
        );

        // Установка созданного градиента в TextPaint
        TextPaint textPaint = myTextView.getPaint();
        textPaint.setShader(gradient);

        spannableString.setSpan(
                new ForegroundColorSpan(Color.BLACK), // Установка цвета по умолчанию
                0, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        myTextView.setText(spannableString);

        CheckBox showPasswordCheckbox = findViewById(R.id.checkBox);
        final EditText passwordEditText = findViewById(R.id.password);

        showPasswordCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                passwordEditText.setSelection(passwordEditText.getText().length());
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                HomeFragment home = new HomeFragment();
//                home. = ed.getText().toString();
                if(Objects.equals("", ed.getText().toString())) {
                    Toast.makeText(AuthActivity.this, "Введите логин и пароль", Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        // Вызов AsyncTask для выполнения запроса на сервер и получения данных
                        if(Objects.equals("Admin", ed.getText().toString())) {
                            new FetchDataAsyncTask(AuthActivity.this).execute("http://pe4bridge.ddns.net/api/Auth/Admin?password=" + ed2.getText().toString());
                        }
                        else {
                            new FetchDataAsyncTask(AuthActivity.this).execute("http://pe4bridge.ddns.net/api/Auth/DeliverymanLogin?login=" +  ed.getText().toString() + "&password=" + ed2.getText().toString());
                        }
                    }
                    catch (Exception e) {
                        Toast.makeText(AuthActivity.this, "Сервер недоступен", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public class FetchDataAsyncTask extends AsyncTask<String, Void, String> {
        Context context;

        public FetchDataAsyncTask(Context context) {
            this.context = context;
        }
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

//            String fieldData = new JsonParser().parseResponse(result);
//            String stringWithoutBrackets = result.replace("[", "").replace("]", "");

                System.out.println(result);

                SharedPreferences.Editor editor = context.getSharedPreferences("AuthToken", Context.MODE_PRIVATE).edit();
                editor.putString("auth_token", result);
                editor.apply();

                SharedPreferences.Editor editor1 = context.getSharedPreferences("Login", Context.MODE_PRIVATE).edit();

                try {
                    if(Objects.equals("Admin", ed.getText().toString())) {
                        new countDataAsyncTask(AuthActivity.this).execute("http://pe4bridge.ddns.net/api/Orders");
                        String count = loadAccesscount();

                        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                        intent.putExtra("login", ed.getText().toString());
                        intent.putExtra("count", count);
                        startActivity(intent);

                        editor1.putString("login", ed.getText().toString());
                        editor1.apply();

                        finish();
                    }
                    else {
                        result = URLEncoder.encode(result, "UTF-8");
                        // Вызов AsyncTask для выполнения запроса на сервер и получения данных
                        new AuthDataAsyncTask(AuthActivity.this).execute("http://pe4bridge.ddns.net/api/Deliverymen?token=" + result);
                        new countDataAsyncTask(AuthActivity.this).execute("http://pe4bridge.ddns.net/api/Orders");
                        String Accessemail = loadAccessemail();
                        String count = loadAccesscount();

                        System.out.println(Accessemail);

                        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                        intent.putExtra("login", ed.getText().toString());
                        intent.putExtra("email", Accessemail);
                        intent.putExtra("count", count);
                        startActivity(intent);

                        finish();
                    }
                }
                catch (Exception e) {
                    Toast.makeText(AuthActivity.this, "Сервер недоступен", Toast.LENGTH_LONG).show();
                }


//            System.out.println(stringWithoutBrackets);
//            dataTextView.setText(result);
            } else {
                Toast.makeText(AuthActivity.this, "Такого пользователя нету", Toast.LENGTH_LONG).show();

//                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
//                intent.putExtra("login", ed.getText().toString());
//                startActivity(intent);

                SharedPreferences.Editor editor = context.getSharedPreferences("AuthToken", Context.MODE_PRIVATE).edit();
                editor.putString("auth_token", null);
                editor.apply();
            }
        }
    }

    public class AuthDataAsyncTask extends AsyncTask<String, Void, String> {
        Context context;

        public AuthDataAsyncTask(Context context) {
            this.context = context;
        }
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

                SharedPreferences.Editor editor = context.getSharedPreferences("AuthToken", Context.MODE_PRIVATE).edit();

                System.out.println(result);

                JsonReader jsonReader = new JsonReader(new StringReader(result));
                try {
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        String fieldName = jsonReader.nextName();
                        if (fieldName.equals("email")) {
                            String email = jsonReader.nextString();
                            editor.putString("email", email);
                            editor.apply();
                        } else if (fieldName.equals("phone")) {
                            String phone = jsonReader.nextString();
                            editor.putString("phone", phone);
                            editor.apply();
                        } else if (fieldName.equals("login")) {
                            String login = jsonReader.nextString();
                            editor.putString("login", login);
                            editor.apply();
                        } else {
                            jsonReader.skipValue(); // Пропустить неизвестные поля
                        }
                    }
                    // Конец чтения объекта
                    jsonReader.endObject();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(AuthActivity.this, "Такого пользователя нету", Toast.LENGTH_LONG).show();

                SharedPreferences.Editor editor = context.getSharedPreferences("AuthToken", Context.MODE_PRIVATE).edit();
                editor.putString("auth_token", null);
                editor.apply();
            }
        }
    }

    public class countDataAsyncTask extends AsyncTask<String, Void, String> {
        Context context;
        public countDataAsyncTask(Context context) {
            this.context = context;
        }

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
                SharedPreferences.Editor editor = context.getSharedPreferences("AuthToken", Context.MODE_PRIVATE).edit();

                // Создайте объект JsonReader с использованием jsonString
                JsonReader jsonReader = new JsonReader(new StringReader(result));

                try {
                    // Начало чтения массива JSON
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        // Начало чтения объекта
                        jsonReader.beginObject();

                        // Чтение полей объекта
                        while (jsonReader.hasNext()) {
                            String fieldName = jsonReader.nextName();
                            if (fieldName.equals("isFinished")) {
                                boolean isFinished = jsonReader.nextBoolean();
                                if (!isFinished) {
                                    count = count + 1;
                                }
                            } else {
                                jsonReader.skipValue(); // Пропустить неизвестные поля
                            }
                        }
                        jsonReader.endObject();
                    }

                    // Конец чтения массива JSON
                    jsonReader.endArray();

                    editor.putString("count", String.valueOf(count));
                    editor.apply();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {
            }
        }
    }

    public void openLinkInBrowser(View view) {

        Intent intent = new Intent(this, CheckEmail.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
    }

    private String loadAccessToken() {
        String access_token = "";
        try {
            SharedPreferences prefs = this.getSharedPreferences("AuthToken", MODE_PRIVATE);
            access_token = prefs.getString("auth_token", null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return access_token;
    }

    private String loadAccesslogin() {
        String login = "";
        try {
            SharedPreferences prefs = this.getSharedPreferences("AuthToken", MODE_PRIVATE);
            login = prefs.getString("login", null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return login;
    }

    private String loadAccesscount() {
        String count = "";
        try {
            SharedPreferences prefs = this.getSharedPreferences("AuthToken", MODE_PRIVATE);
            count = prefs.getString("count", null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    private String loadAccessemail() {
        String email = "";
        try {
            SharedPreferences prefs = this.getSharedPreferences("AuthToken", MODE_PRIVATE);
            email = prefs.getString("email", null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return email;
    }


}



