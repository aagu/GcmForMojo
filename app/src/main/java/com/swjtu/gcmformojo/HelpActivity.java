package com.swjtu.gcmformojo;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    private TextView textView_Version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        textView_Version = (TextView) findViewById(R.id.textVersion);
        textView_Version.setText(getVersion());


    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    private String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            return "("+info.versionName+" By jklmn of SWJTU)";
        } catch (Exception e) {
            e.printStackTrace();
            return this.getString(R.string.can_not_find_version_name);
        }
    }

}
