package com.example.awais.gtl.Activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.awais.gtl.Adapters.BagAdapter;
import com.example.awais.gtl.Adapters.CheckOutItemsAdapter;
import com.example.awais.gtl.Adapters.IMEIAdapter;
import com.example.awais.gtl.Constants;
import com.example.awais.gtl.MediaConversion;
import com.example.awais.gtl.Pojos.CheckOutItem;
import com.example.awais.gtl.Pojos.Client;
import com.example.awais.gtl.Pojos.SoldItem;
import com.example.awais.gtl.R;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.riyagayasen.easyaccordion.AccordionView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by awais on 11/28/2017.
 */

public class CheckOutActivity extends AppCompatActivity
{
    CheckOutItemsAdapter adapter;
    RecyclerView checkout_product_recycler_view;
    ArrayList<CheckOutItem> checkOutItemsArrayList;
    long total=0;
    String path, filename="ReceiptPDF",invoice_date;
    int invoice_id;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_out_activity);
        permissions.add(INTERNET);
        permissions.add(ACCESS_NETWORK_STATE);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_EXTERNAL_STORAGE);
        // permissions.add(CAMERA);

        //        permissions.add(RECORD_AUDIO);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //calling the os to take permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
        //invoice file path and name
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDFfiles/");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }

        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDFfiles/";
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDFfiles/");

        path = path + filename + ".pdf";
        //end file path
        try {


            final Client client = (Client) getIntent().getSerializableExtra("client");
            final String currentbalance = getIntent().getStringExtra("current_balance");

            ((TextView) findViewById(R.id.client_name)).setText(client.getClient_name());
            ((TextView) findViewById(R.id.client_current_balance)).setText(currentbalance);
            ((TextView) findViewById(R.id.client_company_name)).setText(client.getCompany_name());
//            String string = "{\n" +
//                    "    \"imei_list\": [\n" +
//                    "        {\n" +
//                    "            \"product_id\": 2,\n" +
//                    "            \"sale_price\": 170,\n" +
//                    "            \"imei\": \"352619090969784\",\n" +
//                    "            \"product_name\": \"Glaxy S6\"\n" +
//                    "        },\n" +
//                    "        {\n" +
//                    "            \"product_id\": 2,\n" +
//                    "            \"imei\": \"352619090969636\",\n" +
//                    "            \"sale_price\": 170,\n" +
//                    "            \"product_name\": \"Glaxy S6\"\n" +
//                    "        },\n" +
//                    "        {\n" +
//                    "            \"product_id\": 1,\n" +
//                    "            \"imei\": \"352619090592966\",\n" +
//                    "            \"sale_price\": 50,\n" +
//                    "            \"product_name\": \"Nokia\"\n" +
//                    "        }\n" +
//                    "    ],\n" +
//                    "    \"success\": \"Sale created successfully.\",\n" +
//                    "    \"status_code\": \"200\"\n" +
//                    "}";

            JSONObject respObject = new JSONObject(getIntent().getStringExtra("respObject"));
//            JSONObject respObject = new JSONObject(string);
            Log.d(Constants.TAG,"data: "+respObject.toString());
            checkOutItemsArrayList = new ArrayList<>();
            ArrayList<SoldItem> soldItems = new ArrayList<>();
            SoldItem soldItem = new SoldItem();
            JSONArray allProducts = respObject.getJSONArray("imei_list");
            for(int i=0;i<allProducts.length();i++)
            {
                JSONObject object = allProducts.getJSONObject(i);
                soldItem.setProduct_id(object.getInt("product_id"));
                soldItem.setProduct_name(object.getString("product_name"));
                soldItem.setProduct_Model(object.getString("model"));
                soldItem.setImei(object.getString("imei"));
                soldItem.setSale_price(object.getLong("sale_price"));
                total+=object.getLong("sale_price");
                soldItems.add(soldItem);
                //object= new JSONObject();
                soldItem = new SoldItem();
            }

            int oldProductId =-1;
            CheckOutItem checkOutItem= new CheckOutItem();
            for(SoldItem item :soldItems)
            {
                if(item.getProduct_id()!=oldProductId)
                {
                    checkOutItem.setProductName(item.getProduct_name());
                    checkOutItem.setCollectivePrice(item.getSale_price());
                    checkOutItem.getIMEINos().add(item.getImei());
                    checkOutItem.setSalePrice(item.getSale_price());
                    checkOutItem.setProductModel(item.getProduct_Model());
                    checkOutItemsArrayList.add(checkOutItem);
                    oldProductId=item.getProduct_id();
                    checkOutItem= new CheckOutItem();
                }
                else
                {
                    for(CheckOutItem checkOutItem1:checkOutItemsArrayList)
                    {
                        if(checkOutItem1.getProductName().equals(item.getProduct_name()))
                        {
                            checkOutItem1.setCollectivePrice(checkOutItem1.getCollectivePrice()+item.getSale_price());
                            checkOutItem1.getIMEINos().add(item.getImei());
                        }
                    }
                }
            }

            //set recycler view here
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(CheckOutActivity.this, 1);
            checkout_product_recycler_view = (RecyclerView) findViewById(R.id.checkout_product_recycler_view);
            adapter = new CheckOutItemsAdapter(CheckOutActivity.this, checkOutItemsArrayList);
            checkout_product_recycler_view.setLayoutManager(mLayoutManager);
            checkout_product_recycler_view.setAdapter(adapter);
            int resId = R.anim.layout_animation_from_left;
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(CheckOutActivity.this, resId);
            checkout_product_recycler_view.setLayoutAnimation(animation);
            ((TextView) findViewById(R.id.product_grand_total_price)).setText(total+" €");
            createPdf(path,checkOutItemsArrayList);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.d(Constants.TAG,"exception :"+ex.getMessage());
        }

    }
    public void createPdf(String dest,ArrayList<CheckOutItem> checkOutItemsArrayList) throws IOException, DocumentException {
//        Document document = new Document();
//        PdfWriter.getInstance(document, new FileOutputStream(dest));
//
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        document.setMargins(50,10,10,10);

        PdfContentByte canvas = writer.getDirectContent();
        Rectangle rect = new Rectangle(36, 36, 559, 806);
        rect.setBorder(Rectangle.BOX);
        rect.setBorderWidth(1);
        canvas.rectangle(rect);
        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 25.0f, Font.BOLD, BaseColor.BLACK);
        Chunk c = new Chunk("Receipt Detail", f);
        Paragraph p = new Paragraph(c);
        p.setAlignment(Element.ALIGN_LEFT);
        p.setIndentationLeft(20.5f);
        document.add(p);
        //
        Paragraph p4= new Paragraph("Receipt Id:"+invoice_id);
        p4.setAlignment(Element.ALIGN_RIGHT);
        p4.setIndentationRight(20.5f);
        document.add(p4);

        p4 = new Paragraph(invoice_date);
        p4.setAlignment(Element.ALIGN_RIGHT);
        p4.setIndentationRight(20.5f);
        document.add(p4);

        p4 = new Paragraph( ((TextView) findViewById(R.id.client_name)).getText().toString());
        p4.setIndentationLeft(20.5f);
        document.add(p4);

        p4 = new Paragraph(((TextView) findViewById(R.id.client_company_name)).getText().toString());
        p4.setIndentationLeft(20.5f);
        document.add(p4);
        p4= new Paragraph("Current Balance: "+((TextView) findViewById(R.id.client_current_balance)).getText().toString());
        p4.setIndentationLeft(20.5f);
        document.add(p4);



        CheckOutItem item = new CheckOutItem();
        for(int i=0;i<checkOutItemsArrayList.size();i++) {
            item= checkOutItemsArrayList.get(i);

            Paragraph heading = new Paragraph(item.getProductName()+" "+item.getProductModel());
            heading.add("        ("+item.getIMEINos().size()+" x "+item.getSalePrice()+" €)");
            heading.add("        "+item.getCollectivePrice()+" €");

            heading.setIndentationLeft(40.5f);
            heading.setPaddingTop(50);
            heading.setSpacingBefore(10);
            document.add(heading);
            List imeiList = new List(List.ORDERED);
            imeiList.setFirst(1);
            for (int j = 0; j < item.getIMEINos().size(); j++) {
                imeiList.add(item.getIMEINos().get(j));
            }
            imeiList.setIndentationLeft(60.0f);
            document.add(imeiList);

        }
        Paragraph pp = new Paragraph("____________________________________________________________________");
        pp.setAlignment(Element.ALIGN_CENTER);
        document.add(pp);
        // set total price
        //Font f = new Font(Font.FontFamily.TIMES_ROMAN, 25.0f, Font.BOLD, BaseColor.BLACK);
        Chunk cc = new Chunk("Total              "+((TextView) findViewById(R.id.product_grand_total_price)).getText().toString(), f);
        Paragraph p2 = new Paragraph(cc);
        p2.setAlignment(Element.ALIGN_LEFT);
        p2.setIndentationLeft(20.5f);

        document.add(p2);

        document.close();
        Log.d(Constants.TAG,"File successfully created");
    }
    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (hasPermission(perms)) {

                    } else {

                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application to vew PDF Files or Share it . Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                                //Log.d("API123", "permisionrejected " + permissionsRejected.size());

                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(Constants.TAG,"here to create owerflow menu");
        getMenuInflater().inflate(R.menu.receipt_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            finish();
            return  true;
        }
        else if(item.getItemId()==R.id.view_pdf)
        {
            File file = new File(path);
            Uri uri = FileProvider.getUriForFile(CheckOutActivity.this, getApplicationContext().getPackageName() + ".provider",file);
            Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
            pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pdfOpenintent.setDataAndType(uri, "application/pdf");
            pdfOpenintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pdfOpenintent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            try {
                startActivity(pdfOpenintent);
            }
            catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Log.e(Constants.TAG,e.getMessage());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Constants.gotoClient=true;
    }
}
