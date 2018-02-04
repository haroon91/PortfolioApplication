package com.example.haroonshahid.seekerscapital;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.haroonshahid.seekerscapital.model.StockPosition;

import java.io.Closeable;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by haroonshahid on 1/2/2018.
 */

public class CommonUtils {
    public static void close(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (Exception ignore) {
        }
    }

    public static String convert(InputStream is) {
        // regex \A matches the beginning of input. This tells Scanner to
        // tokenize the entire stream.
        return new java.util.Scanner(is).useDelimiter("\\A").next();
    }

    public static boolean isConnectedToNetwork() {
        return isConnectedToWifi() || isConnectedToEthernet() || isConnectedToMobile();
    }

    private static boolean isConnectedTo(int type) {
        ConnectivityManager cm = (ConnectivityManager) SeekersApplication.instance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getNetworkInfo(type);
        return info != null && info.isConnected();
    }

    // For emulator
    public static boolean isConnectedToMobile() {
        return isConnectedTo(ConnectivityManager.TYPE_MOBILE);
    }

    public static boolean isConnectedToWifi() {
        return isConnectedTo(ConnectivityManager.TYPE_WIFI);
    }

    public static boolean isConnectedToEthernet() {
        return isConnectedTo(ConnectivityManager.TYPE_ETHERNET);
    }

    static boolean isHoliday(Calendar cal) {
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        if (month == 12 && dayOfMonth == 25) {
            return true;
        } else if (month == 1 && dayOfMonth == 1){
            return true;
        }

        // more checks

        return false;
    }

    public static String buildStockUrl(StockPosition currentStockPosition) {

        String url = Constants.PRICE_URL;
        url = url.replace(Constants.SYMBOL_PLACEHOLDER, currentStockPosition.getStock().symbol);
        url = url.replace(Constants.API_KEY_PLACEHOLDER, Constants.API_KEY);
        url = url.replace(Constants.CURRENT_DATE_PLACEHOLDER, lastBusinessDay());

        return url;
    }

    static String lastBusinessDay() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        int dayOfWeek;
        do {
            cal.add(Calendar.DAY_OF_MONTH, -1);
            dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        } while (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY || isHoliday(cal));

        Date lastBusinessDate = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        return dateFormat.format(lastBusinessDate);
    }

}

