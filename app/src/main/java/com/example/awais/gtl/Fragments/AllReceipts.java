package com.example.awais.gtl.Fragments;

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

import com.example.awais.gtl.Adapters.ReceiptAdapter;
import com.example.awais.gtl.Pojos.Receipt;
import com.example.awais.gtl.R;
import com.riyagayasen.easyaccordion.AccordionView;

import java.util.ArrayList;

/**
 * Created by awais on 11/20/2017.
 */

public class AllReceipts extends Fragment {
    ReceiptAdapter adapter;
    ArrayList<Receipt> receiptsArrayList;
    RecyclerView todayReceiptRecyclerView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.all_receipts, container, false);
        AccordionView accordionView = (AccordionView) rootView.findViewById(R.id.test_accordion);
        TextView headingText = (TextView) accordionView.findViewById(R.id.heading);
        headingText.setText("May-24-2017");
        headingText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        ((ImageView)accordionView.findViewById(R.id.dropup_image)).getDrawable().setTint(getResources().getColor(R.color.colorPrimaryDark));

        receiptsArrayList= new ArrayList<>();
        Receipt receipt = new Receipt();
        receipt.setClientName("Muhammad Awais");
        receipt.setReceiptPrice("$ 23,333");
        receipt.setInvoiceID("invoice # 11223");
        receipt.setCompanyName("Globe TeleLink");
        receiptsArrayList.add(receipt);

        receipt = new Receipt();
        receipt.setClientName("Muhammad Imran");
        receipt.setReceiptPrice("$ 3,333");
        receipt.setInvoiceID("invoice # 11224");
        receipt.setCompanyName("Globe TeleLink");
        receiptsArrayList.add(receipt);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        todayReceiptRecyclerView = (RecyclerView) accordionView.findViewById(R.id.today_receipt_recycler_view);
        adapter= new ReceiptAdapter(getContext(),receiptsArrayList);
        todayReceiptRecyclerView .setLayoutManager(mLayoutManager);
//        candidateRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
//        //or
//        candidateRecyclerView.addItemDecoration(new DividerItemDecoration(this));
//        //or
//        candidateRecyclerView.addItemDecoration(
//                new DividerItemDecoration(this, R.drawable.divider));
        //salesman_bag_recyler_view .setItemAnimator(new DefaultItemAnimator());
        todayReceiptRecyclerView.setAdapter(adapter);

        return rootView;
    }
}
