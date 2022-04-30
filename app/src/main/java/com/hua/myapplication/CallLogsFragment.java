package com.hua.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.CallLog;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.SimpleFormatter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CallLogsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CallLogsFragment extends ParentFragment {


    private Activity activity;
private  ListView listView;
//内容提供其对象
private  ContentResolver resolver;
//查询工具类对象。
SelectData selectData;
//存放通话记录数据的集合。
private  List<MyCall_logs> myCall_logsList;
//adapter对象。
    private  CallLogAdapter callLogAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CallLogsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CallLogsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CallLogsFragment newInstance(String param1, String param2) {
        CallLogsFragment fragment = new CallLogsFragment();
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
                View view = inflater.inflate(R.layout.fragment_call_logs, container, false);
        activity = getActivity();
                getPer(activity, addPermissionsList());
init(view);
                return  view;
            }



            public  void init(View view){

        listView = (ListView) view.findViewById(R.id.call_logs_list);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.switerrefreshlayout);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.background_light);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final  int position =i;
                AlertDialog dialog =new AlertDialog.Builder(activity).setTitle("删除确认？").setMessage("您确定要删除这条通话记录吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteCall(position);
                    }
                }).setNegativeButton("取消", null).create();
               dialog.show();

                return false;
            }
        });
            }

            public  void deleteCall(int i){
                selectData.deleteCallLog(resolver, "number=? and date=?", new String[]{myCall_logsList.get(i).getNumber(), myCall_logsList.get(i).getTime().toString()});
                selectCallLogs();
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
        resolver = activity.getContentResolver();
        selectData= new SelectData();
        selectCallLogs();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                 selectCallLogs();
                 swipeRefreshLayout.setRefreshing(false);
            }
        });
listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
String id =selectData.getOneContactId(resolver, myCall_logsList.get(i).getNumber(), myCall_logsList.get(i).getName());

        Intent intent = new Intent(activity, ContactAndCallActivity.class);
        intent.putExtra("id", id);
            String name =myCall_logsList.get(i).getName();
            if (name ==null){
                name =myCall_logsList.get(i).getNumber();
            }
            intent.putExtra("name", name);

            intent.putExtra("from", "卡"+(myCall_logsList.get(i).getSim_id()+1));
            if (id.equals("-1")){
            intent.putExtra("number", myCall_logsList.get(i).getNumber());

        }
        startActivity(intent);
    }
});
    }

    public  void selectCallLogs(){
 myCall_logsList = selectData.selectAllOrwhereCall(resolver, null);
 //if (callLogAdapter ==null) {
     callLogAdapter = new CallLogAdapter(activity, myCall_logsList);
 //} else  {

 //}

        listView.setAdapter(callLogAdapter);
            }

}