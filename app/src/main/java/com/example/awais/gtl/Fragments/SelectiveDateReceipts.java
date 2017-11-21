package com.example.awais.gtl.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.awais.gtl.R;

/**
 * Created by awais on 11/20/2017.
 */

public class SelectiveDateReceipts extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.selective_date_receipts, container, false);
        return rootView;
    }
}
