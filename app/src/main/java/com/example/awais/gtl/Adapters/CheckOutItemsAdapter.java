package com.example.awais.gtl.Adapters;

/**
 * Created by awais on 8/29/2017.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awais.gtl.Constants;
import com.example.awais.gtl.Pojos.CartItem;
import com.example.awais.gtl.Pojos.CheckOutItem;
import com.example.awais.gtl.R;
import com.riyagayasen.easyaccordion.AccordionView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class CheckOutItemsAdapter extends RecyclerView.Adapter<CheckOutItemsAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<CheckOutItem> checkOutItemsArrayList;
    RecyclerView recyclerView;
    View rootView ;
    long subTotal=0;
    IMEIAdapter adapter;
    ArrayList<Bitmap> checkOutItemBitmaps = new ArrayList<>();
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView product_name,product_total_price,quantity_saleprice;
        AccordionView accordionView ;
        RecyclerView imei_no_recycler_view;

        public MyViewHolder(View view) {
            super(view);
            product_name = (TextView) view.findViewById(R.id.product_name);
            accordionView = (AccordionView) view.findViewById(R.id.imei_accordion);
            imei_no_recycler_view = (RecyclerView) accordionView.findViewById(R.id.imei_no_recycler_view);
            //  product_quantity_price = (TextView) view.findViewById(R.id.product_quantity_price);
            product_total_price = (TextView) view.findViewById(R.id.product_total_price);
            quantity_saleprice = (TextView) view.findViewById(R.id.quantity_price);
        }
    }


    public CheckOutItemsAdapter(Context mContext, ArrayList<CheckOutItem> checkOutItemsArrayList) {
        this.mContext = mContext;
        this.checkOutItemsArrayList= checkOutItemsArrayList;
        //rootView = ((Activity)mContext).getWindow().getDecorView().findViewById(R.id.parent_rlv);
        //recyclerView = (RecyclerView) rootView.findViewById(R.id.product_cart_recycler_view);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.check_out_single_item, parent, false);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        itemView.setLayoutParams(params);
        Log.d(Constants.TAG,"view holder product name"+((TextView)itemView.findViewById(R.id.product_name)).getText().toString());
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            final CheckOutItem checkOutItem= checkOutItemsArrayList.get(position);
            holder.product_name.setText(checkOutItem.getProductName()+" " + checkOutItem.getProductModel());
//            holder.product_quantity_price.setText(cartItem.getQuantity()+" x "+cartItem.getSale_price()+"€");
            holder.product_total_price.setText(checkOutItem.getCollectivePrice()+" €");
            holder.accordionView.setHeadingString("IMEI/Serial Numbers");
            ((TextView)holder.accordionView.findViewById(R.id.heading)).setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            ((TextView)holder.accordionView.findViewById(R.id.heading)).setTextSize(15);
            //((TextView)holder.accordionView.findViewById(R.id.heading));
            ((ImageView)holder.accordionView.findViewById(R.id.dropup_image)).getDrawable().setTint(mContext.getResources().getColor(R.color.colorPrimaryDark));

            //setting the imei adapter
            adapter = new IMEIAdapter(mContext,checkOutItem.getIMEINos());
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 1);
            holder.imei_no_recycler_view.setLayoutManager(mLayoutManager);
            holder.imei_no_recycler_view.setAdapter(adapter);
            holder.quantity_saleprice.setText("("+checkOutItem.getIMEINos().size()+" x "+checkOutItem.getSalePrice()+" €)");




//            holder.btn_delete_item.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    Toast.makeText(mContext, "delete this item", Toast.LENGTH_SHORT).show();
//                    cartItemArrayList.remove(position);
//                    Constants.cartItemsMap.remove(cartItem.getProduct_name());
//
//                    Log.d(Constants.TAG,"size "+ Constants.cartItemsMap.size());
//                    Log.d(Constants.TAG,"set quantity to zero of bag item : "+ cartItem.getBagIndex());
//                    Constants.bagProductArrayList.get(cartItem.getBagIndex()).setInitialQuantity(0);
//                    //runLayoutAnimation(recyclerView);
//                    updateSubTotal();
//                    notifyDataSetChanged();
//
//                }
//            });


            // loading album cover using Glide library
//        Glide.with(mContext).load(category.getCatIcon()).into(holder.catIcon);

//        holder.candidateImage.setImageBitmap(MediaConversion.decodeBase64(candidate.getCandidateImageBase64()));
            //holder.operationIcon.setImageResource(operation.getOperationIcon());
            //Glide.with(mContext).fromBytes().asBitmap().into(holder.candidateImage);
//          holder.overflow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopupMenu(holder.overflow);
//            }
//        });
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.profile_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.setting_menu:
                    Toast.makeText(mContext, "settings", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.logout:
                    Toast.makeText(mContext, "logout", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return checkOutItemsArrayList.size();
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    public void  updateSubTotal()
    {
        subTotal=0;
        for (Map.Entry<String,CartItem> cartItem:Constants.cartItemsMap.entrySet())
        {
            subTotal+=cartItem.getValue().getCollective_price();
        }
        TextView product_receipt_total_price = (TextView) rootView.findViewById(R.id.product_receipt_total_price);
        TextView product_grand_total_price = (TextView) rootView.findViewById(R.id.product_grand_total_price);
        product_receipt_total_price.setText(subTotal+"€");
        product_grand_total_price.setText(subTotal+"€");
    }
}
