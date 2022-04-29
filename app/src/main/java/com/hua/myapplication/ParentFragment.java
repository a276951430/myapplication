package com.hua.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ParentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ParentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ParentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParentFragment newInstance(String param1, String param2) {
        ParentFragment fragment = new ParentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parent, container, false);
    }

    //添加权限
    public List<String> addPermissionsList(){
        List<String> permissionList = new ArrayList<>();
        permissionList.add(android.Manifest.permission.CALL_PHONE);
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

                ParentFragment.this.requestPermissions( permissionList.toArray(new String[permissionList.size()]), 1002);

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
            //Toast.makeText(this, "系统检测出您已拒绝权限，可在设置>应用>权限中从新开启权限", Toast.LENGTH_LONG).show();
        } else {
            permissionsOK();
        }

    }

    public void  no(){

    }

    public  void  permissionsOK(){
        //Toast.makeText(activity, "获取权限成功", Toast.LENGTH_LONG).show();
    }



}