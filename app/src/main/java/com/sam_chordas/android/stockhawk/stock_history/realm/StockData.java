package com.sam_chordas.android.stockhawk.stock_history.realm;

import com.sam_chordas.android.stockhawk.rest.Constants;
import com.sam_chordas.android.stockhawk.stock_history.model.Quote;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * This class will be stored in Realm Database
 */
public class StockData extends RealmObject {
	private static final String LOG_TAG = StockData.class.getSimpleName();
	@PrimaryKey
	private String stock_symbol;
	private RealmList<Quote> stockDataList = new RealmList<>();

	public StockData(String stock_symbol, List<Quote> quoteList) {
		try {
			this.stock_symbol = stock_symbol;
			SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_TEMPLATE, Locale.ENGLISH);
			for (Quote quote: quoteList ) {
				// set Actual Date for Graph sorting
				Date date = dateFormat.parse(quote.getDate());
				quote.setActualDate(date);
				this.stockDataList.add(quote);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
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
