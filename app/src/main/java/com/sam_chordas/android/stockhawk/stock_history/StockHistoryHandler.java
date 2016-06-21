package com.sam_chordas.android.stockhawk.stock_history;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sam_chordas.android.stockhawk.stock_history.model.HistoryData;
import com.sam_chordas.android.stockhawk.stock_history.model.Quote;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.List;

/**
 * Created by rnztx on 16/6/16.
 */
public class StockHistoryHandler {

	OkHttpClient mClient = new OkHttpClient();
	public String fetchStockHistory(String url) {
		try {
			Request request = new Request.Builder()
					.url(url)
					.build();

			Response response = mClient.newCall(request).execute();
			return response.body().string();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public List<Quote> getStockQuotes(String url){
		String jsonData = fetchStockHistory(url);
		Gson gson = new GsonBuilder().create();
		HistoryData historyData = gson.fromJson(jsonData,HistoryData.class);
		return historyData.getQuotes();
	}

}
