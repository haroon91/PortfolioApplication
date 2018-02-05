package com.example.haroonshahid.seekerscapital;

import android.util.Log;

import com.example.haroonshahid.seekerscapital.model.Stock;
import com.example.haroonshahid.seekerscapital.model.StockPosition;
import com.example.haroonshahid.seekerscapital.parser.PositionsParser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import me.doubledutch.lazyjson.LazyArray;
import me.doubledutch.lazyjson.LazyObject;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by haroonshahid on 1/2/2018.
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "app/src/main/AndroidManifest.xml")
public class PositionParserTest {

    @Test
    public void testPositionParsing() {
        String filename = "app/src/test/java/open_positions.json";
        File file = new File(filename);

        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String raw = CommonUtils.convert(is);
        Log.d("raw: ", raw);
        try {
            is.close();
        } catch (IOException e) {
            assertTrue(false);
        }

        PositionsParser positionsParser = new PositionsParser();
        List<StockPosition> stockPositionList = null;
        try {
            stockPositionList = positionsParser.parse(raw);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertEquals("Apple Inc.", stockPositionList.get(0).getStock().name);
        assertEquals("NASDAQ", stockPositionList.get(0).getStock().exchange);
        assertEquals("AAPL", stockPositionList.get(0).getStock().symbol);
        assertEquals(1, stockPositionList.get(0).transactionType);
        assertEquals(174.22, stockPositionList.get(0).getTransactionPrice());
        assertEquals(100.0, stockPositionList.get(0).getQuantity());
        assertEquals("2018-01-24", stockPositionList.get(0).getTransactionDate());

        assertEquals("Microsoft Corporation", stockPositionList.get(1).getStock().name);
        assertEquals("NASDAQ", stockPositionList.get(1).getStock().exchange);
        assertEquals("MSFT", stockPositionList.get(1).getStock().symbol);
        assertEquals(1, stockPositionList.get(1).transactionType);
        assertEquals(91.82, stockPositionList.get(1).getTransactionPrice());
        assertEquals(100.0, stockPositionList.get(1).getQuantity());
        assertEquals("2018-01-24", stockPositionList.get(1).getTransactionDate());

        assertEquals(2, stockPositionList.size());
    }

    @Test
    public void testParseWhenRawStringIsEmpty() {
        PositionsParser positionsParser = new PositionsParser();
        String raw = "";
        try {
            List<StockPosition> stockPositionList = positionsParser.parse(raw);
            assertEquals(null, stockPositionList);
        } catch (Exception e) {
            assertTrue(false);
        }

    }
}
