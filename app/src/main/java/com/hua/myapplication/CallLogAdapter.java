package com.hua.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class CallLogAdapter extends BaseAdapter {

 private Context context;
 private List<MyCall_logs> myCall_logsList;


public CallLogAdapter(Context context, List<MyCall_logs> myCall_logsList){
 this.context =context;
 this.myCall_logsList = myCall_logsList;
}

 @Override
 public int getCount() {
  return myCall_logsList.size();
 }

 @Override
 public Object getItem(int i) {
  return myCall_logsList.get(i);
 }

 @Override
 public long getItemId(int i) {
  return i;
 }

 @Override
 public View getView(int i, View view, ViewGroup viewGroup) {
MyCall_logs myCall_logs = myCall_logsList.get(i);
ViewHolder viewHolder;
if (view ==null){
 viewHolder = new ViewHolder();
 view = LayoutInflater.from(context).inflate(R.layout.callloglist_layout, null);
 viewHolder.call_imageView = (ImageView) view.findViewById(R.id.type_image);
 viewHolder.call_textview =(TextView) view.findViewById(R.id.call_log_number);
 viewHolder.call_sim = (TextView) view.findViewById(R.id.call_log_sum_id);
 viewHolder.call_geocode_location =(TextView) view.findViewById(R.id.call_log_geocode_location);
 viewHolder.call_log_date = (TextView) view.findViewById(R.id.call_log_date);
 view.setTag(viewHolder);
} else  {
 viewHolder = (ViewHolder) view.getTag();
}
if (myCall_logs.getCall_type() ==1){
 viewHolder.call_imageView.setImageResource(R.drawable.type_1);
viewHolder.call_imageView.setContentDescription("已接电话");
} else  if (myCall_logs.getCall_type() ==2){
 viewHolder.call_imageView.setImageResource(R.drawable.type_2);
viewHolder.call_imageView.setContentDescription("已拨电话");
} else{
 viewHolder.call_imageView.setImageResource(R.drawable.type_3);
 viewHolder.call_imageView.setContentDescription("未接电话");
}
if (myCall_logs.getName() ==null){
 viewHolder.call_textview.setText(myCall_logs.getNumber());
} else  {
 viewHolder.call_textview.setText(myCall_logs.getName());
}
String geocode_location =myCall_logs.getGeocode_location();
if (geocode_location ==null){
 geocode_location ="未知";
}
viewHolder.call_sim.setText("卡"+(myCall_logs.getSim_id()+1));
 viewHolder.call_geocode_location.setText(geocode_location);
long dateLong = myCall_logs.getTime();
String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(dateLong));
String time = new SimpleDateFormat("HH:mm").format(new Date(dateLong));
String ddayCurrent = new  SimpleDateFormat("dd").format(new Date());
String dayRecord = new SimpleDateFormat("dd").format(new Date(dateLong));
if (Integer.parseInt(dayRecord) == Integer.parseInt(ddayCurrent)){
 viewHolder.call_log_date.setText(time);
} else  if (Integer.parseInt(dayRecord) ==(Integer.parseInt(ddayCurrent) -1)){
 viewHolder.call_log_date.setText("昨天："+time);
} else  {
 viewHolder.call_log_date.setText(date);
}
return  view;
 }




 class  ViewHolder{

 ImageView call_imageView;
 TextView call_textview;
 TextView call_sim;
 TextView call_geocode_location;
 TextView call_log_date;
 }

}
