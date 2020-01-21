package com.example.project4_test1.InfoFragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.project4_test1.R;

import java.util.ArrayList;

public class InfoSub extends AppCompatActivity {
    Button returnBtn;
    Toast toast;

    //viewpager
    private ArrayList<Integer> imageList;
    private static final int DP = 24;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_info_sub);
//        returnBtn = (Button) findViewById(R.id.second_returnBtn);

        //viewpager
        this.initializeData();
        ViewPager viewPager = findViewById(R.id.info_viewPager);
        viewPager.setClipToPadding(false);
        float density = getResources().getDisplayMetrics().density;
        int margin = (int) (DP * density);
//        viewPager.setPadding(margin, 0, margin, 0);
//        viewPager.setPageMargin(margin/2);

        String[] test = getResources().getStringArray(R.array.cpr); //심폐소생술
        String[] test1 = getResources().getStringArray(R.array.H); //하임리히법(성인)
        String[] hy = getResources().getStringArray(R.array.HY); //하임리히법(영아)
        String[] burn = getResources().getStringArray(R.array.burn);

        viewPager.setAdapter(new InfoViewPagerAdapter(this, imageList, test));


        Intent intent = getIntent();

        int Index = intent.getExtras().getInt("Index");
        switch (Index) {
            case 0:

                break;
            case 1:
                viewPager.setAdapter(new InfoViewPagerAdapter(this, imageList, test1));

                imageList.clear();
                imageList.add(R.drawable.h1);
                imageList.add(R.drawable.h2);
                viewPager.getAdapter().notifyDataSetChanged();
                break;
            case 2:
                viewPager.setAdapter(new InfoViewPagerAdapter(this, imageList, hy));

                imageList.clear();
                imageList.add(R.drawable.hy1);
                imageList.add(R.drawable.hy2);
                imageList.add(R.drawable.hy3);
                imageList.add(R.drawable.hy4);
                viewPager.getAdapter().notifyDataSetChanged();
                break;
            case 3:

                viewPager.setAdapter(new InfoViewPagerAdapter(this, imageList, burn));

                imageList.clear();
                imageList.add(R.drawable.burn1);
                imageList.add(R.drawable.burn2);
                imageList.add(R.drawable.burn3);
                imageList.add(R.drawable.burn4);
                imageList.add(R.drawable.burn5);
                viewPager.getAdapter().notifyDataSetChanged();
                break;
            case 4:

                break;
            case 5:

                break;
            case 6:

                break;
            case 7:

                break;
            case 8:

                break;
            case 9:

                break;
        }

//        returnBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }

    public void initializeData()
    {
        imageList = new ArrayList();

        imageList.add(R.drawable.cpr1);
        imageList.add(R.drawable.cpr2);
        imageList.add(R.drawable.cpr3);
        imageList.add(R.drawable.cpr4);
        imageList.add(R.drawable.cpr5);
        imageList.add(R.drawable.cpr6);
        imageList.add(R.drawable.cpr7);
        imageList.add(R.drawable.cpr8);
        imageList.add(R.drawable.cpr9);
    }
}
