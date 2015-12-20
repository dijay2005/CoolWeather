package com.coolweather.app.util;

import android.text.TextUtils;

import com.coolweather.app.db.CoolWeatherDb;
import com.coolweather.app.model.City;
import com.coolweather.app.model.Country;
import com.coolweather.app.model.Province;

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
}
