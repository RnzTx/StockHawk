package com.sam_chordas.android.stockhawk.rest;

import android.content.ContentProviderOperation;

import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sam_chordas on 10/8/15.
 */
public class Utils {

    public static final String QUERY = "query";
    public static final String COUNT = "count";
    public static final String RESULTS = "results";
    public static final String QUOTE = "quote";
    public static final String CHANGE = "Change";
    public static final String SYMBOL = "symbol";
    public static final String BID = "Bid";
    public static final String CHANGEIN_PERCENT = "ChangeinPercent";
    public static final String NAME = "Name";

    private static String LOG_TAG = Utils.class.getSimpleName();
    public static boolean showPercent = true;


    public static ArrayList quoteJsonToContentVals(String JSON){
        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>();
        JSONObject jsonObject = null;
        JSONArray resultsArray = null;
        try{
            jsonObject = new JSONObject(JSON);
            if (jsonObject != null && jsonObject.length() != 0){
                jsonObject = jsonObject.getJSONObject(QUERY);
                int count = Integer.parseInt(jsonObject.getString(COUNT));
                if (count == 1){
                    jsonObject = jsonObject.getJSONObject(RESULTS)
                            .getJSONObject(QUOTE);
                    /**
                     * If Stock name doesn't exists !
                     * it will return null
                     */
                    ContentProviderOperation operation = buildBatchOperation(jsonObject);
                    batchOperations.add(operation);
                } else{
                    resultsArray = jsonObject.getJSONObject(RESULTS).getJSONArray(QUOTE);

                    if (resultsArray != null && resultsArray.length() != 0){
                        for (int i = 0; i < resultsArray.length(); i++){
                            jsonObject = resultsArray.getJSONObject(i);
                            batchOperations.add(buildBatchOperation(jsonObject));
                        }
                    }
                }
            }
        } catch (JSONException e){
//            Log.e(LOG_TAG, "String to JSON failed: " + e);
            e.printStackTrace();
        }
        return batchOperations;
    }

    public static String truncateBidPrice(String bidPrice){
        bidPrice = String.format(Locale.ENGLISH,"%.2f", Float.parseFloat(bidPrice));
        return bidPrice;
    }

    public static String truncateChange(String change, boolean isPercentChange){
        String weight = change.substring(0,1);
        String ampersand = "";
        if (isPercentChange){
            ampersand = change.substring(change.length() - 1, change.length());
            change = change.substring(0, change.length() - 1);
        }
        change = change.substring(1, change.length());
        double round = (double) Math.round(Double.parseDouble(change) * 100) / 100;
        change = String.format(Locale.ENGLISH,"%.2f", round);
        StringBuffer changeBuffer = new StringBuffer(change);
        changeBuffer.insert(0, weight);
        changeBuffer.append(ampersand);
        change = changeBuffer.toString();
        return change;
    }

    public static ContentProviderOperation buildBatchOperation(JSONObject jsonObject){
        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
                QuoteProvider.Quotes.CONTENT_URI);
        try {

		String change = jsonObject.getString(CHANGE);
            builder.withValue(QuoteColumns.SYMBOL, jsonObject.getString(SYMBOL));
            builder.withValue(QuoteColumns.BIDPRICE, truncateBidPrice(jsonObject.getString(BID)));
            builder.withValue(QuoteColumns.PERCENT_CHANGE, truncateChange(
                    jsonObject.getString(CHANGEIN_PERCENT), true));
            builder.withValue(QuoteColumns.CHANGE, truncateChange(change, false));
            // get Company Name
            builder.withValue(QuoteColumns.NAME,jsonObject.getString(NAME));
            builder.withValue(QuoteColumns.ISCURRENT, 1);
            if (change.charAt(0) == '-'){
                builder.withValue(QuoteColumns.ISUP, 0);
            }else{
                builder.withValue(QuoteColumns.ISUP, 1);
            }

        } catch (JSONException e){
            e.printStackTrace();
        }
        return builder.build();
    }

    public static String buildStockHistoryDataUrl(String stock_symbol){
        String startDate = Utils.getStartDate();
        String endDate = Utils.getEndDate();
        try{
            String BASE_URL = "http://query.yahooapis.com/v1/public/yql?q=";
            String TABLE_QUERY = "select * from yahoo.finance.historicaldata where " +
                    "symbol = \""+stock_symbol+"\" and startDate = \""+startDate+"\" " +
                    "and endDate = \""+endDate+"\"";
            String FINAL_URL = BASE_URL + URLEncoder.encode(TABLE_QUERY, "UTF-8")
                    +"&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables."
                    + "org%2Falltableswithkeys&callback=";
            return FINAL_URL;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getEndDate(){
    //Today's date
        Calendar calendar = Calendar.getInstance();
        return Constants.SIMPLE_DATE_FORMAT.format(calendar.getTime());
    }

    public static String getStartDate(){
    //Previous Year date
        Calendar today = Calendar.getInstance();
        today.add(Calendar.MONTH,-12);
        return Constants.SIMPLE_DATE_FORMAT.format(today.getTime());
    }
}
