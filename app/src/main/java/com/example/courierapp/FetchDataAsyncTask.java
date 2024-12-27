package com.example.courierapp;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.widget.Toast;

import com.example.courierapp.auth.AuthActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
            connection.setReadTimeout(10000);

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
            if (result.equals("true")) {
                System.out.println(result);

            }


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
                        if (fieldName.equals("id")) {
                            int id = jsonReader.nextInt();
                            System.out.println("ID: " + id);
                        } else if (fieldName.equals("first_name")) {
                            String firstName = jsonReader.nextString();
                            System.out.println("First Name: " + firstName);
                        } else if (fieldName.equals("last_name")) {
                            String lastName = jsonReader.nextString();
                            System.out.println("Last Name: " + lastName);
                        } else if (fieldName.equals("phone")) {
                            String phone = jsonReader.nextString();
                            System.out.println("Phone: " + phone);
                        } else if (fieldName.equals("password")) {
                            String password = jsonReader.nextString();
                            System.out.println("Password: " + password);
                        } else if (fieldName.equals("document_number")) {
                            String documentNumber = jsonReader.nextString();
                            System.out.println("Document Number: " + documentNumber);
                        } else if (fieldName.equals("api_token")) {
                            String apiToken = jsonReader.nextString();
                            System.out.println("API Token: " + apiToken);
                        } else if (fieldName.equals("created_at")) {
                            String createdAt = jsonReader.nextString();
                            System.out.println("Created At: " + createdAt);
                        } else if (fieldName.equals("updated_at")) {
                            String updatedAt = jsonReader.nextString();
                            System.out.println("Updated At: " + updatedAt);
                        } else {
                            jsonReader.skipValue(); // Пропустить неизвестные поля
                        }
                    }

                    // Конец чтения объекта
                    jsonReader.endObject();
                }

                // Конец чтения массива JSON
                jsonReader.endArray();
            } catch (IOException e) {
                e.printStackTrace();
            }

//            System.out.println(stringWithoutBrackets);
//            dataTextView.setText(result);
        } else {
//            dataTextView.setText("Error retrieving data");
        }
    }

    public class JsonParser {

        public String parseResponse(String jsonResponse) {
            String fieldData = null;

            try {
                JSONObject response = new JSONObject(jsonResponse);
                fieldData = response.getString("first_name");

                // Можете также получить другие поля из ответа
                // String otherFieldData = response.getString("otherFieldName");
                // int numericFieldData = response.getInt("numericFieldName");
                // ...

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return fieldData;
        }
    }
}
