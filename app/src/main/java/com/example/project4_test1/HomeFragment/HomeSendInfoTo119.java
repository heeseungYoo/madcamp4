package com.example.project4_test1.HomeFragment;

import android.os.AsyncTask;
import android.util.Log;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HomeSendInfoTo119 extends AsyncTask<String, Void, Boolean> {

    public HomeSendInfoTo119(){}

    @Override
    protected Boolean doInBackground(String... strings) {
        String text = strings[0];
        HttpURLConnection connection = null;

        try {
            URL url = new URL("http://192.249.19.252:1180");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
            osw.write(text);
            osw.flush();
            osw.close();

            if(connection.getResponseCode() <205)
            {
                return true;
            }
            else
            {
                return false;
            }
        } catch (Exception e) {
            e.getMessage();
            Log.d("Got error", e.getMessage());
            return false;
        } finally {
            connection.disconnect();
        }
    }
}