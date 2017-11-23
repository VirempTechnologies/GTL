package com.example.awais.gtl.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.awais.gtl.Adapters.CustomSpinnerAdapter;
import com.example.awais.gtl.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by awais on 11/23/2017.
 */

public class AddClientFirst extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_client_first);
        //Simple Spinner Android Example


        //Android Custom Spinner Example Programmatically
        initCustomSpinner();
    }

    private void initCustomSpinner() {

        Spinner city_spinner= (Spinner) findViewById(R.id.city_spinner);
        // Spinner Drop down elements
        ArrayList<String> languages = new ArrayList<String>();
        languages.add("Andorid");
        languages.add("IOS");
        languages.add("PHP");
        languages.add("Java");
        languages.add(".Net");
        CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(AddClientFirst.this,languages);
        city_spinner.setAdapter(customSpinnerAdapter);
        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();

                Toast.makeText(parent.getContext(), "Android Custom Spinner Example Output..." + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


}
