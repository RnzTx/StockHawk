package com.sam_chordas.android.stockhawk.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;

/**
 * Created by rnztx on 18/6/16.
 */
public class StockWidgetProvider extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		int widgetIcon = R.drawable.ic_sample;
		String stockDescription = "some Description";
		String stockName = "FIT";

		// update all StockWidgets
		for (int widgetId: appWidgetIds){
			int layoutId = R.layout.widget_small_sample;
			RemoteViews widgetView = new RemoteViews(context.getPackageName(),layoutId);

			// add Data to Remote View
			widgetView.setImageViewResource(R.id.widget_small_icon,widgetIcon);
			widgetView.setTextViewText(R.id.widget_small_stock_name,stockName);

			// Create Widget to launch StockHawk
			Intent intent = new Intent(context, MyStocksActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
			widgetView.setOnClickPendingIntent(R.id.widget_small,pendingIntent);

			// Tell AppWidgetManager to perform Update!
			appWidgetManager.updateAppWidget(widgetId,widgetView);
		}
	}
}
