package com.coolweather.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.coolweather.app.db.CoolWeatherDb;
import com.coolweather.app.model.City;
import com.coolweather.app.model.Country;
import com.coolweather.app.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Author：DJ
 * Time：2015/12/20 0020 20:54
 * Name：CoolWeather
 * Description：
 */
public class Utility
{
    /**
     * 解析服务器处理的省级数据
     * @param coolWeatherDb
     * @param response
     * @return
     */
    public synchronized static boolean handleProvincesResponse(CoolWeatherDb coolWeatherDb, String
            response)
    {
        if (!TextUtils.isEmpty(response))
        {
            String[] allProvince = response.split(",");
            if (allProvince != null && allProvince.length > 0)
            {
                for (String p : allProvince)
                {
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    coolWeatherDb.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析服务器处理的市级数据
     * @param coolWeatherDb
     * @param response
     * @param provinceId
     * @return
     */
    public synchronized static boolean handleCitiesResponse(CoolWeatherDb coolWeatherDb,String
            response,int provinceId)
    {
        if (!TextUtils.isEmpty(response))
        {
            String[] allCities = response.split(",");
            if (allCities != null && allCities.length > 0)
            {
                for (String c : allCities)
                {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    coolWeatherDb.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean handleCountriesResponse(CoolWeatherDb coolWeatherDb, String
            response, int cityId)
    {
        if (!TextUtils.isEmpty(response))
        {
            String[] allCountries = response.split(",");
            if (allCountries != null && allCountries.length > 0)
            {
                for (String c : allCountries)
                {
                    String[] array = c.split("\\|");
                    Country country = new Country();
                    country.setCountryCode(array[0]);
                    country.setCountryName(array[1]);
                    country.setCityId(cityId);
                    coolWeatherDb.saveCountry(country);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析服务器返回的JSON数据，并将解析出的数据储存到本地。
     * @param context
     * @param response
     */
    public static void handleWeatherResponse(Context context,String response)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");
            saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 将服务器返回的所有天气信息储存到SharedPreferences文件中
     * @param context
     * @param cityName
     * @param weatherCode
     * @param temp1
     * @param temp2
     * @param weatherDesp
     * @param publishTime
     */
    public static void saveWeatherInfo(Context context,String cityName,String weatherCode,String
                                       temp1,String temp2,
                                       String weatherDesp,String publishTime)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context)
                .edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_data", sdf.format(new Date()));
        editor.commit();
    }
}
