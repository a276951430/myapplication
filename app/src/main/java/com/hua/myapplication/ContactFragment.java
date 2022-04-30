package com.hua.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.net.ipsec.ike.IkeTunnelConnectionParams;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.InputStream;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactFragment extends  ParentFragment {

    //活动对象
    private Activity activity;
    //显示联系人的列表
    private ListView contactListView;
//适配器
    private  ContactAdapter contactAdapter;
//联系人集合
private  List<Contact> contactList = new ArrayList<>();
    //自定义侧边栏
    private  SideBar sideBar;
//工具类对象
    private  SelectData selectData;
//下拉刷新
    private SwipeRefreshLayout swipeRefreshLayout;
//内容提供器
    private  ContentResolver resolver;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ContactFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactFragment newInstance(String param1, String param2) {
        ContactFragment fragment = new ContactFragment();
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
        View view =  inflater.inflate(R.layout.fragment_contact, container, false);
        activity = getActivity();
        getPer(activity, addPermissionsList());
        init(view);

        return  view;
    }

    public  void init(View view){
        contactListView = (ListView) view.findViewById(R.id.contact_list);
sideBar = (SideBar) view.findViewById(R.id.side_bar);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.contact_switerrefreshlayout);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.background_light);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        contactListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final  int position =i;
                AlertDialog dialog =new AlertDialog.Builder(activity).setTitle("删除确认？").setMessage("您确定要将"+contactList.get(position).getName()+"从您的联系人列表中删除吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteContact(position);
                    }
                }).setNegativeButton("取消", null).create();
                dialog.show();

                return false;
            }
        });
        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(activity, ContactAndCallActivity.class);
                intent.putExtra("id", contactList.get(i).getId());
intent.putExtra("name", contactList.get(i).getName());
intent.putExtra("from", contactList.get(i).getFrom());
startActivity(intent);
            }
        });
    }

    //删除联系人函数
    public  void deleteContact(int i){
                selectData.deleteContact(resolver, ContactsContract.RawContacts.CONTACT_ID+"=?", new String[]{contactList.get(i).getId()});
        selectContact();
    }

    //重写添加要申请的权限函数，这里申请读取和写入联系人权限
    @Override
    public List<String> addPermissionsList() {
        super.addPermissionsList();
        List<String> permissionList =new ArrayList<>();
        permissionList.add(android.Manifest.permission.READ_CONTACTS);
        permissionList.add(Manifest.permission.WRITE_CONTACTS);
return  permissionList;
    }
//权限申请成功的函数
    @Override
    public void permissionsOK() {
        super.permissionsOK();
        //初始化内容提供器
        resolver =activity.getContentResolver();
        //初始化资源类
        selectData =new SelectData();
        //读取联系人
        selectContact();
        //下拉控件的回调
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉刷新，读取联系人函数
                selectContact();
                //刷新后停止动画
                swipeRefreshLayout.setRefreshing(false);
            }
        });
//侧边栏监听事件
        sideBar.setOnStrSelectCallBack(new SideBar.ISideBarSelectCallBack() {
    @Override
    public void onSelectStr(int index, String selectStr) {
        //如果联系人集合不为空
        if (contactList !=null){
            //便利集合并判断当前字母和联系人首字母是否一致
            for (int i =0;i <contactList.size();i++){
                if (selectStr.equals(contactList.get(i).getFirstStr())){
                    //判断成功，滑动列表并return
                    contactListView.setSelection(i);
                    return;
                }
            }
        }
    }
});
    }

    //读取联系人信息
    public  void selectContact() {
//清除集合数据，以前遇到过不清除闪退的情况
        contactList.clear();
        contactList =selectData.getAllContacts(resolver);
contactAdapter =new ContactAdapter(activity, contactList);
contactListView.setAdapter(contactAdapter);

        /*contactList.clear();
        //获取联系人信息的uri
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        //获取ContentResolver
        ContentResolver contentResolver = activity.getContentResolver();
//查询数据返回Cursor
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
while (cursor.moveToNext()){
Contact contact = new Contact();
    //获取联系人的id
    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
    contact.setId(contactId);
    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
    contact.setName(name);
//查询电话类型的数据操作
    List<String> numberList = new ArrayList<>();
    Cursor phonesCursor =contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"="+contactId, null, null);
    while (phonesCursor.moveToNext()){

        String phoneNumber = phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        //添加phone的信息。
        numberList.add(phoneNumber);
    }
    phonesCursor.close();
    contact.setNumber(numberList);

contact.setBitmap(getContactBitmap(contactId));

    Cursor emailCursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID+"="+contactId, null, null);
while(emailCursor.moveToNext()){

    String email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

}
emailCursor.close();

Cursor accountCursor = contentResolver.query(ContactsContract.RawContacts.CONTENT_URI,null ,ContactsContract.RawContacts.CONTACT_ID+"="+contactId ,null, null );
while (accountCursor.moveToNext()){
String accountName = accountCursor.getString(accountCursor.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_NAME));
    contact.setFrom(updateAccountNameString(accountName));
String pinyin = accountCursor.getString(accountCursor.getColumnIndex(ContactsContract.RawContacts.SORT_KEY_ALTERNATIVE));
contact.setPinyin(pinyin);

} accountCursor.close();

    Cursor addressCursor = contentResolver.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, null, ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID+"="+contactId, null, null);
while (addressCursor.moveToNext()){
    String work = addressCursor.getString(addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.DATA));

} addressCursor.close();
String orgWhere = ContactsContract.Data.CONTACT_ID+"= ? AND " + ContactsContract.Data.MIMETYPE+"=?";
String[] orgWhereTarams = new String[]{contactId, ContactsContract.CommonDataKinds.Organization.CONTACT_ID};
Cursor orgCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, null, orgWhere, orgWhereTarams, null);
while (orgCursor.moveToNext()){
    String company = addressCursor.getString(orgCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
    String title = orgCursor.getString(orgCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));

} orgCursor.close();

contactList.add(contact);

} cursor.close();

contactList =sorts(contactList);
contactAdapter = new ContactAdapter(activity, contactList);
        contactListView.setAdapter(contactAdapter);*/
    }

}