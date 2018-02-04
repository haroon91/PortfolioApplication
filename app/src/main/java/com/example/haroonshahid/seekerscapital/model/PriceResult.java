package com.example.haroonshahid.seekerscapital.model;

/**
 * Created by haroonshahid on 4/2/2018.
 */

public class PriceResult {

    public String symbol;
    public String openingPrice;
    public String closingPrice;

    public PriceResult(String symbol, String openingPrice, String closingPrice) {
        this.symbol = symbol;
        this.openingPrice = openingPrice;
        this.closingPrice = closingPrice;
    }

}
