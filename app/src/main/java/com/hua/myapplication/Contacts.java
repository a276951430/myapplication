package com.hua.myapplication;

import android.graphics.Bitmap;

import java.util.List;

//联系人类
class Contact  {
//联系人id
   private  String id;
   //联系人名称
   private  String name;
   //联系人头像
   private Bitmap bitmap;
   //联系人来源账户，卡1、卡2、手机
   private  String from;
//联系人电话，因为不止一个，这里使用列表记录
   private List<String> number;
//联系人拼音
   private  String pinyin;
//联系人首字母
private String firstStr;


   public String getId() {
      return id;
   }

   public String getName(){
      return  name;
   }

   public Bitmap getBitmap(){
      return  bitmap;
   }

   public String getFrom(){
      return  from;
   }

   public List<String> getNumber() {
      return number;
   }

   public void setNumber(List<String> number) {
      this.number = number;
   }

   public void setId(String id) {
      this.id = id;
   }

   public void setBitmap(Bitmap bitmap){
      this.bitmap =bitmap;
   }

   public void setFrom(String from) {
      this.from = from;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getPinyin() {
      return pinyin;
   }

   public void setPinyin(String pinyin) {
      this.pinyin = pinyin;
   }

   public String getFirstStr() {
      return firstStr;
   }

   public void setFirstStr(String firstStr) {
      this.firstStr = firstStr;
   }
}
