package com.coolweather.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.coolweather.app.service.AutoUpdateService;

/**
 * Author：DJ
 * Time：2015/12/22 0022 21:45
 * Name：CoolWeather
 * Description：
 */
public class AutoUpdateReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent i = new Intent(context, AutoUpdateService.class);
        context.startActivity(i);
    }
}
