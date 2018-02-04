package com.example.haroonshahid.seekerscapital.parser;

import com.example.haroonshahid.seekerscapital.model.Stock;
import com.example.haroonshahid.seekerscapital.model.StockPosition;

import java.util.ArrayList;
import java.util.List;

import me.doubledutch.lazyjson.LazyArray;
import me.doubledutch.lazyjson.LazyObject;

/**
 * Created by haroonshahid on 31/1/2018.
 */

public class PositionsParser {

    public List<StockPosition> parse(String raw) throws Exception {
        if (raw == null || raw.isEmpty())
            return null;

        try {
            return parseLazyJson(raw);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<StockPosition> parseLazyJson (String raw) {

        List<StockPosition> stockPositionList = new ArrayList<>();

        try {
            LazyObject lazyObject = new LazyObject(raw);
            LazyArray lazyArray = lazyObject.getJSONArray("positions");
            for (int i=0; i<lazyArray.length(); i++) {
                LazyObject positionObject = lazyArray.getJSONObject(i);
                if (positionObject != null) {

                    StockPosition stockPosition =
                            new StockPosition(new Stock(positionObject.getString("name"), positionObject.getString("symbol"), positionObject.getString("exchange")),
                                    positionObject.getInt("transactionType"),
                                    positionObject.getDouble("transactionPrice"),
                                    positionObject.getDouble("quantity"),
                                    positionObject.getString("transactionDate"));

                    stockPositionList.add(stockPosition);
                }
            }
            return stockPositionList;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
