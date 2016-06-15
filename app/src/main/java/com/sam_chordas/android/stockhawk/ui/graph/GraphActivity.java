package com.sam_chordas.android.stockhawk.ui.graph;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.rest.Constants;

public class GraphActivity extends AppCompatActivity {
	private static final String LOG_TAG = GraphActivity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph);
		if(savedInstanceState==null){
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.container_graph_activity,new GraphFragment())
					.commit();
		}


	}
}
