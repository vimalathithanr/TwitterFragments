package com.codepath.tweets.tweets;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.tweets.tweets.fragments.HomeTimelineFragment;
import com.codepath.tweets.tweets.fragments.MentionsTimelineFragment;
import com.codepath.tweets.tweets.fragments.UserTimelineFragment;
import com.codepath.tweets.tweets.models.Tweet;
import com.codepath.tweets.tweets.models.User;

import java.util.ArrayList;

public class TimelineActivity extends AppCompatActivity {


    private ImageView ivCompose;
    private ImageView ivProfile;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));

        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getApplicationContext(),tweets);


        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(viewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.custom_actionbar, null);
        actionBar.setCustomView(v);




        ivCompose = (ImageView) findViewById(R.id.ivCompose);
        ivCompose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent in = new Intent(TimelineActivity.this, ComposeTweetActivity.class);
                startActivityForResult(in, 1);
            }
        });

        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        ivProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent in = new Intent(TimelineActivity.this, ProfileActivity.class);
                startActivity(in);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Entering2", "Hello2");
        String newTweet = data.getStringExtra("tweet");
        Tweet newTweets = new Tweet();
        User newUser = new User();
        newTweets.setComposeTweet(newTweet);
        newTweets.setBody(newTweet);
        newTweets.setCreatedAt(data.getStringExtra("createdAt"));

        newUser.setName(data.getStringExtra("name"));
        newUser.setScreenName(data.getStringExtra("screenName"));
        newUser.setProfileImageUrl(data.getStringExtra("profileImage"));
        newTweets.setUser(newUser);
        tweets.add(0, newTweets);
        aTweets.notifyDataSetChanged();
    }


    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeTimelineFragment();
            } else if (position == 1) {
                return new MentionsTimelineFragment();
            } else {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }

}
