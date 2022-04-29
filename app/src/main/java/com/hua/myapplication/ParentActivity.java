package com.hua.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ParentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);
    }

    //添加权限
    public List<String> addPermissionsList(){
        List<String> permissionList = new ArrayList<>();
        permissionList.add(Manifest.permission.CALL_PHONE);
//permissionList.add(com.hua.myapplication.Manifest.permission.READ_PRIVILEGED_PHONE_STATE);
permissionList.add(Manifest.permission.READ_PHONE_STATE);
        return  permissionList;
    }

    //动态申请权限
    public void getPer(final Activity activity, final List<String> permissionList) {
        //判断安卓版本，高于6.0则动态申请权限
        if (Build.VERSION.SDK_INT >= 23) {
            for (int i = 0; i < permissionList.size(); i++) {
//判断是否有权限
                //有权限则从集合集合中移除
                if (ContextCompat.checkSelfPermission(activity, permissionList.get(i)) == PackageManager.PERMISSION_GRANTED) {
                    permissionList.remove(i);

                }
            }
            //判断集合不为空则获取权限
            if (!permissionList.isEmpty()) {

                        ActivityCompat.requestPermissions(activity, permissionList.toArray(new String[permissionList.size()]), 1002);

            }
        } else  {
            permissionsOK();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        List<String> permissionList =new ArrayList<>();
        switch (requestCode){
            case  1002:
                if (grantResults.length >0){
                    for (int i =0;i <grantResults.length;i++){
//没有获取到的权限
                        if (grantResults[i] ==PackageManager.PERMISSION_DENIED){
                            permissionList.add(permissions[i]);
                        }
                    }

                }
        }
        if (permissionList.size() >0){

        } else {
            permissionsOK();
        }

    }

    public  void no(){
        Toast.makeText(this, "系统检测出您已拒绝权限，可在设置>应用>权限中从新开启权限", Toast.LENGTH_LONG).show();
    }


    public  void  permissionsOK(){
        Toast.makeText(this, "获取权限成功", Toast.LENGTH_LONG).show();
    }


}