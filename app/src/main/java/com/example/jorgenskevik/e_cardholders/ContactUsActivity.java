package com.example.jorgenskevik.e_cardholders;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import static android.R.id.message;

/**
 * The type Contact us activity.
 */
public class ContactUsActivity extends Activity {
    /**
     * The constant phoneNumber.
     */
//public static final String phoneNumber = "tel:72894940";
    public static final String phoneNumber = "tel:998560594";
    public static final String emailNumber = "stine.bottolfs@signo.no";
    private static final int WHITE = 0xFFFFFFFF;

    Button phone;
    Button email;

    /**
     * The constant web.
     */
    public static final String web = "http://www.kortfri.no";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.contact_us_view);

        phone = (Button) findViewById(R.id.editText);
        email = (Button) findViewById(R.id.editText2);
        emailNumber.toLowerCase();
        phone.setText(phoneNumber);
        phone.setHintTextColor(WHITE);
        phone.setTextColor(WHITE);
        email.setText(emailNumber);
        email.setHintTextColor(WHITE);
        email.setTextColor(WHITE);
    }

    /**
     * Open phone.
     *
     * @param view the view
     */
    public void OpenPhone(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(phoneNumber));
        startActivity(intent);
    }

    /**
     * Open mail.
     *
     * @param view the view
     */
    public void OpenMail(View view){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {"stine.bottolfs@signo.no"});
        Intent mailer = Intent.createChooser(intent, null);
        startActivity(mailer);
    }

    /**
     * Open browser.
     *
     * @param view the view
     */
    public void openBrowser(View view){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(web)));
    }
}
