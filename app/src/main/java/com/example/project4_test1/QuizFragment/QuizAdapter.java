package com.example.project4_test1.QuizFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project4_test1.R;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {

    public String[] mData ;
    private OnItemClickListener mListener = null;

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }



    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.fragment_quiz_checkbox, parent, false) ;
        QuizAdapter.ViewHolder vh = new QuizAdapter.ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String item = mData[position];
        holder.quiz_checkbox.setText(item);
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckedTextView quiz_checkbox;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            quiz_checkbox = itemView.findViewById(R.id.checkbox);
            quiz_checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckedTextView view = (CheckedTextView) v;
                    view.toggle();

                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        if (mListener != null){
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });
        }


    }

    QuizAdapter(String[] list) {mData = list;}

}
