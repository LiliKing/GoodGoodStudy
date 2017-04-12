package com.example.xianglijin.goodpermissions;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    public static final int GET_WRITE_EXTERNAL_STORAGE = 10010;
    private Button mBtnCamera;
    private Button mBtnPhotoAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnCamera = (Button) findViewById(R.id.btn_1);
        mBtnCamera.setOnClickListener(new View.OnClickListener() {
                                          @TargetApi(Build.VERSION_CODES.M)
                                          @Override
                                          public void onClick(View view) {
                                              QuickPermission.CheckPermission(new QuickPermission.Permission() {
                                                  @Override
                                                  public void permissionGranted() {
                                                      Toast.makeText(MainActivity.this, "允许照相机的权限", Toast.LENGTH_SHORT).show();
                                                  }

                                                  @Override
                                                  public void permissionDeny() {
                                                      Toast.makeText(MainActivity.this, "拒绝照相机的权限", Toast.LENGTH_SHORT).show();
                                                  }

                                                  @Override
                                                  public void permissionAsk() {
                                                      Toast.makeText(MainActivity.this, "询问照相机的权限", Toast.LENGTH_SHORT).show();
                                                  }
                                              }, MainActivity.this, Manifest.permission.CAMERA, true, GET_WRITE_EXTERNAL_STORAGE);

                                          }
                                      }

        );

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        if (requestCode == GET_WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "询问：用户同意使用相册权限", Toast.LENGTH_SHORT).show();
            } else {
                // Permission Denied
                Toast.makeText(MainActivity.this, "询问：用户拒绝使用相册权限", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
