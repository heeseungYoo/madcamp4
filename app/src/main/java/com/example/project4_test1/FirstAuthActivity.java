package com.example.project4_test1;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project4_test1.HomeFragment.permission;

public class FirstAuthActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        permission permission_check = new permission();
        permission_check.checkPermissions(this);

        if (SaveSharedPreference.getUserID(FirstAuthActivity.this).length() == 0) {
            intent = new Intent(FirstAuthActivity.this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        } else {
            intent = new Intent(FirstAuthActivity.this, MainActivity.class);
            intent.putExtra("userID", SaveSharedPreference.getUserID(this));
            startActivity(intent);
            this.finish();
        }
    }

}
