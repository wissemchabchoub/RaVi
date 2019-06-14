package com.example.ravi.Quizz.Adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.ravi.Quizz.Activities.NumberOfTravelers;
import com.example.ravi.Quizz.Activities.TypeOfTrip;
import com.example.ravi.Quizz.Answer;
import com.example.ravi.R;

import java.util.List;

public class Item2Adapter  extends RecyclerView.Adapter<Item2Adapter.Item2ViewHolder> {

    public Answer selectedAnswer;

    private List<Answer> mDataset;

    public Item2Adapter(List<Answer> mDataset) {
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public Item2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item2, parent, false);
        return new Item2ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Item2ViewHolder holder, int position) {
        holder.bind(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class Item2ViewHolder extends ViewHolder {

        private RadioButton radioButton;

        public Item2ViewHolder(@NonNull View v) {
            super(v);

            radioButton = v.findViewById(R.id.radio_button);

            radioButton.setOnClickListener(new RadioButton.OnClickListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onClick(View v) {
                    if(selectedAnswer != null) selectedAnswer.select();
                    selectedAnswer = mDataset.get(getAdapterPosition());
                    selectedAnswer.select();
                    notifyDataSetChanged();
                    if (selectedAnswer != null) {
                        if(v.getContext() instanceof NumberOfTravelers) ((NumberOfTravelers) v.getContext()).next_btn.setVisibility(View.VISIBLE);
                        if(v.getContext() instanceof TypeOfTrip) ((TypeOfTrip) v.getContext()).next_btn.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        public void bind(Answer answer) {
            radioButton.setText(answer.getTitle());
            radioButton.setChecked(answer.isSelected);
            Log.i("TAG", "binding: " + answer.getTitle());
        }
    }
}
