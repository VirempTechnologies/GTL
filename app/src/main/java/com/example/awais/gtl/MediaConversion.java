package com.example.awais.gtl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by awais on 8/16/2017.
 */

public class MediaConversion {
    //String myBase64Image = encodeToBase64(myBitmap, Bitmap.CompressFormat.JPEG, 100);
    //Bitmap myBitmapAgain = decodeBase64(myBase64Image);
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
    public static String ConvertTOBase64(File file) throws IOException {
        byte[] bytes;
        FileInputStream fileInputStream;
        fileInputStream = new FileInputStream(file);
        bytes =  new  byte[fileInputStream.available()];
        fileInputStream.read(bytes);
        String base64String = Base64.encodeToString(bytes,Base64.DEFAULT);
        return  base64String;
    }

    public static String convertImagetoString(ImageView img)
    {
        Bitmap photo = ((BitmapDrawable) img.getDrawable()).getBitmap();// this is your image.
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);

        byte[] byteArray = stream.toByteArray();
        return Base64.encodeToString(byteArray,Base64.DEFAULT);
    }
    public static Bitmap getBimapFromByteArray(byte[] array)
    {
        return  BitmapFactory.decodeByteArray(array,0,array.length);
    }
    public static Bitmap getBitmapFromView(View v) {
        //v.measure(View.MeasureSpec.makeMeasureSpec(v.getLayoutParams().width, View.MeasureSpec.EXACTLY),
      //          View.MeasureSpec.makeMeasureSpec(v.getLayoutParams().height, View.MeasureSpec.EXACTLY));
        //v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =v.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        v.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }
    public static Bitmap getBitmapFromViewNew(View v) {
        v.measure(View.MeasureSpec.makeMeasureSpec(v.getLayoutParams().width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(v.getLayoutParams().height, View.MeasureSpec.EXACTLY));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

        Log.d(Constants.TAG,v.getHeight()+"");
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ALPHA_8);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }


}
