package com.hua.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.tv.TvContract;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.JsonReader;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    //来源,手机、sim卡
    private String from;
//号码集合
private  List<String> numberList;
//归属地集合
    private  List<String> geocode_locationList;


    //查询工具类对象
private  SelectData selectData;
//横向的线性布局
private  List<LinearLayout> hor_linearLayoutList;
//显示号码和归属地的文本
private List<TextView> num_textViewList;
//拨号按钮
private  List<ImageButton> call_buttonList;
//拨号按钮id
private  List<Integer> button_idList;

Handler handler =new Handler(Looper.getMainLooper()){
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case  1:

                addNumberView(geocode_locationList);
                break;
        }
    }
};

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
from =intent.getStringExtra("from");
name_textview.setText(name);
        if (id.equals("-1")){
    String num =intent.getStringExtra("number");
    numberList.add(num);
            //geocode_locationList.add(geocode_location);
} else  {
numberList = selectData.getContactNumber(resolver, id);
Bitmap bitmap =selectData.getContactBitmap(resolver, id);
if (bitmap !=null){
    imageView.setImageBitmap(bitmap);
}
}
       getNumberLocation(numberList);
        //addNumberView(numberList);
    }

public  void addNumberView(List<String> numberList){
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

public void getNumberLocation(List<String> nubList){
        //开启县城获取网络归属地信息
        new Thread(new Runnable() {
            @Override
            public void run() {
//String BAIDU_URL = "https://www.baifubao.com/callback?cmd=1059&callback=phone&phone=";
//这是360的url，就不改变量名了
String BAIDU_URL = "https://cx.shouji.360.cn/phonearea.php?number=";
//便利号码列表
for (int i =0;i <nubList.size();i++){

                    StringBuilder builder = new StringBuilder();
                    //添加手机号做参数
                    String urlStr = BAIDU_URL+nubList.get(i);

                    try {
//将String生成url对象
                        URL url = new URL(urlStr);
//使用HttpURLConnection访问获取网络数据
                    HttpURLConnection connection =(HttpURLConnection) url.openConnection();
                    //设置方法，GET还是POST
                    connection.setRequestMethod("GET");
                    //设置超时
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
//建立连接
                    connection.connect();

                    //使用io读取网络数据
InputStream inputStream = connection.getInputStream();
    InputStreamReader reader = new InputStreamReader(inputStream);
    BufferedReader bufferedReader = new BufferedReader(reader);
String line;
    while ((line =bufferedReader.readLine()) !=null){
        builder.append(line);

    }

} catch (IOException e){
    e.printStackTrace();
} catch (Exception e){
                        e.printStackTrace();
                    }
                    //解析json数据
try {
      JSONObject jsonObject = new JSONObject(builder.toString());
      JSONObject dataJson =jsonObject.optJSONObject("data");
      //省份
      String province = dataJson.optString("province");
      //城市
      String city = dataJson.optString("city");
      //运营商
      String sp = dataJson.optString("sp");
    //归属地字符串，如果为空则设成未知。
      String location =province+city+sp;
    if (location.equals("")){
        location ="未知";
    }
    geocode_locationList.add(location);
    } catch ( Exception e){
    e.printStackTrace();
    }
}
//发送消息通知主线程
handler.sendEmptyMessage(1);
            }
        }).start();
}

/*public  void gsonToLocation(){
    for (int i =0;i <numAndJsonList.size();i++) {
        String phoneJson = numAndJsonList.get(i).get("json");
        JSONObject object = new JSONObject(phoneJson);
        JSONObject numberObject = object.getJSONObject("response").getJSONObject(numAndJsonList.get(i).get("num"));
        geocode_locationList.add(numberObject.getString("location"));
    }
}*/


    public  static int dip2ps(Context context, float dpValue){
        final  float scale =context.getResources().getDisplayMetrics().density;
        return  (int)(dpValue*scale +0.5f);
    }



}