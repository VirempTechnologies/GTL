package com.example.awais.gtl.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;

import com.example.awais.gtl.Adapters.BagAdapter;
import com.example.awais.gtl.Constants;
import com.example.awais.gtl.Pojos.BagProduct;
import com.example.awais.gtl.MediaConversion;
import com.example.awais.gtl.R;
import com.example.awais.gtl.WebServiceHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BagActivity extends AppCompatActivity {
    BagAdapter adapter;
    ArrayList<BagProduct> bagProductArrayList;
    RecyclerView salesman_bag_recyler_view;
    WebServiceHelper webServiceHelper;
    ProgressDialog prgDialog;
    JSONObject respObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bag_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //filling the bag list

        webServiceHelper = new WebServiceHelper();

        try {


            JSONObject headers = new JSONObject();
            headers.put("user_id", 10);
            headers.put("salesman_id", 1);
            headers.put("api_token", "JAAu8Z2LDXvbWIUtp0Sh3ejTPargCtve0ewuTR9CZT2NYVkQdUvk52p5pHFI");

            JSONObject params = new JSONObject();
            params.put("headers", headers);

            //---------------//
//            JSONObject params = new JSONObject();
//            params.put("ResellerUserName", "PJSoftTest");
//            params.put("ResellerPassword", "asdf@123");
//            params.put("ResellerHostAddress", "1.1.1.1.1");
//            Log.d("checklog", "sending request : " + params.toString());

//            JSONObject respObject = webServiceHelper.sendPostRequest(params,BagActivity.this,"http://85.25.217.41/ResellerAPI_v2/ResellerOperationService.svc/getProductList");
            respObject = webServiceHelper.sendPostRequest(params, BagActivity.this, Constants.getBagUrl);

            Log.d("checklog", "responce : " + respObject.toString());

            bagProductArrayList = new ArrayList<>();
            BagProduct bagProduct = new BagProduct();
            JSONArray allProducts = respObject.getJSONArray("products");
            for (int i=0;i<allProducts.length();i++)
            {
                JSONObject product = allProducts.getJSONObject(i);
                bagProduct = new BagProduct();
        bagProduct.setProductName(product.getString("name"));
        bagProduct.setSalePrice(product.getInt("sale_price"));
        bagProduct.setMyStock(product.getInt("quantity"));
        bagProduct.setCompanyStock(00);
        bagProduct.setImage(MediaConversion.encodeToBase64(BitmapFactory.decodeResource(getResources(),R.drawable.product_1), Bitmap.CompressFormat.PNG,30));
        bagProductArrayList.add(bagProduct);

            }
//
//        bagProduct.setProductName("Samsung J5 pro J530");
//        bagProduct.setSalePrice(990);
//        bagProduct.setMyStock(10);
//        bagProduct.setCompanyStock(200);
//        bagProduct.setImage(MediaConversion.encodeToBase64(BitmapFactory.decodeResource(getResources(),R.drawable.product_1), Bitmap.CompressFormat.PNG,30));
//        bagProductArrayList.add(bagProduct);
//
//        bagProduct = new BagProduct();
//        bagProduct.setProductName("Samsung J5 pro J530");
//        bagProduct.setSalePrice(800);
//        bagProduct.setMyStock(5);
//        bagProduct.setCompanyStock(100);
//        bagProduct.setImage(MediaConversion.encodeToBase64(BitmapFactory.decodeResource(getResources(),R.drawable.product_1), Bitmap.CompressFormat.PNG,30));
//        bagProductArrayList.add(bagProduct);
//
//        bagProduct = new BagProduct();
//        bagProduct.setProductName("Samsung J5 pro J530");
//        bagProduct.setSalePrice(610);
//        bagProduct.setMyStock(20);
//        bagProduct.setCompanyStock(400);
//        bagProduct.setImage(MediaConversion.encodeToBase64(BitmapFactory.decodeResource(getResources(),R.drawable.product_1), Bitmap.CompressFormat.PNG,30));
//        bagProductArrayList.add(bagProduct);
//
//        bagProduct = new BagProduct();
//        bagProduct.setProductName("Samsung J5 pro J530");
//        bagProduct.setSalePrice(800);
//        bagProduct.setMyStock(5);
//        bagProduct.setCompanyStock(100);
//        bagProduct.setImage(MediaConversion.encodeToBase64(BitmapFactory.decodeResource(getResources(),R.drawable.product_1), Bitmap.CompressFormat.PNG,30));
//        bagProductArrayList.add(bagProduct);
//
//        bagProduct = new BagProduct();
//        bagProduct.setProductName("Samsung J5 pro J530");
//        bagProduct.setSalePrice(610);
//        bagProduct.setMyStock(20);
//        bagProduct.setCompanyStock(400);
//        bagProduct.setImage(MediaConversion.encodeToBase64(BitmapFactory.decodeResource(getResources(),R.drawable.product_1), Bitmap.CompressFormat.PNG,30));
//        bagProductArrayList.add(bagProduct);


            //end filling bag list
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
            salesman_bag_recyler_view = (RecyclerView) findViewById(R.id.salesman_bag_recycler_view);
            adapter = new BagAdapter(this.getApplicationContext(), bagProductArrayList);
            salesman_bag_recyler_view.setLayoutManager(mLayoutManager);
//        candidateRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
//        //or
//        candidateRecyclerView.addItemDecoration(new DividerItemDecoration(this));
//        //or
//        candidateRecyclerView.addItemDecoration(
//                new DividerItemDecoration(this, R.drawable.divider));
            //salesman_bag_recyler_view .setItemAnimator(new DefaultItemAnimator());
            salesman_bag_recyler_view.setAdapter(adapter);
            int resId = R.anim.layout_animation_from_left;
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
            salesman_bag_recyler_view.setLayoutAnimation(animation);


            RelativeLayout invoice_total_rlv = ((RelativeLayout) findViewById(R.id.invoice_total_rlv));
            int animId = R.anim.layout_animation_from_bottom;
            LayoutAnimationController animation2 = AnimationUtils.loadLayoutAnimation(this, animId);
            invoice_total_rlv.setLayoutAnimation(animation2);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(Constants.TAG,"her to refresh activity");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(Constants.TAG,"here to create owerflow menu");
        getMenuInflater().inflate(R.menu.profile_menu,menu);


        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            finish();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }



}
