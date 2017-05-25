package com.example.egardening.egardening.Session;

import android.app.Application;

import com.example.egardening.egardening.Entities.Plant;

import java.util.ArrayList;

/**
 * Created by Ayman on 19/02/2015.
 */
public class App extends Application {

    Session session  = new Session();

    public ArrayList<Plant> getGarden() {
        return session.garden;
    }

    public void setGarden(ArrayList<Plant> plants) {
        session.garden = plants;
    }

    public ArrayList<Plant> getDictionary() {
        return session.dictionary;
    }

    public void setDictionary(ArrayList<Plant> plants) {
        session.dictionary = plants;
    }

    public String getMessage() {
        return session.message;
    }

    public void setMessage(String msg) {
        session.message = msg;
    }
}
