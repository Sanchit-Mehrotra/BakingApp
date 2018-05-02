package com.kumarutsavanand.bakingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.kumarutsavanand.bakingapp.IdlingResources.SimpleIdlingResource;
import com.kumarutsavanand.bakingapp.pojos.Recipe;


public class MainActivity extends AppCompatActivity implements RecipeListAdapter.ItemViewClickListener {

    public static final String CLICKED_RECIPE_BUNDLE = "CLICKED_RECIPE_BUNDLE";


    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getSupportActionBar().setHomeButtonEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        getSupportActionBar().setTitle("Nutella Pie");

        getIdlingResource();
    }

    @Override
    public void onItemClickListener(Recipe clickedRecipe) {

        Bundle selectedRecipeBundle = new Bundle();
        selectedRecipeBundle.putParcelable(CLICKED_RECIPE_BUNDLE, clickedRecipe);
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtras(selectedRecipeBundle);
        startActivity(intent);

    }
}
