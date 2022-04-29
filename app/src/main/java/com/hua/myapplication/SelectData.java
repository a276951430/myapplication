package com.hua.myapplication;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;

import java.io.InputStream;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.recyclerview.widget.AsyncListDiffer;

class SelectData {

    //查询所有通话记录或指定号码查询
    public List<MyCall_logs> selectAllOrwhereCall(ContentResolver resolver, String where) {
        List<MyCall_logs> callLogs = new ArrayList<>();
        Uri uri = CallLog.Calls.CONTENT_URI;
        Cursor cursor = resolver.query(uri, null, where, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        while (cursor.moveToNext()) {
            MyCall_logs myCall_logs = new MyCall_logs();
            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
            myCall_logs.setName(name);
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            myCall_logs.setNumber(number);
            long dateLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
            myCall_logs.setTime(dateLong);
            String day = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(dateLong));

            int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));
            myCall_logs.setDuration(duration);

            int call_type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
            myCall_logs.setCall_type(call_type);
            String geocode_location = cursor.getString(cursor.getColumnIndex(CallLog.Calls.GEOCODED_LOCATION));
            myCall_logs.setGeocode_location(geocode_location);
            int sim_id = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID));
            myCall_logs.setSim_id(sim_id);
            callLogs.add(myCall_logs);
        }
        cursor.close();
        return callLogs;
    }

    //删除通话记录
    public int deleteCallLog(ContentResolver resolver, String where, String[] values) {
        int result = resolver.delete(CallLog.Calls.CONTENT_URI, where, values);
        return result;
    }

    //删除通话记录
    public int deleteContact(ContentResolver resolver, String where, String[] values) {
        int result = resolver.delete(ContactsContract.RawContacts.CONTENT_URI, where, values);
        return result;
    }



    //联系人排序函数
public  List<Contact> sorts(List<Contact> contactList){
    Collections.sort(contactList, new Comparator<Contact>(){
        public Collator getInstance(){
            return Collator.getInstance(Locale.CHINA);
        }
        @Override
        public int compare(Contact contact, Contact t1) {
            int con_axcii =contact.getPinyin().toUpperCase().charAt(0);
            int t1_ascii = t1.getPinyin().toUpperCase().charAt(0);
            if (con_axcii<65 ||con_axcii>90){
                return  1;
            } else  if (t1_ascii<65||t1_ascii>90){
                return  -1;
            } else {
                return contact.getPinyin().toUpperCase().compareTo(t1.getPinyin().toUpperCase());
            }
        }
    });
    return  contactList;
}


    public  String getOneContactId(ContentResolver resolver, String phone, String name){
String contentId ="-1";

        Uri uri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI, Uri.encode(phone));
        Cursor cursor =resolver.query(uri, null, null, null, null);
        while (cursor.moveToNext()){
String id  =  cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
        if (name !=null){
            Cursor cursor1 =resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            while (cursor1.moveToNext()) {
                String co_id = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));
                String co_name = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
if (name.equals(co_name) &&id.equals(co_id)){
    contentId =id;
}
            } cursor1.close();

        } else  {
            contentId =id;
        }
        }cursor.close();


        return  contentId;
    }


    //获取所有联系人信息
    public List<Contact> getAllContacts(ContentResolver resolver) {
        List<Contact> contactList = new ArrayList<>();
                //获取联系人信息的uri
                Uri     uri = ContactsContract.Contacts.CONTENT_URI;
//查询数据返回Cursor
        Cursor cursor = resolver.query(uri, null, null, null, null);
        while (cursor.moveToNext()) {
            Contact contact = new Contact();
            //获取联系人的id
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            contact.setId(contactId);
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            contact.setName(name);
            contact.setBitmap(getContactBitmap(resolver, contactId));
contact.setFrom(getContactFrom(resolver, contactId));
String pin =getContactPinyin(resolver, contactId);
contact.setPinyin(pin);
contact.setFirstStr(getContactFirstStr(pin));
            contactList.add(contact);
        }
        cursor.close();
        contactList = sorts(contactList);
        return  contactList;
    }

    //查询联系人电话号码
    public List<String> getContactNumber(ContentResolver resolver, String contactId) {
        //查询电话类型的数据操作
        List<String> numberList = new ArrayList<>();
        Cursor phonesCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
        while (phonesCursor.moveToNext()) {
            String phoneNumber = phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            //添加phone的信息。
            numberList.add(phoneNumber);
        }
        phonesCursor.close();
        return numberList;
    }

    //获取头像
    public Bitmap getContactBitmap(ContentResolver resolver,String contactId) {
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));
//打开头像图片的InputStream
        InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        /*if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.contact_drawable);
        }*/
        return bitmap;
    }

    //获取联系人来源，手机还是sim卡
    public String getContactFrom(ContentResolver resolver, String contactId) {
        String account_name="";
        Cursor accountCursor = resolver.query(ContactsContract.RawContacts.CONTENT_URI, null, ContactsContract.RawContacts.CONTACT_ID + "=" + contactId, null, null);
        while (accountCursor.moveToNext()) {
            account_name = accountCursor.getString(accountCursor.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_NAME));

        }
        accountCursor.close();

            if (account_name.equalsIgnoreCase("Phone")) {
                account_name = "手机";
            } else if (account_name.equals("sim1")) {
                account_name = "卡1";
            } else if (account_name.equals("sim2")) {
                account_name = "卡2";
            } else {
                account_name = "未知";
            }
            return account_name;
    }

    public  String getContactPinyin(ContentResolver resolver, String contactId){
        String pinyin ="";
        Cursor accountCursor =resolver.query(ContactsContract.RawContacts.CONTENT_URI, null, ContactsContract.RawContacts.CONTACT_ID + "=" + contactId, null, null);
        while (accountCursor.moveToNext()) {
            pinyin = accountCursor.getString(accountCursor.getColumnIndex(ContactsContract.RawContacts.SORT_KEY_ALTERNATIVE));

        }
        accountCursor.close();
return  pinyin;
    }
//获取首字母，用于排序
    public  String getContactFirstStr(String pinyin){
        String firstStr;
        char charA_Z = pinyin.toUpperCase().charAt(0);
        if (charA_Z <65||charA_Z>90){
            firstStr ="#";
        } else  {
            firstStr = String.valueOf(charA_Z);
        }
        return  firstStr;
    }

    //获取电子邮箱
    public  String getContactEmail(ContentResolver resolver, String contactId){
        String email ="";
        Cursor emailCursor = resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + contactId, null, null);
        while (emailCursor.moveToNext()) {
            email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
        }
        emailCursor.close();
        return  email;
    }

//获取住址
 public  String getContactWork(ContentResolver resolver, String contactId){
     String work ="";
        Cursor addressCursor = resolver.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, null, ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + "=" + contactId, null, null);
     while (addressCursor.moveToNext()) {
         work = addressCursor.getString(addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.DATA));
     }
     addressCursor.close();
     return work;
 }


 /*public  List<Map>getContactData(ContentResolver resolver, String contactId){
     //获取公司和职位
        String orgWhere = ContactsContract.Data.CONTACT_ID + "= ? AND " + ContactsContract.Data.MIMETYPE + "=?";
     String[] orgWhereTarams = new String[]{contactId, ContactsContract.CommonDataKinds.Organization.CONTACT_ID};
     Cursor orgCursor = resolver.query(ContactsContract.Data.CONTENT_URI, null, orgWhere, orgWhereTarams, null);
     while (orgCursor.moveToNext()) {
         String company = addressCursor.getString(orgCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
         String title = orgCursor.getString(orgCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));

     }
     orgCursor.close();
     return  null;
 }*/




}