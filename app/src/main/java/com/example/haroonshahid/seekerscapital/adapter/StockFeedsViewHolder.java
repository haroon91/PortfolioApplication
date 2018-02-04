package com.example.haroonshahid.seekerscapital.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.haroonshahid.seekerscapital.R;

/**
 * Created by haroonshahid on 5/2/2018.
 */

public class StockFeedsViewHolder extends RecyclerView.ViewHolder {

    TextView feedHeading, feedDetail, dateView;
    String feedUrl;

    public StockFeedsViewHolder(View itemView) {
        super(itemView);

        feedHeading = itemView.findViewById(R.id.tv_feed_heading);
        feedDetail = itemView.findViewById(R.id.tv_feed_detail);
        dateView = itemView.findViewById(R.id.tv_feed_date);

        final Context context = itemView.getContext();

        RelativeLayout feedLayout = itemView.findViewById(R.id.rl_feed_layout);
        feedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(feedUrl));
                context.startActivity(intent);
            }
        });
    }
}
