package com.example.awais.gtl.Activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;

import com.example.awais.gtl.Constants;
import com.example.awais.gtl.Pojos.Operation;
import com.example.awais.gtl.Adapters.OperationsAdapter;
import com.example.awais.gtl.R;
import com.example.awais.gtl.WebServiceHelper;

import java.util.ArrayList;

/**
 * Created by awais on 17/05/2017.
 */
public class SalemanProfile extends AppCompatActivity {
    OperationsAdapter adapter;
    ArrayList<Operation> operationArrayList;
    RecyclerView salesman_operation_recyler_view;
    WebServiceHelper webServiceHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.salesman_profile);
        try {


            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
            actionBar.setElevation(0);
            //temp data base
            webServiceHelper = new WebServiceHelper();
            //fill all the operations

            operationArrayList = new ArrayList<>();
            Operation operation = new Operation();
            operation.setOperationName("My Sales");
            operation.setOperationIcon(R.drawable.today_invoice);
            operationArrayList.add(operation);

            operation = new Operation();
            operation.setOperationName("My Clients");
            operation.setOperationIcon(R.drawable.my_client);
            operationArrayList.add(operation);

            operation = new Operation();
            operation.setOperationName("My Reports");
            operation.setOperationIcon(R.drawable.my_report);
            operationArrayList.add(operation);

            operation = new Operation();
            operation.setOperationName("Add Customer");
            operation.setOperationIcon(R.drawable.add_customer);
            operationArrayList.add(operation);

            operation = new Operation();
            operation.setOperationName("My Stock");
            operation.setOperationIcon(R.drawable.my_stock);
            operationArrayList.add(operation);

            operation = new Operation();
            operation.setOperationName("Company Stock");
            operation.setOperationIcon(R.drawable.company_stock);
            operationArrayList.add(operation);


            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);

            salesman_operation_recyler_view = (RecyclerView) findViewById(R.id.salesman_operations_recycler_view);
            adapter = new OperationsAdapter(this, operationArrayList);
            salesman_operation_recyler_view.setLayoutManager(mLayoutManager);
//        candidateRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
//        //or
//        candidateRecyclerView.addItemDecoration(new DividerItemDecoration(this));
//        //or
//        candidateRecyclerView.addItemDecoration(
//                new DividerItemDecoration(this, R.drawable.divider));
            salesman_operation_recyler_view.setItemAnimator(new DefaultItemAnimator());
            salesman_operation_recyler_view.setAdapter(adapter);
            salesman_operation_recyler_view.setAdapter(adapter);

            // grid animations
            int resId = R.anim.layout_animation_fall_down;
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
            salesman_operation_recyler_view.setLayoutAnimation(animation);
            //end
            //user profile animiation
            RelativeLayout profileLayout = (RelativeLayout) findViewById(R.id.user_profile_info_rlv);
            int animationId = R.anim.layout_animation_from_left;
            LayoutAnimationController profileDataAnimation = AnimationUtils.loadLayoutAnimation(this, animationId);
            profileLayout.setLayoutAnimation(profileDataAnimation);
            //end
            RelativeLayout profileImageLayout = (RelativeLayout) findViewById(R.id.parent_rlv);
            int profileImageAnimationId = R.anim.layout_animation_fall_down;
            LayoutAnimationController profileImageAnimation = AnimationUtils.loadLayoutAnimation(this, profileImageAnimationId);
            profileImageLayout.setLayoutAnimation(profileImageAnimation);
            //end


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
        Log.d(Constants.TAG,"here to refresh activity");
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

}
