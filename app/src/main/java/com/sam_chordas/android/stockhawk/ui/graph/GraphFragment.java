package com.sam_chordas.android.stockhawk.ui.graph;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.rest.Constants;
import com.sam_chordas.android.stockhawk.stock_history.StockHistoryHandler;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends Fragment {
	private static final String LOG_TAG = GraphFragment.class.getSimpleName();

	public GraphFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_graph, container, false);
		String stock_name = getActivity().getIntent().getExtras().getString(Constants.KEY_STOCK_SYMBOL);
		Log.e(LOG_TAG,stock_name);
		LineChart lineChart = (LineChart)rootView.findViewById(R.id.stock_line_chart);

		// create entries
		ArrayList<Entry> dummyEntries = new ArrayList<>();
		dummyEntries.add(new Entry(2f,0));
		dummyEntries.add(new Entry(3f,1));
		dummyEntries.add(new Entry(4f,2));
		dummyEntries.add(new Entry(5f,3));
		dummyEntries.add(new Entry(9f,4));
		dummyEntries.add(new Entry(4.2f,5));

		// create dataset from entries
		LineDataSet lineDataSet = new LineDataSet(dummyEntries,"Label Dataset");

		// X-axis labels for graph
		ArrayList<String> labels = new ArrayList<>();
		labels.add("one"); labels.add("two"); labels.add("three"); labels.add("four"); labels.add("five");
		labels.add("six");

		// create data from label and dataset
		LineData lineData = new LineData(labels,lineDataSet);

		// Add data to LineChart
		lineChart.setData(lineData);

		String raw_data = new StockHistoryHandler().fetchStockHistory();
		Log.e(LOG_TAG,raw_data);

		return rootView;
	}

}
