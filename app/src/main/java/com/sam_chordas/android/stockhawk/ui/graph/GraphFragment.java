package com.sam_chordas.android.stockhawk.ui.graph;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.rest.Constants;
import com.sam_chordas.android.stockhawk.rest.Utils;
import com.sam_chordas.android.stockhawk.stock_history.model.Quote;
import com.sam_chordas.android.stockhawk.stock_history.realm.RealmController;
import com.sam_chordas.android.stockhawk.stock_history.realm.StockData;

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
	RadioGroup radioGroup;
	RadioButton radioButtonMonth;
	static final int GRAPH_COLOR = Color.rgb(33,150,243);
	public GraphFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRealmController = RealmController.with(this);
		mRealm = mRealmController.getRealm();
		mStockSymbol = getActivity().getIntent().getExtras().getString(Constants.KEY_STOCK_SYMBOL);
		mLineDataSet.clear();
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_graph, container, false);
		mLineChart = (LineChart)rootView.findViewById(R.id.stock_line_chart);
		radioGroup = (RadioGroup)rootView.findViewById(R.id.radio_group_graph_time_span);
		radioButtonMonth = (RadioButton)rootView.findViewById(R.id.radio_button_1_month);
		getActivity().setTitle(mStockSymbol);

		graphStyling();

		// use data from realm database
		if (mRealmController.hasStockData(mStockSymbol)) {
			try {
				Calendar startDateMonth = Calendar.getInstance();
				startDateMonth.add(Calendar.MONTH,-1);
				populateGraph(startDateMonth.getTime());
				radioGroup.check(R.id.radio_button_1_month);

				mLineChart.setDescription(" ");
				mLineDataSet.setLabel(getResources().getString(R.string.desc_graph));
				mLineChart.animateX(1000);
				mLineChart.invalidate();// refresh chart
			}catch (Exception e){
				e.printStackTrace();
			}
		}

		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int SPAN = Calendar.YEAR;
				int DURATION = -1;
				switch (checkedId){
					case R.id.radio_button_1_week:
						SPAN = Calendar.WEEK_OF_MONTH;
						DURATION = -1;
						break;
					case R.id.radio_button_1_month:
						SPAN = Calendar.MONTH;
						DURATION = -1;
						break;
					case R.id.radio_button_3_months:
						SPAN = Calendar.MONTH;
						DURATION = -3;
						break;
					case R.id.radio_button_6_months:
						SPAN = Calendar.MONTH;
						DURATION = -6;
						break;
					default:
						// default is 1 year
				}
				Calendar startTime = Calendar.getInstance();
				startTime.add(SPAN,DURATION);
				populateGraph(startTime.getTime());
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
						new Entry(Float.valueOf(quote.getClose()), i)
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
		mLineDataSet.setDrawFilled(true);

		// data border color
		mLineDataSet.setColor(GRAPH_COLOR);
		// data color transparency level 0-255
		mLineDataSet.setFillAlpha(255);
		// data fill color
		mLineDataSet.setFillColor(GRAPH_COLOR);

		// disable xAxis grid lines
		xAxis.setDrawGridLines(false);
		// remove right side markings
		mLineChart.getAxisRight().setEnabled(false);
		// remove top markings
		mLineChart.getXAxis().setDrawAxisLine(false);
		// remove y axis lines
		mLineChart.getAxisLeft().setDrawAxisLine(false);

		yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
	}

}
