package com.sam_chordas.android.stockhawk.stock_history;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rnztx on 16/6/16.
 */
public class HistoryData {
	// see Json Data for YQL stock historical data
	Query query;
	public List<Quote> getQuotes(){
		return this.query.results.quote;
	}

	class Query{
		public Results results;
	}
	class Results{
		public List<Quote>	quote = new ArrayList<>();
	}

	public Query getQuery() {
		return query;
	}
}
