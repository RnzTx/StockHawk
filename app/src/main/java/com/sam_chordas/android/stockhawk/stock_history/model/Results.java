package com.sam_chordas.android.stockhawk.stock_history.model;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;

/**
 * Created by rnztx on 21/6/16.
 */
class Results{
	public List<Quote> quote = new ArrayList<>();
	public Results() {
	}
}
