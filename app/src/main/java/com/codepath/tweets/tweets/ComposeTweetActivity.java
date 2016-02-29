package com.codepath.tweets.tweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.tweets.tweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class ComposeTweetActivity extends AppCompatActivity {

    private TwitterClient client;
    private EditText etCompose;
    private Tweet tweet;
    private TextView tvCount;
    String composeTweet;
    private String createdAt;
    private String id;
    private String text;
    private String name;
    private String screenName;
    private String profileImage;
    private int length = 140;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_tweet);
        tvCount = (TextView) findViewById(R.id.tvCount);
        etCompose = (EditText) findViewById(R.id.etCompose);

        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCount.setText(String.valueOf(length - count));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void postTweet(View v) {

        tweet = new Tweet();
        client = TwitterApplication.getRestClient();

        if (etCompose.getText().toString() != null) {
            composeTweet = etCompose.getText().toString();
            tweet.setComposeTweet(composeTweet);

        }


        client.postTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {

                try {
                    createdAt = json.getString("created_at");
                    text = json.getString("text");
                    name = json.getJSONObject("user").getString("name");
                    screenName = json.getJSONObject("user").getString("screen_name");
                    profileImage = json.getJSONObject("user").getString("profile_image_url");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent data = new Intent();
                data.putExtra("tweet", composeTweet);
                data.putExtra("createdAt", createdAt);
                data.putExtra("text", text);
                data.putExtra("name", name);
                data.putExtra("screenName", screenName);
                data.putExtra("profileImage", profileImage);
                setResult(RESULT_OK, data);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

}
