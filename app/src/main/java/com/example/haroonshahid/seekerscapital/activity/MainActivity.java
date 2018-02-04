package com.example.haroonshahid.seekerscapital.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haroonshahid.seekerscapital.Constants;
import com.example.haroonshahid.seekerscapital.R;
import com.example.haroonshahid.seekerscapital.SeekersApplication;
import com.example.haroonshahid.seekerscapital.adapter.StockListAdapter;
import com.example.haroonshahid.seekerscapital.model.StockPosition;
import com.example.haroonshahid.seekerscapital.ui.SimpleDividerForRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private static boolean inited = false;

    private RelativeLayout progressLayout;

    private TextView portfolioValueView, percentageChangeView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public StockListAdapter stockListAdapter;

    private List<StockPosition> stockPositionList = new ArrayList<>();

    private BroadcastReceiver stockListBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.INTENT_ACTION_POSITIONS_LIST_BROADCAST)) {
                SeekersApplication.instance().fetchStockPrices();
            } else if (intent.getAction().equals(Constants.INTENT_ACTION_PRICE_UPDATE)) {
                swipeRefreshLayout.setRefreshing(false);
            }

            updateUI();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        if(getActionBar() != null) {
            getActionBar().hide();
        }
        setContentView(R.layout.activity_main);

        progressLayout = findViewById(R.id.rl_progressbar);
        startProgressBar();

        getActionBar().show();
        initActionBarLayout();

        portfolioValueView = findViewById(R.id.tv_portfolio_value);
        percentageChangeView = findViewById(R.id.tv_percentage_change);
        RecyclerView recyclerView = findViewById(R.id.rv_stocks);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new SimpleDividerForRecyclerView(this));
        stockListAdapter = new StockListAdapter(stockPositionList);
        recyclerView.setAdapter(stockListAdapter);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        SeekersApplication.instance().resetStockFetchTimer();
                        SeekersApplication.instance().fetchStockPrices();
                        Toast.makeText(MainActivity.this, "Refreshing ...", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        if (!inited) {
            SeekersApplication.instance().retrieveStockPositionsList();
            inited = true;
        } else {
            updateUI();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.INTENT_ACTION_POSITIONS_LIST_BROADCAST);
        intentFilter.addAction(Constants.INTENT_ACTION_PRICE_UPDATE);
        registerReceiver(stockListBroadcastReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(stockListBroadcastReceiver);
    }

    private void initActionBarLayout() {
        ActionBar mActionBar = getActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayShowHomeEnabled(false);
            mActionBar.setDisplayShowTitleEnabled(false);

            LayoutInflater mInflater = LayoutInflater.from(this);
            View mCustomView = mInflater.inflate(R.layout.actionbar_portfolio_page, null);
            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            Drawable background = getResources().getDrawable(R.drawable.actionbar_bkg);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    private void updateUI() {
        stockPositionList = SeekersApplication.instance().stockPositionList;
        stockListAdapter.setStockPositionsList(stockPositionList);
        stockListAdapter.notifyDataSetChanged();

        calculatePortfolioValue();
        stopProgressBar();
    }

    private void calculatePortfolioValue() {
        double portValueCurrent = 0.0;
        double portValueInvested = 0.0;
        for (StockPosition position : stockPositionList) {
            portValueCurrent += (position.getQuantity() * (position.closingPrice != -1 ? position.closingPrice : position.getTransactionPrice()));
            portValueInvested += (position.getQuantity() * position.getTransactionPrice());
        }
        portfolioValueView.setText(String.format("$%.2f",portValueCurrent));

        double changeValue = portValueCurrent-portValueInvested;
        double changePercent = changeValue / portValueInvested;
        percentageChangeView.setText(getResources().getString(R.string.loss_gain_portfolio));

        if (changeValue < 0) {
            percentageChangeView.append(Html.fromHtml( "<font color=\"#D0021B\">"
                    + String.format(" %.2f (%.2f%%)", changeValue, changePercent) + "</font>"));

        } else {
            percentageChangeView.append(Html.fromHtml( "<font color=\"#6ABD0F\">"
                    + String.format(" +%.2f (%.2f%%)", changeValue, changePercent) + "</font>"));

        }


    }

    private void startProgressBar() {
        progressLayout.setVisibility(View.VISIBLE);
    }

    private void stopProgressBar() {
        progressLayout.setVisibility(View.INVISIBLE);
    }
}
