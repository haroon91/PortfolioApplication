package com.example.haroonshahid.seekerscapital;

import android.graphics.Color;

/**
 * Created by haroonshahid on 2/2/2018.
 */

public class Constants {

    public static final String API_KEY = "PDzjB69Z-_kynWh3Rhi6";

    //urls
    public static final String NASDAQ_CHART_URL =
            "https://www.tradingview.com/widgetembed/?symbol=NASDAQ%3A!SYMBOL_PLACEHOLDER!&" +
                    "interval=D&hidetoptoolbar=1&hidesidetoolbar=1&symboledit=0&saveimage=0&toolbarbg=f1f3f6" +
                    "&studies=%5B%5D&hideideas=1&theme=Light&style=3&timezone=Etc%2FUTC&studies_overrides=%7B%7D&overrides=%7B%7D" +
                    "&enabled_features=%5B%5D&disabled_features=%5B%5D&locale=en&utm_source=www.tradingview.com&utm_medium=widget" +
                    "&utm_campaign=chart&utm_term=NASDAQ%3A!SYMBOL_PLACEHOLDER!";

    public static final String PRICE_URL = "https://www.quandl.com/api/v3/datatables/WIKI/PRICES?date=!CURRENT_DATE_PLACEHOLDER!" +
            "&ticker=!SYMBOL_PLACEHOLDER!&api_key=!API_KEY!";

    public static final String FEEDS_URL = "https://feeds.finance.yahoo.com/rss/2.0/headline?s=!SYMBOL_PLACEHOLDER!&region=US&lang=en-US";

    public static final String SYMBOL_PLACEHOLDER = "!SYMBOL_PLACEHOLDER!";
    public static final String CURRENT_DATE_PLACEHOLDER = "!CURRENT_DATE_PLACEHOLDER!";
    public static final String API_KEY_PLACEHOLDER = "!API_KEY!";

    public static final String PRICE_RESPONSE_STRING = "priceResponseString";

    //intents
    public static final String INTENT_ACTION_POSITIONS_LIST_BROADCAST = "intentActionPositionsListBroadcast";
    public static final String INTENT_ACTION_PRICE_UPDATE = "intentActionPriceUpdate";


    //colors
    public static final int RED_COLOR = Color.rgb(208, 2, 27);
    public static final int GREEN_COLOR = Color.rgb(106, 189, 15);
}
