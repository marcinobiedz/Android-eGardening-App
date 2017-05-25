package com.example.egardening.egardening;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.graphics.*;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.*;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.egardening.egardening.Entities.Plant;
import com.example.egardening.egardening.Session.App;
import com.example.egardening.egardening.Session.TypeFaceUtil;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;


public class Main extends Activity {

    String[] menu;
    DrawerLayout dLayout;
    ListView dList;
    CustomListAdapter adapter;
    Typeface menuFont;
    TextView menuIcon, fragmentTitle, menuPlants;
    MenuItem menuItems[];
    LinearLayout menuIconContainer;

    String username;
    int userId;
    ArrayList<Plant> plantsInGarden;

    IntentResult scanResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TypeFaceUtil.applyFont(this, findViewById(R.id.mainlayout), "Montserrat-Regular.ttf");
        TypeFaceUtil.applyFont(this, findViewById(R.id.calendarlayout), "Montserrat-Regular.ttf");
        TypeFaceUtil.applyFont(this, findViewById(R.id.mainlayout), "Montserrat-Regular.ttf");
        TypeFaceUtil.applyFont(this, findViewById(R.id.delailslayout), "Montserrat-Regular.ttf");
        TypeFaceUtil.applyFont(this, findViewById(R.id.weatherlayout), "Montserrat-Regular.ttf");
        TypeFaceUtil.applyFont(this, findViewById(R.id.myGridView), "Montserrat-Regular.ttf");
        TypeFaceUtil.applyFont(this, findViewById(R.id.selectGridView), "Montserrat-Regular.ttf");
        TypeFaceUtil.applyFont(this, findViewById(R.id.left_drawer), "Montserrat-Regular.ttf");



        //Ayman 14/02/15
        //Profile info on the slider menu
        SharedPreferences sp = getSharedPreferences("SessionUser", 0);
        username = sp.getString("USERNAME", null);
        userId = sp.getInt("USERID",0);
        //End Ayman

        //Ayman 19/02/15
        plantsInGarden = new ArrayList<Plant>();
        //

        menuFont = Typeface.createFromAsset(getAssets(), "IcoMoon-Free.ttf");
        menuIcon = (TextView) findViewById(R.id.menu_icon);
        menuIcon.setTypeface(menuFont);
        menuIcon.setText("\ue9bd");


        menuItems = new MenuItem[]{
                new MenuItem("\ue9a4", "My garden"),
                new MenuItem("\ue91f", "Select plants"),
                new MenuItem("\ue953", "Calendar"),
                new MenuItem("\ue9d4", "Weather"),
                new MenuItem("\ue937", "Barcode reader"),
                new MenuItem("\uea14", "Log out"),
        };

        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        dList = (ListView) findViewById(R.id.left_drawer);
        //Ayman 14/02/15
        //Setting the header of the slider menu
        View header = getLayoutInflater().inflate(R.drawable.profile_list_row, dList, false);
        //set header height programatically
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        header.getLayoutParams().height = height/3;
        ///
        dList.addHeaderView(header);
        TextView menuUsername = (TextView) findViewById(R.id.menu_username);
        menuUsername.setText("Hey " +username+ "!");

        //Ayman 18/02/15
        menuPlants = (TextView) findViewById(R.id.menu_plants);
        //App app = (App) getApplication();/////////////
        //app.setGarden(null);/////////////////
        //new LoadGardenTask().execute();
        //menuPlants.setText(plantsInGarden.size()+" Plants in your garden");
        //End Ayman

        //End Ayman
        adapter = new CustomListAdapter(this, R.drawable.list_row, menuItems);
        dList.setAdapter(adapter);
        dList.setSelector(android.R.color.darker_gray);

        //LinearLayout listHeader = (LinearLayout) findViewById(R.id.profile_info_container);
        //listHeader.getLayoutParams().height = 20;


        dList.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
                dLayout.closeDrawers();
                Fragment fragment = new FragmentsController();
                Bundle bundle = new Bundle();
                //bundle.putString("Menu", menu[position]);
                System.out.println(position);
                bundle.putInt("Menu", position);
                //bundle.putString("User");
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

                fragmentTitle = (TextView) findViewById(R.id.fragment_title);
                //Ayman 15/02/15 Fix
                if(position == 0 )
                    fragmentTitle.setText(menuItems[position].title);
                else if(position == 6) {
                    Session session = Session.getActiveSession();
                    session.closeAndClearTokenInformation();
                    Intent intent = new Intent(Main.this, Login.class);
                    startActivity(intent);
                    Main.this.finish();
                }
                else
                    fragmentTitle.setText(menuItems[position-1].title);
                //End Ayman
            }
        });

        menuIconContainer = (LinearLayout) findViewById(R.id.menu_icon_container);

        menuIconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dLayout.isDrawerOpen(dList)){
                    dLayout.closeDrawers();
                }
                else{
                    dLayout.openDrawer(dList);
                }
            }
        });


        //Ayman 13/02/15
        //Automatically redirect to My Garden fragment when login is successfull
        Fragment fragment = new FragmentsController();
        Bundle bundle = new Bundle();
        int position = 0;
        bundle.putInt("Menu", position);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        fragmentTitle = (TextView) findViewById(R.id.fragment_title);
        fragmentTitle.setText(menuItems[position].title);
        //End Ayman


    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            //App app = (App) getApplication();
            //ArrayList<Plant> dictionary = app.getDictionary();
            new PlantsDicitonaryTask().execute();
        }
    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Main.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        App app = (App) getApplication();
        app.setGarden(null);
    }


    //Ayman 18/02/15 LoadGardenTask Inner Class
    class LoadGardenTask extends AsyncTask<String, String, Void> {

        String result = "";
        HttpEntity entity = null;
        HttpResponse response = null;
        ArrayList<NameValuePair> nameValuePairs;

        private ProgressDialog progressDialog = new ProgressDialog(Main.this);

        protected void onPreExecute() {
            progressDialog.setMessage("Loading plants...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    LoadGardenTask.this.cancel(false);
                }
            });
            //super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String...params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://egarden-pl.byethost17.com/egardening/app/getgarden.php");

            try {
                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("user_id",String.valueOf(userId)));

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
            try {
                entity = response.getEntity();
                InputStream is = entity.getContent();

                JSONArray json_array = new JSONArray(convertStreamToString(is));
                if(json_array.length()>0) {
                    for(int i=0; i<json_array.length(); i++) {
                        JSONObject json_data = json_array.getJSONObject(i);
                        Plant plant = new Plant();
                        plant.setPlant_id(Integer.parseInt(json_data.getString("plant_id")));
                        plant.setPlant_name(json_data.getString("plant_name"));
                        plantsInGarden.add(plant);

                        App app = (App) getApplication();
                        app.setGarden(plantsInGarden);
                    }
                }

                this.progressDialog.dismiss();

                //Ayman 13/02/15
                //Automatically redirect to My Garden fragment when login is successfull
                Fragment fragment = new FragmentsController();
                Bundle bundle = new Bundle();
                int position = 0;
                bundle.putInt("Menu", position);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                fragmentTitle = (TextView) findViewById(R.id.fragment_title);
                fragmentTitle.setText(menuItems[position].title);
                //End Ayman

                //System.out.println("plants number "+plantsInGarden.size());
                if(plantsInGarden.size()==0)
                    menuPlants.setText("No Plants in your garden");
                else if(plantsInGarden.size()==1)
                    menuPlants.setText(plantsInGarden.size()+" Plant in your garden");
                else
                    menuPlants.setText(plantsInGarden.size()+" Plants in your garden");

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                Log.e("log_tag", "Error parsing data " + e.toString());

                this.progressDialog.dismiss();
            }


        }


        public  String convertStreamToString(InputStream is) {
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


    //Ayman 18/02/15 PlantsDicitonaryTask Inner Class
    class PlantsDicitonaryTask extends AsyncTask<String, String, Void> {

        String result = "";
        HttpEntity entity = null;
        HttpResponse response = null;



        protected void onPreExecute() {

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

                    ArrayList<Plant> dictionary = new ArrayList<Plant>();

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

                    App app = (App) getApplication();
                    app.setDictionary(dictionary);
                    System.out.println("dictionary size: " + dictionary.size());


                    //Toast.makeText(Main.this, scanResult.getContents(), Toast.LENGTH_LONG).show();

                    for(Plant p : dictionary) {

                        if(scanResult.getContents().equals(p.getBarcode())) {
                            Fragment fragment = new FragmentsController();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("Plant", p);
                            fragment.setArguments(bundle);
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                            TextView fragmentTitle = (TextView) findViewById(R.id.fragment_title);
                            fragmentTitle.setText("Plant info");
                        }

                        else {
                            Toast.makeText(Main.this, "Sorry, there is no plant with such barcode..", Toast.LENGTH_LONG).show();
                        }
                    }

                }


            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                Log.e("log_tag", "Error parsing data " + e.toString());

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
}
