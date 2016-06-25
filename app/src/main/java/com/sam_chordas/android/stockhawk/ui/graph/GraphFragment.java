package com.sam_chordas.android.stockhawk.ui.graph;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.Constants;
import com.sam_chordas.android.stockhawk.stock_history.model.Quote;
import com.sam_chordas.android.stockhawk.stock_history.realm.RealmController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * A simple {@link Fragment} subclass.
 * BIG THANKS github.com/PhilJay for MPAndroidChart library
 * refer. https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/CubicLineChartActivity.java
 * TODO add MarkerView
 */
public class GraphFragment extends Fragment {
	private static final String LOG_TAG = GraphFragment.class.getSimpleName();
	LineChart mLineChart;
	LineDataSet mLineDataSet = new LineDataSet(new ArrayList<Entry>(),"Values");
	LineData mLineData;
	Realm mRealm;
	RealmController mRealmController;
	String mStockSymbol;
	Cursor mCursor;
	TextView tv_stock_history;
	TabLayout tab_time_span;
	private CustomMarkerView markerView;
	static final int GRAPH_COLOR = Color.rgb(69,39,160);
	public GraphFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRealmController = RealmController.with(this);
		mRealm = mRealmController.getRealm();
		mLineDataSet.clear();
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		Bundle arguments = getArguments();
		if (arguments!=null)
			mStockSymbol = arguments.getString(Constants.KEY_STOCK_SYMBOL);

		mCursor = getContext().getContentResolver().query(
				QuoteProvider.Quotes.withSymbol(mStockSymbol),
				null,
				QuoteColumns.ISCURRENT + " = ?",
				new String[]{"1"},
				null
		);
		String bid_price = "0";
		try{
			mCursor.moveToFirst();
			bid_price = "$"+mCursor.getString(mCursor.getColumnIndex(QuoteColumns.BIDPRICE));
		}catch (Exception e){
			e.printStackTrace();
		}

		View rootView = inflater.inflate(R.layout.fragment_graph, container, false);
		TextView tv_bid_price = (TextView)rootView.findViewById(R.id.tv_bid_price);
		mLineChart = (LineChart)rootView.findViewById(R.id.stock_line_chart);
		tv_stock_history = (TextView)rootView.findViewById(R.id.tv_bid_price_history);
		tab_time_span = (TabLayout)rootView.findViewById(R.id.tabbar_time_span);
		// set Current Stock Price
		tv_bid_price.setText(bid_price);

		// use data from realm database
		if (mRealmController.hasStockData(mStockSymbol)) {
			try {
				graphStyling();
				// display month of Data by default
				Calendar startDateMonth = Calendar.getInstance();
				startDateMonth.add(Calendar.MONTH,-1);
				populateGraph(startDateMonth.getTime());

				// add MarkerView to browse stock price in history
				markerView = new CustomMarkerView(getContext(),R.layout.marker_view_layout);
				mLineChart.setMarkerView(markerView);
				mLineChart.invalidate();
			}catch (Exception e){
				e.printStackTrace();
			}
		}

		tab_time_span.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				int position = tab.getPosition();
				int SPAN = Calendar.YEAR;
				int DURATION = -1;
				switch (position){
					case 0:
						SPAN = Calendar.MONTH;
						DURATION = -1;
						break;
					case 1:
						SPAN = Calendar.MONTH;
						DURATION = -3;
						break;
					case 2:
						SPAN = Calendar.MONTH;
						DURATION = -6;
						break;
					default:
						break;
				}
				Calendar startTime = Calendar.getInstance();
				startTime.add(SPAN,DURATION);
				populateGraph(startTime.getTime());
				tab.select();
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});
		return rootView;
	}
	private void populateGraph(Date startDate){
		RealmList<Quote> stockDataList = mRealmController.getStockData(mStockSymbol).getStockDataList();
		ArrayList<String> xVaList = new ArrayList<>();
		mLineDataSet.clear();
//		mLineData.clearValues();
		for (int i = 0; i < stockDataList.size(); i++) {
			Quote quote = stockDataList.get(i);
			// Date comparison for Graph sorting
			if (quote.getActualDate().after(startDate)){
				mLineDataSet.addEntry(
						// add Stock entry to yValues
						new Entry(Float.valueOf(quote.getClose()), i,quote)
				);
				// add date to xValues
				xVaList.add(quote.getDate());
			}
		}
		mLineData = new LineData(xVaList, mLineDataSet);
		mLineChart.setData(mLineData);
		mLineData.notifyDataChanged(); // let Data know its DataSet changed
		mLineChart.notifyDataSetChanged(); // let chart know its Data changed
		mLineChart.invalidate();
	}
	private void graphStyling(){
		YAxis yAxis = mLineChart.getAxisLeft();
		XAxis xAxis = mLineChart.getXAxis();

		// Style as Cubical Line Chart
		mLineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
		// disable Draw values from chart
		mLineDataSet.setDrawValues(false);
		// disable draw circles
		mLineDataSet.setDrawCircles(false);
		// fill chart with color
//		mLineDataSet.setDrawFilled(true);

		// data border color
		mLineDataSet.setColor(GRAPH_COLOR);
		// data color transparency level 0-255
		mLineDataSet.setFillAlpha(255);
		// data fill color
//		mLineDataSet.setFillColor(GRAPH_COLOR);

		// disable grid lines
		xAxis.setDrawGridLines(false);
		yAxis.setDrawGridLines(false);
		// remove right side markings
		mLineChart.getAxisRight().setEnabled(false);
		// remove x axis info
//		mLineChart.getXAxis().setDrawAxisLine(false);
		mLineChart.getXAxis().setEnabled(false);
		// remove y axis info
//		mLineChart.getAxisLeft().setDrawAxisLine(false);
		mLineChart.getAxisLeft().setEnabled(false);
		mLineChart.setDescription(" ");

		mLineChart.getLegend().setEnabled(false);
//		yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
		mLineChart.setViewPortOffsets(0,0,0,0);
		mLineChart.setPinchZoom(false);
		mLineChart.setDoubleTapToZoomEnabled(false);
		mLineChart.invalidate();// refresh chart

	}

	private class CustomMarkerView extends MarkerView{
		public CustomMarkerView(Context context, int layoutResource) {
			super(context, layoutResource);
		}

		@Override
		public void refreshContent(Entry e, Highlight highlight) {
			Quote quote = (Quote)e.getData();
			String price = String.format(Locale.ENGLISH,"\t\t$%.2f",Float.valueOf(quote.getClose()));
			SimpleDateFormat SimpleFormat = new SimpleDateFormat("dd MMM yyyy",Locale.ENGLISH);
			String date = SimpleFormat.format(quote.getActualDate());
			tv_stock_history.setText(date.concat(price));
		}

		@Override
		public int getXOffset(float xpos) {
			return 0;
		}

		@Override
		public int getYOffset(float ypos) {
			return 0;
		}
	}
}
