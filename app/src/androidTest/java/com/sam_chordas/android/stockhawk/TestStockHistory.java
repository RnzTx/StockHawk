package com.sam_chordas.android.stockhawk;

import android.test.AndroidTestCase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sam_chordas.android.stockhawk.stock_history.HistoryData;
import com.sam_chordas.android.stockhawk.stock_history.Quote;
import com.sam_chordas.android.stockhawk.stock_history.StockHistoryHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rnztx on 16/6/16.
 */
public class TestStockHistory extends AndroidTestCase{

	public void testClassModel(){
		StockHistoryHandler dataHandler = new StockHistoryHandler();
		List<Quote> stockQuotes = dataHandler.getStockQuotes("Raw Json Data String");
		String message = "Data Count: "+stockQuotes.size();
		assertSame(message,10,stockQuotes.size());
	}
}
