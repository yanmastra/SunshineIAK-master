package com.example.satyakresna.sunshineiak.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.satyakresna.sunshineiak.R;
import com.example.satyakresna.sunshineiak.model.Weather;
import com.example.satyakresna.sunshineiak.model.WeatherItem;
import com.example.satyakresna.sunshineiak.utilities.SunshineWeatherUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.data;
import static android.R.attr.thickness;
import static com.example.satyakresna.sunshineiak.R.string.day;
import static com.example.satyakresna.sunshineiak.R.string.weather;

/**
 * Created by satyakresna on 06-Aug-17.
 */

public class ForecastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = ForecastAdapter.class.getSimpleName();
    private List<WeatherItem> weatherItemList = new ArrayList<>();

    private static final int VIEW_TODAY = 0;
    private static final int VIEW_OTHER = 1;
    private final ItemClickListener mOnclickListener;

    public interface ItemClickListener{
        void onItemClick(WeatherItem dat, int position);
    }

    public ForecastAdapter(List<WeatherItem> weatherItemList, ItemClickListener mOnclickListener) {
        this.weatherItemList = weatherItemList;
        this.mOnclickListener = mOnclickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TODAY) {
            return new TodayViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_today_forecast, parent, false));
        } else {
            return new ForecastViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_forecast_item, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TODAY) {
            ((TodayViewHolder) holder).bind(weatherItemList.get(position), mOnclickListener);

        } else {
            ((ForecastViewHolder) holder).bind(weatherItemList.get(position), position, mOnclickListener);
        }
    }

    @Override
    public int getItemCount() {
        return weatherItemList.size();
    }

    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TODAY;
        } else {
            return VIEW_OTHER;
        }
    }

    public class ForecastViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_weather_icon) ImageView weatherIcon;
        @BindView(R.id.tv_day) TextView day;
        @BindView(R.id.tv_weather) TextView weather;
        @BindView(R.id.tv_max_temp) TextView maxTemp;
        @BindView(R.id.tv_min_temp) TextView minTemp;

        public ForecastViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final WeatherItem data, final int position, final ItemClickListener itemClickListener) {
            weatherIcon.setImageResource(
                    SunshineWeatherUtils.getSmallArtResourceIdForWeatherCondition(
                            data.getWeather().get(0).getId()
                    )
            );
            day.setText(data.getReadableDateTime(position));
            weather.setText(data.getWeather().get(0).getDescription());
            minTemp.setText(data.getTemp().getResolvedTemp(data.getTemp().getMin()));
            maxTemp.setText(data.getTemp().getResolvedTemp(data.getTemp().getMax()));
            itemView.setOnClickListener( new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    itemClickListener.onItemClick(data, position);
                }
            });
        }
    }

    public class TodayViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_weather_icon_today) ImageView weatherIconToday;
        @BindView(R.id.tv_day_today) TextView dayToday;
        @BindView(R.id.tv_weather_today) TextView weatherToday;
        @BindView(R.id.tv_min_temp_today) TextView minTempToday;
        @BindView(R.id.tv_max_temp_today) TextView maxTempToday;
        public TodayViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final WeatherItem data, final ItemClickListener itemClickListener) {
            weatherIconToday.setImageResource(
                    SunshineWeatherUtils.getSmallArtResourceIdForWeatherCondition(
                            data.getWeather().get(0).getId()
                    )
            );
            dayToday.setText(data.getTodayReadableTime());
            weatherToday.setText(data.getWeather().get(0).getDescription());
            minTempToday.setText(data.getTemp().getResolvedTemp(data.getTemp().getMin()));
            maxTempToday.setText(data.getTemp().getResolvedTemp(data.getTemp().getMax()));
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    itemClickListener.onItemClick(data, 0);
                }
            });
        }
    }
}