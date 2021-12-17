package com.thingsenz.energymeter.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;

    private static final String DATABASE_NAME="energy_db";

    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EnergyModel.CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+EnergyModel.TABLE_NAME);

        onCreate(db);
    }



    public long insertData(String power,String energy) {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values=new ContentValues();

        values.put(EnergyModel.COLUMN_ENERGY,energy);
        values.put(EnergyModel.COLUMN_POWER,power);
        //values.put(EnergyModel.COLUMN_TIMESTAMP,(System.currentTimeMillis()/1000));

        long id=db.insert(EnergyModel.TABLE_NAME,null,values);

        db.close();
        return id;

    }

    public EnergyModel getData(long id) {
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor=db.query(EnergyModel.TABLE_NAME,new String[]{EnergyModel.COLUMN_ID,EnergyModel.COLUMN_POWER,EnergyModel.COLUMN_ENERGY,EnergyModel.COLUMN_TIMESTAMP},
                EnergyModel.COLUMN_ID+"=?",new String[]{String.valueOf(id)},null,null,null,null);

        if (cursor!=null)
            cursor.moveToFirst();

        EnergyModel energyModel=new EnergyModel(
                cursor.getInt(cursor.getColumnIndex(EnergyModel.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(EnergyModel.COLUMN_POWER)),
                cursor.getString(cursor.getColumnIndex(EnergyModel.COLUMN_ENERGY)),
                cursor.getString(cursor.getColumnIndex(EnergyModel.COLUMN_TIMESTAMP))
        );

        cursor.close();
        return energyModel;
    }


    public List<EnergyModel> getAllData() {
        List<EnergyModel> energyModelList=new ArrayList<>();

        String selectQuery="SELECT * FROM "+EnergyModel.TABLE_NAME+
                " ORDER BY "+EnergyModel.COLUMN_TIMESTAMP+" DESC";
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()) {
            do {
                EnergyModel energyModel=new EnergyModel();
                energyModel.setId(cursor.getInt(cursor.getColumnIndex(EnergyModel.COLUMN_ID)));
                energyModel.setPower(cursor.getString(cursor.getColumnIndex(EnergyModel.COLUMN_POWER)));
                energyModel.setEnergy(cursor.getString(cursor.getColumnIndex(EnergyModel.COLUMN_ENERGY)));
                energyModel.setTimestamp(cursor.getString(cursor.getColumnIndex(EnergyModel.COLUMN_TIMESTAMP)));
                energyModelList.add(energyModel);
                //Log.d("Loopity llop",energyModel.getTimestamp());
            } while (cursor.moveToNext());
        }

        db.close();

        return energyModelList;

    }


    public int getDataCount() {
        String countQuery="SELECT * FROM "+EnergyModel.TABLE_NAME;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(countQuery,null);

        int count=cursor.getCount();
        cursor.close();

        return count;
    }


    public int updateNote(EnergyModel note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EnergyModel.COLUMN_POWER, note.getPower());
        values.put(EnergyModel.COLUMN_ENERGY,note.getEnergy());

        // updating row
        return db.update(EnergyModel.TABLE_NAME, values, EnergyModel.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote(EnergyModel note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EnergyModel.TABLE_NAME, EnergyModel.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }

}
