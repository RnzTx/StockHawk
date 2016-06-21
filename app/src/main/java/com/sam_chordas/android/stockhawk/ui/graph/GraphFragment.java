package com.sam_chordas.android.stockhawk.ui.graph;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.rest.Constants;
import com.sam_chordas.android.stockhawk.rest.Utils;
import com.sam_chordas.android.stockhawk.stock_history.StockDataTask;

import java.util.ArrayList;

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
	Context mContext = getContext();
	static final int GRAPH_COLOR = Color.rgb(33,150,243);
	public GraphFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_graph, container, false);
		String stock_name = getActivity().getIntent().getExtras().getString(Constants.KEY_STOCK_SYMBOL);
		mLineChart = (LineChart)rootView.findViewById(R.id.stock_line_chart);
		getActivity().setTitle(stock_name);

		YAxis yAxis = mLineChart.getAxisLeft();
		XAxis xAxis = mLineChart.getXAxis();
		// Graph
		// empty X-axis labels for graph
		ArrayList<String> xVals = new ArrayList<>();

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


		// create dummy data from label and dataset
		mLineData = new LineData(xVals,mLineDataSet);
		mLineChart.setData(mLineData);
		mLineChart.setDescription(" ");
		mLineDataSet.setLabel(getResources().getString(R.string.desc_graph));
		mLineChart.animateXY(2000,2000);
		mLineChart.invalidate();
		// get Url
		String url = Utils.buildStockHistoryDataUrl(stock_name);
		StockDataTask stockDataTask = new StockDataTask(url,mLineChart, mLineData,mLineDataSet);
		stockDataTask.execute();
		return rootView;
	}

}
