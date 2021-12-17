package com.thingsenz.energymeter;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.thingsenz.energymeter.database.DatabaseHelper;
import com.thingsenz.energymeter.database.EnergyModel;

import java.util.List;

public class PredictionActivity extends AppCompatActivity {

    GraphView graphView;
    List<EnergyModel> energyModelList;

    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);

        setContentView(R.layout.activity_prediction);
        graphView=findViewById(R.id.graph);

        energyModelList=new DatabaseHelper(getApplicationContext()).getAllData();


        try {

            LineGraphSeries<DataPoint> series=new LineGraphSeries<>();
            for(int i=0;i<energyModelList.size();i++) {

                series.appendData(new DataPoint(i,Float.parseFloat(energyModelList.get(i).getPower())),true,50);
            }

            graphView.addSeries(series);

        } catch (Exception e){
            e.printStackTrace();
        }



    }

}
