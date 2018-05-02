package com.kumarutsavanand.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kumarutsavanand.bakingapp.pojos.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.RecipeStepViewHolder> {

    private List<Step> mSteps;
    OnStepClickListener mOnStepClickListener;

    //
    public interface OnStepClickListener {
        void onStepClick(List<Step> step, int clickedPosition);
    }

    public RecipeStepAdapter(List<Step> steps, OnStepClickListener onStepClickListener) {
        this.mSteps = steps;
        this.mOnStepClickListener = onStepClickListener;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecipeStepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recipe_step_element_view, parent, false);
        RecipeStepViewHolder recipeStepViewHolder = new RecipeStepViewHolder(view);
        return recipeStepViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepViewHolder holder, int position) {
        holder.recipeStepTextView.setText(mSteps.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    public class RecipeStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipe_step_text_view)
        TextView recipeStepTextView;


        public RecipeStepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        //
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnStepClickListener.onStepClick(mSteps, clickedPosition);

        }


    }
}

