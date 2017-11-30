package com.example.awais.gtl.Adapters;

/**
 * Created by awais on 8/29/2017.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awais.gtl.Activities.BagActivity;
import com.example.awais.gtl.Activities.MyStockAcitivity;
import com.example.awais.gtl.Activities.ReceiptsActivity;
import com.example.awais.gtl.Activities.SaleDetailActitvity;
import com.example.awais.gtl.Constants;
import com.example.awais.gtl.Pojos.Operation;
import com.example.awais.gtl.Pojos.Receipt;
import com.example.awais.gtl.R;

import java.util.ArrayList;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Receipt> receiptsArrayList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView clientName,receiptPrice,invoiceID,companyName;
        public RelativeLayout receiptElementRlV;

        public MyViewHolder(View view) {
            super(view);
            clientName = (TextView) view.findViewById(R.id.client_name);
            receiptPrice = (TextView) view.findViewById(R.id.invoice_total_price);
            invoiceID = (TextView) view.findViewById(R.id.invoice_no);
            companyName = (TextView) view.findViewById(R.id.company_name_text);
            receiptElementRlV = (RelativeLayout) view.findViewById(R.id.parent_rlv);

        }
    }


    public ReceiptAdapter(Context mContext, ArrayList<Receipt> receiptsArrayList) {
        this.mContext = mContext;
        this.receiptsArrayList = receiptsArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipt_single_item, parent, false);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        itemView.setLayoutParams(params);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        try {
            final Receipt receipt = receiptsArrayList.get(position);
            holder.clientName.setText(receipt.getClient_name());
            holder.companyName.setText(receipt.getCompany_name());
            holder.invoiceID.setText("invoice # "+receipt.getInvoice_id());
            holder.receiptPrice.setText(receipt.getTotal_amount()+" €");
            holder.receiptElementRlV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.d(Constants.TAG, holder.clientName.getText() + "");
                    Toast.makeText(mContext, "invoice id: "+receipt.getInvoice_id(), Toast.LENGTH_SHORT).show();
                    (mContext).startActivity((new Intent(mContext, SaleDetailActitvity.class).putExtra("invoice_id",receipt.getInvoice_id())).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    ((Activity) mContext).overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                }
            });

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
        return receiptsArrayList.size();
    }
}
