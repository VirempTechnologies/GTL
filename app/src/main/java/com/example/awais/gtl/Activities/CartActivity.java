package com.example.awais.gtl.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.TextView;

import com.example.awais.gtl.Adapters.BagAdapter;
import com.example.awais.gtl.Adapters.CartAdapter;
import com.example.awais.gtl.Constants;
import com.example.awais.gtl.Pojos.BagProduct;
import com.example.awais.gtl.Pojos.CartItem;
import com.example.awais.gtl.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by awais on 11/21/2017.
 */

public class CartActivity extends AppCompatActivity {
    CartAdapter adapter;
    ArrayList<CartItem> cartItemArrayList;
    RecyclerView product_cart_recycler_view;
    double subTotal=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        cartItemArrayList = new ArrayList<>();
        for (Map.Entry<String,CartItem> cartItem:Constants.cartItemsMap.entrySet())
        {
            cartItemArrayList.add(cartItem.getValue());
            subTotal+=cartItem.getValue().getCollective_price();
        }



        //setting the recycler view
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        product_cart_recycler_view = (RecyclerView) findViewById(R.id.product_cart_recycler_view);
        adapter = new CartAdapter(this, cartItemArrayList);
        product_cart_recycler_view.setLayoutManager(mLayoutManager);
        product_cart_recycler_view.setAdapter(adapter);
        int resId = R.anim.layout_animation_from_left;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        product_cart_recycler_view.setLayoutAnimation(animation);

        Button addAnotherItemBtn = (Button) findViewById(R.id.btn_addAnother);
        addAnotherItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
        TextView product_receipt_total_price = (TextView) findViewById(R.id.product_receipt_total_price);
        TextView product_grand_total_price = (TextView) findViewById(R.id.product_grand_total_price);
        product_receipt_total_price.setText(subTotal+"€");
        product_grand_total_price.setText(subTotal+"€");


    }
}
