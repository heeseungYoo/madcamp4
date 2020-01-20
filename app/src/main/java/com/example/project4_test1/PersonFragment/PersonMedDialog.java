package com.example.project4_test1.PersonFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.project4_test1.R;
import com.example.project4_test1.RetrofitService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PersonMedDialog extends DialogFragment {

    public static final String TAG = "FullScreenDialog";
    private TextView userAllergy;
    private TextView userBloodtype;
    private TextView userHeight;
    private TextView userWeight;
    private TextView userEmerContact;
    private TextInputEditText userAllergyD;
    private Retrofit mRetrofit;
    private RetrofitService mRetrofitAPI;
    String birthDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    private Calendar birthCalender = Calendar.getInstance();

    private String showDate() {

        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                birthCalender.set(Calendar.YEAR, year);
                birthCalender.set(Calendar.MONTH, month);
                birthCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd";    // 출력형식   2018/11/28
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
                birthDate = sdf.format(birthCalender.getTime());

            }
        }, birthCalender.get(Calendar.YEAR), birthCalender.get(Calendar.MONTH), birthCalender.get(Calendar.DAY_OF_MONTH)).show();

        return birthDate;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_person_medi, null);
        builder.setView(view);

        // PersonFragment에 입력되어 있는 값 받아오기
        Bundle mArgs = getArguments();
        final String userID = mArgs.getString("userID");
        String name = mArgs.getString("name");
        String userBirth = mArgs.getString("userBirth");
        String userAllergy = mArgs.getString("userAllergy");
        String userBloodtype = mArgs.getString("userBloodtype");
        String userHeight = mArgs.getString("userHeight");
        String userWeight = mArgs.getString("userWeight");
        String userEmerContact = mArgs.getString("userEmerContact");

        final TextView textName = view.findViewById(R.id.textName);
        final EditText textBirth = view.findViewById(R.id.textBirth);
        final EditText textAllergyD = view.findViewById(R.id.textAllergyD);
        final EditText textBloodtypeD = view.findViewById(R.id.textBloodtypeD);
        final EditText textHeightD = view.findViewById(R.id.textHeightD);
        final EditText textWeightD = view.findViewById(R.id.textWeightD);
        final EditText textEmerContactD = view.findViewById(R.id.textEmerContactD);



        textBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textBirth.setText(showDate());

            }
        });



        // 입력되어 있는 값 setText & cursor 위치 끝으로
        textName.setText(name);

        textBirth.setText(userBirth);

        textAllergyD.setText(userAllergy);
        textAllergyD.setSelection(textAllergyD.getText().length());

        textBloodtypeD.setText(userBloodtype);
        textBloodtypeD.setSelection(textBloodtypeD.getText().length());

        textHeightD.setText(userHeight);
        textHeightD.setSelection(textHeightD.getText().length());

        textWeightD.setText(userWeight);
        textWeightD.setSelection(textWeightD.getText().length());

        textEmerContactD.setText(userEmerContact);
        textEmerContactD.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        textEmerContactD.setSelection(textEmerContactD.getText().length());

        // PersonFrament에 저장
        final Button submit = view.findViewById(R.id.submitBtn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPersonRetrofitInit();

                String Birthday = textBirth.getText().toString();
                String Allergy = textAllergyD.getText().toString();
                String Bloodtype = textBloodtypeD.getText().toString();
                String Height = textHeightD.getText().toString();
                String Weight = textWeightD.getText().toString();
                String EmerContact = textEmerContactD.getText().toString();

                Call<JsonObject> editData = mRetrofitAPI.setPersonInfo(userID, Birthday,"yyyyyy", Allergy, Bloodtype, Height, Weight, EmerContact);

                System.out.println(editData);

                Callback<JsonObject> mRetrofitCallback = new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.d("Retrofit Success", response.toString());
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        t.printStackTrace();
                        Log.e("Err", t.getMessage());
                    }
                };

                editData.enqueue(mRetrofitCallback);

                Intent data = new Intent();
                data.putExtra("textBirth", Birthday);
                data.putExtra("textAllergy", Allergy);
                data.putExtra("textBloodtype", Bloodtype);
                data.putExtra("textHeight", Height);
                data.putExtra("textWeight", Weight);
                data.putExtra("textEmerContact", EmerContact);

                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
                dismiss();
            }
        });
        //view.findViewById(R.id.submitBtn).setOnClickListener(this);

        // cancel
        final Button cancel = view.findViewById(R.id.cancelBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    private void setPersonRetrofitInit() {
        String baseUrl = "http://192.249.19.251:980/";
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRetrofitAPI = mRetrofit.create(RetrofitService.class);
    }

/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        super.onCreateView(inflater, parent, state);

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_person_medi, parent, false);
        view.findViewById(R.id.submitBtn).setOnClickListener(this);

        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(false);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitBtn:
                EditText userAllergyD = v.findViewById(R.id.textAllergyD);
                String Allergy = userAllergyD.getText().toString();
                Intent data = new Intent();
                data.putExtra("textAllergy", Allergy);

                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);

                dismiss();
                break;

            case R.id.cancelBtn:
                dismiss();
        }
    }
*/

}