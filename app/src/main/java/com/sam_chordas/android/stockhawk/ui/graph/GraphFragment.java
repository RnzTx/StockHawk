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
import com.sam_chordas.android.stockhawk.rest.Utils;
import com.sam_chordas.android.stockhawk.stock_history.StockDataTask;
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
		LineChart lineChart = (LineChart)rootView.findViewById(R.id.stock_line_chart);

		// get Url
		String url = Utils.buldStockHistoryDataUrl(stock_name);
		StockDataTask stockDataTask = new StockDataTask(url,lineChart);
		stockDataTask.execute();
		return rootView;
	}

}
