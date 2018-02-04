package com.example.haroonshahid.seekerscapital.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.haroonshahid.seekerscapital.R;
import com.example.haroonshahid.seekerscapital.activity.StockDetailActivity;

/**
 * Created by haroonshahid on 31/1/2018.
 */

public class StockListViewHolder extends RecyclerView.ViewHolder {

    TextView stockSymbol, stockDetail, positionValue, priceChange, percentageChange;
    ImageView nextArrow;
    RelativeLayout rowLayout;

    public StockListViewHolder(final View itemView) {
        super(itemView);

        stockSymbol = itemView.findViewById(R.id.tv_stock_symbol);
        stockDetail = itemView.findViewById(R.id.tv_stock_detail);
        positionValue = itemView.findViewById(R.id.tv_position_value);
        priceChange = itemView.findViewById(R.id.tv_price_change);
        percentageChange = itemView.findViewById(R.id.tv_percentage_change);
        nextArrow = itemView.findViewById(R.id.iv_next);

        rowLayout = itemView.findViewById(R.id.rl_stock_row);

        rowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(itemView.getContext(), StockDetailActivity.class);
                intent.putExtra("symbol", String.valueOf(stockSymbol.getText()));
                itemView.getContext().startActivity(intent);
            }
        });
    }
}
