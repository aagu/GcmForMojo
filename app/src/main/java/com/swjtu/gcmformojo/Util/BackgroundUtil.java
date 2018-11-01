package com.swjtu.gcmformojo.Util;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class BackgroundUtil {
    /**
     * APP是否正在运行
     *
     * @param context     应用上下文
     * @param packageName APP的包名
     * @return
     */
    public static boolean isAppOnForeground(Context context, String packageName)
    {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();

        //一般不会跑到这里来，如果跑到这里来的话，要么就是这个app废了，要不就是见鬼了。。
        if (appProcessInfos == null)
        {
            Log.d("BackgroundUtil", "appProcessInfos == null");
            return false;
        }
        else
        {
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcessInfos)
            {
                if (appProcess.processName.equals(packageName))
                {
                    Log.d("BackgroundUtil", packageName+"正在后台运行...");
                    return true;
                }
            }
            return false;
        }
    }
}
