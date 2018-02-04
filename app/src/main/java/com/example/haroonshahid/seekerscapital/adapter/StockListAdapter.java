package com.example.haroonshahid.seekerscapital.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.haroonshahid.seekerscapital.Constants;
import com.example.haroonshahid.seekerscapital.R;
import com.example.haroonshahid.seekerscapital.model.StockPosition;

import java.util.List;
import java.util.Locale;

/**
 * Created by haroonshahid on 31/1/2018.
 */

public class StockListAdapter extends RecyclerView.Adapter<StockListViewHolder> {

    private List<StockPosition> stockPositionsList;

    public StockListAdapter(List<StockPosition> stockPositionsList) {
        this.stockPositionsList = stockPositionsList;
    }

    public void setStockPositionsList(List<StockPosition> stockPositionsList) {
        this.stockPositionsList.clear();
        if (stockPositionsList != null && stockPositionsList.size() > 0) {
            this.stockPositionsList.addAll(stockPositionsList);
        }
    }

    @Override
    public StockListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_invested_row, parent, false);

        return new StockListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StockListViewHolder holder, int position) {
        StockPosition stockPosition = stockPositionsList.get(position);
        //TODO set stock closingPrice,symbol etc here
        double positionValue = 0.0;
        double priceChange = 0.0;
        double percentageChange = 0.0;

        if (stockPosition.closingPrice != -1) {
            positionValue = stockPosition.getQuantity() * stockPosition.closingPrice;
            priceChange = stockPosition.closingPrice - stockPosition.getTransactionPrice();
            percentageChange = (stockPosition.getTransactionPrice() - stockPosition.closingPrice) / stockPosition.getTransactionPrice();
        } else {
            positionValue = stockPosition.getQuantity() * stockPosition.getTransactionPrice();
        }

        holder.stockDetail.setText(stockPosition.getStock().name);
        holder.stockSymbol.setText(stockPosition.getStock().symbol);
        holder.positionValue.setText(String.format(Locale.US, "$%.2f", positionValue));
//        holder.stockPrice.setText(String.valueOf(stockPosition.getTransactionPrice()));
        if (priceChange < 0) {
            holder.priceChange.setTextColor(Constants.RED_COLOR);
            holder.priceChange.setText(String.format("%.2f", priceChange));
            holder.percentageChange.setTextColor(Constants.RED_COLOR);
            holder.percentageChange.setText(String.format("(%.2f%%)", percentageChange));

        } else {
            holder.priceChange.setTextColor(Constants.GREEN_COLOR);
            holder.priceChange.setText(String.format("+%.2f", priceChange));
            holder.percentageChange.setTextColor(Constants.GREEN_COLOR);
            holder.percentageChange.setText(String.format("(%.2f%%)", percentageChange));
        }

    }

    @Override
    public int getItemCount() {
        return stockPositionsList.size();
    }
}
