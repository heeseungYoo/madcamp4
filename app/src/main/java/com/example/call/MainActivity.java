package com.example.call;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    ImageButton button_call;
    Button button_findAED;
    LinearLayout map_container;

    Double latitude;
    Double longitude;

    SupportMapFragment mapFragment;
    GoogleMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //전화 걸기 PERMISSION 요청
        permission permission_check = new permission();
        permission_check.checkPermissions(MainActivity.this);


        //View 전달
        button_call = findViewById(R.id.button_call);
        button_findAED = findViewById(R.id.button_findAED);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map_container = findViewById(R.id.map_container);

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
                        Toast.makeText(getApplicationContext(), "현위치를 받아오는 중입니다. 한번 더 눌러주세요!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //현위치 넘겨주고 그 근처 AED 주소, 전화번호, 위치 담긴 AEDinfo Arraylist 받아옴
                    ArrayList<AEDinfo> result = task.execute(new LatLng(latitude, longitude)).get();
                    Toast.makeText(getApplicationContext(), "AED를 찾았습니다!", Toast.LENGTH_LONG).show();

                    for (int i = 0; i < result.size(); i++){

                        // 결과를 받아는 왔는데 내용이 비어있을 경우 에러처리
                        if (result.get(i).error.equals("error")) {
                            Toast.makeText(getApplicationContext(), "검색 결과가 없어요 ㅜㅜ", Toast.LENGTH_SHORT).show();
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
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.setMyLocationEnabled(true);

            }
        });
        try {
            MapsInitializer.initialize(this);
        } catch(Exception e) {
            e.printStackTrace();
        }
        requestMyLocation(14);
    }


    // 내 정보를 보낼지 말지 물어보는 Dialog 생성
    public void ask_myinfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.ask_info, null, false);
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
                dialog.dismiss();
                call_119();
            }
        });

        //남이 다쳐서 내가 신고하는 경우
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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
                LinearLayout info = new LinearLayout(getApplicationContext());
                info.setOrientation(LinearLayout.VERTICAL);
                TextView title = new TextView(getApplicationContext());
                title.setTextColor(Color.BLACK);
                Typeface face = ResourcesCompat.getFont(getApplicationContext(), R.font.ridibatang);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());
                title.setTypeface(face);
                TextView snippet = new TextView(getApplicationContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());
                snippet.setTypeface(face);
                info.addView(title);
                info.addView(snippet);
                return info;
            }
        });
    }


}
