package com.example.awais.gtl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
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

}
