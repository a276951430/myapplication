package com.hua.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.params.HttpConnectionParams;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ContactAndCallActivity extends ParentActivity{




    //联系人id
    private String id;
//显示头像的ImageView
    private ImageView imageView;
//显示名字的文本
private TextView name_textview;
//内容提供器
private ContentResolver resolver;
//容器LinearLayout
private LinearLayout num_linearLayout;
//姓名
private  String name;
//号码集合
private  List<String> numberList;
//查询工具类对象
private  SelectData selectData;
//横向的线性布局
private  List<LinearLayout> hor_linearLayoutList;
//显示号码和归属地的id
private List<TextView> num_textViewList;
//拨号按钮id
private  List<ImageButton> call_buttonList;
private  List<Integer> button_idList;
//号码归属地集合
private  List<String> geocode_locationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_and_call);
        getPer(this, addPermissionsList());
        imageView =(ImageView) findViewById(R.id.cac_imageview);
        name_textview =(TextView) findViewById(R.id.cac_name_text);
        num_linearLayout =(LinearLayout) findViewById(R.id.cac_nums_linear);
        //OkGo.getInstance().init(this);
    }


    @Override
    public List<String> addPermissionsList() {
        super.addPermissionsList();
        List<String> permissionList = new ArrayList<>();
        permissionList.add(android.Manifest.permission.READ_CALL_LOG);
        permissionList.add(Manifest.permission.WRITE_CALL_LOG);
        permissionList.add(android.Manifest.permission.READ_CONTACTS);
        permissionList.add(Manifest.permission.WRITE_CONTACTS);
        return  permissionList;
    }

    @Override
    public void permissionsOK() {
        super.permissionsOK();
init();
    }

    public  void init(){
        resolver =getContentResolver();
        selectData =new SelectData();
        numberList =new ArrayList<>();
        geocode_locationList =new ArrayList<>();
        Intent intent =getIntent();
        id =   intent.getStringExtra("id");
name =intent.getStringExtra("name");
name_textview.setText(name);
        if (id.equals("-1")){
    String num =intent.getStringExtra("number");
String geocode_location =intent.getStringExtra("geocode_location");
    numberList.add(num);
            geocode_locationList.add(geocode_location);
} else  {
numberList = selectData.getContactNumber(resolver, id);
}
        //addNumberView(numberList);
    }

public  void  addNumberView(List<String> numberList){
hor_linearLayoutList =new ArrayList<>();
num_textViewList =new ArrayList<>();
button_idList =new ArrayList<>();
call_buttonList =new ArrayList<>();
        int button_id =3001;
        for (int i =0;i <numberList.size();i++) {
            LinearLayout layout = new LinearLayout(this);
        TextView textView =new TextView(this);
        textView.setText(numberList.get(i));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
        LinearLayout.LayoutParams layoutParams =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            //layoutParams.setMargins(dip2ps(this, 10f),dip2ps(this, 10f), dip2ps(this, 10f), dip2ps(this, 10f) );
            textView.setLayoutParams(layoutParams);
            textView.setPadding(dip2ps(this, 10f),dip2ps(this, 10f), dip2ps(this, 10f), dip2ps(this, 10f));
            num_textViewList.add(textView);
            layout.addView(textView);

            Drawable drawable =getResources().getDrawable(R.drawable.call1);
            drawable.setBounds(0, 0, dip2ps(this, 30f), dip2ps(this, 30f));
            ImageButton button =new ImageButton(this);
button.setContentDescription("拨打");
button.setImageResource(R.drawable.call1);
button.setId(button_id);
button_idList.add(button_id);
button_id++;
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(dip2ps(this, 30f), dip2ps(this, 30f), 0);
layoutParams1.setMargins(dip2ps(this, 10f),dip2ps(this, 10f), dip2ps(this, 10f), dip2ps(this, 10f) );
            button.setLayoutParams(layoutParams1);
layout.addView(button);
call_buttonList.add(button);

hor_linearLayoutList.add(layout);
            num_linearLayout.addView(layout);
        }
}




    public  static int dip2ps(Context context, float dpValue){
        final  float scale =context.getResources().getDisplayMetrics().density;
        return  (int)(dpValue*scale +0.5f);
    }



}