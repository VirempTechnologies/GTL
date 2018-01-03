package com.example.awais.gtl.Adapters;

/**
 * Created by awais on 8/29/2017.
 */
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awais.gtl.Activities.BagActivity;
import com.example.awais.gtl.Activities.SaleHistoryActivity;
import com.example.awais.gtl.Pojos.Client;
import com.example.awais.gtl.Pojos.Transaction;
import com.example.awais.gtl.R;

import java.util.ArrayList;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Transaction> transactionsArrayList;
    RecyclerView recyclerView;
    View rootView ;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView transaction_date,account_disc,debit_balance,credit_balance;
        public RelativeLayout transaction_item;

        public MyViewHolder(View view) {
            super(view);
            transaction_date = (TextView) view.findViewById(R.id.transaction_date_text);
            account_disc = (TextView) view.findViewById(R.id.account_disc_text);
            debit_balance = (TextView) view.findViewById(R.id.debit_balance_text);
            credit_balance = (TextView) view.findViewById(R.id.credit_balance_text);
            transaction_item = (RelativeLayout) view.findViewById(R.id.parent_rlv);
        }
    }


    public TransactionAdapter(Context mContext, ArrayList<Transaction> transactionsArrayList) {
        this.mContext = mContext;
        this.transactionsArrayList = transactionsArrayList;
        //rootView = ((Activity)mContext).getWindow().getDecorView().findViewById(R.id.parent_rlv);
        //recyclerView = (RecyclerView) rootView.findViewById(R.id.product_cart_recycler_view);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_single_item, parent, false);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        itemView.setLayoutParams(params);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            final Transaction transaction = transactionsArrayList.get(position);
            holder.transaction_date.setText(transaction.getTransactionDate());
            holder.account_disc.setText(transaction.getAccountType()+"|"+transaction.getAccountTitle());
            holder.debit_balance.setText(transaction.getDebitBalance()+" €");
            holder.credit_balance.setText(transaction.getCreditBalance()+" €");

            holder.transaction_item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    return false;
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
                   // Toast.makeText(mContext, "settings", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.logout:
                 //   Toast.makeText(mContext, "logout", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return transactionsArrayList.size();
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
