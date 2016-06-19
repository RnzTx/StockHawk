package com.sam_chordas.android.stockhawk.widgets;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

/**
 * Created by rnztx on 19/6/16.
 */
public class StockWidgetRemoteViewService extends RemoteViewsService {
	private static final String LOG_TAG = StockWidgetRemoteViewService.class.getName();
	public StockWidgetRemoteViewService() {
	}

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return new WidgetItemRemoteView(this.getApplicationContext(),intent);
	}

	class WidgetItemRemoteView implements RemoteViewsService.RemoteViewsFactory{
		Context mContext;
		Cursor mCursor;
		Intent mIntent;

		public WidgetItemRemoteView(Context mContext, Intent mIntent) {
			this.mContext = mContext;
			this.mIntent = mIntent;
		}

		@Override
		public void onCreate() {
			// nothing To DO
		}

		@Override
		public int getCount() {
			return mCursor != null ? mCursor.getCount() : 0;
		}

		@Override
		public void onDataSetChanged() {
			Log.e(LOG_TAG,"onDataSetChanged updating Cursor");
			// refer http://stackoverflow.com/a/16076336
			if (mCursor!=null)
				mCursor.close();

			final long pId = Binder.clearCallingIdentity();

			mCursor = getContentResolver().query(
					QuoteProvider.Quotes.CONTENT_URI,
					null,
					QuoteColumns.ISCURRENT + " = ?",
					new String[]{"1"},
					null
			);

			Binder.restoreCallingIdentity(pId);
		}

		@Override
		public int getViewTypeCount() {
			return 1;
		}

		@Override
		public RemoteViews getViewAt(int position) {
			mCursor.moveToPosition(position);
			int priceChangeColorId;
			Log.e(LOG_TAG,"Cursor Count: "+getCount());
			// get Stock Symbol information
			String stockSymbol = mCursor.getString(mCursor.getColumnIndex("symbol"));
			String stockBidPrice = mCursor.getString(mCursor.getColumnIndex(QuoteColumns.BIDPRICE));
			String stockPriceChange = mCursor.getString(mCursor.getColumnIndex(QuoteColumns.CHANGE));
			int isUp = mCursor.getInt(mCursor.getColumnIndex(QuoteColumns.ISUP));

			// create Item in Widget ListView
			RemoteViews listItemRemoteView = new RemoteViews(mContext.getPackageName(), R.layout.list_item_quote);
			listItemRemoteView.setTextViewText(R.id.stock_symbol,stockSymbol);
			listItemRemoteView.setTextViewText(R.id.bid_price,stockBidPrice);
			listItemRemoteView.setTextViewText(R.id.change,stockPriceChange);

			// if stock price is Up then background of price Change is Green else, Red
			if (isUp==1)
				priceChangeColorId = R.drawable.percent_change_pill_green;
			else
				priceChangeColorId = R.drawable.percent_change_pill_red;

			listItemRemoteView.setInt(R.id.change,"setBackgroundResource",priceChangeColorId);
			return listItemRemoteView;
		}

		@Override
		public long getItemId(int position) {
			mCursor.moveToPosition(position);
			return mCursor.getLong(mCursor.getColumnIndex(QuoteColumns._ID));
		}

		@Override
		public void onDestroy() {
			if (mCursor!=null)
				mCursor.close();
		}

		@Override
		public RemoteViews getLoadingView() {
			return null;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}
	}
}
