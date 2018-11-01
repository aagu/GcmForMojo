package com.swjtu.gcmformojo.Util;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.Context.USAGE_STATS_SERVICE;
import static com.swjtu.gcmformojo.MyApplication.MYTAG;

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

    /**
     * 通过使用UsageStatsManager获取，此方法是android5.0A之后提供的API
     * 必须：
     * 1. 此方法只在android5.0以上有效
     * 2. AndroidManifest中加入此权限<uses-permission xmlns:tools="http://schemas.android.com/tools" android:name="android.permission.PACKAGE_USAGE_STATS"
     * tools:ignore="ProtectedPermissions" />
     * 3. 打开手机设置，点击安全-高级，在有权查看使用情况的应用中，为这个App打上勾
     *
     * @param context     上下文参数
     * @param packageName 需要检查是否位于栈顶的App的包名
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static boolean queryAppUsageStats(Context context, String packageName)
    {
        class RecentUseComparator implements Comparator<UsageStats>
        {
            @Override
            public int compare(UsageStats lhs, UsageStats rhs)
            {
                return (lhs.getLastTimeUsed() > rhs.getLastTimeUsed()) ? -1 : (lhs.getLastTimeUsed() == rhs.getLastTimeUsed()) ? 0 : 1;
            }
        }

        RecentUseComparator mRecentComp = new RecentUseComparator();
        UsageStatsManager mUsageStatsManager = null;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
        {
            mUsageStatsManager = (UsageStatsManager) context.getSystemService(USAGE_STATS_SERVICE);
        }

        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.YEAR, -1);
        long startTime = calendar.getTimeInMillis();

        assert mUsageStatsManager != null;
        List<UsageStats> usageStats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
        if (usageStats == null || usageStats.size() == 0)
        {
            if (!HavePermissionForTest(context))
            {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                //   Looper.prepare();
                //   Toast.makeText(context, "权限不够\n请打开手机设置，点击安全-高级，在有权查看使用情况的应用中，为这个App打上勾", Toast.LENGTH_SHORT).show();
                //   Looper.loop();
            }
            return false;
        }
        Collections.sort(usageStats, mRecentComp);
        String currentTopPackage = usageStats.get(0).getPackageName();
        Log.d(MYTAG, currentTopPackage);

        return currentTopPackage.equals(packageName);
    }

    /**
     * 判断是否有用权限
     *
     * @param context 上下文参数
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static boolean HavePermissionForTest(Context context)
    {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);
        } catch (PackageManager.NameNotFoundException e)
        {
            return true;
        }
    }

    /**
            * 判断应用是否已经启动
     *
             * @param context  一个context
     * @param serviceClassName 要判断应用的包名
     * @return boolean
     */
    public static boolean isServiceRunning(Context context, String serviceClassName)
    {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : services)
        {
            if (runningServiceInfo.service.getPackageName().equals(serviceClassName))
            {
                return true;
            }
        }
        return false;
    }
}
