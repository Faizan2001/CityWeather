package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    String weather = "";
    EditText cityName;
    String link2;
    TextView resultView;
    String aCityName = "";


    public void ButtonPressed(View view) {


       Log.i("City Name", cityName.getText().toString());

        link2 = cityName.getText().toString();
        String link1 = "http://api.openweathermap.org/data/2.5/weather?q=";
        String link3 = "&appid=b0d4eb526223d692313d79df59c2d3ef";

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(), 0);


        try {
            String urlEncodedText = URLEncoder.encode(link2, "UTF-8");

            aCityName = link1 + urlEncodedText  + link3;

            Log.i("Link", aCityName);

            DownloadTask task = new DownloadTask();
            task.execute(aCityName);
        } catch (UnsupportedEncodingException e) {

            Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG);

        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        cityName = (EditText) findViewById(R.id.cityNameEntered);
        resultView = (TextView) findViewById(R.id.ResultView);




    }

    public class DownloadTask extends AsyncTask<String, Void, String> {




        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;


            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;
                    result += current;

                    data = reader.read();

                }

            } catch (Exception e) {

                Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG);
            }


            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i("Website contents", result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo = jsonObject.getString("weather");

                JSONArray jsonArray = new JSONArray(weatherInfo);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonPart = jsonArray.getJSONObject(i);

                    Log.i("main", jsonPart.getString("main"));
                    Log.i("description", jsonPart.getString("description"));

                    weather = jsonPart.getString("main") + " ; " + jsonPart.getString("description");

                    resultView.setText("Weather : " + weather);



                }
            } catch (JSONException e) {

                Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG);
            }
        }
    }
}