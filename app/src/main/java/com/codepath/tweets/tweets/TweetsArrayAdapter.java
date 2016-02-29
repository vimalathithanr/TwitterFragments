package com.codepath.tweets.tweets;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.tweets.tweets.fragments.UserTimelineFragment;
import com.codepath.tweets.tweets.models.Tweet;
import com.squareup.picasso.Picasso;

import org.ocpsoft.pretty.time.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by VRAJA03 on 2/19/2016.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    RecyclerView.ViewHolder holder;

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Tweet tweet = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }

        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        final TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        final TextView tvUserhandle = (TextView) convertView.findViewById(R.id.tvUserHandle);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTimestamp);
        TextView tvretweetCount = (TextView) convertView.findViewById(R.id.tvretweetCount);
        TextView tvfavCount = (TextView) convertView.findViewById(R.id.tvfavCount);

        tvUserName.setText(tweet.getUser().getName());
        tvUserhandle.setText(tweet.getUser().getScreenName());
        tvretweetCount.setText(String.valueOf(tweet.getRetweetCount()));
        tvBody.setText(tweet.getBody());
        tvTime.setText(dateFormat(tweet.getCreatedAt()));
        //tvfavCount.setText(String.valueOf(tweet.getFavCount()));
        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);


        if (tweet != null) {
            ImageView ivProfileImageset = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            //set as the tag the position parameter
            ivProfileImageset.setTag(new Integer(position));
            ivProfileImageset.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Integer realPosition = (Integer) v.getTag();
                    Intent intent = new Intent(getContext(), ProfileActivity.class);

                    intent.putExtra("screen_name", tvUserhandle.getText().toString());
                    v.getContext().startActivity(intent);
                }
            });

        }


        return convertView;
    }


    public String dateFormat(String serverDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy");
        Date d = null;
        try {
            d = sdf.parse(serverDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(d);
        long time = c.getTimeInMillis();

        String prettyTimeString = new PrettyTime().format(new Date(time));
        String prettyTime = null;

        if (prettyTimeString.contains("minutes")) {
            prettyTime = prettyTimeString.replace(" minutes ago", "m");
        } else if (prettyTimeString.contains("minute")) {
            prettyTime = prettyTimeString.replace(" minute ago", "m");
        } else if (prettyTimeString.contains("hours")) {
            prettyTime = prettyTimeString.replace(" hours ago", "h");
        } else if (prettyTimeString.contains("hour")) {
            prettyTime = prettyTimeString.replace(" hour ago", "h");
        } else if (prettyTimeString.contains("seconds")) {
            prettyTime = prettyTimeString.replace(" seconds ago", "s");
        } else if (prettyTimeString.contains("second")) {
            prettyTime = prettyTimeString.replace(" second ago", "s");
        } else if (prettyTimeString.contains("moments")) {
            prettyTime = prettyTimeString.replace("moments ago", "Just Now");
        } else if (prettyTimeString.contains("moments")) {
            prettyTime = prettyTimeString.replace("moments from now", "Just Now");
        }

        return prettyTime;

    }
}
