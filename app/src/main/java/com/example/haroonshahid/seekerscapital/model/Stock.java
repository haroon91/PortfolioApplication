package com.example.haroonshahid.seekerscapital.model;

/**
 * Created by haroonshahid on 31/1/2018.
 */

public class Stock {

    public String name;
    public String symbol;
    public String exchange;

    public Stock(String name, String symbol, String exchange) {
        this.name = name;
        this.symbol = symbol;
        this.exchange = exchange;
    }
}
