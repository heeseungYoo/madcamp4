package com.example.project4_test1.InfoFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project4_test1.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InfoFragment extends Fragment {

    private View v;
    public static final int REQUEST_CODE = 1001;
    ListView listView;
    ArrayAdapter<String> ListAdapter;
    TextView textView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_info, container, false);

        listView = v.findViewById(R.id.main_listView);
        //textView = v.findViewById(R.id.main_textView);
        String[] first_aid = getResources().getStringArray(R.array.first_aid);

        List<String> FirstAidList = new ArrayList<>(Arrays.asList(first_aid));
        ListAdapter = new ArrayAdapter<>(
                v.getContext(),
                android.R.layout.simple_list_item_1,
                FirstAidList
        );
        listView.setAdapter(ListAdapter);
        listView.setOnItemClickListener(itemClickListener);

        return v;
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String selectedGame = (String)parent.getAdapter().getItem(position);
            Intent intent = new Intent(getContext(),InfoSub.class);
            intent.putExtra("Index", position);
            startActivity(intent);
        }
    };

}
