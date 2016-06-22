package com.sam_chordas.android.stockhawk.stock_history.realm;

import com.sam_chordas.android.stockhawk.stock_history.model.Quote;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rnztx on 21/6/16.
 */
public class StockData extends RealmObject {
	@PrimaryKey
	private String stock_symbol;
	private RealmList<Quote> stockDataList = new RealmList<>();

	public StockData(String stock_symbol, List<Quote> stockDataList) {
		this.stock_symbol = stock_symbol;
		for (Quote quote: stockDataList )
			this.stockDataList.add(quote);
	}

	public StockData() {
	}
	public int size(){
		return stockDataList.size();
	}

	public RealmList<Quote> getStockDataList() {
		return stockDataList;
	}
}
