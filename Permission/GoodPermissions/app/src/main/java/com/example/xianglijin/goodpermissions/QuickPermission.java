package com.example.xianglijin.goodpermissions;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by Xianglijin on 2016/11/29.
 */
public class QuickPermission {

    interface Permission {
        void permissionGranted();//权限允许状态

        void permissionDeny();//权限拒绝

        void permissionAsk();//权限询问
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void CheckPermission(Permission permissionBack, Context context, String permission, boolean needToAsk, int requestCode) {//needToAsk是是否调用app自己的权限询问框;
        // 需要传入requestCode当用户选择允许或者拒绝的时候可以直接做操作
        if (ContextCompat.checkSelfPermission(context,
                permission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {//询问
            permissionBack.permissionAsk();
            if (needToAsk)
                ((Activity) context).requestPermissions(new String[]{permission},
                        requestCode);
        } else if (ContextCompat.checkSelfPermission(context,
                permission) != PackageManager.PERMISSION_GRANTED) {
            permissionBack.permissionGranted();
        } else if (ContextCompat.checkSelfPermission(context,
                permission) != PackageManager.PERMISSION_DENIED) {
            permissionBack.permissionDeny();
        }
    }
}
