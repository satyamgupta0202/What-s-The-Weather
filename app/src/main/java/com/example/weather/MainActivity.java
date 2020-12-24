package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText city;
    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city = findViewById(R.id.editText);
        result = findViewById(R.id.result);
    }

    public void getWeather(View view) {
        DownloadTask task = new DownloadTask();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q="+city.getText().toString()+"&APPID=be1eab311204153c5b99974ec7df464c");
        InputMethodManager mgr = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(city.getWindowToken(),0);
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
                Toast.makeText(getApplicationContext(),"could not find weather",Toast.LENGTH_LONG).show();
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
                String string = "";
                for(int i=0;i<arr.length();i++){
                    JSONObject jsonPart = arr.getJSONObject(i);
                    String main =  jsonPart.getString("main");
                    String description=jsonPart.getString("description");
                    if(!main.equals("") && !description.equals(""))
                    string += main + ": " + description + "\r\n";
                }
                if(!string.equals(""))
                    result.setText(string);
                else
                {
                    Toast.makeText(getApplicationContext(),"could not find weather",Toast.LENGTH_SHORT).show();

                }
            }
            catch (Exception e){

                Toast.makeText(getApplicationContext(),"could not find weather",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}