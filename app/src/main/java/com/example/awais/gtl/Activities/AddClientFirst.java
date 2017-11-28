package com.example.awais.gtl.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.example.awais.gtl.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

/**
 * Created by awais on 11/23/2017.
 */

public class AddClientFirst extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_client_first);

        MaterialBetterSpinner city_spinner ;
        String[] SPINNER_DATA = {"ANDROID","PHP","BLOGGER","WORDPRESS"};
        city_spinner = (MaterialBetterSpinner)findViewById(R.id.city_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddClientFirst.this, android.R.layout.simple_dropdown_item_1line, SPINNER_DATA);
        city_spinner.setAdapter(adapter);

    }

}