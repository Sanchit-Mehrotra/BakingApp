package com.kumarutsavanand.bakingapp;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.kumarutsavanand.bakingapp.pojos.Recipe;
import com.kumarutsavanand.bakingapp.pojos.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RecipeStepVideoFragment extends Fragment {

    private Recipe mRecipe;
    private ArrayList<Step> mSteps;
    private int mStepIndex;
    private String mRecipeName;
    private String videoUrl;
    private SimpleExoPlayer mPlayer;
    private SimpleExoPlayerView mPlayerView;
    private ImageView thumbNail;
    private TextView stepDescriptionText;
    private long playBackPosition;
    private Button nextButton;
    private Button previousButton;
    private int currentWindow;
    private boolean playWhenReady = true;
    private static String PLAYBACK_POSITION = "PLAYBACK_POSITION";

    public RecipeStepVideoFragment() {
        // Empty Constructor
    }

    private RecipeStepAdapter.OnStepClickListener onStepClickListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_step_video, container, false);




        onStepClickListener = (RecipeDetailActivity) getActivity();
        mPlayerView = rootView.findViewById(R.id.playerView);
        thumbNail = rootView.findViewById(R.id.thumbnail);
        stepDescriptionText = rootView.findViewById(R.id.recipe_step_detail_text);
        nextButton = rootView.findViewById(R.id.nextStep);
        previousButton = rootView.findViewById(R.id.previousStep);


        mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);


        if(savedInstanceState != null) {
            mSteps = savedInstanceState.getParcelableArrayList(RecipeDetailActivity.SELECTED_STEP);
            mStepIndex = savedInstanceState.getInt(RecipeDetailActivity.SELECTED_STEP_INDEX);
            mRecipeName = savedInstanceState.getString(RecipeDetailActivity.SELECTED_RECIPE_NAME);
            playBackPosition = savedInstanceState.getLong(PLAYBACK_POSITION);


        }
        else {
            mSteps = getArguments().getParcelableArrayList(RecipeDetailActivity.SELECTED_STEP);
            if (mSteps != null) {
                mStepIndex = getArguments().getInt(RecipeDetailActivity.SELECTED_STEP_INDEX);
                mRecipeName = getArguments().getString(RecipeDetailActivity.SELECTED_RECIPE_NAME);
            }
            else {
                mRecipe = getArguments().getParcelable(MainActivity.CLICKED_RECIPE_BUNDLE);
                mSteps = mRecipe.getSteps();
                mStepIndex = 0;
            }
        }



        videoUrl = mSteps.get(mStepIndex).getVideoURL();
        String imageUrl = mSteps.get(mStepIndex).getThumbnailURL();

        stepDescriptionText.setText(mSteps.get(mStepIndex).getDescription());


        previousButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mSteps.get(mStepIndex).getId() > 0) {
                    if (mPlayer!=null){
                        mPlayer.stop();
                    }
                    onStepClickListener.onStepClick(mSteps,mSteps.get(mStepIndex).getId() - 1);
                }
                else {
                    Toast.makeText(getContext(),"Already on first step of recipe", Toast.LENGTH_SHORT).show();
                }
            }});

        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int lastStep = (mSteps.size() - 1);
                if (mSteps.indexOf(mSteps.get(mStepIndex)) < mSteps.indexOf(mSteps.get(lastStep))) {
                    if (mPlayer!=null){
                        mPlayer.stop();
                    }
                    onStepClickListener.onStepClick(mSteps, mSteps.indexOf(mSteps.get(mStepIndex + 1)));
                }
                else {
                    Toast.makeText(getContext(), "Already on last step of recipe", Toast.LENGTH_SHORT).show();
                }
            }});

        if(!imageUrl.equals("")) {
            Picasso.with(getContext())
                    .load(Uri.parse(imageUrl))
                    .into(thumbNail);
        }



        if(!videoUrl.isEmpty()) {
            mPlayerView.setVisibility(View.VISIBLE);
            initializePlayer();
            if (rootView.findViewWithTag("sw600dp") != null) {
                getActivity().findViewById(R.id.video_container)
                        .setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
                mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            }

            else if(rootView.findViewWithTag("sw600dp") != null && getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                stepDescriptionText.setVisibility(View.GONE);


            }

        }
        else {
            mPlayer = null;
            mPlayerView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        }



        return rootView;
    }




    @Override
    public void onDetach() {
        super.onDetach();
        if(mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayer != null) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mPlayer != null) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();        }
    }

    private void initializePlayer() {
        if(mPlayer == null) {
            mPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            mPlayerView.setPlayer(mPlayer);
            mPlayer.setPlayWhenReady(playWhenReady);
            mPlayer.seekTo(currentWindow, playBackPosition);
        }
        MediaSource mediaSource = buildMediaSource(Uri.parse(videoUrl));
        mPlayer.prepare(mediaSource, true, false);
    }

    private void releasePlayer() {
        if(mPlayer != null) {
            playBackPosition = mPlayer.getCurrentPosition();
            currentWindow = mPlayer.getCurrentWindowIndex();
            playWhenReady = mPlayer.getPlayWhenReady();
            mPlayer.release();
            mPlayer = null;
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory("Baking_App"))
                .createMediaSource(uri);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(RecipeDetailActivity.SELECTED_STEP, mSteps);
        outState.putInt(RecipeDetailActivity.SELECTED_STEP_INDEX, mStepIndex);
        outState.putString(RecipeDetailActivity.SELECTED_RECIPE_NAME, mRecipeName);
        outState.putLong(PLAYBACK_POSITION, playBackPosition);
    }


}
