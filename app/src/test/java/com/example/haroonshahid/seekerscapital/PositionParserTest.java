package com.example.haroonshahid.seekerscapital;

import android.util.Log;

import com.example.haroonshahid.seekerscapital.model.Stock;
import com.example.haroonshahid.seekerscapital.model.StockPosition;

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


    @Before
    public void setUp() throws Exception {
        ShadowLog.stream = System.out;
//        SeekersApplication.instance(true);
    }

    @Test
    public void myTest() {
        String filename = "app/src/test/java/open_positionss.json";
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

        LazyObject lazyObject = new LazyObject(raw);
        LazyArray lazyArray = lazyObject.getJSONArray("positions");
        List<StockPosition> stockPositionList = new ArrayList<>();
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

        assertEquals(5, stockPositionList.size());

    }
}
