package com.duzoo.android.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.duzoo.android.application.MyApplication;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by RRaju on 3/18/2015.
 */
public class Util {


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
            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
            return bmp;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertBitmapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,1, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static Bitmap convertStringToBitmap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static void hideKeyBoard(Activity activity) {
        if (activity == null)
            return;

        InputMethodManager manager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocus = activity.getCurrentFocus();
        if (manager == null || currentFocus == null)
            return;

        manager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
    }

    public static void focusKeyBoard(Context context, View view) {
        if (view != null) {
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInputFromWindow(view.getApplicationWindowToken(),
                    InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static File getAlbumStorageDir() {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), DuzooConstants.IMAGE_SAVE_DIRECTORY_NAME);
        if (!file.mkdirs()) {
            file.mkdir();
        }
        return file;
    }

    public static boolean saveImageToAppDirectory(ParseFile file,String fileName) {
        FileOutputStream outputStream;

        try {
            outputStream = MyApplication.getContext().openFileOutput(getAlbumStorageDir()+"/"+fileName, Context.MODE_PRIVATE);
            outputStream.write(file.getData());
            outputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
