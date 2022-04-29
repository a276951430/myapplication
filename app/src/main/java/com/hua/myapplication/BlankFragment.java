            package com.hua.myapplication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

            /**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends ParentFragment {

                //监听器类对象
                private ButtonClick buttonClick;
                //显示号码的区域
                private TextView textView;
                //数字0-9和#*。
                private  Button[]buttons =new Button[12];
                //存放按钮0/9和#*的id数组。
                private  int[]buttonId = {R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9, R.id.button10, R.id.button11, R.id.button12};
                //退格按钮。
                private ImageButton imageButton, ib;
                //最后一行的LinearLayout
                private LinearLayout linearLayout;
                //拨打电话的卡槽id
                private  int card_id;
                //是否有默认sim卡
                private  boolean isCard;
                //两个拨打电话按钮。
                private  ImageButton buttonCall1, buttonCall2;

                //为了检测sim卡插拔动作的广播
                //内部广播类
                private MyReceiver myReceiver;
                //IntentFilter
                private IntentFilter intentFilter;
    //宿主activity对象
                private Activity activity;



                // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
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
        View view =inflater.inflate(R.layout.fragment_blank, container, false);
        activity = getActivity();
        getPer(activity, addPermissionsList());
        init(view);
//注册广播
        //add();

        return  view;
    }



                @Override
                public void permissionsOK() {
                    super.permissionsOK();

                    add();
                }

                public  void  add(){
        myReceiver = new MyReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        activity.getApplicationContext().registerReceiver(myReceiver, intentFilter);
    }




    public  void init(View view){
        buttonClick = new ButtonClick();
        imageButton = (ImageButton) view.findViewById(R.id.imagebutton);
        ib = (ImageButton) view.findViewById(R.id.ib);
        linearLayout = (LinearLayout) view.findViewById(R.id.ll1);
        textView = (TextView) view.findViewById(R.id.num_text);
        buttonCall1 = (ImageButton) view.findViewById(R.id.buttoncard1);
        buttonCall2 = (ImageButton) view.findViewById(R.id.buttoncard2);
        buttonCall1.setOnClickListener(buttonClick);
        buttonCall2.setOnClickListener(buttonClick);
        imageButton.setOnClickListener(buttonClick);
        ib.setOnClickListener(buttonClick);
        imageButton.setEnabled(false);
        ib.setEnabled(false);

        for (int i =0;i<buttons.length;i++){
            buttons[i] =(Button) view.findViewById(buttonId[i]);
            buttons[i].setOnClickListener(buttonClick);
        }

    }

                //图片上加文字
                public Bitmap draw(String text){
//从资源文件读取图片
                    Bitmap callBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.call1 );
//获取宽高
                    int call_width = callBitmap.getWidth();
                    int call_height = callBitmap.getHeight();
//获取指定坐标点的颜色
                    int call_color = callBitmap.getPixel(call_width/2, call_height/2);
//新建位图，宽为资源图片的5倍，高不变
                    Bitmap testBitmap =Bitmap.createBitmap((call_width*3+call_width/2) , call_height, Bitmap.Config.ARGB_8888);
//创建画布
                    Canvas canvas = new Canvas(testBitmap);
//创建一个圆角矩形，把资源图片和文字都写在这个矩形的上面
                    RectF rectF = new RectF(0, 0, testBitmap.getWidth(), testBitmap.getHeight());
                    //创建圆角矩形的画笔
                    Paint paint1 = new Paint();
                    //填充矩形的颜色
                    paint1.setColor(call_color);
                    //绘制圆角矩形
                    canvas.drawRoundRect(rectF, 300f, 300f, paint1);
                    //绘制左边的资源图片
                    canvas.drawBitmap(callBitmap, 0, 0, null);
                    //创建文字画笔
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//设置文字为填充，其他还有描边和填充加描边
                    paint.setStyle(Paint.Style.FILL);
                    //将文字颜色设置为白色
                    paint.setColor(Color.WHITE);
//设置字体
                    paint.setTypeface(Typeface.DEFAULT_BOLD);
                    //设置文字大小
                    paint.setTextSize(testBitmap.getWidth()/10);
//绘制文字
                    canvas.drawText(text,call_width, call_height/2, paint);
                    //保存及恢复
                    canvas.save();
                    canvas.restore();
//返回绘制好的bitmap
                    return  testBitmap;
                }

                //获取sim卡
                public void getSim(){
                    TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(activity.TELEPHONY_SERVICE);
                    SubscriptionManager subscriptionManager = SubscriptionManager.from(activity);
//获取默认通话sim卡id
                    int sub_id =SubscriptionManager.getDefaultVoiceSubscriptionId();
//获取有几张卡
                    int count = subscriptionManager.getActiveSubscriptionInfoCount();
//判断是否没有设置默认通话
        /*if (sub_id!=-1 ){
            buttonCall2.setVisibility(View.GONE);
            ib.setVisibility(View.VISIBLE);
            //获取卡槽id
            SubscriptionInfo info =subscriptionManager.getActiveSubscriptionInfo(sub_id);
            card_id = info.getSimSlotIndex();
            isCard = true;
            String text = "卡"+(card_id+1)+":"+info.getCarrierName().toString().trim();
        buttonCall1.setContentDescription(text+"拨打");
buttonCall1.setImageBitmap(draw(text));
        }*/

                    //else {
                    //获取sim卡信息
                    isCard = false;
                    List<SubscriptionInfo> infoList = subscriptionManager.getActiveSubscriptionInfoList();
                    switch (infoList.size()){
                        case  0:
                            Toast.makeText(activity, "无sim卡", Toast.LENGTH_LONG).show();
                            buttonCall2.setVisibility(View.GONE);
                            ib.setVisibility(View.VISIBLE);
                            buttonCall1.setImageBitmap(draw(""));
                            buttonCall1.setContentDescription("拨号");
                            break;
                        case  2:
                            if (infoList.get(1) !=null && infoList.get(0) !=null){
                                buttonCall2.setVisibility(View.VISIBLE);
                                ib.setVisibility(View.GONE);

                                String text2 = "卡2："+updateName(infoList.get(1).getCarrierName().toString().trim());
                                buttonCall2.setContentDescription(text2+"拨打");
                                buttonCall2.setImageBitmap(draw(text2));
                                String text1 = "卡1："+updateName(infoList.get(0).getCarrierName().toString().trim());
                                buttonCall1.setContentDescription(text1+"拨打");
                                buttonCall1.setImageBitmap(draw(text1));
                            }
                            break;
                        case  1:

                            if (infoList.get(0) !=null){
                                buttonCall2.setVisibility(View.GONE);
                                ib.setVisibility(View.VISIBLE);
                                String text1 = "卡"+infoList.get(0).getSimSlotIndex()+":"+updateName(infoList.get(0).getCarrierName().toString().trim());
                                buttonCall1.setContentDescription(text1+"拨打");
                                buttonCall1.setImageBitmap(draw(text1));
                            }

                    }
                    //}


                }
                //修复联通显示英文的问题
                public  String updateName(String name){
                    if (name.equals("CHN-UNICOM")){
                        name = "中国联通";
                    }
                    return  name;
                }


                //打电话
                public  void callPhone(String phoneNumber, int card_id){
                    List<PhoneAccountHandle> phoneAccountHandles = new ArrayList<>();
                    TelecomManager telecomManager = (TelecomManager) activity.getSystemService(activity.TELECOM_SERVICE);

                    if (telecomManager !=null){
                        phoneAccountHandles = telecomManager.getCallCapablePhoneAccounts();
                    }
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+phoneNumber));
                    if (phoneAccountHandles.size() >0) {
                        callIntent.putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandles.get(card_id));
                    }

                    startActivity(callIntent);
                }


                @Override
                public void onDestroy() {
                    super.onDestroy();
                    activity.getApplicationContext().unregisterReceiver(myReceiver);
                }


                //内部类实现广播
                class  MyReceiver extends BroadcastReceiver {

                    @Override
                    public void onReceive(Context context, Intent intent) {
                        getSim();
                        //textView.setText("ok");
                    }
                }



                //内部类实现按钮监听接口
                class  ButtonClick implements View.OnClickListener{

                    @Override
                    public void onClick(View view) {
                        //如果点击了退格键
                        if (view.getId() ==R.id.imagebutton||view.getId() ==R.id.ib){
//获取输入内容
                            String text = textView.getText().toString();
                            textView.setText(text.substring(0, text.length() - 1));
                            //判断文本框中还有没有字符，如果没有就把退格设置成不可用
                            if (textView.getText().length() <=0){
                                imageButton.setEnabled(false);
                                ib.setEnabled(false);
                            }
                        } else  if (view.getId() ==R.id.buttoncard1){
                            if (!isCard){
                                card_id =0;
                            }
                            callPhone(textView.getText().toString(), card_id);
                        }
                        else  if (view.getId() ==R.id.buttoncard2){
                            if (!isCard){
                                card_id =1;
                            }
                            callPhone(textView.getText().toString(), card_id);
                        }

                        //点击了0-9或#*任何一个按键
                        else {
                            //设置退格可用
                            imageButton.setEnabled(true);
                            ib.setEnabled(true);

                            //将view强行转换成Button类型
                            Button button = (Button) view;
                            //生成新的字符串并显示
                            String text = textView.getText() + button.getText().toString();
                            textView.setText(text);
                        }
                    }
                }



            }