package com.example.awais.gtl.Adapters;

/**
 * Created by awais on 8/29/2017.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awais.gtl.Activities.BagActivity;
import com.example.awais.gtl.Activities.CartActivity;
import com.example.awais.gtl.Activities.CheckOutActivity;
import com.example.awais.gtl.Activities.ClientLedgerActivity;
import com.example.awais.gtl.Activities.SaleHistoryActivity;
import com.example.awais.gtl.Activities.SalemanClientsActivity;
import com.example.awais.gtl.Constants;
import com.example.awais.gtl.Pojos.CartItem;
import com.example.awais.gtl.Pojos.Client;
import com.example.awais.gtl.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.MyViewHolder> implements Filterable {

    private Context mContext;
    private ArrayList<Client> clientArrayList;
    private ArrayList<Client> filterClientArrayList;
    RecyclerView recyclerView;
    SharedPreferences settings;
    View rootView ;

    @Override
    public Filter getFilter() {


        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchString = constraint.toString().toLowerCase();
                if(searchString.isEmpty())
                {
                    filterClientArrayList=clientArrayList;

                }
                else
                {
                    ArrayList<Client> filteredClientArrayList= new ArrayList<>();
                    for (Client client : clientArrayList)
                    {
                        if(client.getCompany_name().toLowerCase().contains(searchString) || client.getClient_name().toLowerCase().contains(searchString) || client.getCurrent_bal().toLowerCase().contains(searchString))
                        {
                            filteredClientArrayList.add(client);
                        }
                    }
                    filterClientArrayList=filteredClientArrayList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterClientArrayList;
                return filterResults;

                //return null;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterClientArrayList = (ArrayList<Client>) results.values;
                notifyDataSetChanged();
            }
        };

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView client_name,company_name,current_balance;
        public RelativeLayout client_item;

        public MyViewHolder(View view) {
            super(view);
            client_name = (TextView) view.findViewById(R.id.client_name);
            company_name = (TextView) view.findViewById(R.id.company_name);
            current_balance = (TextView) view.findViewById(R.id.current_balance);
            client_item = (RelativeLayout) view.findViewById(R.id.parent_rlv);
        }
    }


    public ClientAdapter(Context mContext, ArrayList<Client> clientArrayList) {
        this.mContext = mContext;
        this.clientArrayList = clientArrayList;
        this.filterClientArrayList = clientArrayList;
        rootView = ((Activity)mContext).getWindow().getDecorView().findViewById(R.id.parent_rlv);
        //recyclerView = (RecyclerView) rootView.findViewById(R.id.product_cart_recycler_view);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_single_item, parent, false);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        itemView.setLayoutParams(params);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
//            final Client client = clientArrayList.get(position);
            final Client client = filterClientArrayList.get(position);
            holder.client_name.setText(client.getClient_name());
            holder.company_name.setText(client.getCompany_name());
            holder.current_balance.setText(client.getCurrent_bal()+"");

            holder.client_item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final String[]items = new String[]{"Create New Order","Orders History","View Profile","Ledger Report","Statistics","Payment"};
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Select an Option");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(which==0)
                            {
                                (mContext).startActivity((new Intent(mContext, BagActivity.class).putExtra("client",client)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                ((Activity) mContext).overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                            }
                            else if(which==1)
                            {
                                (mContext).startActivity((new Intent(mContext, SaleHistoryActivity.class).putExtra("client",client)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                ((Activity) mContext).overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                            }
                            else if(which==3)
                            {
                                (mContext).startActivity((new Intent(mContext, ClientLedgerActivity.class).putExtra("client",client)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                ((Activity) mContext).overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                            }
                            else if(which==5)
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyle);
                                builder.setTitle("Enter Payment Received");
                                View viewInflated = ((Activity) mContext).getLayoutInflater().inflate(R.layout.payment_dialog, null);
                                final AutoCompleteTextView input = (AutoCompleteTextView) viewInflated.findViewById(R.id.payment_text);
                                builder.setView(viewInflated);

                                // Set up the buttons
                                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        if(Long.parseLong(input.getText().toString())<Float.parseFloat(client.getCurrent_bal().replace(" €","")))
                                        {
                                            AlertDialog.Builder builder =
                                                    new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyle);
                                            builder.setTitle("Opps");
                                            builder.setIcon(R.drawable.corss);
                                            builder.setMessage("payment not be greater then current balance");
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });
                                            builder.show();

                                        }
                                        else {
                                            try {
                                                Calendar cal = Calendar.getInstance();
                                                //cal.add(Calendar.DATE, 0);
                                                SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
                                                String todayDate = format1.format(cal.getTime());

                                                settings = ((Activity) mContext).getSharedPreferences("GTL-Settings", 0);
                                                final String headerss = settings.getString("headers", null);
                                                JSONObject headers = new JSONObject(headerss);
                                                final JSONObject params = new JSONObject();
                                                params.put("headers", headers);
                                                params.put("payer_id", client.getClient_id());
                                                params.put("date", todayDate);
                                                params.put("amount", Long.parseLong(input.getText().toString()));
                                                sendPayment(params, mContext);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
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
                                //InputMethodManager imm = (InputMethodManager) ((Activity) mContext).getSystemService(Context.INPUT_METHOD_SERVICE);
                                //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                            }


                        }
                    });
                    builder.show();
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
                  //  Toast.makeText(mContext, "logout", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return filterClientArrayList.size();
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
    public void sendPayment(JSONObject params, final Context context){
        final ProgressDialog prgDialog;
        prgDialog = new ProgressDialog(context);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait... sending payment");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        try {
            // Show Progress Dialog

            // Make RESTful webservice call using AsyncHttpClient object
            cz.msebera.android.httpclient.entity.StringEntity entity = new cz.msebera.android.httpclient.entity.StringEntity(params.toString());
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(context ,Constants.getSavePaymentUrl, entity, "application/json",new JsonHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started
                    prgDialog.show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject resp) {
                    try {


                        super.onSuccess(statusCode, headers, resp);
                        Log.d(Constants.TAG, "status: " + statusCode);
                        Log.d(Constants.TAG, "success data say congo : " + resp.toString());

                        if(statusCode==200) {
                            if (resp.getString("status_code").equals("200")) {
                                prgDialog.dismiss();
                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                                builder.setTitle("Message");
                                builder.setIcon(R.drawable.tick);
                                builder.setMessage("payment saved successful..");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        final String headerss = settings.getString("headers",null);

                                        JSONObject headers = null;
                                        try {
                                            headers = new JSONObject(headerss);
                                            JSONObject params2 = new JSONObject();
                                            params2.put("headers", headers);
                                            ((SalemanClientsActivity)((Activity)mContext)).invokeWS(params2,mContext);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                                builder.show();


                            } else if (resp.getString("status_code").equals("422")) {
                                prgDialog.dismiss();

                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                                builder.setTitle("Opps");
                                builder.setIcon(R.drawable.corss);
                                builder.setMessage(resp.optString("error"));
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //finish();
                                    }
                                });
                                builder.show();
                            }
                        }
                        if(statusCode==500)
                        {
                            prgDialog.dismiss();

                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                            builder.setTitle("500");
                            builder.setIcon(R.drawable.corss);
                            builder.setMessage("internal Server Error ! ");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //finish();
                                }
                            });
                            builder.show();
                        }
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    prgDialog.dismiss();

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Opps");
                    builder.setIcon(R.drawable.corss);
                    builder.setMessage("Service Failer server not found..! ");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //finish();
                        }
                    });
                    builder.show();

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    prgDialog.dismiss();

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Opps");
                    builder.setIcon(R.drawable.corss);
                    builder.setMessage("Service Failer server not found..! ");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //finish();
                        }
                    });
                    builder.show();

                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                }


            });

        }
        catch (Exception ex)
        {
            ex.getStackTrace();
        }
    }

}
