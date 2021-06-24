package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=59a6d3cd474e593181e0554b4cd2c8b9";
    public static final String TAG = "MainActivity";

    //5 Create list of movies (Uses method from  movie class to populate)
    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create empty movies list
        movies = new ArrayList<>();

        //Get recycler view
        RecyclerView rvMovies = findViewById(R.id.rvMovies);

        //Create the adapter
        MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        //Set the adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);

        //Set a layout manager on the recycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));



        //Sending network request
        //Create a client
        AsyncHttpClient client = new AsyncHttpClient();

        //Get data from client using url
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            //Handles a successful return
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "Success");
                //Get json object from returned json
                JSONObject jsonObject = json.jsonObject;
                try {
                    //Store the results array in a json array variable
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());
                    //Pass results to Movie's fromJsonArray method to create a populated list of movie info and add it to movies list
                    movies.addAll(Movie.fromJsonArray(results));
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Movies: " + movies.size());
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception " + e.getLocalizedMessage().toString());
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "Failure");
            }
        });
    }
}