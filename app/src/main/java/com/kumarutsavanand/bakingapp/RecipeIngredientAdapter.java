package com.kumarutsavanand.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kumarutsavanand.bakingapp.pojos.Ingredient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientAdapter.RecipeIngredientViewHolder> {

    private ArrayList<Ingredient> ingredients;


    public RecipeIngredientAdapter(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
    @NonNull
    @Override
    public RecipeIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recipe_ingredient_element_view, parent, false);
        return new RecipeIngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientViewHolder holder, int position) {
        holder.recipeIngredientTextView.setText("\u2022 " + ingredients.get(position).getIngredient());
        holder.recipeQuantityTextView.setText("Quantity: " + ingredients.get(position).getQuantity().toString());
        holder.recipeMeasureTextView.setText("Meausure: " + ingredients.get(position).getMeasure());
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class RecipeIngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipe_ingredient) TextView recipeIngredientTextView;
        @BindView(R.id.recipe_quantity) TextView recipeQuantityTextView;
        @BindView(R.id.recipe_measure) TextView recipeMeasureTextView;

        public RecipeIngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
