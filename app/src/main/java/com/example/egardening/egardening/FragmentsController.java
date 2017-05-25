package com.example.egardening.egardening;

/**
 * Created by Ayman on 25/12/2014.
 */

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.Iterator;

import com.example.egardening.egardening.Entities.Plant;
import com.example.egardening.egardening.Session.App;
import com.facebook.Session;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;


public class FragmentsController extends Fragment {

    TextView text;

    ArrayList<Plant> dictionary;
    ArrayList<Integer> checkedPlantsIds;
    ArrayList<Plant> plantsInGarden;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {

        View view = null;

        Bundle b = getArguments();
        int itemPosition = b.getInt("Menu");


        if ((itemPosition == 0) || (itemPosition == 1)) {
            view = inflater.inflate(R.layout.my_garden_fragment, container, false);
            manageMyGarden(view);
        } else if (itemPosition == 2) {
            view = inflater.inflate(R.layout.select_fragment, container, false);
            manageSelect(view);
        } else if (itemPosition == 3) {
            view = inflater.inflate(R.layout.calendar_fragment, container, false);
            manageCalendar(view);
        } else if (itemPosition == 4) {
            view = inflater.inflate(R.layout.meteo, container, false);
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            WeatherClass weather = new WeatherClass(view, cm);
            weather.manageWeather();
        } else if (itemPosition == 5) {
            IntentIntegrator integrator = new IntentIntegrator(this.getActivity());
            integrator.initiateScan();
        } /*else if (itemPosition == 6) {
            view = inflater.inflate(R.layout.settings_fragment, container, false);
            manageSettings(view);
        }*/

        if (b.getSerializable("Plant") != null) {
            view = inflater.inflate(R.layout.details_fragment, container, false);
            managePlantDetails(view, b);
        }


        return view;
    }


    public void managePlantDetails(View view, Bundle b) {

        //Icons
        Typeface iconFont, iconFont2;
        TextView seedIcon = (TextView) view.findViewById(R.id.seedIcon);
        TextView flowerIcon = (TextView) view.findViewById(R.id.flowerIcon);
        TextView fruitIcon = (TextView) view.findViewById(R.id.fruitIcon);
        TextView cutIcon = (TextView) view.findViewById(R.id.cutIcon);
        TextView soilTypeIcon = (TextView) view.findViewById(R.id.soilTypeIcon);
        TextView soilPHIcon = (TextView) view.findViewById(R.id.soilPHIcon);
        TextView tempZoneIcon = (TextView) view.findViewById(R.id.tempZoneIcon);
        TextView removeIcon = (TextView) view.findViewById(R.id.removeIcon);

        iconFont = Typeface.createFromAsset(getActivity().getAssets(), "IcoMoon-Free.ttf");
        iconFont2 = Typeface.createFromAsset(getActivity().getAssets(), "GardenFont.ttf");
        cutIcon.setTypeface(iconFont2);
        seedIcon.setTypeface(iconFont2);
        flowerIcon.setTypeface(iconFont2);
        fruitIcon.setTypeface(iconFont2);
        cutIcon.setTypeface(iconFont2);
        soilTypeIcon.setTypeface(iconFont2);
        soilPHIcon.setTypeface(iconFont2);
        tempZoneIcon.setTypeface(iconFont2);
        removeIcon.setTypeface(iconFont2);

        seedIcon.setText("A");
        flowerIcon.setText("B");
        fruitIcon.setText("C");
        cutIcon.setText("D");
        soilTypeIcon.setText("E");
        soilPHIcon.setText("F");
        tempZoneIcon.setText("G");
        removeIcon.setText("H");
        ///

        Plant p = (Plant) b.getSerializable("Plant");
        FrameLayout image = (FrameLayout) view.findViewById(R.id.details_plant_img);
        TextView name = (TextView) view.findViewById(R.id.propName);
        TextView cat = (TextView) view.findViewById(R.id.propCat);
        TextView shape = (TextView) view.findViewById(R.id.propShape);
        TextView seeding = (TextView) view.findViewById(R.id.propSeed);
        TextView flowering = (TextView) view.findViewById(R.id.propFlower);
        TextView fruiting = (TextView) view.findViewById(R.id.propFruit);
        TextView cutting = (TextView) view.findViewById(R.id.propCut);
        TextView soilType = (TextView) view.findViewById(R.id.propSoilType);
        TextView soilPH = (TextView) view.findViewById(R.id.propSoilPH);
        TextView tempZone = (TextView) view.findViewById(R.id.propTemp);


        int imageId = view.getResources().getIdentifier("p" + p.getPlant_id(), "drawable", "com.example.egardening.egardening");
        Drawable d = view.getResources().getDrawable(imageId);
        image.setBackground(d);

        name.setText(p.getPlant_name());
        cat.setText(getCategory(p.getCategory_id()));
        shape.setText(getShape(p.getShape_id()));
        seeding.setText(periodToString(p.getSeeding_start(), p.getSeeding_end()));
        flowering.setText(periodToString(p.getFlowering_start(), p.getFlowering_end()));
        fruiting.setText(periodToString(p.getFruiting_start(), p.getFruiting_end()));
        cutting.setText(periodToString(p.getCutting_start(), p.getCutting_end()));
        soilType.setText(getSoilType(p.getSoil_type_id()));
        soilPH.setText(getSoilPH(p.getSoil_ph_id()));
        tempZone.setText(getTempZone(p.getTemp_zone_id()));

        ////remove button
        LinearLayout removePlant = (LinearLayout) view.findViewById(R.id.removePlant);
        removePlant.setVisibility(LinearLayout.INVISIBLE);

        if (b.getString("Source").equals("Garden")) {
            final String plantId = String.valueOf(p.getPlant_id());
            removePlant.setVisibility(LinearLayout.VISIBLE);
            removePlant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new RemoveFromGardenTask().execute(plantId);
                }
            });
        }


    }

    public String getCategory(int id) {
        if (id == 3) return "Perennial";
        if (id == 4) return "Coniferous";
        if (id == 5) return "Deciduous";
        if (id == 6) return "Fruit";
        if (id == 7) return "Climber";
        if (id == 8) return "Heather";
        if (id == 9) return "Irrelevant";
        if (id == 10) return "Rose";
        return "NA";
    }

    public String getShape(int id) {
        if (id == 1) return "Tree";
        if (id == 2) return "Small Tree";
        if (id == 3) return "Bush";
        if (id == 4) return "Shurb";
        if (id == 5) return "Climber";
        if (id == 6) return "Perennial";
        return "NA";
    }

    public String getSoilType(int id) {
        if (id == 1) return "Acidic";
        if (id == 2) return "Sandy";
        if (id == 3) return "Garden";
        if (id == 4) return "Humus";
        if (id == 5) return "Clay";
        if (id == 6) return "Tolerant";
        return "NA";
    }

    public String getSoilPH(int id) {
        if (id == 1) return "Acidic";
        if (id == 2) return "Neutral";
        if (id == 3) return "Alkaline";
        if (id == 4) return "Tolerant";
        return "NA";
    }

    public String getTempZone(int id) {
        if (id == 1) return "1";
        if (id == 2) return "2";
        if (id == 3) return "3";
        if (id == 4) return "4";
        if (id == 5) return "5";
        if (id == 6) return "5a";
        if (id == 7) return "5b";
        if (id == 8) return "6";
        if (id == 9) return "6a";
        if (id == 10) return "6b";
        if (id == 11) return "7";
        if (id == 12) return "7a";
        if (id == 13) return "7b";
        if (id == 14) return "8";
        if (id == 15) return "8a";
        if (id == 16) return "8b";
        if (id == 17) return "9";
        if (id == 18) return "Irrelevant";
        return "NA";
    }

    public String periodToString(int start, int end) {
        if (start != 0 && end != 0) {
            if (start == end)
                return "During " + getMonth(start);
            else
                return "From " + getMonth(start) + " to " + getMonth(end);
        }

        return "NA";
    }

    public String getMonth(int m) {
        if (m == 1) return "Jan";
        if (m == 2) return "Feb";
        if (m == 3) return "Mar";
        if (m == 4) return "Apr";
        if (m == 5) return "May";
        if (m == 6) return "Jun";
        if (m == 7) return "Jul";
        if (m == 8) return "Aug";
        if (m == 9) return "Sep";
        if (m == 10) return "Oct";
        if (m == 11) return "Nov";
        if (m == 12) return "Dec";
        return null;
    }

    public void manageMyGarden(View view) {
        new LoadGardenTask().execute();
    }


    public void manageSelect(View view) {

        new PlantsDicitonaryTask().execute();
        LinearLayout addPlant = (LinearLayout) view.findViewById(R.id.addPlant);

        addPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddToGardenTask().execute();
                new LoadGardenTask().execute();///?
            }
        });

    }


    public void manageCalendar(View view) {
        new LoadGardenTask().execute();
    }//End manageCalendar()


    public void getSeason(TextView season, TextView m1, TextView m2, TextView m3) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);

        if (month == 11 || month == 0 || month == 1) {
            season.setText("Winter");
            m1.setText("Dec");
            m2.setText("Jan");
            m3.setText("Feb");
        } else if (2 <= month && month <= 4) {
            season.setText("Spring");
            m1.setText("Mar");
            m2.setText("Apr");
            m3.setText("May");
        } else if (5 <= month && month <= 7) {
            season.setText("Summer");
            m1.setText("Jun");
            m2.setText("Jul");
            m3.setText("Aug");
        } else if (9 <= month && month <= 11) {
            season.setText("Autumn");
            m1.setText("Sep");
            m2.setText("Oct");
            m3.setText("Nov");
        }

    }


    //Ayman 18/02/15 PlantsDicitonaryTask Inner Class
    class PlantsDicitonaryTask extends AsyncTask<String, String, Void> {

        String result = "";
        HttpEntity entity = null;
        HttpResponse response = null;


        private ProgressDialog progressDialog = new ProgressDialog(getActivity());

        protected void onPreExecute() {
            progressDialog.setMessage("Loading plants dictionary...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    PlantsDicitonaryTask.this.cancel(false);
                }
            });

        }

        @Override
        protected Void doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://egarden-pl.byethost17.com/egardening/app/getplants.php");

            try {

                Thread.sleep(2000);
                response = httpclient.execute(httppost);

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error converting result " + e.toString());
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            try {
                entity = response.getEntity();
                InputStream is = entity.getContent();

                JSONArray json_array = new JSONArray(convertStreamToString(is));
                if (json_array.length() > 0) {

                    dictionary = new ArrayList<Plant>();

                    for (int i = 0; i < json_array.length(); i++) {
                        JSONObject json_data = json_array.getJSONObject(i);
                        Plant plant = new Plant();
                        plant.setPlant_id(Integer.parseInt(json_data.getString("plant_id")));
                        plant.setPlant_name(json_data.getString("plant_name"));
                        plant.setShape_id(Integer.parseInt(json_data.getString("shape_id")));
                        plant.setCategory_id(Integer.parseInt(json_data.getString("category_id")));
                        plant.setFlowering_start(Integer.parseInt(json_data.getString("flowering_start")));
                        plant.setFlowering_end(Integer.parseInt(json_data.getString("flowering_end")));
                        plant.setFruiting_start(Integer.parseInt(json_data.getString("fruiting_start")));
                        plant.setFruiting_end(Integer.parseInt(json_data.getString("fruiting_end")));
                        plant.setTemp_zone_id(Integer.parseInt(json_data.getString("temp_zone_id")));
                        plant.setSoil_type_id(Integer.parseInt(json_data.getString("soil_type_id")));
                        plant.setHumidity(Integer.parseInt(json_data.getString("humidity")));
                        plant.setWinter(Integer.parseInt(json_data.getString("winter")));
                        plant.setSeeding_start(Integer.parseInt(json_data.getString("seeding_start")));
                        plant.setSeeding_end(Integer.parseInt(json_data.getString("seeding_end")));
                        plant.setCutting_start(Integer.parseInt(json_data.getString("cutting_start")));
                        plant.setCutting_end(Integer.parseInt(json_data.getString("cutting_end")));
                        plant.setSoil_ph_id(Integer.parseInt(json_data.getString("soil_ph_id")));
                        plant.setBarcode(json_data.getString("barcode"));
                        dictionary.add(plant);
                    }

                    App app = (App) getActivity().getApplication();
                    app.setDictionary(dictionary);
                    System.out.println("dictionary size: " + dictionary.size());
                    checkedPlantsIds = new ArrayList<Integer>();

                    ////////////////Populate gridview
                    String[] names = new String[dictionary.size()];
                    int[] ids = new int[dictionary.size()];

                    for (int i = 0; i < dictionary.size(); i++) {
                        names[i] = dictionary.get(i).getPlant_name();
                        ids[i] = dictionary.get(i).getPlant_id();
                    }

                    final TextView text;
                    final GridView gridView;
                    final String[] plantNames = names;
                    final int[] plantIds = ids;

                    gridView = (GridView) getActivity().findViewById(R.id.selectGridView);
                    final CustomGridAdapter gridAdapter = new CustomGridAdapter(getActivity(), plantNames, plantIds);
                    gridView.setAdapter(gridAdapter);
                    gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);

                    gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            if (!gridAdapter.CHECK_ENEBLED) {
                                gridAdapter.CHECK_ENEBLED = true;
                                gridView.setAdapter(gridAdapter);
                            } else if (gridAdapter.CHECK_ENEBLED) {
                                gridAdapter.CHECK_ENEBLED = false;
                                gridView.setAdapter(gridAdapter);
                            }

                            return true;
                        }
                    });

                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            System.out.println(gridView.getCheckedItemCount() + " items are checked");
                            if (gridAdapter.CHECK_ENEBLED) {
                                int checkedPosition = position - gridView.getFirstVisiblePosition();
                                GridViewItem cell = (GridViewItem) gridView.getChildAt(checkedPosition);
                                int pId = dictionary.get(position).getPlant_id();

                                if (cell.isChecked()) {
                                    System.out.println("cell " + position + " checked");

                                    for (Integer i : checkedPlantsIds) {
                                        if (i == pId) {
                                            break;
                                        }
                                    }

                                    checkedPlantsIds.add(pId);
                                } else if (!cell.isChecked()) {
                                    System.out.println("cell " + position + " unchecked");
                                    Iterator<Integer> iter = checkedPlantsIds.iterator();
                                    while (iter.hasNext()) {
                                        Integer i = iter.next();
                                        if (i == pId) {
                                            iter.remove();
                                        }
                                    }
                                }

                                System.out.println("checked plants are: " + checkedPlantsIds);

                            } else if (!gridAdapter.CHECK_ENEBLED) {
                                Fragment fragment = new FragmentsController();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("Plant", dictionary.get(position));
                                bundle.putString("Source", "Select");
                                fragment.setArguments(bundle);
                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                                TextView fragmentTitle = (TextView) getActivity().findViewById(R.id.fragment_title);
                                fragmentTitle.setText("Plant info");
                            }
                        }
                    });
                }

                this.progressDialog.dismiss();
                Toast.makeText(getActivity().getBaseContext(), "Perform a long press on an item to enable checking", Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                Log.e("log_tag", "Error parsing data " + e.toString());

                this.progressDialog.dismiss();
            }


        }


        public String convertStreamToString(InputStream is) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line = null;
            try {
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                    Log.e("pass 2", "connection success ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println(sb);
            return sb.toString();
        }

    }
    //End Ayman


    //Ayman 18/02/15 LoadGardenTask Inner Class
    class LoadGardenTask extends AsyncTask<String, String, Void> {

        String result = "";
        HttpEntity entity = null;
        HttpResponse response = null;
        ArrayList<NameValuePair> nameValuePairs;


        private ProgressDialog progressDialog;

        String fragmentTitle = ((TextView) getActivity().findViewById(R.id.fragment_title)).getText().toString();

        protected void onPreExecute() {
            if (fragmentTitle.equals("My garden")) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Loading plants from garden...");
                progressDialog.show();
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface arg0) {
                        LoadGardenTask.this.cancel(false);
                    }
                });
            } else if (fragmentTitle.equals("Calendar")) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Loading calendar...");
                progressDialog.show();
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface arg0) {
                        LoadGardenTask.this.cancel(false);
                    }
                });
            }

            //super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {

            SharedPreferences sp = getActivity().getSharedPreferences("SessionUser", 0);
            int userId = sp.getInt("USERID", 0);

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://egarden-pl.byethost17.com/egardening/app/getgarden.php");


            try {
                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("user_id", String.valueOf(userId)));

                Thread.sleep(2000);

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                System.out.println("passed value " + nameValuePairs.get(0).getValue());
                response = httpclient.execute(httppost);

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error converting result " + e.toString());
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            plantsInGarden = new ArrayList<Plant>();////?

            try {
                entity = response.getEntity();
                InputStream is = entity.getContent();

                JSONArray json_array = new JSONArray(convertStreamToString(is));

                if (json_array.length() > 0) {
                    for (int i = 0; i < json_array.length(); i++) {
                        JSONObject json_data = json_array.getJSONObject(i);
                        Plant plant = new Plant();
                        plant.setPlant_id(Integer.parseInt(json_data.getString("plant_id")));
                        plant.setPlant_name(json_data.getString("plant_name"));
                        plant.setShape_id(Integer.parseInt(json_data.getString("shape_id")));
                        plant.setCategory_id(Integer.parseInt(json_data.getString("category_id")));
                        plant.setFlowering_start(Integer.parseInt(json_data.getString("flowering_start")));
                        plant.setFlowering_end(Integer.parseInt(json_data.getString("flowering_end")));
                        plant.setFruiting_start(Integer.parseInt(json_data.getString("fruiting_start")));
                        plant.setFruiting_end(Integer.parseInt(json_data.getString("fruiting_end")));
                        plant.setTemp_zone_id(Integer.parseInt(json_data.getString("temp_zone_id")));
                        plant.setSoil_type_id(Integer.parseInt(json_data.getString("soil_type_id")));
                        plant.setHumidity(Integer.parseInt(json_data.getString("humidity")));
                        plant.setWinter(Integer.parseInt(json_data.getString("winter")));
                        plant.setSeeding_start(Integer.parseInt(json_data.getString("seeding_start")));
                        plant.setSeeding_end(Integer.parseInt(json_data.getString("seeding_end")));
                        plant.setCutting_start(Integer.parseInt(json_data.getString("cutting_start")));
                        plant.setCutting_end(Integer.parseInt(json_data.getString("cutting_end")));
                        plant.setSoil_ph_id(Integer.parseInt(json_data.getString("soil_ph_id")));
                        plant.setBarcode(json_data.getString("barcode"));

                        plantsInGarden.add(plant);

                        //App app = (App) getActivity().getApplication();
                        //app.setGarden(plantsInGarden);
                    }
                }

                //this.progressDialog.dismiss();


                ///The following actions depend on the current fragment:


                ///If the My garden fragment is active
                if (fragmentTitle.equals("My garden")) {

                    this.progressDialog.dismiss();

                    if ((plantsInGarden == null) || (plantsInGarden.size() == 0)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Your garden is empty!\nDo you want to add a plant?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Fragment fragment = new FragmentsController();
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("Menu", 2);
                                        fragment.setArguments(bundle);
                                        FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                                        TextView fragmentTitle = (TextView) getActivity().findViewById(R.id.fragment_title);
                                        fragmentTitle.setText("Select plants");
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                    } else {
                        System.out.println("plants in garden: " + plantsInGarden.size());

                        String[] names = new String[plantsInGarden.size()];
                        int[] ids = new int[plantsInGarden.size()];

                        for (int i = 0; i < plantsInGarden.size(); i++) {
                            names[i] = plantsInGarden.get(i).getPlant_name();
                            ids[i] = plantsInGarden.get(i).getPlant_id();
                        }


                        final TextView text;
                        final GridView gridView;
                        final String[] plantNames = names;
                        final int[] plantIds = ids;

                        gridView = (GridView) getActivity().findViewById(R.id.myGridView);
                        CustomGridAdapter gridAdapter = new CustomGridAdapter(getActivity(), plantNames, plantIds);
                        gridView.setAdapter(gridAdapter);

                        //Just for test
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                Fragment fragment = new FragmentsController();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("Plant", plantsInGarden.get(position));
                                bundle.putString("Source", "Garden");
                                fragment.setArguments(bundle);
                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                                TextView fragmentTitle = (TextView) getActivity().findViewById(R.id.fragment_title);
                                fragmentTitle.setText("Plant info");
                            }
                        });
                    }
                }

                ///If the Calendar fragment is active
                else if (fragmentTitle.equals("Calendar")) {

                    this.progressDialog.dismiss();
                    System.out.println("from calendar plants in garden: " + plantsInGarden.size());

                    final TextView lArrow, rArrow, season, month1, month2, month3;
                    Typeface font;
                    final ListView calendar;
                    CalendarAdapter adapter;

                    lArrow = (TextView) getActivity().findViewById(R.id.left_arrow);
                    rArrow = (TextView) getActivity().findViewById(R.id.right_arrow);
                    font = Typeface.createFromAsset(getActivity().getAssets(), "IcoMoon-Free.ttf");

                    lArrow.setTypeface(font);
                    rArrow.setTypeface(font);

                    season = (TextView) getActivity().findViewById(R.id.season);
                    month1 = (TextView) getActivity().findViewById(R.id.month_1);
                    month2 = (TextView) getActivity().findViewById(R.id.month_2);
                    month3 = (TextView) getActivity().findViewById(R.id.month_3);

                    getSeason(season, month1, month2, month3);
                    calendar = (ListView) getActivity().findViewById(R.id.calendar);


                    final Plant[] plants = new Plant[plantsInGarden.size()];

                    for (int i = 0; i < plantsInGarden.size(); i++) {
                        plants[i] = plantsInGarden.get(i);
                    }

                    adapter = new CalendarAdapter(plants, getActivity(), season.getText().toString());
                    calendar.setAdapter(adapter);

                    lArrow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (season.getText().toString().equals("Winter")) {
                                season.setText("Autumn");
                                month1.setText("Sep");
                                month2.setText("Oct");
                                month3.setText("Nov");
                                CalendarAdapter adapter = new CalendarAdapter(plants, getActivity(), "Autumn");
                                calendar.setAdapter(adapter);

                            } else if (season.getText().toString().equals("Spring")) {
                                season.setText("Winter");
                                month1.setText("Dec");
                                month2.setText("Jan");
                                month3.setText("Feb");
                                CalendarAdapter adapter = new CalendarAdapter(plants, getActivity(), "Winter");
                                calendar.setAdapter(adapter);
                            } else if (season.getText().toString().equals("Summer")) {
                                season.setText("Spring");
                                month1.setText("Mar");
                                month2.setText("Apr");
                                month3.setText("May");
                                CalendarAdapter adapter = new CalendarAdapter(plants, getActivity(), "Spring");
                                calendar.setAdapter(adapter);
                            } else if (season.getText().toString().equals("Autumn")) {
                                season.setText("Summer");
                                month1.setText("Jun");
                                month2.setText("Jul");
                                month3.setText("Aug");
                                CalendarAdapter adapter = new CalendarAdapter(plants, getActivity(), "Summer");
                                calendar.setAdapter(adapter);
                            }
                        }
                    });

                    rArrow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (season.getText().toString().equals("Winter")) {
                                season.setText("Spring");
                                month1.setText("Mar");
                                month2.setText("Apr");
                                month3.setText("May");
                                CalendarAdapter adapter = new CalendarAdapter(plants, getActivity(), "Spring");
                                calendar.setAdapter(adapter);
                            } else if (season.getText().toString().equals("Spring")) {
                                season.setText("Summer");
                                month1.setText("Jun");
                                month2.setText("Jul");
                                month3.setText("Aug");
                                CalendarAdapter adapter = new CalendarAdapter(plants, getActivity(), "Summer");
                                calendar.setAdapter(adapter);
                            } else if (season.getText().toString().equals("Summer")) {
                                season.setText("Autumn");
                                month1.setText("Sep");
                                month2.setText("Oct");
                                month3.setText("Nov");
                                CalendarAdapter adapter = new CalendarAdapter(plants, getActivity(), "Autumn");
                                calendar.setAdapter(adapter);
                            } else if (season.getText().toString().equals("Autumn")) {
                                season.setText("Winter");
                                month1.setText("Dec");
                                month2.setText("Jan");
                                month3.setText("Feb");
                                CalendarAdapter adapter = new CalendarAdapter(plants, getActivity(), "Winter");
                                calendar.setAdapter(adapter);
                            }
                        }
                    });
                }

                ////////////


                //Update text on slider
                TextView menuPlants = (TextView) getActivity().findViewById(R.id.menu_plants);
                if (plantsInGarden.size() == 0)
                    menuPlants.setText("No Plants in your garden");
                else if (plantsInGarden.size() == 1)
                    menuPlants.setText(plantsInGarden.size() + " Plant in your garden");
                else
                    menuPlants.setText(plantsInGarden.size() + " Plants in your garden");

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                Log.e("log_tag", "Error parsing data " + e.toString());

                this.progressDialog.dismiss();
            }


        }


        public String convertStreamToString(InputStream is) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line = null;
            try {
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                    Log.e("pass 2", "connection success ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println(sb);
            return sb.toString();
        }

    }
    //End Ayman


    //Ayman 22/02/15 AddToGardenTask Inner Class
    class AddToGardenTask extends AsyncTask<String, String, Void> {

        InputStream is = null;
        String result = "";
        boolean b = true;

        App app = (App) getActivity().getApplication();
        //ArrayList<Plant> garden = app.getGarden();
        ArrayList<Plant> dictionary = app.getDictionary();

        ArrayList<Integer> gardenIds = new ArrayList<Integer>();

        protected void onPreExecute() {
            //super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            SharedPreferences sp = getActivity().getSharedPreferences("SessionUser", 0);
            int userId = sp.getInt("USERID", 0);

            Iterator<Integer> iter = checkedPlantsIds.iterator();
            Iterator<Plant> iterDic = dictionary.iterator();

            if (plantsInGarden == null) {
                plantsInGarden = new ArrayList<Plant>();
            } else {
                for (Plant pl : plantsInGarden) {
                    gardenIds.add(pl.getPlant_id());
                }
            }

            while (iter.hasNext()) {
                Integer i = iter.next();
                if (!gardenIds.contains(i)) {
                    addPlant(userId, i);
                    //while (iter.hasNext()) {
                    //Plant pl = iterDic.next();
                    //plantsInGarden.add(pl);
                    //}
                    System.out.println("plant " + i + " added to garden");
                }
            }

            return null;
        }

        protected void onPostExecute(Void result) {

            if (b) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Plants added successfully to your garden")
                        .setCancelable(false)
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                                Fragment fragment = new FragmentsController();
                                Bundle bundle = new Bundle();
                                bundle.putInt("Menu", 2);
                                fragment.setArguments(bundle);
                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                                TextView fragmentTitle = (TextView) getActivity().findViewById(R.id.fragment_title);
                                fragmentTitle.setText("Select plants");
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                Toast.makeText(getActivity().getBaseContext(), "There was something wrong.\nPlease Try Again",
                        Toast.LENGTH_LONG).show();
            }
        }


        public void addPlant(int userId, int plantId) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("user_id", String.valueOf(userId)));
            nameValuePairs.add(new BasicNameValuePair("plant_id", String.valueOf(plantId)));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://egarden-pl.byethost17.com/egardening/app/addplants.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "connection success ");
            } catch (Exception e) {
                Log.e("Fail 1", e.toString());
                Toast.makeText(getActivity().getApplicationContext(), "Invalid IP Address",
                        Toast.LENGTH_LONG).show();
            }

            try {
                BufferedReader reader = new BufferedReader
                        (new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
                Log.e("pass 2", "connection success ");
            } catch (Exception e) {
                Log.e("Fail 2", e.toString());
            }

            try {
                JSONObject json_data = new JSONObject(result);
                int code = (json_data.getInt("code"));
                System.out.println(code);
                if (code == 1) {
                    b = b && true;
                } else {
                    b = b && false;
                }
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }

        }

    }
    //End Ayman


    //Ayman 22/02/15 AddToGardenTask Inner Class
    class RemoveFromGardenTask extends AsyncTask<String, String, Void> {

        InputStream is = null;
        String result = "";
        boolean b = true;


        protected void onPreExecute() {
            //super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            SharedPreferences sp = getActivity().getSharedPreferences("SessionUser", 0);
            int userId = sp.getInt("USERID", 0);

            int plantId = Integer.parseInt(params[0]);
            System.out.println("remove id " + plantId);
            removePlant(userId, plantId);

            return null;
        }

        protected void onPostExecute(Void result) {

            if (b) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Plant was removed from your garden")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                Toast.makeText(getActivity().getBaseContext(), "There was something wrong.\nPlease Try Again",
                        Toast.LENGTH_LONG).show();
            }
        }


        public void removePlant(int userId, int plantId) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("user_id", String.valueOf(userId)));
            nameValuePairs.add(new BasicNameValuePair("plant_id", String.valueOf(plantId)));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://egarden-pl.byethost17.com/egardening/app/removeplant.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "connection success ");
            } catch (Exception e) {
                Log.e("Fail 1", e.toString());
                Toast.makeText(getActivity().getApplicationContext(), "Invalid IP Address",
                        Toast.LENGTH_LONG).show();
            }

            try {
                BufferedReader reader = new BufferedReader
                        (new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
                Log.e("pass 2", "connection success ");
            } catch (Exception e) {
                Log.e("Fail 2", e.toString());
            }

            try {
                JSONObject json_data = new JSONObject(result);
                int code = (json_data.getInt("code"));
                System.out.println("json code " + code);
                if (code == 1) {
                    b = true;
                } else {
                    b = false;
                }
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }

        }

    }
    //End Ayman


}
