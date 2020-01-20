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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private Retrofit mRetrofit;
    private RetrofitService mRetrofitAPI;
    private AlertDialog dialog;
    private boolean validate = false;
    private EditText idText;
    private Button validateButton;
    private EditText passwordText;
    private EditText emailText;
    private EditText nameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        idText = findViewById(R.id.idText);
        validateButton = findViewById(R.id.validateButton);
        passwordText = findViewById(R.id.passwordText);
        emailText = findViewById(R.id.emailText);
        nameText = findViewById(R.id.nameText);

        final Button registerButton = findViewById(R.id.registerButton);

        validateButton.setOnClickListener(validateBtnListener);
        registerButton.setOnClickListener(registerBtnListener);
    }

    View.OnClickListener validateBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String userID = idText.getText().toString();
            if(validate){
                return;//검증 완료
            }
            //ID 값을 입력하지 않았다면
            if(userID.equals("")){
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                dialog = builder.setMessage("ID is empty")
                        .setPositiveButton("OK", null)
                        .create();
                dialog.show();
            }

            // 검증 시작
            getValidate(userID);

        }
    };

    View.OnClickListener registerBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String userID = idText.getText().toString();
            String userPassword = passwordText.getText().toString();
            String name = nameText.getText().toString();
            String userEmail = emailText.getText().toString();

            //ID 중복체크를 했는지 확인함
            if(!validate){
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                dialog = builder.setMessage("First Check ID plz")
                        .setNegativeButton("OK", null)
                        .create();
                dialog.show();
                return;
            }

            //한칸이라도 빠뜨렸을 경우
            if(userID.equals("")||userPassword.equals("")||name.equals("")||userEmail.equals("")){
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                dialog = builder.setMessage("Empty text exist")
                        .setNegativeButton("OK", null)
                        .create();
                dialog.show();
            }

            // 회원가입 시작
            getRegister(userID, userPassword, name, userEmail);

        }
    };

    // retrofit register init
    private void setRegisterRetrofitInit() {
        String baseUrl = "http://192.249.19.251:980/";
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mRetrofitAPI = mRetrofit.create(RetrofitService.class);

    }

    private void setValidateRetrofitInit() {
        String baseUrl = "http://192.249.19.251:980/";
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRetrofitAPI = mRetrofit.create(RetrofitService.class);

    }

    public void getValidate(String userID) {
        setValidateRetrofitInit();

        Call<JsonArray> validateData = mRetrofitAPI.validate(userID);

        Callback<JsonArray> mRetrofitCallback = new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                Log.d("Retrofit Success", response.toString());
                JsonArray validateJA = response.body();
                Log.d("=====1=====", validateJA.toString());
                String success = validateJA.toString();
                if(success.equals("[]")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("you can use ID")
                            .setPositiveButton("OK", null)
                            .create();
                    dialog.show();
                    idText.setEnabled(false);//아이디값을 바꿀 수 없도록 함
                    validate = true;//검증완료
                    idText.setBackgroundResource(R.drawable.shape_solid_layout);
                    validateButton.setBackgroundResource(R.drawable.shape_solid_layout);
                }
                else{//사용할 수 없는 아이디라면
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("alreay used ID")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                t.printStackTrace();
                Log.e("Err", t.getMessage());
            }
        };
        validateData.enqueue(mRetrofitCallback);
    }

    // retrofit register
    public void getRegister(String userID, String userPassword, String name, String email) {
        setRegisterRetrofitInit();

        Call<JsonPrimitive> registerData = mRetrofitAPI.register(userID, userPassword, name, email);

        Callback<JsonPrimitive> mRetrofitCallback = new Callback<JsonPrimitive>() {
            @Override
            public void onResponse(Call<JsonPrimitive> call, Response<JsonPrimitive> response) {
                Log.d("Retrofit Success", response.toString());
                if(validate){//사용할 수 있는 아이디라면
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("Register Your ID")
                            .setPositiveButton("OK", yesButtonClickListener)
                            .create();
                    dialog.show();

                }else{//사용할 수 없는 아이디라면
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("Register fail")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                }
            }

            @Override
            public void onFailure(Call<JsonPrimitive> call, Throwable t) {
                t.printStackTrace();
                Log.e("Err", t.getMessage());
            }
        };
        registerData.enqueue(mRetrofitCallback);
    }

    private DialogInterface.OnClickListener yesButtonClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            RegisterActivity.this.startActivity(intent);
            finish();//액티비티를 종료시킴(회원등록 창을 닫음)
        }
    };

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

}
