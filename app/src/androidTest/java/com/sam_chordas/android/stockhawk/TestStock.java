package com.sam_chordas.android.stockhawk;

import android.test.AndroidTestCase;

import com.sam_chordas.android.stockhawk.rest.Utils;

/**
 * Created by rnztx on 16/6/16.
 */
public class TestStock extends AndroidTestCase{
	public void testDate(){
		String startDate = Utils.getStartDate();
		String endDate = Utils.getEndDate();
		String today = "2016-06-17"; // this should be today's date in yyyy-MM-dd
		String lastMonth = "2016-05-17";
		assertEquals(today,endDate);
		assertEquals(lastMonth,startDate);
	}
}
