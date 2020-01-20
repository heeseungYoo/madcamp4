package com.example.project4_test1.HomeFragment;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

//How to use =======================================================================
//        sendRequest task = new sendRequest();
////        try {
////                ArrayList<RequestItem> result = task.execute(item).get();
////
////        for (int i = 0; i < result.size(); i++){
////        MarkerOptions temp = new MarkerOptions();
////        temp.position(new LatLng(result.get(i).lat, result.get(i).lng));
////        temp.title(result.get(i).name);
////        temp.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pin));
////        restaurant_map.addMarker(temp);
////        }
////        } catch (InterruptedException e) {
////        e.printStackTrace();
////        } catch (ExecutionException e) {
////        e.printStackTrace();
////        }
//=====================================================================================

public class sendRequestAED extends AsyncTask<LatLng, Void, ArrayList<AEDinfo>> {

    static double lat;
    static double lng;

    public sendRequestAED(){}

    @Override
    protected ArrayList<AEDinfo> doInBackground(LatLng... latLngs) {

        LatLng current_point = latLngs[0];
        this.lat = current_point.latitude;
        this.lng = current_point.longitude;

//        http://apis.data.go.kr/B552657/AEDInfoInqireService/getAedLcinfoInqire?WGS84_LON=127.085156592737&WGS84_LAT=37.4881325624879&pageNo=1&numOfRows=1

        String LOG_TAG = "ExampleApp";
        String API_BASE = "http://apis.data.go.kr/B552657/AEDInfoInqireService/getAedLcinfoInqire";
        String API_KEY = "BVi8HM8Sym3gnktGu9casYAQVAkud9GjbGIAwh6Fre5I%2FgXI%2F6h5xojX8gYzjmGlmqevxvR%2B%2B5WNFtflXedgiA%3D%3D";

        //Boolean resultList = false;
        ArrayList<AEDinfo> resultList = new ArrayList<>();

        HttpURLConnection conn = null;
        StringBuilder result = new StringBuilder();

        try {
            // requestItem 정보 이용해서 요청할 url 생성 .
            StringBuilder sb = new StringBuilder(API_BASE);
            sb.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "="+API_KEY); /*Service Key*/
//            sb.append("&" + URLEncoder.encode("","UTF-8") + "=" + URLEncoder.encode(API_KEY, "UTF-8")); /**/
//            sb.append("&" + URLEncoder.encode("","UTF-8") + "=" + URLEncoder.encode(API_KEY, "UTF-8")); /**/
            sb.append("&_type=json");
            sb.append("&WGS84_LON="+lng);
            sb.append("&WGS84_LAT="+lat);

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;

            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(result.toString());
            JSONObject json_response = jsonObj.getJSONObject("response");
            JSONObject json_body = json_response.getJSONObject("body");
            JSONObject json_items = json_body.getJSONObject("items");
            JSONArray predsJsonArray = json_items.getJSONArray("item");

            if (predsJsonArray.length() == 0){
                AEDinfo temp = new AEDinfo().setError();
                resultList.add(temp);
            }

            else{
                // Extract the Place descriptions from the results
                for (int i = 0; i < predsJsonArray.length(); i++) {

                    String address = predsJsonArray.getJSONObject(i).getString("buildAddress");
                    String tel = predsJsonArray.getJSONObject(i).getString("clerkTel");
                    double lat = predsJsonArray.getJSONObject(i).getDouble("wgs84Lat");
                    double lng = predsJsonArray.getJSONObject(i).getDouble("wgs84Lon");

                    AEDinfo temp = new AEDinfo().setItem(address, tel, lat, lng);
                    resultList.add(temp);
                }
            }


        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error processing JSON results", e);
            AEDinfo temp = new AEDinfo().setError();
            resultList.add(temp);
        }

        return resultList;

    }
}
