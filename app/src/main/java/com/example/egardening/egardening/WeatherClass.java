package com.example.egardening.egardening;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.TextView;
import android.os.StrictMode;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.Reader;

import org.apache.http.client.ClientProtocolException;

import java.io.IOException;

import android.widget.Toast;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;

import android.widget.ImageView;


/**
 * Created by Marcin on 2015-02-18.
 */
public class WeatherClass {
    TextView temp, textTemp, lever, coucher, vitesse, humidite, visibilite, barometre, tempDemain, textTempDemain, miseAJour;
    ImageView icon, iconDemain;
    //-------------------------------------------------------
    private View view;
    private ConnectivityManager cm;

    public WeatherClass(View view, ConnectivityManager cm) {
        this.cm = cm;
        this.view = view;
    }

    public void manageWeather() {
        if (Connexion()) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            temp = (TextView) view.findViewById(R.id.temp);
            textTemp = (TextView) view.findViewById(R.id.textTemp);
            lever = (TextView) view.findViewById(R.id.lever);
            coucher = (TextView) view.findViewById(R.id.coucher);
            vitesse = (TextView) view.findViewById(R.id.vitesse);
            humidite = (TextView) view.findViewById(R.id.humidite);
            visibilite = (TextView) view.findViewById(R.id.visibilite);
            barometre = (TextView) view.findViewById(R.id.barometre);
            tempDemain = (TextView) view.findViewById(R.id.tempDemain);
            textTempDemain = (TextView) view.findViewById(R.id.textTempDemain);
            miseAJour = (TextView) view.findViewById(R.id.miseAJour);
            //------------------------------------------------------------------
            String weatherString = QueryYahooWeather();
            Document weatherDoc = convertStringToDocument(weatherString);
            MyWeather weatherResult = parseWeather(weatherDoc);

            temp.setText(weatherResult.tempToString());
            lever.setText(weatherResult.leverToString());
            coucher.setText(weatherResult.coucherToString());
            vitesse.setText(weatherResult.vitesseToString());
            humidite.setText(weatherResult.humiditeToString());
            visibilite.setText(weatherResult.visibiliteToString());
            barometre.setText(weatherResult.barometreToString());
            tempDemain.setText(weatherResult.tempDemainToString());
            //miseAJour.setText(weatherResult.miseAJourToString(view.getResources().getString(R.id.miseAJour)));

            String code = weatherResult.conditioncode;
            textTemp.setText(view.getResources().getIdentifier("c_" + code, "string", "com.example.egardening.egardening"));
            //textTemp.setText(code + " ºC");

            String codeDemain = weatherResult.prevcode;
            textTempDemain.setText(view.getResources().getIdentifier("c_" + codeDemain, "string", "com.example.egardening.egardening"));


            icon = (ImageView) view.findViewById(R.id.icon);
            icon.setImageResource(view.getResources().getIdentifier("ic_" + code,
                    "drawable", "com.example.egardening.egardening"));

            iconDemain = (ImageView) view.findViewById(R.id.iconDemain);
            iconDemain.setImageResource(view.getResources().getIdentifier(
                    "ic_" + codeDemain, "drawable", "com.example.egardening.egardening"));
        } /*else {
            setContentView(R.layout.meteohc);
            try {
                AlertDialog alertDialog = new AlertDialog.Builder(Meteo.this)
                        .create();
                String titre = getResources().getString(R.string.titretoast);
                String msj = getResources().getString(R.string.msjtoast);

                alertDialog.setTitle(titre);
                alertDialog
                        .setMessage(msj);
                 alertDialog.setIcon(R.drawable.alerticon);
                String activer = getResources().getString(R.string.activer);
                alertDialog.setButton(activer,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Meteo.this.startActivity(new Intent(
                                        Settings.ACTION_WIRELESS_SETTINGS));
                            }
                        });
                alertDialog.show();

                Button actualiser = (Button) this.findViewById(R.id.actualiser);
                actualiser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Meteo.this, Meteo.class);
                        startActivity(i);
                        finish();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    private String QueryYahooWeather() {
        String qResult = "";
        String queryString = "http://weather.yahooapis.com/forecastrss?w=505120&u=c";
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(queryString);
        try {
            HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();
            if (httpEntity != null) {
                InputStream inputStream = httpEntity.getContent();
                Reader in = new InputStreamReader(inputStream);
                BufferedReader bufferedreader = new BufferedReader(in);
                StringBuilder stringBuilder = new StringBuilder();

                String stringReadLine = null;

                while ((stringReadLine = bufferedreader.readLine()) != null) {
                    stringBuilder.append(stringReadLine + "\n");
                }
                qResult = stringBuilder.toString();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            //Toast.makeText(WeatherClass.this, e.toString(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(Meteo.this, e.toString(), Toast.LENGTH_LONG).show();
        }
        return qResult;
    }

    public boolean Connexion() {
        NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileNetwork = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if ((wifi == null || !wifi.isConnected() || !wifi.isAvailable())
                && (mobileNetwork == null || !mobileNetwork.isConnected() || !mobileNetwork
                .isAvailable())
                && (activeNetwork == null || !activeNetwork.isConnected() || !activeNetwork
                .isAvailable())) {
            return false;
        }
        return true;
    }

    private Document convertStringToDocument(String src) {
        Document dest = null;

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser;

        try {
            parser = dbFactory.newDocumentBuilder();
            dest = parser.parse(new ByteArrayInputStream(src.getBytes()));
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
            //Toast.makeText(Meteo.this, e1.toString(), Toast.LENGTH_LONG).show();
        } catch (SAXException e) {
            e.printStackTrace();
            //Toast.makeText(Meteo.this, e.toString(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(Meteo.this, e.toString(), Toast.LENGTH_LONG).show();
        }

        return dest;
    }

    private MyWeather parseWeather(Document srcDoc) {

        MyWeather myWeather = new MyWeather();


        Node windNode = srcDoc.getElementsByTagName("yweather:wind").item(0);
        myWeather.vitessevent = windNode.getAttributes().getNamedItem("speed")
                .getNodeValue().toString();


        Node atmosphereNode = srcDoc
                .getElementsByTagName("yweather:atmosphere").item(0);
        myWeather.humidite = atmosphereNode.getAttributes()
                .getNamedItem("humidity").getNodeValue().toString();
        myWeather.visibilite = atmosphereNode.getAttributes()
                .getNamedItem("visibility").getNodeValue().toString();
        myWeather.barometre = atmosphereNode.getAttributes()
                .getNamedItem("pressure").getNodeValue().toString();


        Node astronomyNode = srcDoc.getElementsByTagName("yweather:astronomy")
                .item(0);
        myWeather.sunrise = astronomyNode.getAttributes()
                .getNamedItem("sunrise").getNodeValue().toString();
        myWeather.sunset = astronomyNode.getAttributes().getNamedItem("sunset")
                .getNodeValue().toString();


        Node conditionNode = srcDoc.getElementsByTagName("yweather:condition")
                .item(0);
        myWeather.conditiontext = conditionNode.getAttributes()
                .getNamedItem("text").getNodeValue().toString();
        myWeather.conditiontemp = conditionNode.getAttributes()
                .getNamedItem("temp").getNodeValue().toString();
        myWeather.conditioncode = conditionNode.getAttributes()
                .getNamedItem("code").getNodeValue().toString();

        // prévisions
        Node forecastNode = srcDoc.getElementsByTagName("yweather:forecast")
                .item(1);
        myWeather.prevtext = forecastNode.getAttributes().getNamedItem("text")
                .getNodeValue().toString();
        myWeather.prevmin = forecastNode.getAttributes().getNamedItem("low")
                .getNodeValue().toString();
        myWeather.prevmax = forecastNode.getAttributes().getNamedItem("high")
                .getNodeValue().toString();
        myWeather.prevcode = forecastNode.getAttributes().getNamedItem("code")
                .getNodeValue().toString();

        // derniere mise a jour
        myWeather.miseajour = srcDoc.getElementsByTagName("lastBuildDate")
                .item(0).getTextContent();

        return myWeather;
    }
}
