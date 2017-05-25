package com.example.egardening.egardening;

import android.graphics.Typeface;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.app.Activity;
import android.widget.*;

/**
 * Created by Ayman on 25/12/2014.
 */
public class CustomListAdapter extends ArrayAdapter<MenuItem> {

        Context context;
        int layoutResourceId;
        MenuItem data[] = null;

        public CustomListAdapter(Context context, int layoutResourceId, MenuItem[] data) {
            super(context, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.data = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ItemHolder holder = null;

            Typeface menuFont = Typeface.createFromAsset(getContext().getAssets(), "IcoMoon-Free.ttf");

            if(row == null)
            {
                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);

                holder = new ItemHolder();
                holder.txtIcon = (TextView) row.findViewById(R.id.item_icon);
                holder.txtIcon.setTypeface(menuFont);
                holder.txtTitle = (TextView)row.findViewById(R.id.item_text);

                row.setTag(holder);
            }

            else
            {
                holder = (ItemHolder)row.getTag();
            }

            MenuItem item = data[position];
            holder.txtTitle.setText(item.title);
            holder.txtIcon.setText(item.icon);
            holder.txtIcon.setTypeface(menuFont);

            return row;
        }

        static class ItemHolder
        {
            TextView txtIcon;
            TextView txtTitle;
        }

    

}
