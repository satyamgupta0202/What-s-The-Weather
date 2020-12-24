package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city = findViewById(R.id.editText);
    }

    public void getWeather(View view) {
        DownloadTask task = new DownloadTask();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q="+city.getText().toString()+"&APPID=be1eab311204153c5b99974ec7df464c");
    }
    public class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection connection = null;
            String result = "";
            try {
                url =  new URL(urls[0]);
                connection = (HttpURLConnection)url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1){
                    char current = (char)data;
                    result = result+current;
                    data= reader.read();
                }
                return result;
            }
            catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                JSONArray arr = new JSONArray(weatherInfo);
                for(int i=0;i<4;i++){
                    JSONObject jsonPart = arr.getJSONObject(i);
                    Log.i("main",jsonPart.getString("main"));
                    Log.i("description",jsonPart.getString("description"));
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}