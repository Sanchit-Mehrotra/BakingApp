package com.kumarutsavanand.bakingapp.utilities;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kumarutsavanand.bakingapp.pojos.Recipe;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {
    static RecipeCallInterface recipeCallInterface;

    public static RecipeCallInterface RetrieveGson() {
        String RECIPE_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
        Gson gson = new GsonBuilder().create();
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();

        recipeCallInterface = new Retrofit.Builder()
                .baseUrl(RECIPE_BASE_URL)
                .callFactory(okhttpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build().create(RecipeCallInterface.class);

        return recipeCallInterface;
    }
}
