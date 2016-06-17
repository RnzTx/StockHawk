package com.sam_chordas.android.stockhawk.ui.graph;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.rest.Constants;
import com.sam_chordas.android.stockhawk.rest.Utils;
import com.sam_chordas.android.stockhawk.stock_history.StockDataTask;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends Fragment {
	private static final String LOG_TAG = GraphFragment.class.getSimpleName();
	LineChart mLineChart;
	LineDataSet mLineDataSet = new LineDataSet(new ArrayList<Entry>(),"Values");
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

		// Graph
		// empty X-axis labels for graph
		ArrayList<String> xVals = new ArrayList<>();

		// create dummy data from label and dataset
		LineData lineData = new LineData(xVals,mLineDataSet);
		mLineChart.setData(lineData);
		mLineChart.setDescription(Utils.getStartDate()+" To "+Utils.getEndDate());
		mLineDataSet.setLabel("Stock Price");
		// get Url
		String url = Utils.buildStockHistoryDataUrl(stock_name);
		StockDataTask stockDataTask = new StockDataTask(url,mLineChart,lineData,mLineDataSet);
		stockDataTask.execute();
		return rootView;
	}

}
