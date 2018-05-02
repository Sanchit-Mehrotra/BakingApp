package com.kumarutsavanand.bakingapp.utilities;


import com.kumarutsavanand.bakingapp.pojos.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeCallInterface {
    String BAKING_JSON_PATH = "baking.json";
    @GET(BAKING_JSON_PATH)
    Call<ArrayList<Recipe>> getRecipeArrayList();

}
