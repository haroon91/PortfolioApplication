package com.example.haroonshahid.seekerscapital.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.haroonshahid.seekerscapital.R;
import com.prof.rssparser.Article;

import java.util.ArrayList;

/**
 * Created by haroonshahid on 5/2/2018.
 */

public class StockFeedsAdapter extends RecyclerView.Adapter<StockFeedsViewHolder> {

    ArrayList<Article> articles = new ArrayList<>();

    public StockFeedsAdapter(ArrayList<Article> articles) {
        this.articles = articles;
    }

    public void setArticlesList(ArrayList<Article> articles) {
        this.articles = articles;
    }

    @Override
    public StockFeedsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_feeds_row, parent, false);

        return new StockFeedsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StockFeedsViewHolder holder, int position) {
        Article article = articles.get(position);

        holder.feedHeading.setText(article.getTitle());
        holder.feedDetail.setText(article.getDescription());
        holder.dateView.setText(String.valueOf(article.getPubDate()));
        holder.feedUrl = article.getLink();

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}
