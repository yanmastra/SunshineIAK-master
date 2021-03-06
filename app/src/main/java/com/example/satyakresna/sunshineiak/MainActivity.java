package com.example.satyakresna.sunshineiak;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.satyakresna.sunshineiak.adapter.ForecastAdapter;
import com.example.satyakresna.sunshineiak.database.ForecastDBHelper;
import com.example.satyakresna.sunshineiak.model.DailyForecast;
import com.example.satyakresna.sunshineiak.model.WeatherItem;
import com.example.satyakresna.sunshineiak.utilities.Constant;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
    implements ForecastAdapter.ItemClickListener{
    public static final String TAG = MainActivity.class.getSimpleName();

    private List<WeatherItem> weatherItemList = new ArrayList<>();
    @BindView(R.id.rv_forecast)
    RecyclerView mRecyclerView;
    private ForecastAdapter mAdapter;
    private Gson gson = new Gson();
    private DividerItemDecoration mDividerItemDecoration;
    @BindView(R.id.line_netowrk_retry) LinearLayout mLinearLayoutRetry;
    @BindView(R.id.tv_error_message) TextView mDisplayErrorMessage;

    private ForecastDBHelper dbHelper;
    private static final String cityTarget = " Badung";
    private DailyForecast dailyForecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mAdapter = new ForecastAdapter(weatherItemList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(layoutManager);
        mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        if(isNetworkConnected() || isWifiConnected()){
            getDatFromAPI("-8.581930", "115.177059", "10", "metric");
        }else{
            mRecyclerView.setVisibility(View.INVISIBLE);
            mLinearLayoutRetry.setVisibility(View.VISIBLE);
        }

        ActionBar toolbar = getSupportActionBar();
        if(toolbar != null){
            toolbar.setElevation(0);
        }
        dbHelper = new ForecastDBHelper(this);
    }

    private void getDatFromAPI(String lat, String lon, String cnt, String units) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = Constant.URL_API + Constant.PARAM_DAILY +
                Constant.PARAM_LAT + lat + "&" + Constant.PARAM_LON + lon + "&" +
                Constant.PARAM_CNT + cnt + "&" + Constant.PARAM_UNIT + units + "&" +
                Constant.PARAM_API_KEY + Constant.API_KEY + "&q=" + cityTarget;
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            DailyForecast dailyForecast = gson.fromJson(response, DailyForecast.class);
                            for (WeatherItem item : dailyForecast.getList()) {
                                weatherItemList.add(item);
                            }
                            saveForecastToDB(dailyForecast);
                            mAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (dbHelper.isDataAlreadyExist(cityTarget)) {
                            //data is exist on SQLite, show it
                            dailyForecast = dbHelper.getSavedForecast(cityTarget);
                            showDataFromDB(dailyForecast);
                        }else {
                            if (error != null) {
                                Log.e(TAG, error.getMessage());
                            } else {
                                Log.e(TAG, "Something error happened!");
                            }
                        }
                    }
                }
        );
        requestQueue.add(stringRequest);
    }

    private void showDataFromDB(DailyForecast dailyForecast){
        weatherItemList.clear();
        for(WeatherItem item : dailyForecast.getList()){
            weatherItemList.add(item);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void saveForecastToDB(DailyForecast dailyForecast){
        if(dbHelper.isDataAlreadyExist(cityTarget)){
            //delete data first
            dbHelper.deleteForUpdate(cityTarget);
        }
        for (WeatherItem item : dailyForecast.getList()){
            dbHelper.saveForecast(dailyForecast.getCity(), item);
        }
    }

    @Override
    public void onItemClick(WeatherItem data, int position){
        Intent startDetailActivity = new Intent(MainActivity.this, DetailActivity.class);
        startDetailActivity.putExtra("data", gson.toJson(data));
        startDetailActivity.putExtra("position", position);
        startActivity(startDetailActivity);
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public boolean isWifiConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected() && (ConnectivityManager.TYPE_WIFI == networkInfo.getType());
    }
}
//dkjhfkshdkfn skdjhfkusdf kjshdfiu skjdhfiusf
//sfkjhkehf kwekfjhk