package com.example.ravi.Quizz.Adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.ravi.Quizz.Activities.Interests;
import com.example.ravi.Quizz.Activities.Places;
import com.example.ravi.Quizz.Activities.Restaurants;
import com.example.ravi.Quizz.Answer;
import com.example.ravi.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemAdapter  extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    public int nSelected;

    private List<Answer> mDataset;

    public ItemAdapter(List<Answer> mDataset) {
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ItemViewHolder extends ViewHolder {

        private CircleImageView image;
        private TextView text;

        public ItemViewHolder(@NonNull View v) {
            super(v);

            image = v.findViewById(R.id.image);
            text = v.findViewById(R.id.text);

            image.setOnClickListener(new CircleImageView.OnClickListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onClick(View v) {
                    //TODO : Select item.
                    Answer selectedAnswer = mDataset.get(getAdapterPosition());
                    if(selectedAnswer.select()) {
                        image.setBorderWidth(5);
                        nSelected++;
                    } else {
                        image.setBorderWidth(0);
                        nSelected--;
                    }
                    if (nSelected >= 3) {
                        if(v.getContext() instanceof Restaurants) ((Restaurants) v.getContext()).next_btn.setVisibility(View.VISIBLE);
                        if(v.getContext() instanceof Interests) ((Interests) v.getContext()).next_btn.setVisibility(View.VISIBLE);
                        if(v.getContext() instanceof Places) ((Places) v.getContext()).next_btn.setVisibility(View.VISIBLE);
                    } else {
                        if(v.getContext() instanceof Restaurants) ((Restaurants) v.getContext()).next_btn.setVisibility(View.GONE);
                        if(v.getContext() instanceof Interests) ((Interests) v.getContext()).next_btn.setVisibility(View.GONE);
                        if(v.getContext() instanceof Places) ((Places) v.getContext()).next_btn.setVisibility(View.GONE);
                    }
                }
            });
        }

        public void bind(Answer answer) {
            text.setText(answer.getTitle());
            image.setImageDrawable(answer.getBackground());
            Log.i("TAG", "bind: " + answer.getTitle());
        }
    }
}
