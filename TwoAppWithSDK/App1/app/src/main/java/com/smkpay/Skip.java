package com.smkpay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import java.util.List;

/**
 * 跳转工具类
 */
public class Skip {
    private static Skip instance = null;
    private Context mContext;

    public Skip(Context context) {
        this.mContext = context;
    }

    public static Skip getInstance(Context context) {
        if (instance == null) {
            instance = new Skip(context);
        }
        return instance;
    }


    public boolean skip() {
        if (!checkHasInstallApp(mContext, "com.example.xianglijin.app2")) {
            Toast.makeText(mContext, "没有安装App2这个应用", Toast.LENGTH_SHORT).show();
            return false;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setType("test/");
        intent.putExtra("name", "ssss");
        ((Activity) mContext).startActivityForResult(intent, 2);
        return true;
    }


    private boolean checkHasInstallApp(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();

        //获取系统中安装的应用包的信息
        List<PackageInfo> listPackageInfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < listPackageInfo.size(); i++) {
            if (listPackageInfo.get(i).packageName.equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;

    }

}
