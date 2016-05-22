package couchbase.com.mvig_android_user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.*;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mustafa on 20.5.2016.
 */
public class CustomAdapter extends ArrayAdapter<Message> {

        Context context;
        int layoutResourceId;
        List<Message> bankalar = null;
        public CustomAdapter(Context context, int resource, List<Message> list) {
            super(context, resource, list);
            this.context = context;
            this.layoutResourceId = resource;
            this.bankalar = list;
            }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            bankaViewHolder holder = null;
            if(row == null){
                LayoutInflater inflater = LayoutInflater.from(context);

                row = inflater.inflate(layoutResourceId, parent, false);

                holder = new bankaViewHolder();

                holder.img = (ImageView) row.findViewById(R.id.icon);
                holder.baslik = (TextView) row.findViewById(R.id.txt_message);
                holder.detay = (TextView) row.findViewById(R.id.txt_telephonenumber);

                row.setTag(holder);
                }
            else{
                holder = (bankaViewHolder) row.getTag();
            }

            Message bBilgi = bankalar.get(position);

            holder.baslik.setText(bBilgi.getMessage_body());
            holder.detay.setText(bBilgi.getMessag_receiver());
            return row;
            }

        static class bankaViewHolder{
        ImageView img;
        TextView baslik;
        TextView detay;
        }
    }

