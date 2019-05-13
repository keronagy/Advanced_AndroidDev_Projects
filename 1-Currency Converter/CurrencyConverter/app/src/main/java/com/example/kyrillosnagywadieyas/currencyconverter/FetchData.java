package com.example.kyrillosnagywadieyas.currencyconverter;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchData extends AsyncTask<Void,Void,Void>{

    private String data = "";
    private float value =0;
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("http://www.floatrates.com/daily/usd.json");
            //URL url = new URL("https://api.myjson.com/bins/tiu28");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line ="";
            while (line != null)
            {
                line = bufferedReader.readLine();
                data = data+line;
            }
            JSONObject jsonArray = new JSONObject(data);
            JSONObject jo = (JSONObject) jsonArray.get("egp");
            value = (float) jo.getDouble("rate");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        MainActivity.data = this.value;
    }
}
