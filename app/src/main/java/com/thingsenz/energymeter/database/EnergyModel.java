package com.thingsenz.energymeter.database;

public class EnergyModel {

    public static final String TABLE_NAME="energy";
    public static final String COLUMN_ID="id";
    public static final String COLUMN_POWER="value";
    public static final String COLUMN_ENERGY="energy";
    public static final String COLUMN_TIMESTAMP="timestamp";

    private int id;
    private String power,energy,timestamp;

    public static final String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+"("+
            COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +COLUMN_POWER+" TEXT,"+COLUMN_ENERGY+" TEXT,"
            +COLUMN_TIMESTAMP+" DATETIME DEFAULT CURRENT_TIMESTAMP"
            +")";

    public EnergyModel() {}

    public EnergyModel(int id, String power, String energy,String timestamp) {
        this.id=id;
        this.power=power;
        this.energy=energy;
        this.timestamp=timestamp;
    }


    public int getId() {
        return id;
    }

    public String getPower() {
        return power;
    }

    public String getEnergy() {
        return energy;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public void setEnergy(String energy) {
        this.energy = energy;
    }
}
