package com.duzoo.android.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.parse.ParseException;
import com.parse.ParseFile;

import java.io.ByteArrayOutputStream;

/**
 * Created by RRaju on 3/18/2015.
 */
public class ImageUtil {


    public static String convertParseFileToString(ParseFile parseFile) {
        try {
            byte[] data = parseFile.getData();
            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0,data.length);
            String image = convertBitmapToString(bmp);
            return image;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap convertParseFileToBitmap(ParseFile parseFile) {
        try {
            byte[] data = parseFile.getData();
            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0,data.length);
            return bmp;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertBitmapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static Bitmap convertStringToBitmap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
