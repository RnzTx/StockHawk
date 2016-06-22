package com.sam_chordas.android.stockhawk.stock_history.realm;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import io.realm.Realm;

/**
 * Created by rnztx on 21/6/16.
 * Thanks to Ravi Tamada for Awesome Blog about Realm database integration
 * http://www.androidhive.info/2016/05/android-working-with-realm-database-replacing-sqlite-core-data/
 */
public class RealmController {
	private static RealmController instance;
	private final Realm realm;
	public RealmController(Application application){
		realm = Realm.getDefaultInstance();
		realm.setAutoRefresh(true);
	}


	public static RealmController with(Fragment fragment) {

		if (instance == null) {
			instance = new RealmController(fragment.getActivity().getApplication());
		}
		return instance;
	}

	public static RealmController with(Activity activity) {

		if (instance == null) {
			instance = new RealmController(activity.getApplication());
		}
		return instance;
	}

	public static RealmController with(Application application) {

		if (instance == null) {
			instance = new RealmController(application);
		}
		return instance;
	}

	public static RealmController getInstance() {
		return instance;
	}

	public Realm getRealm() {
		return realm;
	}
	public StockData getStockData(String stock_symbol){
		return realm.where(StockData.class).equalTo("stock_symbol",stock_symbol).findFirst();
	}

	// check if data present
	public boolean hasStockData(String stock_symbol){
		return getStockData(stock_symbol)!=null;
	}

	public void deleteStockGraphData(String stock_symbol){
		realm.beginTransaction();
		if (hasStockData(stock_symbol)){
			StockData stockData = getStockData(stock_symbol);
			stockData.deleteFromRealm();
		}
		realm.commitTransaction();
	}
}
