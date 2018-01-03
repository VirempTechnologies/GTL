package com.example.awais.gtl.Adapters;

/**
 * Created by awais on 8/29/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.awais.gtl.Activities.BagActivity;
import com.example.awais.gtl.Constants;
import com.example.awais.gtl.Pojos.BagProduct;
import com.example.awais.gtl.MediaConversion;
import com.example.awais.gtl.Pojos.CartItem;
import com.example.awais.gtl.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class BagAdapter extends RecyclerView.Adapter<BagAdapter.MyViewHolder> {

    private Context mContext;
    View rootView ;
    double subTotal=0;
    private ArrayList<BagProduct> bagProductArrayList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView product_image;
        public TextView product_name,my_stock,company_stock,sale_price,quantity_text,collective_price;
        public Button plus_btn,minus_btn,changeBtn;
        public RelativeLayout bagRlv;

        public MyViewHolder(View view) {
            super(view);
            product_name = (TextView) view.findViewById(R.id.product_name);
            my_stock = (TextView) view.findViewById(R.id.my_stock);
            company_stock = (TextView) view.findViewById(R.id.company_stock_text);
            sale_price = (TextView) view.findViewById(R.id.price_text);
            quantity_text = (TextView) view.findViewById(R.id.quantity_text);
            collective_price = (TextView) view.findViewById(R.id.single_product_collective_price);

            bagRlv = (RelativeLayout) view.findViewById(R.id.bag_rlv);
            product_image= (ImageView) view.findViewById(R.id.product_image);
            plus_btn =(Button)view.findViewById(R.id.btn_plus);
            minus_btn =(Button)view.findViewById(R.id.btn_minus);
            changeBtn = (Button) view.findViewById(R.id.btn_change_price);
        }
    }


    public BagAdapter(Context mContext, ArrayList<BagProduct> bagProductArrayList) {
        this.mContext = mContext;
        this.bagProductArrayList= bagProductArrayList;
        rootView = ((Activity)mContext).getWindow().getDecorView().findViewById(R.id.parent_rlv);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bag_single_item, parent, false);
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
        if(bagProduct.getDiscountedPrice()==0) {
            holder.sale_price.setText(bagProduct.getSalePrice() + ".00 €");
        }
        else if(bagProduct.getDiscountedPrice()!=0) {
            holder.sale_price.setText(bagProduct.getDiscountedPrice() + ".00 €");
        }
        holder.quantity_text.setText(bagProduct.getInitialQuantity()+"");
        if(bagProduct.getInitialQuantity()==0)
        {
            holder.collective_price.setText(0+".00 €");
            updateSum();
        }
        holder.bagRlv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(mContext, "bag item", Toast.LENGTH_SHORT).show();
                // ((Activity) mContext).overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
        holder.plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, "plus btn", Toast.LENGTH_SHORT).show();
                if(Integer.parseInt(holder.quantity_text.getText().toString())==bagProduct.getMyStock())
                {

                }
                else if(Integer.parseInt(holder.quantity_text.getText().toString())<bagProduct.getMyStock())
                {


                    int oldQuantity =Integer.parseInt(holder.quantity_text.getText().toString());
                    int newQuantity = oldQuantity+1;
                    holder.quantity_text.setText(newQuantity+"");

                    long price=0;
                    if(bagProduct.getDiscountedPrice()!=0)
                    {
                        price= newQuantity*bagProduct.getDiscountedPrice();
                    }
                    else if(bagProduct.getDiscountedPrice()==0)
                    {
                        price= newQuantity*bagProduct.getSalePrice();

                    }
                    holder.collective_price.setText(price+".00 €");

                    if(Constants.cartItemsMap.containsKey(bagProduct.getProductName())) {
                        Log.d(Constants.TAG,"Update old product of ..");
                        Constants.cartItemsMap.get(bagProduct.getProductName()).setQuantity(newQuantity);
                        Constants.cartItemsMap.get(bagProduct.getProductName()).setCollective_price(price);
                        Constants.bagProductArrayList.get(position).setInitialQuantity(newQuantity);

                        updateSum();
                    }
                    else
                    {
                        Log.d(Constants.TAG,"add new product to cart..");
                        CartItem item = new CartItem();
                        item.setProduct_id(bagProduct.getProduct_id());
                        item.setProduct_name(bagProduct.getProductName()+" "+bagProduct.getModel());
                        item.setQuantity(newQuantity);
                        Log.d(Constants.TAG,"discounted price="+bagProduct.getDiscountedPrice()+" salePrice="+bagProduct.getSalePrice());
                        if(bagProduct.getDiscountedPrice()!=0) {
                            item.setSale_price(bagProduct.getDiscountedPrice());
                            Log.d(Constants.TAG,"in the discounted price");

                        }

                        else if(bagProduct.getDiscountedPrice()==0)
                        {
                            item.setSale_price(bagProduct.getSalePrice());
                            Log.d(Constants.TAG,"in the sale price");

                        }
                        item.setCollective_price(price);
                        item.setBagIndex(position);
                        Constants.cartItemsMap.put(bagProduct.getProductName()+" "+bagProduct.getModel(),item);
                        Log.d(Constants.TAG,"new item added successfully");
                        Constants.bagProductArrayList.get(position).setInitialQuantity(newQuantity);

                        updateSum();
                    }
                }
            }
        });
        holder.minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(mContext, "minus btn", Toast.LENGTH_SHORT).show();
                Log.d(Constants.TAG,holder.quantity_text.getText().toString()+"");

                if(Integer.parseInt(holder.quantity_text.getText().toString())<=0)
                {

                }
                else if(Integer.parseInt(holder.quantity_text.getText().toString())<=bagProduct.getMyStock())
                {
                    int oldQuantity =Integer.parseInt(holder.quantity_text.getText().toString());
                    int newQuantity = oldQuantity-1;
                    if(newQuantity<=0)
                    {
                            Log.d(Constants.TAG,"delete this product");
                            Constants.cartItemsMap.remove(bagProduct.getProductName()+" "+bagProduct.getModel());
                            Log.d(Constants.TAG,"product successfully deleted...");
                            holder.quantity_text.setText(newQuantity + "");
                            Constants.bagProductArrayList.get(position).setInitialQuantity(newQuantity);
                        holder.collective_price.setText("0.00 €");
                        updateSum();
                    }
                    else {
                        holder.quantity_text.setText(newQuantity + "");
                        long price = newQuantity * bagProduct.getSalePrice();
                        holder.collective_price.setText(price + ".00 €");
                        Log.d(Constants.TAG, "Update old product of ..");
                        Constants.cartItemsMap.get(bagProduct.getProductName()+" "+bagProduct.getModel()).setQuantity(newQuantity);
                        Constants.cartItemsMap.get(bagProduct.getProductName()+" "+bagProduct.getModel()).setCollective_price(price);
                        Constants.bagProductArrayList.get(position).setInitialQuantity(newQuantity);

                        updateSum();
                    }
//                    holder.quantity_text.setText((Integer.parseInt(holder.quantity_text.getText().toString())-1)+"");

                }
            }
        });
        holder.changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(Constants.TAG,"quantity="+holder.quantity_text.getText());
                if(holder.quantity_text.getText().equals("0")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Enter Discounted price");
                    final View viewInflated = ((Activity) mContext).getLayoutInflater().inflate(R.layout.payment_dialog, null);
                    final AutoCompleteTextView input = (AutoCompleteTextView) viewInflated.findViewById(R.id.payment_text);
                    final InputMethodManager imm = (InputMethodManager) ((Activity) mContext).getSystemService(Context.INPUT_METHOD_SERVICE);

                    builder.setView(viewInflated);

                    // Set up the buttons
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            try {

                                    if (Long.parseLong(input.getText().toString()) >= bagProduct.getSalePrice() - 5) {
                                        holder.sale_price.setText(input.getText() + ".00 €");
//                                        bagProduct.setSalePrice(Long.parseLong(input.getText().toString()));
                                        bagProduct.setDiscountedPrice(Long.parseLong(input.getText().toString()));
                                        Log.d(Constants.TAG,"discounted price="+bagProduct.getDiscountedPrice()+" salePrice="+bagProduct.getSalePrice());
                                        holder.sale_price.setTextColor(mContext.getResources().getColor(R.color.colorGreen));
                                    } else {
                                        AlertDialog.Builder builder =
                                                new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyle);
                                        builder.setTitle("Opps");
                                        builder.setIcon(R.drawable.corss);
                                        builder.setMessage("Discounted price not less then " + (bagProduct.getSalePrice() - 5) + " €");
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //finish();
                                            }
                                        });
                                        builder.show();
                                    }
                                    imm.hideSoftInputFromWindow(
                                            input.getWindowToken(), 0);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                    input.requestFocus();
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);

//                imm.showSoftInput(input,
//                        InputMethodManager.SHOW_FORCED);
                    //input.requestFocus();
                }

            else
            {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyle);
                builder.setTitle("Opps");
                builder.setIcon(R.drawable.corss);
                builder.setMessage("First remove all items of this product.\n then set discounted price..");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    }
                });
                builder.show();

            }

        }
        });
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
                   // Toast.makeText(mContext, "settings", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.logout:
                    //Toast.makeText(mContext, "logout", Toast.LENGTH_SHORT).show();
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
