package com.sam_chordas.android.stockhawk.stock_history.model;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rnztx on 16/6/16.
 */
public class HistoryData{
	// see Json Data of YQL stock historical data

	public HistoryData() {
	}

	Query query;
	public List<Quote> getQuotes(){
		return this.query.results.quote;
	}

}
