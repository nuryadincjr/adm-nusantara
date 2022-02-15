package com.nuyradincjr.ebusantara.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.util.ArrayList;
import java.util.List;

public class Constant {
    public static final String TITLE_VIW_ONLY = "View only";
    public static final int RC_SIGN_IN = 0;

//    public static Bitmap getQrCode(String code){
//        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//        Bitmap bitmap = null;
//        try {
//            bitmap = barcodeEncoder.encodeBitmap(code, BarcodeFormat.QR_CODE, 700, 700);
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//
//        return bitmap;
//    }

    public static String getFileExtension(Uri imageUri, Context context) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(context
                .getContentResolver().getType(imageUri));
    }

    public static List<String> getList(String str) {
        String[] myStrings = str.split(",");
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < myStrings.length; i++) {
            String item = myStrings[i].replace(" ", "");
            if(item.equals("OtherProducts")) item = "Other Products";

            stringList.add(i ,item);
        }
        return stringList;
    }

    public static void getSinggleImage(Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(intent, 25);
    }
}
