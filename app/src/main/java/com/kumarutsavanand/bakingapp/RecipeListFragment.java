package com.kumarutsavanand.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kumarutsavanand.bakingapp.IdlingResources.SimpleIdlingResource;
import com.kumarutsavanand.bakingapp.pojos.Recipe;
import com.kumarutsavanand.bakingapp.utilities.NetworkUtils;
import com.kumarutsavanand.bakingapp.utilities.RecipeCallInterface;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecipeListFragment extends Fragment {

    ArrayList<Recipe> recipes;

    public static final String RECIPES_BUNDLE = "RECIPES";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        final Context context = getContext();
        final RecyclerView recipeListRecyclerView = rootView.findViewById(R.id.recipe_list_recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        final RecipeListAdapter recipeListAdapter = new RecipeListAdapter((MainActivity)getActivity());

        // Inflate the layout for this fragment
        System.out.println("onCreateView");


        if(savedInstanceState != null) {
            System.out.println("savedInstanceState");
            recipes = savedInstanceState.getParcelableArrayList(RECIPES_BUNDLE);
            recipeListAdapter.setRecipeList(recipes, getContext());
            recipeListRecyclerView.setAdapter(recipeListAdapter);
            recipeListRecyclerView.setLayoutManager(linearLayoutManager);
        } else {

            RecipeCallInterface recipeCallInterface = NetworkUtils.RetrieveGson();
            final Call<ArrayList<Recipe>> recipe = recipeCallInterface.getRecipeArrayList();
            final SimpleIdlingResource idlingResource = (SimpleIdlingResource)((MainActivity)getActivity()).getIdlingResource();
            if (idlingResource != null) {
                idlingResource.setIdleState(false);
            }

            recipe.enqueue(new Callback<ArrayList<Recipe>>() {

                @Override
                public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                    recipes = response.body();
                    recipeListAdapter.setRecipeList(recipes, context);
                    recipeListRecyclerView.setAdapter(recipeListAdapter);
                    recipeListRecyclerView.setLayoutManager(linearLayoutManager);

                    recipeListAdapter.setRecipeList(recipes, getContext());
                    if (idlingResource != null) {
                        idlingResource.setIdleState(true);
                    }

                }

                @Override
                public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {

                }
            });
        }



        return rootView;



    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if(recipes != null) {
            outState.putParcelableArrayList(RECIPES_BUNDLE, recipes);
        }
    }
}
