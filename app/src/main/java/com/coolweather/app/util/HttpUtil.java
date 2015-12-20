package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Author：DJ
 * Time：2015/12/20 0020 20:36
 * Name：CoolWeather
 * Description：
 */
public class HttpUtil
{
    public static void sendHttpRequest(final String address, final HttpCallbackListener listener)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                HttpURLConnection connection = null;
                try
                {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuffer response = new StringBuffer();
                    String line;
                    while ((line = reader.readLine()) != null)
                    {
                        response.append(line);
                    }
                    if (listener != null)
                    {
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e)
                {
                    if (listener != null)
                    {
                        listener.onError(e);
                    }
                }finally
                {
                    if (connection != null)
                    {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
