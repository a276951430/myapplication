package com.hua.myapplication;

class MyCall_logs {

   //通话记录状态标识，1已接， 2已拨， 3未接
   private  int call_type;
//名字
   private  String name;
   //电话号码
   private  String number;
   //通话记录发生的时间
   private  Long time;
   //通话时长，单位是秒；
   private  int duration;
//归属地
   private  String geocode_location;
   //卡槽id

   private  int sim_id;

   public int getCall_type() {
      return call_type;
   }

   public String getName() {
      return name;
   }

   public String getNumber() {
      return number;
   }

   public int getDuration() {
      return duration;
   }

   public Long getTime() {
      return time;
   }

   public void setCall_type(int call_type) {
      this.call_type = call_type;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setNumber(String number) {
      this.number = number;
   }

   public void setTime(Long time) {
      this.time = time;
   }

   public void setDuration(int duration) {
      this.duration = duration;
   }

   public String getGeocode_location() {
      return geocode_location;
   }

   public void setGeocode_location(String geocode_location) {
      this.geocode_location = geocode_location;
   }

   public int getSim_id() {
      return sim_id;
   }

   public void setSim_id(int sim_id) {
      this.sim_id = sim_id;
   }
}
