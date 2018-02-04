package com.example.haroonshahid.seekerscapital;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;

import com.example.haroonshahid.seekerscapital.model.StockPosition;
import com.example.haroonshahid.seekerscapital.parser.PositionsParser;
import com.example.haroonshahid.seekerscapital.service.StockPriceFetchService;

import java.io.InputStream;
import java.util.List;

/**
 * Created by haroonshahid on 31/1/2018.
 */

public class SeekersApplication extends Application {

    private static final int FETCH_PRICES_TIME_DELAY = 60 * 1000 * 1; // 1 mins

    private static SeekersApplication instance = null;
    private boolean isTest = false;

    public List<StockPosition> stockPositionList;

    private Handler mHandler = new Handler();

    public static SeekersApplication instance() {
        if (instance == null) {
            throw new SeekersApplicationNotInitializedError("SeekersApplication is not initialized, instance is null");
        }
        return instance;
    }

    public static SeekersApplication instance(boolean isTest) {
        if (instance == null) {
            throw new SeekersApplicationNotInitializedError("SeekersApplication is not initialized, instance is null");
        }
        instance.setTest(isTest);
        return instance;
    }

    private Runnable stockFetchTimeRunnable = new Runnable() {
        @Override
        public void run() {
            fetchStockPrices();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        SeekersApplication.instance = this;

        mHandler.postDelayed(stockFetchTimeRunnable, FETCH_PRICES_TIME_DELAY);
    }

    private static class SeekersApplicationNotInitializedError extends RuntimeException {

        private static final long serialVersionUID = 1L;

        SeekersApplicationNotInitializedError(String message) {
            super(message);
        }
    }

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }

    public List<StockPosition> retrieveStockPositionsList() {
        new OpenPositionsRetriever().execute();
        return stockPositionList;
    }

    public void fetchStockPrices () {
        for (StockPosition stockPosition : stockPositionList) {
            Intent intent = new Intent(SeekersApplication.this, StockPriceFetchService.class);
            intent.putExtra(StockPriceFetchService.REQUEST_STRING, CommonUtils.buildStockUrl(stockPosition));
            startService(intent);
        }
    }

    public void resetStockFetchTimer() {
        mHandler.removeCallbacks(stockFetchTimeRunnable);
        mHandler.postDelayed(stockFetchTimeRunnable, FETCH_PRICES_TIME_DELAY);
    }

    class OpenPositionsRetriever extends AsyncTask<Void, Integer, List<StockPosition>> {

        @Override
        public void onPreExecute() {
            //todo progress bar here
        }
        @Override
        protected List<StockPosition> doInBackground(Void... voids) {

            try {
                InputStream fileStream = getResources().openRawResource(R.raw.open_positions);
                String raw = CommonUtils.convert(fileStream);

                PositionsParser positionsParser = new PositionsParser();

                return positionsParser.parse(raw);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public void onPostExecute(List<StockPosition> stockPositions) {
            if (stockPositions != null) {
                stockPositionList = stockPositions;
                sendBroadcast(new Intent(Constants.INTENT_ACTION_POSITIONS_LIST_BROADCAST));
            }
        }
    }
}
