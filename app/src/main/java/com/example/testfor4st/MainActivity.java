package com.example.testfor4st;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1001;
    ListView listView;
    ArrayAdapter<String> ListAdapter;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.main_listView);
        textView = (TextView) findViewById(R.id.main_textView);
        String[] first_aid = getResources().getStringArray(R.array.first_aid);

        List<String> FirstAidList = new ArrayList<String>(Arrays.asList(first_aid));
        ListAdapter = new ArrayAdapter<String>(
                this,
                R.layout.activity_main,
                R.id.main_textView,
                FirstAidList
        );
        listView.setAdapter(ListAdapter);
        listView.setOnItemClickListener(itemClickListener);
    }
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String selectedGame = (String)parent.getAdapter().getItem(position);
            Intent intent = new Intent(getBaseContext(),second.class);
            intent.putExtra("Index", position);
            startActivity(intent);
        }
    };
}