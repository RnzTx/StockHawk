package com.sam_chordas.android.stockhawk.stock_history;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.sam_chordas.android.stockhawk.rest.Utils;
import com.sam_chordas.android.stockhawk.stock_history.model.Quote;
import com.sam_chordas.android.stockhawk.stock_history.realm.RealmController;
import com.sam_chordas.android.stockhawk.stock_history.realm.StockData;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by rnztx on 16/6/16.
 */
public class StockDataTask extends AsyncTask<Void,Void,List<Quote>> {
	private static final String LOG_TAG = StockDataTask.class.getSimpleName();
	String url;
	LineChart mLineChart;
	LineData mLineData;
	LineDataSet mLineDataSet;
	Realm mRealm;
	String mStockName;
	public StockDataTask(String stock_name, LineChart mLineChart, LineData mLineData, LineDataSet mLineDataSet
			, Fragment fragment) {
		this.url = Utils.buildStockHistoryDataUrl(stock_name);
		this.mStockName = stock_name;
		this.mLineChart = mLineChart;
		this.mLineData = mLineData;
		this.mLineDataSet = mLineDataSet;
		mRealm = RealmController.with(fragment).getRealm();
	}

	@Override
	protected List<Quote> doInBackground(Void... params) {
		// get Stock Data
		try{
			StockHistoryHandler dataHandler = new StockHistoryHandler();
			List<Quote> stockQuotes = dataHandler.getStockQuotes(this.url);
			Log.e(LOG_TAG,"Size: "+stockQuotes.size());
			return stockQuotes;
		}catch (Exception e){
			Log.e(LOG_TAG,e.toString());
		}
		return null;
	}

	@Override
	protected void onPostExecute(List<Quote> quotes) {
		super.onPostExecute(quotes);
		if (!quotes.isEmpty()){
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

//			// persist data with Realm
			StockData stockData = new StockData(mStockName,quotes);
			mRealm.beginTransaction();
			mRealm.copyToRealmOrUpdate(stockData);
			mRealm.commitTransaction();
		}
	}
}
