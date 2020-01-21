package com.example.project4_test1.QuizFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project4_test1.HomeFragment.HomeFragment;
import com.example.project4_test1.MainActivity;
import com.example.project4_test1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class QuizFragment extends Fragment {

    private FragmentManager fragmentManager = getFragmentManager();

    private RecyclerView recyclerView;
    private TextView textView_question_number;
    private TextView textView_question;
    private Button button_next_question;
    private Button button_previous_question;
    private Button button_start_quiz;

    private QuizAdapter adapter;
    private QuizAdapter.OnItemClickListener mListener;
    private String[] quiz_questions;
    private int[] quiz_answers;
    private int quiz_score;
    private int current_number;

    private View v;

    public static QuizFragment newInstance() {
        return new QuizFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_quiz, container, false);

        // 화면 구성 View 전달
        textView_question_number = v.findViewById(R.id.question_number);
        textView_question = v.findViewById(R.id.question);
        button_next_question = v.findViewById(R.id.button_next_question);
        button_previous_question = v.findViewById(R.id.button_previous_question);
        button_start_quiz = v.findViewById(R.id.button_start_quiz);

        //quiz에 필요한 값 전달
        quiz_questions = getResources().getStringArray(R.array.quiz_question);
        quiz_answers = getResources().getIntArray(R.array.answer);
        quiz_score = 0;
        current_number = 1;

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        recyclerView = v.findViewById(R.id.recyclerview_quizitem) ;
        recyclerView.setLayoutManager(new LinearLayoutManager
                (getContext(), LinearLayoutManager.VERTICAL, false));

        // 퀴즈 시작 버튼 리스너 등록
        button_start_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.VISIBLE);
                textView_question_number.setVisibility(View.VISIBLE);
                textView_question.setVisibility(View.VISIBLE);
                button_next_question.setVisibility(View.VISIBLE);
                button_previous_question.setVisibility(View.VISIBLE);

                button_start_quiz.setVisibility(View.INVISIBLE);
                v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.quiz_background_simple));

            }
        });

        // 다음 문제 버튼 리스너 등록
        button_next_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { changeQuestion(true); }
        });

        // 이전 문제 버튼 리스너 등록
        button_previous_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { changeQuestion(false); }
        });

        //quiz 정답/오답처리 리스너
        mListener = new QuizAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                //정답이랑 비교해서 색 바꿔주기 - 맞추면 green, 틀리면 red + 정답green으로 표시
                String number_string = textView_question_number.getText().subSequence(1,2).toString();
                int number = Integer.parseInt(number_string);
                int answer = quiz_answers[number-1];

                if ((answer-1)==pos){
                    v.setBackgroundColor(getResources().getColor(R.color.green_right));
                    quiz_score+=10;
                }

                else{
                    v.setBackgroundColor(getResources().getColor(R.color.red_wrong));
                    View view_answer = recyclerView.getLayoutManager().findViewByPosition(answer-1);
                    view_answer.setBackgroundColor(getResources().getColor(R.color.green_right));
                }
            }
        };

        //quiz option 띄울 어댑터 구현
        adapter = new QuizAdapter(getResources().getStringArray(R.array.Q1));
        adapter.setOnItemClickListener(mListener);
        recyclerView.setAdapter(adapter) ;

        return v;
    }

    // up이 true 이면 문제 번호 증가, false 이면 감소
    public void changeQuestion(boolean up){

        if (up)
            current_number++;
        else
            current_number--;

        //마지막 문제라면 점수페이지로 넘어감
        if (current_number==11){
            show_score();
            return;
        }

        //1번 문제에서 뒤로가기 방지
        if (current_number>=1){
            textView_question_number.setText("Q"+ current_number+".");
            textView_question.setText(quiz_questions[current_number-1]);

            int id = getContext().getResources().getIdentifier("Q"+current_number,
                    "array", getContext().getPackageName());

            adapter = new QuizAdapter(getResources().getStringArray(id));
            adapter.setOnItemClickListener(mListener);
            recyclerView.setAdapter(adapter) ;
        }

        System.out.println(current_number);

    }

    //퀴즈 끝나고 점수화면 Alert Dialog로 띄어줌, 점수에 따라 다른 text 세팅
    public void show_score(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_quiz_score_page, null, false);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        final TextView score = view.findViewById(R.id.score);
        final TextView comment = view.findViewById(R.id.comment);
        final ImageView star1 = view.findViewById(R.id.star1);
        final ImageView star2 = view.findViewById(R.id.star2);
        final ImageView star3 = view.findViewById(R.id.star3);
        final ImageView star4 = view.findViewById(R.id.star4);
        final ImageView star5 = view.findViewById(R.id.star5);
        final Button retryBtn = view.findViewById(R.id.retryBtn);
        final Button homeBtn = view.findViewById(R.id.homeBtn);

        score.setText(quiz_score+"점");

        switch ((int)quiz_score/20){
            case 0:
                star1.setImageResource(R.drawable.empty_star);
                star2.setImageResource(R.drawable.empty_star);
                star3.setImageResource(R.drawable.empty_star);
                star4.setImageResource(R.drawable.empty_star);
                star5.setImageResource(R.drawable.empty_star);

                comment.setText("... 할말하않");
                break;
            case 1:
                star2.setImageResource(R.drawable.empty_star);
                star3.setImageResource(R.drawable.empty_star);
                star4.setImageResource(R.drawable.empty_star);
                star5.setImageResource(R.drawable.empty_star);

                comment.setText("다시 공부해 볼까요^^?");
                break;
            case 2:
                star3.setImageResource(R.drawable.empty_star);
                star4.setImageResource(R.drawable.empty_star);
                star5.setImageResource(R.drawable.empty_star);

                comment.setText("공부가 조금 더 필요해요!");
                break;
            case 3:
                star4.setImageResource(R.drawable.empty_star);
                star5.setImageResource(R.drawable.empty_star);

                comment.setText("나쁘지 않네요!");
                break;
            case 4:
                star5.setImageResource(R.drawable.empty_star);

                comment.setText("훌륭하군요!");
                break;
            default:
                comment.setText("당신은 완벽한 힐러!");
                break;
        }

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragment(QuizFragment.newInstance());
                dialog.dismiss();
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragment(HomeFragment.newInstance());
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
