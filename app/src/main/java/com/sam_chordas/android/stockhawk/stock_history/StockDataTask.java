package com.sam_chordas.android.stockhawk.stock_history;

import android.os.AsyncTask;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rnztx on 16/6/16.
 */
public class StockDataTask extends AsyncTask<Void,Void,List<Quote>> {
	private static final String LOG_TAG = StockDataTask.class.getSimpleName();
	String url;
	LineChart mLineChart;
	LineData mLineData;
	LineDataSet mLineDataSet;

	public StockDataTask(String url, LineChart mLineChart, LineData mLineData, LineDataSet mLineDataSet) {
		this.url = url;
		this.mLineChart = mLineChart;
		this.mLineData = mLineData;
		this.mLineDataSet = mLineDataSet;
	}

	@Override
	protected List<Quote> doInBackground(Void... params) {
		try{
			// get Stock Data
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
			ArrayList<String> xVaList = new ArrayList<>();
			for (int i=0;i<quotes.size();i++){
				Quote quote = quotes.get(i);
				mLineDataSet.addEntry(
						// add Stock entry to yValues
						new Entry(Float.valueOf(quote.getClose()),i)
				);
				// add date to xValues
				xVaList.add(quote.getDate());
			}
			mLineData.setXVals(xVaList); // Update xValues - date
			mLineData.notifyDataChanged(); // let Data know its DataSet changed
			mLineChart.notifyDataSetChanged(); // let chart know its Data changed
			mLineChart.invalidate();// refresh chart
		}
	}
}
