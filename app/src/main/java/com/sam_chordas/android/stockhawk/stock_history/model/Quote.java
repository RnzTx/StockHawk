package com.sam_chordas.android.stockhawk.stock_history.model;

import io.realm.RealmObject;

/**
 * Created by rnztx on 16/6/16.
 */
public class Quote{
	public Quote() {
	}

	public String Symbol;
	public String Date;
	public String Close;

	public String getSymbol() {
		return Symbol;
	}

	public String getDate() {
		return Date;
	}

	public String getClose() {
		return Close;
	}
}
