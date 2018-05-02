package com.kumarutsavanand.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kumarutsavanand.bakingapp.pojos.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Callback;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListItemViewHolder> {

    private ArrayList<Recipe> mRecipes;
    private Context mContext;
    private ItemViewClickListener mItemViewClickListener;

    public RecipeListAdapter(ItemViewClickListener itemViewClickListener) {
        this.mItemViewClickListener = itemViewClickListener;
    }

    public void setRecipeList(ArrayList<Recipe> recipes, Context context) {
        this.mRecipes = recipes;
        this.mContext = context;
        notifyDataSetChanged();
    }

    public interface ItemViewClickListener {
        void onItemClickListener(Recipe clickedRecipe);
    }

    @NonNull
    @Override
    public RecipeListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recipe_list_element_view, parent, false);
        RecipeListItemViewHolder recipeListeItemViewHolder = new RecipeListItemViewHolder(view);
        return recipeListeItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListItemViewHolder holder, int position) {
        holder.recipeName.setText(mRecipes.get(position).getName());
       if(!mRecipes.get(position).getImage().equals("")) {
            Picasso.with(mContext)
                    .load(mRecipes.get(position).getImage())
                    .resize(100, 100)
                    .placeholder(R.drawable.muffin)
                    .error(R.drawable.muffin)
                    .fit()
                    .into(holder.recipeImageView);
        }
        else {
           Picasso.with(mContext)
                   .load(R.drawable.muffin)
                   .resize(100, 100)
                   .into(holder.recipeImageView);
       }
    }

    @Override
    public int getItemCount() {
        if(mRecipes != null) {
            return mRecipes.size();
        }
        else {
            return 0;
        }
    }

    public class RecipeListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @BindView(R.id.recipe_name_text_view) TextView recipeName;

        @BindView(R.id.recipe_image_view) ImageView recipeImageView;

        public RecipeListItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mItemViewClickListener.onItemClickListener(mRecipes.get(clickedPosition));
        }
    }

}
