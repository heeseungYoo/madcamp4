package com.example.project4_test1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.project4_test1.HomeFragment.HomeFragment;
import com.example.project4_test1.InfoFragment.InfoFragment;
import com.example.project4_test1.PersonFragment.PersonFragment;
import com.example.project4_test1.QuizFragment.QuizFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeFragment homeFragment = new HomeFragment();
    private InfoFragment infoFragment = new InfoFragment();
    private QuizFragment quizFragment = new QuizFragment();
    private PersonFragment personFragment = new PersonFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        Toast.makeText(this, userID + " 로그인 하셨습니다.", Toast.LENGTH_SHORT).show();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, homeFragment).commitAllowingStateLoss();

    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId())
            {
                case R.id.homeItem:
                    transaction.replace(R.id.frameLayout, homeFragment).commitAllowingStateLoss();
                    break;
                case R.id.infoItem:
                    transaction.replace(R.id.frameLayout, infoFragment).commitAllowingStateLoss();
                    break;
                case R.id.quizItem:
                    transaction.replace(R.id.frameLayout, quizFragment).commitAllowingStateLoss();
                    break;
                case R.id.personItem:
                    transaction.replace(R.id.frameLayout, personFragment).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }

    public void replaceFragment(Fragment fragment) {
        System.out.println("---------------------fragment:  " + fragment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        if (fragment.getClass().equals(QuizFragment.class)) {
            bottomNavigationView.setSelectedItemId(R.id.quizItem);
        }
        if (fragment.getClass().equals(HomeFragment.class)) {
            bottomNavigationView.setSelectedItemId(R.id.homeItem);
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림");
        builder.setMessage("앱을 종료하시겠습니까?");
        builder.setNegativeButton("취소", null);
        builder.setPositiveButton("종료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        builder.show();
    }
}
