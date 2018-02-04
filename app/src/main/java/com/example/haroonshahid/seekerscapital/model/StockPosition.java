package com.example.haroonshahid.seekerscapital.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by haroonshahid on 31/1/2018.
 */

public class StockPosition {


    private Stock stock;

    @IntDef({TransactionType.LONG, TransactionType.SHORT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TransactionType {
        int LONG = 1;
        int SHORT = 0;
    }

    private double transactionPrice = 0.0;
    public double openingPrice = -1;
    public double closingPrice = -1;
    private double quantity = 0.0;
    private String transactionDate = "";

    public StockPosition(Stock stock, int transactionType, double transactionPrice, double quantity, String transactionDate) {
        this.stock = stock;
        this.transactionType = transactionType;
        this.transactionPrice = transactionPrice;
        this.quantity = quantity;
        this.transactionDate = transactionDate;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public double getTransactionPrice() {
        return transactionPrice;
    }

    public void setTransactionPrice(double transactionPrice) {
        this.transactionPrice = transactionPrice;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    @TransactionType
    public int transactionType;
    public int getType() {
        return transactionType;
    }

    public void setType(@TransactionType int type) {
        this.transactionType = type;
    }
}
