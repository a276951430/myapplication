package com.hua.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

class ContactAdapter extends BaseAdapter {

   private Context context;
private  List<Contact> contactList;



public  ContactAdapter(Context context,  List<Contact> contactList){
this.context =context;
this.contactList =contactList;
//this.layoutInflater =inflater;
}

   @Override
   public int getCount() {
      return contactList.size();
   }

   @Override
   public Object getItem(int i) {
      return contactList.get(i);
   }

   @Override
   public long getItemId(int i) {
      return i;
   }

   @Override
   public View getView(int i, View view, ViewGroup viewGroup) {
   Contact contact = contactList.get(i);
ViewHolder viewHolder;
if (view ==null) {
   viewHolder = new ViewHolder();
   view = LayoutInflater.from(context).inflate(R.layout.contactlist_layout, null);
   viewHolder.imageView = (ImageView) view.findViewById(R.id.contact_image);
   viewHolder.textView = (TextView) view.findViewById(R.id.contact_text);

   viewHolder.fromTextView = (TextView) view.findViewById(R.id.contact_from);
viewHolder.a_z =(TextView) view.findViewById(R.id.contact_a_z);
   view.setTag(viewHolder);
} else  {
   viewHolder = (ViewHolder) view.getTag();
}
   viewHolder.textView.setText(contact.getName());
if (contact.getBitmap() !=null){
   viewHolder.imageView.setImageBitmap(contact.getBitmap());
} else  {
   viewHolder.imageView.setImageResource(R.drawable.contact_drawable);
}
      viewHolder.fromTextView.setText(contact.getFrom());


if (i ==getPositionForSection(contact.getFirstStr())){
   viewHolder.a_z.setVisibility(View.VISIBLE);
   viewHolder.a_z.setText(contact.getFirstStr());

} else {
   viewHolder.a_z.setVisibility(View.GONE);
}


return view;
   }

   public  int getPositionForSection(String catalog){
for (int i =0;i <getCount();i++){
   String str = contactList.get(i).getFirstStr();
if (catalog.equalsIgnoreCase(str)){
   return  i;
}
}
return  -1;
   }

   class  ViewHolder{

   ImageView imageView;
   TextView textView;
   TextView fromTextView;
   TextView a_z;
   }

}
