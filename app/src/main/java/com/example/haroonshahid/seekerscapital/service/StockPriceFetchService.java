package com.example.haroonshahid.seekerscapital.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.haroonshahid.seekerscapital.Constants;
import com.example.haroonshahid.seekerscapital.SeekersApplication;
import com.example.haroonshahid.seekerscapital.model.PriceResult;
import com.example.haroonshahid.seekerscapital.model.StockPosition;
import com.example.haroonshahid.seekerscapital.parser.PriceParser;

import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by haroonshahid on 2/2/2018.
 */

public class StockPriceFetchService extends IntentService {

    public static final String REQUEST_STRING = "myRequest";

    private String URL = null;
    private PriceResult priceResult;
    private static final int CONNECTION_TIMEOUT = 5 * 1000;
    private static final int READ_TIMEOUT = 10 * 1000;

    public StockPriceFetchService(){
        super("StockPriceFetchService");
    }
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public StockPriceFetchService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String requestString = intent.getStringExtra(REQUEST_STRING);
        URL = requestString;

        if (URL != null && !URL.isEmpty()) {

            try {

                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                        .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                        .build();

                AndroidNetworking.get(URL)
                        .setOkHttpClient(okHttpClient)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                PriceParser priceParser = new PriceParser();

                                try {
                                    priceResult = priceParser.parse(response.toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                updateStockPositionsList(priceResult);

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.setAction(Constants.INTENT_ACTION_PRICE_UPDATE);
                                Log.d("StockPriceFetchService", String.format("Symbol: %s, Price: %s",
                                        priceResult.symbol, priceResult.closingPrice));
                                sendBroadcast(broadcastIntent);
                            }

                            @Override
                            public void onError(ANError anError) {
                                anError.printStackTrace();
                                priceResult = null;
                            }
                        });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateStockPositionsList(PriceResult priceResult) {
        List<StockPosition> stockPositionList = SeekersApplication.instance().stockPositionList;
        if (stockPositionList != null && stockPositionList.size() > 0) {
            for (StockPosition position : stockPositionList) {
                if (position.getStock().symbol.equals(priceResult.symbol)) {
                    position.openingPrice = Double.valueOf(priceResult.openingPrice);
                    position.closingPrice = Double.valueOf(priceResult.closingPrice);
                }
            }
        }
    }
}
