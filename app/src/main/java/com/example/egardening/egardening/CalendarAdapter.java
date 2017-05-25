package com.example.egardening.egardening;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.egardening.egardening.Entities.Plant;

/**
 * Created by Ayman on 24/02/2015.
 */
public class CalendarAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    Plant[] data;
    String season;

    public CalendarAdapter(Plant[] data, Context context, String season) {
        super();
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        this.season = season;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = inflater.inflate(R.drawable.calendar_row, null);



        final ImageView plantThumb = (ImageView) convertView.findViewById(R.id.cal_plant_col);
        FrameLayout month1_seed = (FrameLayout) convertView.findViewById(R.id.cal_col1_seed);
        FrameLayout month2_seed = (FrameLayout) convertView.findViewById(R.id.cal_col2_seed);
        FrameLayout month3_seed = (FrameLayout) convertView.findViewById(R.id.cal_col3_seed);
        FrameLayout month1_flower = (FrameLayout) convertView.findViewById(R.id.cal_col1_flower);
        FrameLayout month2_flower = (FrameLayout) convertView.findViewById(R.id.cal_col2_flower);
        FrameLayout month3_flower = (FrameLayout) convertView.findViewById(R.id.cal_col3_flower);
        FrameLayout month1_fruit = (FrameLayout) convertView.findViewById(R.id.cal_col1_fruit);
        FrameLayout month2_fruit = (FrameLayout) convertView.findViewById(R.id.cal_col2_fruit);
        FrameLayout month3_fruit = (FrameLayout) convertView.findViewById(R.id.cal_col3_fruit);
        FrameLayout month1_cut = (FrameLayout) convertView.findViewById(R.id.cal_col1_cut);
        FrameLayout month2_cut = (FrameLayout) convertView.findViewById(R.id.cal_col2_cut);
        FrameLayout month3_cut = (FrameLayout) convertView.findViewById(R.id.cal_col3_cut);

        int thumbId = convertView.getResources().getIdentifier("p"+data[position].getPlant_id() , "drawable", "com.example.egardening.egardening");

        //Get dynamic width of cell tom make height equal to it
        plantThumb.setBackgroundResource(thumbId);


        //int width;

        /*ViewTreeObserver vto = plantThumb.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                plantThumb.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = plantThumb.getMeasuredWidth();
                System.out.println("cell width = " + width);
                plantThumb.getLayoutParams().height = width;
            }



        });*/


        /////

        int seeding_start = data[position].getSeeding_start();
        int seeding_end = data[position].getSeeding_end();
        int flowering_start = data[position].getFlowering_start();
        int flowering_end = data[position].getFlowering_end();
        int fruiting_start = data[position].getFruiting_start();
        int fruiting_end = data[position].getFruiting_end();
        int cutting_start = data[position].getCutting_start();
        int cutting_end = data[position].getCutting_end();

        if((seeding_start!=0) && (seeding_end!=0))
            fillCalendar(month1_seed, month2_seed, month3_seed, "#e08649", seeding_start, seeding_end);
        if((flowering_start!=0) && (flowering_end!=0))
            fillCalendar(month1_flower, month2_flower, month3_flower, "#e76e66", flowering_start, flowering_end);
        if((fruiting_start!=0) && (fruiting_end!=0))
            fillCalendar(month1_fruit, month2_fruit, month3_fruit, "#1b7e5a", fruiting_start, fruiting_end);
        if((cutting_start!=0) && (cutting_end!=0))
            fillCalendar(month1_cut, month2_cut, month3_cut, "#579aa9", cutting_start, cutting_end);



        return convertView;
    }

    public void fillCalendar(FrameLayout m1, FrameLayout m2, FrameLayout m3, String color, int start, int end) {

        boolean[] period = new boolean[12];

        for(int i=0; i<period.length; i++)
            period[i] = false;

        if(start <= end) {
            for(int i=start-1; i<=end-1; i++)
                period[i] = true;
        }

        else if(start > end) {
            for(int i=start-1; i<=11; i++)
                period[i] = true;
            for(int i=0; i<=end-1; i++)
                period[i] = true;
        }

        if(season.equals("Spring")) {
            /*if(period[2]) m1.setText(period_name);
            if(period[3]) m2.setText(period_name);
            if(period[4]) m3.setText(period_name);*/
            if(period[2]) m1.setBackgroundColor(Color.parseColor(color));
            if(period[3]) m2.setBackgroundColor(Color.parseColor(color));
            if(period[4]) m3.setBackgroundColor(Color.parseColor(color));
        }

        if(season.equals("Summer")) {
            /*if(period[5]) m1.setText(period_name);
            if(period[6]) m2.setText(period_name);
            if(period[7]) m3.setText(period_name);*/
            if(period[5]) m1.setBackgroundColor(Color.parseColor(color));
            if(period[6]) m2.setBackgroundColor(Color.parseColor(color));
            if(period[7]) m3.setBackgroundColor(Color.parseColor(color));
        }

        if(season.equals("Autumn")) {
            /*if(period[8]) m1.setText(period_name);
            if(period[9]) m2.setText(period_name);
            if(period[10]) m3.setText(period_name);*/
            if(period[8]) m1.setBackgroundColor(Color.parseColor(color));
            if(period[9]) m2.setBackgroundColor(Color.parseColor(color));
            if(period[10]) m3.setBackgroundColor(Color.parseColor(color));
        }

        if(season.equals("Winter")) {
            /*if(period[11]) m1.setText(period_name);
            if(period[0]) m2.setText(period_name);
            if(period[1]) m3.setText(period_name);*/
            if(period[11]) m1.setBackgroundColor(Color.parseColor(color));
            if(period[0]) m2.setBackgroundColor(Color.parseColor(color));
            if(period[1]) m3.setBackgroundColor(Color.parseColor(color));
        }
    }

}
