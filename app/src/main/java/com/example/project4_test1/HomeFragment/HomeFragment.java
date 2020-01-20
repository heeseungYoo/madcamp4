package com.example.project4_test1.HomeFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.project4_test1.R;
import com.example.project4_test1.RetrofitService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private View v;
    private ImageButton button_call;
    private Button button_findAED;
    private LinearLayout map_container;

    private Double latitude;
    private Double longitude;

    private MapView mapView;
    private GoogleMap map;

    private Retrofit mRetrofit;
    private RetrofitService mRetrofitAPI;

    private String userName;
    private String userBirth;
    private String userAllergy;
    private String userBloodtype;
    private String userHeight;
    private String userWeight;
    private String userEmerContact;
    private String userDisease;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);

        Intent intent = getActivity().getIntent();
        final String userID = intent.getStringExtra("userID");

        setPersonRetrofitInit();

        Call<JsonArray> personData = mRetrofitAPI.getPersonInfo(userID);

        Callback<JsonArray> mRetrofitCallback = new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray personJA = response.body();
                userName = personJA.get(0).getAsJsonObject().get("name").getAsString();
                userBirth = personJA.get(0).getAsJsonObject().get("userBirth").getAsString();
                userAllergy = personJA.get(0).getAsJsonObject().get("userAllergy").getAsString();
                userBloodtype = personJA.get(0).getAsJsonObject().get("userBloodtype").getAsString();
                userHeight = personJA.get(0).getAsJsonObject().get("userHeight").getAsString();
                userWeight = personJA.get(0).getAsJsonObject().get("userWeight").getAsString();
                userEmerContact = personJA.get(0).getAsJsonObject().get("userEmerContact").getAsString();
                userDisease = personJA.get(0).getAsJsonObject().get("userDisease").getAsString();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                t.printStackTrace();
                Log.e("Err", t.getMessage());
            }
        };

        personData.enqueue(mRetrofitCallback);

        //전화 걸기 PERMISSION 요청---
        permission permission_check = new permission();
        permission_check.checkPermissions(getContext());


        //View 전달
        button_call = v.findViewById(R.id.button_call);
        button_findAED = v.findViewById(R.id.button_findAED);
        //mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
        mapView =  v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        map_container = v.findViewById(R.id.map_container);

        //신고 버튼 리스너 등록
        button_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Dialog 띄우기
                ask_myinfo();
            }
        });

        //AED 찾기 버튼 리스너 등록
        button_findAED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //지도 보이게 설정
                map_container.setVisibility(View.VISIBLE);

                //AED 공공데이터 요청
                sendRequestAED task = new sendRequestAED();
                try {

                    //너무 빨리 버튼눌러서 아직 현위치 못받은 경우 에러처리
                    if (latitude == null) {
                        Toast.makeText(getContext(), "현위치를 받아오는 중입니다. 한번 더 눌러주세요!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //현위치 넘겨주고 그 근처 AED 주소, 전화번호, 위치 담긴 AEDinfo Arraylist 받아옴
                    ArrayList<AEDinfo> result = task.execute(new LatLng(latitude, longitude)).get();
                    Toast.makeText(getContext(), "AED를 찾았습니다!", Toast.LENGTH_LONG).show();

                    for (int i = 0; i < result.size(); i++){

                        // 결과를 받아는 왔는데 내용이 비어있을 경우 에러처리
                        if (result.get(i).error.equals("error")) {
                            Toast.makeText(getContext(), "검색 결과가 없어요 ㅜㅜ", Toast.LENGTH_SHORT).show();
                            break;
                        }

                        // AED 해당위치에 마커추가
                        map.addMarker(new MarkerOptions()
                                .position(new LatLng(result.get(i).lat, result.get(i).lng))
                                .title(result.get(i).address)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                                .snippet("연락처 : "+ result.get(i).tel));
                    }

                    //예쁜 스니펫 만들어줌 (폰트, 줄바꿈 등등)
                    pretty_snippet();

                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } catch (ExecutionException ex) {
                    ex.printStackTrace();
                }
            }
        });


        //구글지도 Initialize 및 현위치 찍기
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.setMyLocationEnabled(true);

            }
        });
        try {
            MapsInitializer.initialize(getActivity());
        } catch(Exception e) {
            e.printStackTrace();
        }
        requestMyLocation(14);

        return v;
    }

    // 내 정보를 보낼지 말지 물어보는 Dialog 생성
    public void ask_myinfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home_ask_info, null, false);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        final Button btn_yes = view.findViewById(R.id.button_sendinfo_yes);
        final Button btn_no = view.findViewById(R.id.button_sendinfo_no);

        //내가 다친 경우
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //현위치랑 내정보를 보내고 전화연결
                //=====할것 : 서버에 내정보 보내기 ===============
                JSONObject myInfo = new JSONObject();
                try {
                    myInfo.put("userName", userName);
                    myInfo.put("userBirth", userBirth);
                    myInfo.put("userAllergy", userAllergy);
                    myInfo.put("userDisease", userDisease);
                    myInfo.put("userBloodtype", userBloodtype);
                    myInfo.put("userHeight", userHeight);
                    myInfo.put("userWeight", userWeight);
                    myInfo.put("userEmerContact", userEmerContact);
                    myInfo.put("myLatitude", latitude);
                    myInfo.put("myLongitude", longitude);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String json = myInfo.toString();
                HomeSendInfoTo119 task = new HomeSendInfoTo119();
                try {
                    boolean success = task.execute(json).get();
                    Toast.makeText(getContext(), "전송 결과: "+success, Toast.LENGTH_SHORT).show();
                } catch (InterruptedException e) {
                e.printStackTrace();
               } catch (ExecutionException e) {
               e.printStackTrace();
               }

                dialog.dismiss();
                call_119();
            }
        });

        //남이 다쳐서 내가 신고하는 경우
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject otherInfo = new JSONObject();
                try {
                    otherInfo.put("applicant", userName);
                    otherInfo.put("myLatitude", latitude);
                    otherInfo.put("myLongitude", longitude);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String json = otherInfo.toString();
                HomeSendInfoTo119 task = new HomeSendInfoTo119();

                try {
                    boolean success = task.execute(json).get();
                    Toast.makeText(getContext(), "전송 결과: "+success, Toast.LENGTH_SHORT).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                //현위치랑 내정보를 신고자로써 보내고 전화연결
                //========할것 : 서버에 신고정보 보내기 ================
                dialog.dismiss();
                call_119();
            }
        });

        dialog.show();
    }

    //119에 바로 전화연결 시켜줌
    public void call_119() {
        startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + 119)));
    }

    //폰 GPS 서비스, 네트워크 서비스로 내 위치(위도, 경도)받아오기 업데이트 주기는 10초
    private void requestMyLocation(final int zoom) {
        LocationManager manager =
                (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        try {
            long minTime = 10000;
            float minDistance = 0;
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            showCurrentLocation(location, zoom);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) { }

                        @Override
                        public void onProviderEnabled(String provider) {
                        }

                        @Override
                        public void onProviderDisabled(String provider) { }
                    });

            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            showCurrentLocation(location, zoom);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) { }

                        @Override
                        public void onProviderEnabled(String provider) { }

                        @Override
                        public void onProviderDisabled(String provider) { }
                    });

        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    //현위치 받아와서 전역변수 저장하고 지도 확대해줌
    private void showCurrentLocation(Location location, int zoom) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        System.out.println("위도:"+String.format("%.2f", latitude));

        LatLng curPoint = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(curPoint, zoom));
    }

    // 예쁜 스니펫 만들어줌
    private void pretty_snippet(){
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                LinearLayout info = new LinearLayout(getContext());
                info.setOrientation(LinearLayout.VERTICAL);
                TextView title = new TextView(getContext());
                title.setTextColor(Color.BLACK);
                Typeface face = ResourcesCompat.getFont(getContext(), R.font.ridibatang);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());
                title.setTypeface(face);
                TextView snippet = new TextView(getContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());
                snippet.setTypeface(face);
                info.addView(title);
                info.addView(snippet);
                return info;
            }
        });
    }

    private void setPersonRetrofitInit() {
        String baseUrl = "http://192.249.19.251:980/";
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRetrofitAPI = mRetrofit.create(RetrofitService.class);

    }
}
