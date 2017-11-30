package com.example.awais.gtl.Adapters;

/**
 * Created by awais on 8/29/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.awais.gtl.Constants;
import com.example.awais.gtl.MediaConversion;
import com.example.awais.gtl.Pojos.BagProduct;
import com.example.awais.gtl.Pojos.CartItem;
import com.example.awais.gtl.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class MyStockAdapter extends RecyclerView.Adapter<MyStockAdapter.MyViewHolder> {

    private Context mContext;
    View rootView ;
    double subTotal=0;
    private ArrayList<BagProduct> bagProductArrayList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView product_image;
        public TextView product_name,my_stock,company_stock,sale_price;
        public RelativeLayout bagRlv;

        public MyViewHolder(View view) {
            super(view);
            product_name = (TextView) view.findViewById(R.id.product_name);
            my_stock = (TextView) view.findViewById(R.id.my_stock);
            company_stock = (TextView) view.findViewById(R.id.company_stock_text);
            sale_price = (TextView) view.findViewById(R.id.price_text);
            bagRlv = (RelativeLayout) view.findViewById(R.id.bag_rlv);
            product_image= (ImageView) view.findViewById(R.id.product_image);
        }
    }


    public MyStockAdapter(Context mContext, ArrayList<BagProduct> bagProductArrayList) {
        this.mContext = mContext;
        this.bagProductArrayList= bagProductArrayList;
        rootView = ((Activity)mContext).getWindow().getDecorView().findViewById(R.id.parent_rlv);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_stock_single_item, parent, false);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        itemView.setLayoutParams(params);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BagProduct bagProduct= bagProductArrayList.get(position);
        holder.product_name.setText(bagProduct.getProductName() + " "+bagProduct.getModel()+"");
        holder.my_stock.setText(bagProduct.getMyStock()+"");
        holder.company_stock.setText(bagProduct.getCompanyStock()+"");
        holder.sale_price.setText(bagProduct.getSalePrice()+"");
              Drawable d = new BitmapDrawable(mContext.getResources(), MediaConversion.decodeBase64(bagProduct.getImage()));
        Glide.with(mContext)
                .load("")
                .placeholder(d)
                .into(holder.product_image);
        // loading album cover using Glide library
//        Glide.with(mContext).load(R.drawable.product_1).into(holder.product_image);
////        holder.candidateImage.setImageBitmap(MediaConversion.decodeBase64(candidate.getCandidateImageBase64()));
//       // holder.product_image.setImageBitmap(MediaConversion.decodeBase64(bagProduct.getImage()));
//        Glide.with(mContext).fromBytes().asBitmap().into(holder.product_image);
//          holder.overflow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopupMenu(holder.overflow);
//            }
//        });
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
        return bagProductArrayList.size();
    }

    public void updateSum()
    {
        subTotal=0;
        Log.d(Constants.TAG,"cart size: "+Constants.cartItemsMap.size());

        for (Map.Entry<String,CartItem> cartItem:Constants.cartItemsMap.entrySet())
        {
            subTotal+=cartItem.getValue().getCollective_price();
        }
        ((TextView)rootView.findViewById(R.id.client_grand_total)).setText(subTotal+" €");
    }
}
