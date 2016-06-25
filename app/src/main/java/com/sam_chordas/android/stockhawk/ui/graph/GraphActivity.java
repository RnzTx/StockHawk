package com.sam_chordas.android.stockhawk.ui.graph;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.h6ah4i.android.tablayouthelper.TabLayoutHelper;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.Constants;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {
	private static final String LOG_TAG = GraphActivity.class.getSimpleName();
	PageAdapter mPageAdapter;
	ViewPager mViewPager;
	TabLayout mTabLayout;
	TabLayoutHelper mTabLayoutHelper;
	Context mContext =this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_graph);
		getSupportActionBar().setElevation(0);
		mPageAdapter = new PageAdapter(getSupportFragmentManager());

		mViewPager = (ViewPager)findViewById(R.id.view_pager);
		mViewPager.setAdapter(mPageAdapter);

		mTabLayout = (TabLayout)findViewById(R.id.tabbar_stock_symbol);
		mTabLayout.setupWithViewPager(mViewPager);

		// Auto Adjust Tab mode.. Scrollable | centered
		mTabLayoutHelper = new TabLayoutHelper(mTabLayout,mViewPager);
		mTabLayoutHelper.setAutoAdjustTabModeEnabled(true);

		// select tab on list item selection demand
		int tab_position = getIntent().getIntExtra(Constants.KEY_TAB_POSITION,0);
		TabLayout.Tab initialTab = mTabLayout.getTabAt(tab_position);
		if (initialTab!=null) {
			setActionbarTitle(initialTab.getText().toString());
			initialTab.select();
		}
		// handle selection
		mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				setActionbarTitle(tab.getText().toString());
				tab.select();
				mViewPager.setCurrentItem(tab.getPosition(),true);
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});
	}
	public class PageAdapter extends FragmentPagerAdapter{
		Cursor mCursor;
		ArrayList<String> symbolList = new ArrayList<>();
		public PageAdapter(FragmentManager fm){
			super(fm);
			try{
				// get all symbol names
				mCursor = mContext.getContentResolver().query(
						QuoteProvider.Quotes.CONTENT_URI,
						new String[]{QuoteColumns.SYMBOL},
						QuoteColumns.ISCURRENT + " = ?",
						new String[]{"1"},
						null
				);
				// create distinct symbol list
				for (int i=0;i<mCursor.getCount();i++){
					mCursor.moveToPosition(i);
					String name = mCursor.getString(mCursor.getColumnIndex(QuoteColumns.SYMBOL));
					if (!symbolList.contains(name)){
						symbolList.add(name);
					}
				}
				mCursor.close();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		@Override
		public Fragment getItem(int position) {
			Bundle argument = new Bundle();
			argument.putString(Constants.KEY_STOCK_SYMBOL,symbolList.get(position));
			GraphFragment fragment = new GraphFragment();
			fragment.setArguments(argument);
			return fragment;
		}

		@Override
		public int getCount() {
			return symbolList.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return symbolList.get(position);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	public void setActionbarTitle(String symbol){
		Cursor cursor = mContext.getContentResolver().query(
				QuoteProvider.Quotes.withSymbol(symbol),
				new String[]{QuoteColumns.NAME},
				QuoteColumns.ISCURRENT + " = ?",
				new String[]{"1"},
				null
		);
		if (cursor!=null && cursor.moveToFirst()){
			String company_name = cursor.getString(cursor.getColumnIndex(QuoteColumns.NAME));
			getSupportActionBar().setTitle(company_name);
			cursor.close();
		}

	}
}
