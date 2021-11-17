package com.wang.weplugin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import cn.wang.weplugin.core.Shadow;
import cn.wang.weplugin.manager.EnterCallback;
import cn.wang.weplugin.manager.PluginManager;

public class MainActivity extends AppCompatActivity {

    private ViewGroup viewById;


    static {
        
    }

    static int a = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewById = findViewById(R.id.container);

        findViewById(R.id.plugin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //插件APK的文件
                PluginManager pluginManager = Shadow.getPluginManager();
                Bundle bundle = new Bundle();
                bundle.putString(Constant.KEY_PLUGIN_PART_KEY, Constant.PART_KEY_PLUGIN_MAIN_APP);
                bundle.putString(Constant.KEY_PLUGIN_ZIP_PATH, PluginHelper.getInstance().pluginZipFile.getAbsolutePath());
                bundle.putString(Constant.KEY_ACTIVITY_CLASSNAME, "cn.wang.weplugin.MainActivity");
                pluginManager.enter(MainActivity.this, Constant.FROM_ID_START_ACTIVITY, bundle, new EnterCallback() {
                    @Override
                    public void onShowLoadingView(final View view) {
                        Log.e("cc.wang", "MainActivity.onShowLoadingView.");
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                viewById.addView(view);
                            }
                        });
                    }

                    @Override
                    public void onCloseLoadingView() {
                        Log.e("cc.wang", "MainActivity.onCloseLoadingView.");
                    }

                    @Override
                    public void onEnterComplete() {
                        Log.e("cc.wang", "MainActivity.onEnterComplete.");
                    }
                });
            }
        });

    }
}