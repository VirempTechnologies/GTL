package com.example.awais.gtl.Adapters;

/**
 * Created by awais on 8/29/2017.
 */
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
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
import com.example.awais.gtl.Pojos.Sale;
import com.example.awais.gtl.R;
import com.riyagayasen.easyaccordion.AccordionView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AllReciptsAdapter extends RecyclerView.Adapter<AllReciptsAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Sale> salesArrayList;

    View rootView ;
    long subTotal=0;
    ReceiptAdapter adapter;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        AccordionView accordionView ;
        RecyclerView date_receipt_recycler_view;

        public MyViewHolder(View view) {
            super(view);
            accordionView = (AccordionView) view.findViewById(R.id.sale_accordion);
            date_receipt_recycler_view = (RecyclerView) accordionView.findViewById(R.id.date_receipt_recycler_view);
            //  product_quantity_price = (TextView) view.findViewById(R.id.product_quantity_price);
        }
    }


    public AllReciptsAdapter(Context mContext, ArrayList<Sale> salesArrayList) {
        this.mContext = mContext;
        this.salesArrayList= salesArrayList;
        //rootView = ((Activity)mContext).getWindow().getDecorView().findViewById(R.id.parent_rlv);
        //recyclerView = (RecyclerView) rootView.findViewById(R.id.product_cart_recycler_view);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_receipts_single_item, parent, false);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        itemView.setLayoutParams(params);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            final Sale sale= salesArrayList.get(position);
            holder.accordionView.setHeadingString(sale.getSale_date());
            ((TextView)holder.accordionView.findViewById(R.id.heading)).setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            ((TextView)holder.accordionView.findViewById(R.id.heading)).setTextSize(15);
            ((ImageView)holder.accordionView.findViewById(R.id.dropup_image)).getDrawable().setTint(mContext.getResources().getColor(R.color.colorPrimaryDark));

            //setting the imei adapter
            adapter = new ReceiptAdapter(mContext,sale.getSale_receipts());
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 1);
            holder.date_receipt_recycler_view.setLayoutManager(mLayoutManager);
            holder.date_receipt_recycler_view.setAdapter(adapter);
        }
        catch (Exception ex)
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
                    //Toast.makeText(mContext, "settings", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.logout:
                   // Toast.makeText(mContext, "logout", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return salesArrayList.size();
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
