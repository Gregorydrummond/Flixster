package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    int movieID;
    String movieTrailerKey;
    YouTubePlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);

        //Use Intent and parcel to send and retrieve the movie data
        movieID = Parcels.unwrap(getIntent().getParcelableExtra("MovieID"));
        String videoURL = getString(R.string.theMovieDatabaseURL) + String.valueOf(movieID) + "/videos?" + getString(R.string.API_KEY);

        //Create client
        AsyncHttpClient client = new AsyncHttpClient();

        //Request Parameters
        RequestParams params = new RequestParams();
        params.put("movie_id", movieID);

        //API call
        client.get(videoURL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d("MT", "Success");

                //Get json object from returned json
                JSONObject jsonObject = json.jsonObject;

                try {
                    //Store the results array in a json array variable
                    JSONArray results = jsonObject.getJSONArray("results");

                    //Get json object that's in that result array, then get the key from that json object
                    movieTrailerKey = results.getJSONObject(0).getString("key");
                    Log.d("MT", "Key: " + movieTrailerKey);

                    // resolve the player view from the layout
                    playerView = findViewById(R.id.player);

                    setVideo();

                } catch (JSONException e) {
                    Log.e("MT", "Hit json exception: " + e.getLocalizedMessage().toString());
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.e("MT", "Failure");
            }
        });


    }

    public void setVideo() {
        // initialize with API key stored in secrets.xml
        playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                // do any work here to cue video, play video, etc.
                Log.d("MT", "Video Key: " + movieTrailerKey);
                youTubePlayer.setFullscreen(true);
                youTubePlayer.cueVideo(movieTrailerKey);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                // log the error
                Log.e("MT", "Error initializing YouTube player. " + provider.toString());
                Log.e("MT", youTubeInitializationResult.toString());
            }
        });
    }
}