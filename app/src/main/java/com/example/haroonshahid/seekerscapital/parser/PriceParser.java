package com.example.haroonshahid.seekerscapital.parser;

import com.example.haroonshahid.seekerscapital.model.PriceResult;

import me.doubledutch.lazyjson.LazyArray;
import me.doubledutch.lazyjson.LazyObject;

/**
 * Created by haroonshahid on 4/2/2018.
 */

public class PriceParser {

    public PriceResult parse(String raw) throws Exception {
        if (raw == null || raw.isEmpty())
            return null;

        try {
            return parseLazyJson(raw);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private PriceResult parseLazyJson(String raw) {
        PriceResult priceResult = null;

        try {
            LazyObject lazyObject = new LazyObject(raw);
            if (lazyObject.has("datatable")) {
                LazyArray data = lazyObject.getJSONObject("datatable").getJSONArray("data");
                if (data != null && data.length() > 0) {
                    LazyArray finalArray = data.getJSONArray(0);
                    priceResult = new PriceResult(finalArray.getString(0),
                                    finalArray.getString(2),
                                    finalArray.getString(5));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return priceResult;
    }
}
