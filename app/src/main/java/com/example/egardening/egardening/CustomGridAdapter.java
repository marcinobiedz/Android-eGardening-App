package com.example.egardening.egardening;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Ayman on 20/02/2015.
 */
public class CustomGridAdapter extends BaseAdapter {

    private Context context;
    private String[] plantNames;
    private int[] plantIds;
    LayoutInflater inflater;
    public boolean CHECK_ENEBLED = false;

    public CustomGridAdapter(Context context, String[] names, int[] ids) {
        this.context = context;
        this.plantNames = names;
        this.plantIds = ids;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.drawable.grid_cell, null);
        }
        //Button button = (Button) convertView.findViewById(R.id.grid_item);
        GridViewItem container = (GridViewItem) convertView.findViewById(R.id.cell_container);
        container.onMeasure(150,150);

        TextView plantName = (TextView) convertView.findViewById(R.id.grid_plant_name);
        TextView plantId = (TextView) convertView.findViewById(R.id.grid_plant_id);
        plantName.setText(plantNames[position]);
        //plantId.setText("ID: "+String.valueOf(plantIds[position]));

        /////////Setting cell thumbnail
        int cellNormal = convertView.getResources().getIdentifier("p"+plantIds[position] , "drawable", "com.example.egardening.egardening");
        int cellChecked = R.drawable.selected_plant;
        Resources resources = convertView.getResources();

        if(CHECK_ENEBLED) {
            StateListDrawable states = new StateListDrawable();
            Drawable[] drawable = {resources.getDrawable(cellNormal), resources.getDrawable(cellChecked)};
            LayerDrawable layers = new LayerDrawable(drawable);

            states.addState(new int[] {android.R.attr.state_checked}, layers);
            states.addState(new int[] {}, resources.getDrawable(cellNormal));
            container.setBackgroundDrawable(states);
        }

        else {
            container.setBackground(resources.getDrawable(cellNormal));
        }

        /////////
        container.setPadding(10,10,10,10);

        return convertView;
    }

    @Override
    public int getCount() {
        return plantNames.length;
    }

    @Override
    public Object getItem(int position) {
        return plantNames[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



}
