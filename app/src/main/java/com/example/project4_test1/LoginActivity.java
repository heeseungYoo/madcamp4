package com.example.project4_test1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private Retrofit mRetrofit;
    private RetrofitService mRetrofitAPI;

    private AlertDialog dialog;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        final Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //로그인 시작
                getLogin();

            }
        });

    }

    // 화면 누르면 키보드 내려가게 하기
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    // retrofit login init
    private void setLoginRetrofitInit() {
        String baseUrl = "http://192.249.19.251:980/";
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRetrofitAPI = mRetrofit.create(RetrofitService.class);

    }

    // retrofit login
    public void getLogin() {
        setLoginRetrofitInit();
        EditText userIDInput = findViewById(R.id.idText);
        EditText userPasswordInput = findViewById(R.id.passwordText);
        final String userID = userIDInput.getText().toString();
        final String userPassword = userPasswordInput.getText().toString();

        Call<JsonArray> loginData = mRetrofitAPI.login(userID);

        Callback<JsonArray> mRetrofitCallback = new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                Log.d("Retrofit Success", response.toString());
                JsonArray loginJA = response.body();

                if (loginJA.size() == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    dialog = builder.setMessage("계정을 다시 확인하세요")
                            .setNegativeButton("다시시도", noButtonClickListener)
                            .create();
                    dialog.show();
                }

                else {
                    String real_userID = loginJA.get(0).getAsJsonObject().get("userID").getAsString();
                    String real_userPassword = loginJA.get(0).getAsJsonObject().get("userPassword").getAsString();
                    Log.d("Response body", loginJA.get(0).getAsJsonObject().get("userID").getAsString());

                    if(userID.equals(real_userID) && userPassword.equals(real_userPassword)) {
                        SaveSharedPreference.setUserID(LoginActivity.this, userID);
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        dialog = builder.setMessage("로그인에 성공했습니다")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("userID", userID);
                                        LoginActivity.this.startActivity(intent);
                                        finish();
                                    }
                                })
                                .create();
                        dialog.show();
                    }

                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        dialog = builder.setMessage("계정을 다시 확인하세요")
                                .setNegativeButton("다시시도", noButtonClickListener)
                                .create();
                        dialog.show();
                    }
                }



            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                t.printStackTrace();
                Log.e("Err", t.getMessage());

            }
        };
        loginData.enqueue(mRetrofitCallback);
    }

    private DialogInterface.OnClickListener noButtonClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if(dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
