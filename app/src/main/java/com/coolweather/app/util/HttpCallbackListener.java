package com.coolweather.app.util;

/**
 * Author：DJ
 * Time：2015/12/20 0020 20:42
 * Name：CoolWeather
 * Description：
 */
public interface HttpCallbackListener
{
    void onFinish(String response);

    void onError(Exception e);
}
