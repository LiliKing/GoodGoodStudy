package com.smkpay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xianglijin.app1.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button mBtnSend;
    private EditText mEtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mEtName = (EditText) findViewById(R.id.et_name);
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasApplication(MainActivity.this, "com.example.xianglijin.app2")) {
                    Toast.makeText(MainActivity.this, "没有安装App2这个应用", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setType("test/");
                intent.putExtra("name", mEtName.getText().toString().trim());
                startActivityForResult(intent, 2);
            }
        });
    }

    public boolean hasApplication(Context context, String packageName) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (data != null) {
                            Toast.makeText(this, "回来的内容" + data.getDataString(), Toast.LENGTH_LONG).show();
                            Intent intent;
                            if (data.getDataString().contains("1")) {
                                intent = new Intent(MainActivity.this, ResultActivitySuccess.class);
                            } else {
                                intent = new Intent(MainActivity.this, ResultActivityFailed.class);
                            }
                            startActivity(intent);
                        }
                        break;
                }
                break;
        }
    }
}
