package com.example.egardening.egardening.Entities;

import java.io.Serializable;

/**
 * Created by Ayman on 19/02/2015.
 */
public class Plant implements Serializable {

    int plant_id, shape_id, category_id, flowering_start, flowering_end, fruiting_start, fruiting_end, temp_zone_id, soil_type_id, humidity, seeding_start, seeding_end, cutting_start, cutting_end, soil_ph_id, winter;
    String plant_name, barcode;

    public Plant() {
    }

    public Plant(int plant_id, int shape_id, int category_id, int flowering_start, int flowering_end, int fruiting_start, int fruiting_end, int temp_zone_id, int soil_type_id, int humidity, int seeding_start, int seeding_end, int cutting_start, int cutting_end, int soil_ph_id, int winter, String plant_name, String barcode) {
        this.plant_id = plant_id;
        this.shape_id = shape_id;
        this.category_id = category_id;
        this.flowering_start = flowering_start;
        this.flowering_end = flowering_end;
        this.fruiting_start = fruiting_start;
        this.fruiting_end = fruiting_end;
        this.temp_zone_id = temp_zone_id;
        this.soil_type_id = soil_type_id;
        this.humidity = humidity;
        this.seeding_start = seeding_start;
        this.seeding_end = seeding_end;
        this.cutting_start = cutting_start;
        this.cutting_end = cutting_end;
        this.soil_ph_id = soil_ph_id;
        this.winter = winter;
        this.plant_name = plant_name;
        this.barcode = barcode;
    }


    public Plant(int id, String name) {
        this.plant_id = id;
        this.plant_name = name;
    }


    public int getPlant_id() {
        return plant_id;
    }

    public void setPlant_id(int plant_id) {
        this.plant_id = plant_id;
    }

    public int getShape_id() {
        return shape_id;
    }

    public void setShape_id(int shape_id) {
        this.shape_id = shape_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getFlowering_start() {
        return flowering_start;
    }

    public void setFlowering_start(int flowering_start) {
        this.flowering_start = flowering_start;
    }

    public int getFlowering_end() {
        return flowering_end;
    }

    public void setFlowering_end(int flowering_end) {
        this.flowering_end = flowering_end;
    }

    public int getFruiting_start() {
        return fruiting_start;
    }

    public void setFruiting_start(int fruiting_start) {
        this.fruiting_start = fruiting_start;
    }

    public int getFruiting_end() {
        return fruiting_end;
    }

    public void setFruiting_end(int fruiting_end) {
        this.fruiting_end = fruiting_end;
    }

    public int getTemp_zone_id() {
        return temp_zone_id;
    }

    public void setTemp_zone_id(int temp_zone_id) {
        this.temp_zone_id = temp_zone_id;
    }

    public int getSoil_type_id() {
        return soil_type_id;
    }

    public void setSoil_type_id(int soil_type_id) {
        this.soil_type_id = soil_type_id;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getSeeding_start() {
        return seeding_start;
    }

    public void setSeeding_start(int seeding_start) {
        this.seeding_start = seeding_start;
    }

    public int getSeeding_end() {
        return seeding_end;
    }

    public void setSeeding_end(int seeding_end) {
        this.seeding_end = seeding_end;
    }

    public int getCutting_start() {
        return cutting_start;
    }

    public void setCutting_start(int cutting_start) {
        this.cutting_start = cutting_start;
    }

    public int getCutting_end() {
        return cutting_end;
    }

    public void setCutting_end(int cutting_end) {
        this.cutting_end = cutting_end;
    }

    public int getSoil_ph_id() {
        return soil_ph_id;
    }

    public void setSoil_ph_id(int soil_ph_id) {
        this.soil_ph_id = soil_ph_id;
    }

    public int getWinter() {
        return winter;
    }

    public void setWinter(int winter) {
        this.winter = winter;
    }

    public String getPlant_name() {
        return plant_name;
    }

    public void setPlant_name(String plant_name) {
        this.plant_name = plant_name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
