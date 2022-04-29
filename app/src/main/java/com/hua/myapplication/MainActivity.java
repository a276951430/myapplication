package com.hua.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ParentActivity  {


//底部导航栏
    private RadioGroup radioGroup;
//FrameLayout
    private FrameLayout frameLayout;
//存放fragment的列表。
    private List<Fragment> fragments = new ArrayList<>();
//几个fragment
//拨号
    private BlankFragment blankFragment;
//联系人
    private  ContactFragment contactFragment;
    //通话记录
    private  CallLogsFragment callLogsFragment;
    //fragment管理类对象
private FragmentManager fragmentManager;
//事物
private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
setTitle("拨号");
        //获取权限
        //初始化控件
init();

        }




    public  void  init() {
frameLayout = (FrameLayout) findViewById(R.id.framelayout);
radioGroup = (RadioGroup) findViewById(R.id.radiogroup);

        fragmentManager = getSupportFragmentManager();
        blankFragment = new BlankFragment();
        fragments.add(blankFragment);
        hideOthersFragment(blankFragment, true);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
switch (i){
    case  R.id.radiobutton1:
if ( blankFragment ==null){
    blankFragment = new BlankFragment();
    fragments.add(blankFragment);
    hideOthersFragment(blankFragment, true);
} else {
    hideOthersFragment(blankFragment, false);
}
        break;
    case  R.id.radiobutton2:
if (contactFragment ==null){
    contactFragment = new ContactFragment();
    fragments.add(contactFragment);
    hideOthersFragment(contactFragment, true);
} else  {
    hideOthersFragment(contactFragment, false);
}
        break;
    case  R.id.radiobutton3:
if (callLogsFragment ==null){
    callLogsFragment = new CallLogsFragment();
    fragments.add(callLogsFragment);
    hideOthersFragment(callLogsFragment, true);
} else  {
    hideOthersFragment(callLogsFragment, false);
}
        break;
}
    }
});
    }

//加载fragment
    public  void hideOthersFragment(Fragment showFragment, boolean isAdd){
        transaction = fragmentManager.beginTransaction();
if (isAdd){
    transaction.add(R.id.framelayout,showFragment);
}

for (Fragment fragment : fragments){
    if (showFragment.equals(fragment)){
        transaction.show(fragment);
    } else {
        transaction.hide(fragment);
    }

}
transaction.commit();
    }



    //获取到权限后执行的函数
    /*@Override
    public void permissionsOK() {
        super.permissionsOK();
//        init();

    }*/






}