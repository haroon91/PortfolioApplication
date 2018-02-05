package com.example.haroonshahid.seekerscapital;

import android.util.Log;

import com.example.haroonshahid.seekerscapital.model.PriceResult;
import com.example.haroonshahid.seekerscapital.model.StockPosition;
import com.example.haroonshahid.seekerscapital.parser.PositionsParser;
import com.example.haroonshahid.seekerscapital.parser.PriceParser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by haroonshahid on 5/2/2018.
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "app/src/main/AndroidManifest.xml")
public class PriceParserTest {

    @Test
    public void testPriceParsing() {
        String filename = "app/src/test/java/price_info.json";
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

        PriceParser priceParser = new PriceParser();
        try {
            PriceResult priceResult = priceParser.parse(raw);

            assertTrue(priceResult != null);
            assertEquals("FOX", priceResult.symbol);
            assertEquals("37.83", priceResult.openingPrice);
            assertEquals("36.49", priceResult.closingPrice);

        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void testParseWhenRawStringIsEmpty() {
        PriceParser priceParser = new PriceParser();
        String raw = "";
        try {
            PriceResult result = priceParser.parse(raw);
            assertEquals(null, result);
        } catch (Exception e) {
            assertTrue(false);
        }

    }


}
