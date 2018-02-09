package com.example.jorgenskevik.e_cardholders;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;


import java.util.EnumMap;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.example.jorgenskevik.e_cardholders.models.SessionManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import com.example.jorgenskevik.e_cardholders.Variables.KVTVariables;


/**
 * The type Bar code activity.
 */
public class BarCodeActivity extends Activity{

    /**
     * The Session.
     */
    SessionManager session;
    private static double widthParam = 0.92;
    private static double heightParam = 0.5;
    private static double widthSize = 0.9;
    private static double heightSize = 0.7;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        setContentView(linearLayout);
        session = new SessionManager(getApplicationContext());

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        TextView textView = new TextView(this);
        textView.setText(R.string.barcode);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(32);
        textView.setTextColor(BLACK);

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String id = user.get(SessionManager.KEY_STUDENTNUMBER);

        // barcode image
        Bitmap bitmap = null;
        ImageView imageView = new ImageView(this);
        try {

            bitmap = encodeAsBitmap(id, BarcodeFormat.CODE_39, 700, 200);
            imageView.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int)(width*widthParam), (int)(height*heightParam));
        imageView.setLayoutParams(layoutParams);
        layoutParams.gravity=Gravity.CENTER;
        linearLayout.addView(textView);
        linearLayout.addView(imageView);

        Button cancel = new Button(this);
        int selectedColor = Color.rgb(164, 21, 99);
        cancel.setBackgroundColor(selectedColor);
        cancel.setText(R.string.CancelBarCode);

        cancel.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {
                Intent intent = new Intent(BarCodeActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });

        linearLayout.addView(cancel);

        getWindow().setLayout((int) (width*widthSize) ,(int) (height*heightSize));
    }


    /**
     * Encode as bitmap bitmap.
     *
     * @param contents   the contents
     * @param format     the format
     * @param img_width  the img width
     * @param img_height the img height
     * @return the bitmap
     * @throws WriterException the writer exception
     */
    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

}


