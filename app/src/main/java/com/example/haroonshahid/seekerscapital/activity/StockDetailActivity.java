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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.haroonshahid.seekerscapital.CommonUtils;
import com.example.haroonshahid.seekerscapital.Constants;
import com.example.haroonshahid.seekerscapital.R;
import com.example.haroonshahid.seekerscapital.SeekersApplication;
import com.example.haroonshahid.seekerscapital.adapter.StockFeedsAdapter;
import com.example.haroonshahid.seekerscapital.model.StockPosition;
import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;

import java.util.ArrayList;
import java.util.List;

public class StockDetailActivity extends Activity {

    StockPosition currentStockPosition = null;
    ArrayList<Article> articles = new ArrayList<>();
    String chartUrl = "";
    String feedsUrl = "";

    TextView shareValueView, priceChangeView, percentageChangeView;
    WebView chartView;
    RecyclerView recyclerView;
    StockFeedsAdapter adapter;

    private BroadcastReceiver priceUpdateReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("StockDetailActivity", "priceUpdateReceiver");
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

        setContentView(R.layout.activity_stock_detail);
        getActionBar().show();

        Intent intent = getIntent();
        String symbol = intent.getStringExtra("symbol");
        if (!symbol.isEmpty()) {
            List<StockPosition> openPositions = SeekersApplication.instance().stockPositionList;
            for (StockPosition pos : openPositions) {
                if (pos.getStock().symbol.equals(symbol)) {
                    currentStockPosition = pos;
                }
            }
        }
        initActionBarLayout();

        chartView = findViewById(R.id.webview);
        recyclerView = findViewById(R.id.rv_feeds);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new StockFeedsAdapter(articles);
        recyclerView.setAdapter(adapter);

        shareValueView = findViewById(R.id.tv_share_value);
        priceChangeView = findViewById(R.id.tv_price_change);
        percentageChangeView = findViewById(R.id.tv_percentage_change);

        initWebview();
        updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(priceUpdateReciever);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(priceUpdateReciever, new IntentFilter(Constants.INTENT_ACTION_PRICE_UPDATE));
    }

    private void updateUI() {
        if (currentStockPosition.closingPrice >= 0) {
            shareValueView.setText(String.format("$%.2f", currentStockPosition.closingPrice));

            double priceChange = currentStockPosition.closingPrice - currentStockPosition.openingPrice;
            double percentageChange = priceChange / currentStockPosition.openingPrice;
            if (priceChange < 0) {
                priceChangeView.setTextColor(Constants.RED_COLOR);
                priceChangeView.setText(String.format("%.2f", priceChange));
                percentageChangeView.setTextColor(Constants.RED_COLOR);
                percentageChangeView.setText(String.format("(%.2f%%)", percentageChange));
            }
        } else {
            shareValueView.setText("--.--");
        }

        Parser parser = new Parser();
        parser.execute(feedsUrl);
        parser.onFinish(new Parser.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<Article> list) {
                articles = list;
                adapter.setArticlesList(articles);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {
                articles = new ArrayList<>();
                adapter.setArticlesList(articles);
            }
        });
    }


    private void initWebview() {
        setWebSettings(chartView);

        if (currentStockPosition != null) {
            chartUrl = Constants.NASDAQ_CHART_URL.replace(Constants.SYMBOL_PLACEHOLDER, currentStockPosition.getStock().symbol);
            feedsUrl = Constants.FEEDS_URL.replace(Constants.SYMBOL_PLACEHOLDER, currentStockPosition.getStock().symbol);
        } else {
            chartUrl = "about:blank";
            feedsUrl = "about:blank";
        }

        setWebViewClient(chartView, chartUrl);
        chartView.loadUrl(chartUrl);
    }

    private void setWebSettings(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(CommonUtils.isConnectedToNetwork() ? WebSettings.LOAD_DEFAULT : WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
    }

    private void setWebViewClient(final WebView webView, final String url) {
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
        });
    }


    private void initActionBarLayout() {
        ActionBar mActionBar = getActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayShowHomeEnabled(false);
            mActionBar.setDisplayShowTitleEnabled(false);

            LayoutInflater mInflater = LayoutInflater.from(this);
            View mCustomView = mInflater.inflate(R.layout.actionbar_stock_detail, null);
            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);

            TextView stockName = mCustomView.findViewById(R.id.tv_stock_name);
            TextView stockDetail = mCustomView.findViewById(R.id.tv_stock_symbol);
            if (currentStockPosition != null) {
                stockName.setText(currentStockPosition.getStock().name);
                stockDetail.setText(String.format("%s - %s", currentStockPosition.getStock().symbol, currentStockPosition.getStock().exchange));
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            Drawable background = getResources().getDrawable(R.drawable.actionbar_bkg);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }
}
