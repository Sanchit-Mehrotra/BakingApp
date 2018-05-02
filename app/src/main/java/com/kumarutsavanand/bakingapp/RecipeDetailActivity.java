package com.kumarutsavanand.bakingapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.kumarutsavanand.bakingapp.pojos.Recipe;
import com.kumarutsavanand.bakingapp.pojos.Step;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeStepAdapter.OnStepClickListener {

    public static String SELECTED_STEP_INDEX = "SELECTED_STEP_INDEX";
    public static String SELECTED_STEP = "SELECTED_STEP";
    public static String SELECTED_RECIPE_NAME = "SELECTED_RECIPE_NAME";
    public static String RECIPE_DETAIL = "RECIPE_DETAIL";
    public static String RECIPE_STEP_VIDEO = "RECIPE_STEP_VIDEO";



    private Recipe recipe;
    String mRecipeName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Bundle selectedRecipeBundle = getIntent().getExtras();

        recipe = selectedRecipeBundle.getParcelable(MainActivity.CLICKED_RECIPE_BUNDLE);
        mRecipeName = recipe.getName();
        getSupportActionBar().setTitle(mRecipeName);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        if(savedInstanceState == null) {

            final RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
            recipeStepFragment.setArguments(selectedRecipeBundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_detail_fragment, recipeStepFragment).addToBackStack(RECIPE_DETAIL)
                    .commit();

            if (findViewById(R.id.recipe_linear_layout) != null && findViewById(R.id.recipe_linear_layout).getTag().equals("sw600dp")) {
                RecipeStepVideoFragment recipeStepVideoFragment = new RecipeStepVideoFragment();
                recipeStepVideoFragment.setArguments(selectedRecipeBundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.video_container, recipeStepVideoFragment).addToBackStack(RECIPE_STEP_VIDEO)
                        .commit();
            }
        }


    }


    @Override
    public void onStepClick(List<Step> steps, int clickedPosition) {
//        Toast.makeText(this, steps.get(clickedPosition).getShortDescription(), Toast.LENGTH_SHORT).show();
        RecipeStepVideoFragment fragment = new RecipeStepVideoFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        getSupportActionBar().setTitle(mRecipeName);

        Bundle stepBundle = new Bundle();
        stepBundle.putString(SELECTED_RECIPE_NAME, mRecipeName);
        stepBundle.putParcelableArrayList(SELECTED_STEP, (ArrayList<Step>) steps);
        stepBundle.putInt(SELECTED_STEP_INDEX, clickedPosition);
        fragment.setArguments(stepBundle);

        if (findViewById(R.id.recipe_linear_layout) != null && findViewById(R.id.recipe_linear_layout).getTag().equals("sw600dp")) {
            fragmentManager.beginTransaction()
                    .replace(R.id.video_container, fragment).addToBackStack(RECIPE_STEP_VIDEO)
                    .commit();
        }
        else {
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_detail_fragment, fragment)
                    .commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (findViewById(R.id.video_container) == null) {
                    if (fragmentManager.getBackStackEntryCount() > 1) {
                        fragmentManager.popBackStack(RECIPE_STEP_VIDEO, 0);
                    } else if (fragmentManager.getBackStackEntryCount() > 0) {
                        finish();
                    }
                }
                else {
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (findViewById(R.id.video_container) == null) {
            if (fragmentManager.getBackStackEntryCount() > 1) {
                fragmentManager.popBackStack(RECIPE_STEP_VIDEO, 0);
            } else if (fragmentManager.getBackStackEntryCount() > 0) {
                finish();
            }
        }
        else {
            finish();
        }
        super.onBackPressed();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
