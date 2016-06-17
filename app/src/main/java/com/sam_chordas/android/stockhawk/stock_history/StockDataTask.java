package com.sam_chordas.android.stockhawk.stock_history;

import android.os.AsyncTask;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rnztx on 16/6/16.
 */
public class StockDataTask extends AsyncTask<Void,Void,List<Quote>> {
	private static final String LOG_TAG = StockDataTask.class.getSimpleName();
	String url;
	LineChart lineChart;

	public StockDataTask(String url, LineChart lineChart) {
		this.url = url;
		this.lineChart = lineChart;
	}

	@Override
	protected List<Quote> doInBackground(Void... params) {
		try{
			StockHistoryHandler dataHandler = new StockHistoryHandler();
			List<Quote> stockQuotes = dataHandler.getStockQuotes(this.url);
			return stockQuotes;
		}catch (Exception e){
			Log.e(LOG_TAG,e.toString());
		}
		return null;
	}

	@Override
	protected void onPostExecute(List<Quote> quotes) {
		super.onPostExecute(quotes);
		if (quotes!=null){
			// create entries
			ArrayList<Entry> dummyEntries = new ArrayList<>();
			dummyEntries.add(new Entry(2f,0));
			dummyEntries.add(new Entry(3f,1));
			dummyEntries.add(new Entry(4f,2));
			dummyEntries.add(new Entry(5f,3));
			dummyEntries.add(new Entry(9f,4));
			dummyEntries.add(new Entry(4.2f,5));

			// create dataset from entries
			LineDataSet lineDataSet = new LineDataSet(dummyEntries,"Stock Values");

			// X-axis labels for graph
			ArrayList<String> labels = new ArrayList<>();
			labels.add("one"); labels.add("two"); labels.add("three"); labels.add("four"); labels.add("five");
			labels.add("six");

			// create data from label and dataset
			LineData lineData = new LineData(labels,lineDataSet);

			// Add data to LineChart
			this.lineChart.setData(lineData);
			Log.e(LOG_TAG,"Data Size: "+quotes.size());
		}
	}
}
