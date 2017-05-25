package com.example.egardening.egardening;

/**
 * Created by Marcin on 2015-02-18.
 */
public class MyWeather {
    String vitessevent;
    String humidite;
    String visibilite;
    String barometre;

    String sunrise;
    String sunset;

    String conditiontext;
    String conditiondate;
    String conditiontemp;
    String conditioncode;

    String prevmin;
    String prevmax;
    String prevtext;
    String prevcode;

    String miseajour;

    public String tempToString() {
        return conditiontemp + "°C";
    }

    public String tempTextToString() {
        return conditiontext;
    }

    public String leverToString() {
        return sunrise;
    }

    public String coucherToString() {
        return sunset;
    }

    public String vitesseToString() {
        return vitessevent + " km/h";
    }

    public String humiditeToString() {
        return humidite + " %";
    }

    public String visibiliteToString() {
        return visibilite;
    }

    public String barometreToString() {
        return barometre + " mb";
    }

    public String tempDemainToString() {
        return "Min " + prevmin + "°C   Max " + prevmax + "°C";
    }

    public String textTempDemainToString() {
        return prevtext;
    }

//    public String miseAJourToString(String maj) {
//        //String maj = getResources().getString(R.string.dmiseajour);
//        return maj + miseajour;
//    }

}