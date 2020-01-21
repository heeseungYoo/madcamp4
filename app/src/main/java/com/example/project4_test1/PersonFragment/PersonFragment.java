package com.example.project4_test1.PersonFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.project4_test1.LoginActivity;
import com.example.project4_test1.R;
import com.example.project4_test1.RetrofitService;
import com.example.project4_test1.SaveSharedPreference;
import com.google.gson.JsonArray;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PersonFragment extends Fragment {

    private View v;
    private static final int DIALOG_REQUEST_CODE = 1234;
    private Retrofit mRetrofit;
    private RetrofitService mRetrofitAPI;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_person, container, false);

        Intent intent = getActivity().getIntent();
        final String userID =intent.getStringExtra("userID");

        final TextView textName = v.findViewById(R.id.textName);
        final TextView textBirth = v.findViewById(R.id.textBirth);
        final TextView textAllergy = v.findViewById(R.id.textAllergy);
        final TextView textDisease = v.findViewById(R.id.textDisease);
        final TextView textBloodtype = v.findViewById(R.id.textBloodtype);
        final TextView textHeight = v.findViewById(R.id.textHeight);
        final TextView textWeight = v.findViewById(R.id.textWeight);
        final TextView textEmerContact = v.findViewById(R.id.textEmerContact);

        setPersonRetrofitInit();

        Call<JsonArray> personData = mRetrofitAPI.getPersonInfo(userID);

        Callback<JsonArray> mRetrofitCallback = new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray personJA = response.body();
                textName.setText(personJA.get(0).getAsJsonObject().get("name").getAsString());
                textBirth.setText(personJA.get(0).getAsJsonObject().get("userBirth").getAsString());
                textAllergy.setText(personJA.get(0).getAsJsonObject().get("userAllergy").getAsString());
                textDisease.setText(personJA.get(0).getAsJsonObject().get("userDisease").getAsString());
                textBloodtype.setText(personJA.get(0).getAsJsonObject().get("userBloodtype").getAsString());
                textHeight.setText(personJA.get(0).getAsJsonObject().get("userHeight").getAsString());
                textWeight.setText(personJA.get(0).getAsJsonObject().get("userWeight").getAsString());
                textEmerContact.setText(personJA.get(0).getAsJsonObject().get("userEmerContact").getAsString());
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                t.printStackTrace();
                Log.e("Err", t.getMessage());
            }
        };

        personData.enqueue(mRetrofitCallback);

        Button editBtn = v.findViewById(R.id.editBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("userID", userID);
                args.putString("userBirth", textBirth.getText().toString());
                args.putString("name", textName.getText().toString());
                args.putString("userAllergy", textAllergy.getText().toString());
                args.putString("userDisease", textDisease.getText().toString());
                args.putString("userBloodtype", textBloodtype.getText().toString());
                args.putString("userHeight", textHeight.getText().toString());
                args.putString("userWeight", textWeight.getText().toString());
                args.putString("userEmerContact", textEmerContact.getText().toString());

                PersonMedDialog dialog = new PersonMedDialog();
                dialog.setArguments(args);
                dialog.setTargetFragment(PersonFragment.this, DIALOG_REQUEST_CODE);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, PersonMedDialog.TAG);
            }
        });



        TextView logoutBtn = v.findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSharedPreference.clearUserName(getContext());
                Intent intent1 = new Intent(getContext(), LoginActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
            }
        });

        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DIALOG_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                String Birthday = data.getExtras().getString("textBirth");
                TextView userBirthday = v.findViewById(R.id.textBirth);
                userBirthday.setText(Birthday);

                String Allergy = data.getExtras().getString("textAllergy");
                TextView userAllergy = v.findViewById(R.id.textAllergy);
                userAllergy.setText(Allergy);

                String Disease = data.getExtras().getString("textDisease");
                TextView userDisease = v.findViewById(R.id.textDisease);
                userDisease.setText(Disease);

                String Bloodtype = data.getExtras().getString("textBloodtype");
                TextView userBloodtype = v.findViewById(R.id.textBloodtype);
                userBloodtype.setText(Bloodtype);

                String Height = data.getExtras().getString("textHeight");
                TextView userHeight = v.findViewById(R.id.textHeight);
                userHeight.setText(Height);

                String Weight = data.getExtras().getString("textWeight");
                TextView userWeight = v.findViewById(R.id.textWeight);
                userWeight.setText(Weight);

                String EmerContact = data.getExtras().getString("textEmerContact");
                TextView userEmerContact = v.findViewById(R.id.textEmerContact);
                userEmerContact.setText(EmerContact);
            }

        }

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
