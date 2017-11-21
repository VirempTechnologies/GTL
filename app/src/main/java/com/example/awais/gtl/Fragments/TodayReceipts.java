package com.example.awais.gtl.Fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.awais.gtl.Activities.ReceiptsActivity;
import com.example.awais.gtl.Adapters.BagAdapter;
import com.example.awais.gtl.Adapters.ReceiptAdapter;
import com.example.awais.gtl.MediaConversion;
import com.example.awais.gtl.Pojos.BagProduct;
import com.example.awais.gtl.Pojos.Receipt;
import com.example.awais.gtl.R;
import com.riyagayasen.easyaccordion.AccordionView;

import java.util.ArrayList;

/**
 * Created by awais on 11/20/2017.
 */

public class TodayReceipts extends Fragment {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.today_receipts, container, false);

        return rootView;
    }
}
