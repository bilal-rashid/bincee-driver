package com.bincee.driver.helper;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import androidx.core.content.FileProvider;

public class StorageUtils {
    public static File offlineImageNewFile(Context context, String name) {
        try {
            return new File(getFilesDIR(context), URLEncoder.encode(name.trim().replaceAll("/", "").replaceAll(" ", ""), "utf-8") + "-" + System.currentTimeMillis() + ".jpg");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new File(getFilesDIR(context), name.trim().replaceAll("/", "").replaceAll(" ", "") + "-" + System.currentTimeMillis() + ".jpg");

    }


    public static File offlineImagePath(Context context) {
        return getFilesDIR(context);
    }


    private static File getFilesDIR(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
    }

    public static Uri fileToUri(Context context, File imageFile) {
        return   FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName()+".fileprovider" , imageFile);
    }
}
