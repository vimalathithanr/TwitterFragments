package com.codepath.tweets.tweets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.tweets.tweets.fragments.UserTimelineFragment;
import com.codepath.tweets.tweets.models.Tweet;
import com.codepath.tweets.tweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    TwitterClient client;
    User user;
    String userName;
    String userHandle;
    String userFollowers;
    String userFollowing;
    String userImage;
    String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String screenName = getIntent().getStringExtra("screen_name");

        if (screenName == null) {
            client = TwitterApplication.getRestClient();
            client.getUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJSON(response);
                    getSupportActionBar().setTitle(user.getScreenName());
                    populateProfileHeader(user);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        } else {
            Log.i("String Check", screenName);
            client = TwitterApplication.getRestClient();
            client.getUserTimeline(screenName, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {


                    for (int i = 0; i < json.length(); i++) {
                        JSONObject tweetJson = null;
                        try {
                            tweetJson = json.getJSONObject(i).getJSONObject("user");
                            userName = tweetJson.getString("name");
                            userHandle = tweetJson.getString("screen_name");
                            userFollowers = tweetJson.getString("followers_count");
                            userFollowing = tweetJson.getString("friends_count");
                            userImage = tweetJson.getString("profile_image_url_https");
                            tag = tweetJson.getString("description");
                            populateProfileHeader(userName, userHandle, userFollowers, userFollowing, userImage, tag);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });

        }


        if (savedInstanceState == null) {
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();
        }
    }


    private void populateProfileHeader(User user) {
        TextView tvName = (TextView) findViewById(R.id.tvUserName);
        TextView tvUserHandle = (TextView) findViewById(R.id.tvUserHandle);
        TextView tvTag = (TextView) findViewById(R.id.tvTag);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivProfilePic = (ImageView) findViewById(R.id.ivProfilepic);

        tvName.setText(user.getName());
        tvUserHandle.setText("@" + user.getScreenName());
        tvTag.setText(user.getTagLine());
        tvFollowers.setText("Followers " + String.valueOf(user.getFollowersCount()));
        tvFollowing.setText("Following " + String.valueOf(user.getFollowingCount()));
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfilePic);
    }

    private void populateProfileHeader(String name, String userHandle, String followers, String following, String profilepic, String tag) {
        TextView tvName = (TextView) findViewById(R.id.tvUserName);
        TextView tvUserHandle = (TextView) findViewById(R.id.tvUserHandle);
        TextView tvTag = (TextView) findViewById(R.id.tvTag);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivProfilePic = (ImageView) findViewById(R.id.ivProfilepic);

        tvName.setText(name);
        tvUserHandle.setText("@" + userHandle);
        tvTag.setText(tag);
        tvFollowers.setText("Followers " + followers);
        tvFollowing.setText("Following " + following);
        Picasso.with(this).load(profilepic).into(ivProfilePic);

    }

}
