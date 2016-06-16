package com.sam_chordas.android.stockhawk.stock_history;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rnztx on 16/6/16.
 */
public class StockHistoryHandler {
	String mUrl ="https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22YHOO%22%20and%20startDate%20%3D%20%222015-09-11%22%20and%20endDate%20%3D%20%222016-06-10%22&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
	OkHttpClient mClient = new OkHttpClient();
	public String fetchStockHistory() {
		try {
			Request request = new Request.Builder()
					.url(mUrl)
					.build();

			Response response = mClient.newCall(request).execute();
			return response.body().toString();
		} catch (Exception e){
			e.printStackTrace();
		}
		return "NO Data";
	}

	public List<Quote> getStockQuotes(String jsonData){
		Gson gson = new GsonBuilder().create();
		HistoryData historyData = gson.fromJson(jsonData,HistoryData.class);
		return historyData.getQuotes();
	}
}
