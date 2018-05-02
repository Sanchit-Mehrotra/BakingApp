package com.kumarutsavanand.bakingapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kumarutsavanand.bakingapp.pojos.Ingredient;
import com.kumarutsavanand.bakingapp.pojos.Recipe;
import com.kumarutsavanand.bakingapp.widget.UpdateBakingService;

import java.util.ArrayList;

public class RecipeStepFragment extends Fragment {



    private Recipe mRecipe;
    private ArrayList<String> recipeIngredients = new ArrayList<>();


    public RecipeStepFragment() {
        // Empty Constructor
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        rootView.setBackgroundColor(Color.WHITE);
        RecyclerView recipeStepRecyclerView = rootView.findViewById(R.id.recipe_steps_recycler_view);
        RecyclerView recipeIngredientRecyclerView = rootView.findViewById(R.id.recipe_ingridient_recycler_view);

        mRecipe = getArguments().getParcelable(MainActivity.CLICKED_RECIPE_BUNDLE);


        RecipeIngredientAdapter recipeIngredientAdapter = new RecipeIngredientAdapter(mRecipe.getIngredients());
        LinearLayoutManager recipeIngredientLinearLayoutManager = new LinearLayoutManager(getContext());
        recipeIngredientRecyclerView.setLayoutManager(recipeIngredientLinearLayoutManager);
        recipeIngredientRecyclerView.setAdapter(recipeIngredientAdapter);


        RecipeStepAdapter recipeStepAdapter = new RecipeStepAdapter(mRecipe.getSteps(), (RecipeStepAdapter.OnStepClickListener) getActivity());
        LinearLayoutManager recipeStepLinearLayoutManager = new LinearLayoutManager(getContext());
        recipeStepRecyclerView.setLayoutManager(recipeStepLinearLayoutManager);
        recipeStepRecyclerView.setAdapter(recipeStepAdapter);

        for(Ingredient ingredient : mRecipe.getIngredients()) {
            recipeIngredients.add("\u2022 "+ ingredient.getIngredient()+"\n");
            recipeIngredients.add("\t\t\t Quantity: "+ingredient.getQuantity().toString()+"\n");
            recipeIngredients.add("\t\t\t Measure: "+ingredient.getMeasure()+"\n\n");
        }

        UpdateBakingService.startBakingService(getContext(), recipeIngredients);

        return rootView;
    }



}
