package com.coolweather.app.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coolweather.app.R;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

/**
 * Author：DJ
 * Time：2015/12/21 0021 22:04
 * Name：CoolWeather
 * Description：
 */
public class WeatherActivity extends Activity
{
    private LinearLayout weatherInfoLayout;
    /**
     * 用于显示城市名
     */
    private TextView cityNameText;
    /**
     * 用于显示发布时间
     */
    private TextView publishText;
    /**
     * 用于显示天气描述信息
     */
    private TextView weatherDespText;
    /**
     * 用于显示气温1
     */
    private TextView temp1Text;
    /**
     * 用于显示气温2
     */
    private TextView temp2Text;
    /**
     * 用于显示当前时间
     */
    private TextView currentDataText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);
        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        cityNameText = (TextView) findViewById(R.id.city_name);
        publishText = (TextView) findViewById(R.id.publish_text);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        temp1Text = (TextView) findViewById(R.id.temp1);
        temp2Text = (TextView) findViewById(R.id.temp2);
        currentDataText = (TextView) findViewById(R.id.current_data);
        String countryCode = getIntent().getStringExtra("country_code");
        if (!TextUtils.isEmpty(countryCode))
        {
            publishText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeatherCode(countryCode);
        }else
        {
           showWeather();
        }
    }

    /**
     * 查询县级代号所对应的天气代号
     * @param countryCode
     */
    private void queryWeatherCode(String countryCode)
    {
        String address = "http://www.weather.com.cn/data/list3/city" + countryCode + ".xml";
        queryFromServer(address, "countryCode");
    }

    /**
     * 查询天气代号所对应的天气
     * @param weatherCode
     */
    private void queryWeatherInfo(String weatherCode)
    {
        String address = "http://www.weather.com.cn/data/cityinfo" + weatherCode + ".html";
        queryFromServer(address, "weatherCode");
    }

    /**
     * 根据传入的地址和类型去向服务器查询天气代号或者天气信息
     * @param address
     * @param type
     */
    private void queryFromServer(final String address, final String type)
    {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener()
        {
            @Override
            public void onFinish(final String response)
            {
                if ("countryCode".equals(type))
                {
                    if (!TextUtils.isEmpty(response))
                    {
                        String[] array = response.split("\\|");
                        if (array != null && array.length == 2)
                        {
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                }else if ("weatherCode".equals(type))
                        {
                            Utility.handleWeatherResponse(WeatherActivity.this, response);
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    showWeather();
                                }
                            });
                        }
            }

            @Override
            public void onError(Exception e)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        publishText.setText("同步失败");

                    }
                });

            }
        });

    }

    /**
     * 从SharedPreferences文件中读取储存的天气信息，并显示到界面上
     */
    private void showWeather()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText( prefs.getString("city_name", ""));
        temp1Text.setText(prefs.getString("temp1", ""));
        temp2Text.setText(prefs.getString("temp2", ""));
        weatherDespText.setText(prefs.getString("weather_desp", ""));
        publishText.setText("今天"+prefs.getString("publish_time","")+"发布");
        currentDataText.setText(prefs.getString("current_data", ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
    }
}
