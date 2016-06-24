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
		mPageAdapter = new PageAdapter(getSupportFragmentManager());

		mViewPager = (ViewPager)findViewById(R.id.view_pager);
		mViewPager.setAdapter(mPageAdapter);

		mTabLayout = (TabLayout)findViewById(R.id.tabbar_stock_symbol);
		mTabLayout.setupWithViewPager(mViewPager);

		// Auto Adjust Tab mode.. Scrollable | centered
		mTabLayoutHelper = new TabLayoutHelper(mTabLayout,mViewPager);
		mTabLayoutHelper.setAutoAdjustTabModeEnabled(true);
		int tab_position = getIntent().getIntExtra(Constants.KEY_TAB_POSITION,0);
		mTabLayout.getTabAt(tab_position).select();

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
}
