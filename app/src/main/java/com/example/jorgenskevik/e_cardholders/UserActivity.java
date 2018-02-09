package com.example.jorgenskevik.e_cardholders;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.os.StrictMode;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.example.jorgenskevik.e_cardholders.Variables.KVTVariables;
import com.example.jorgenskevik.e_cardholders.models.SessionManager;
import com.example.jorgenskevik.e_cardholders.models.User;
import com.example.jorgenskevik.e_cardholders.remote.UserAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.crashlytics.android.Crashlytics.log;

/**
 * The type User activity.
 */
public class UserActivity extends AppCompatActivity implements ActionSheet.ActionSheetListener {
    /**
     * The constant CAM_REQUEST_CODE.
     */
    public static final int CAM_REQUEST_CODE = 457843;
    private static int IMAGE_GALLERY_REQUEST = 20;
    /**
     * The Session manager.
     */
    SessionManager sessionManager;
    /**
     * The Expiration button.
     */
//Uri imageUri, data;
    Button expirationButton;
    /**
     * The View 2.
     */
    ImageView view2, /**
     * The Image.
     */
    image;
    /**
     * The Code button.
     */
    ImageButton codeButton;
    /**
     * The First and sir name.
     */
    TextView firstAndSirName, /**
     * The Student id.
     */
    studentId, /**
     * The Birth day.
     */
    BirthDay;
    /**
     * The First and sir name string.
     */
    String firstAndSirNameString, /**
     * The This exp date.
     */
    thisExpDate, /**
     * The Birthday string.
     */
    birthdayString, /**
     * The Student id string.
     */
    studentIDString, /**
     * The Path.
     */
    path, /**
     * The Picture.
     */
    picture, /**
     * The Extra student id.
     */
    extraStudentID, /**
     * The Date time birthday.
     */
    dateTimeBirthday, /**
     * The Date time expiration.
     */
    dateTimeExpiration, /**
     * The Build version.
     */
    buildVersion, /**
     * The First letter.
     */
    firstLetter;
    /**
     * The Expiration date string.
     */
    String expirationDateString, /**
     * The Media path.
     */
    mediaPath, /**
     * The Formatted date.
     */
    formattedDate, /**
     * The Expiration date.
     */
    expirationDate, /**
     * The Four digits.
     */
    fourDigits, /**
     * The Picture path.
     */
    picturePath, /**
     * The Authenticate string.
     */
    authenticateString, /**
     * The Username.
     */
    username, /**
     * The Email.
     */
    email, /**
     * The Id.
     */
    id, /**
     * The Student number.
     */
    studentNumber, /**
     * The Role.
     */
    role, /**
     * The Picture token.
     */
    pictureToken;
    /**
     * The Simple date format.
     */
    SimpleDateFormat simpleDateFormat;
    /**
     * The Start date.
     */
    Date startDate, /**
     * The Date.
     */
    date;
    /**
     * The Selected color.
     */
    int selectedColor, /**
     * The Selected white.
     */
    selectedWhite, /**
     * The Duration.
     */
    duration, /**
     * The Number.
     */
    number, /**
     * The Column index.
     */
    columnIndex;
    /**
     * The Target format.
     */
    DateFormat targetFormat;
    /**
     * The Intent.
     */
    Intent intent, /**
     * The Photo picker intent.
     */
    photoPickerIntent;
    /**
     * The User details.
     */
    HashMap<String, String> userDetails;
    /**
     * The Context.
     */
    Context context;
    /**
     * The Toast.
     */
    Toast toast;
    /**
     * The Picture file.
     */
    File pictureFile, /**
     * The Directory.
     */
    directory, /**
     * The My path.
     */
    myPath;
    /**
     * The User date of birth date.
     */
    java.util.Date userDateOfBirthDate, /**
     * The User expiration date.
     */
    userExpirationDate;
    /**
     * The Date time.
     */
    DateTime dateTime, /**
     * The Date time 2.
     */
    dateTime2;
    /**
     * The Date time formatter.
     */
    DateTimeFormatter dateTimeFormatter, /**
     * The Date time formatter 2.
     */
    dateTimeFormatter2;
    /**
     * The Input stream.
     */
    InputStream inputStream;
    /**
     * The Bitmap.
     */
    Bitmap bitmap;
    /**
     * The Stream.
     */
    ByteArrayOutputStream stream;
    //Cursor cursor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.user_view);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        sessionManager = new SessionManager(getApplicationContext());
        view2 = (ImageView) findViewById(R.id.window1);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY);

        //View barcode

        codeButton = (ImageButton) findViewById(R.id.imageButton3);
        firstAndSirName = (TextView) findViewById(R.id.navnstring);
        BirthDay = (TextView) findViewById(R.id.stringfdato);
        studentId = (TextView) findViewById(R.id.studentid);
        userDetails = sessionManager.getUserDetails();

        firstAndSirNameString = userDetails.get(SessionManager.KEY_NAME);
        birthdayString = userDetails.get(SessionManager.KEY_BIRTHDATE);
        targetFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.GERMANY);

        studentIDString = userDetails.get(SessionManager.KEY_STUDENTNUMBER);
        path = userDetails.get(SessionManager.KEY_PATH);
        picture = userDetails.get(SessionManager.KEY_PICTURE);
        studentNumber = userDetails.get(SessionManager.KEY_STUDENTNUMBER);
        JodaTimeAndroid.init(this);


        if (path == null) {
            view2.setImageResource(R.drawable.facebookgirl);

            if (!picture.equals("")) {
                ContextWrapper cw = new ContextWrapper(this);
                File directory = cw.getDir(studentNumber, Context.MODE_PRIVATE);
                File myImageFile = new File(directory, "my_image.jpeg");
                Picasso.with(this).load(myImageFile).resize(300,300).centerCrop().into(view2);
            }
        } else {
            File f = new File(path);
            Picasso.with(getApplicationContext()).load(f).resize(300,300).centerCrop().into(view2);
        }

        firstAndSirName.setText(firstAndSirNameString);
        extraStudentID = getResources().getString(R.string.studentnumber) + " " + studentIDString;
        studentId.setText(extraStudentID);
        BirthDay.setText(birthdayString);
        sessionManager = new SessionManager(getApplicationContext());
        expirationDateString = userDetails.get(SessionManager.KEY_EXPERATIONDATE);
        expirationButton = (Button) findViewById(R.id.button11);

        startDate = null;
        try {
            startDate = simpleDateFormat.parse(expirationDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (System.currentTimeMillis() > startDate.getTime()) {
            //Ugyldig
            selectedColor = Color.rgb(254, 56, 36);
            expirationButton.setText(R.string.expired);
            expirationButton.setTextSize(30);
            expirationButton.setBackgroundColor(selectedColor);

        } else {
            //gyldig
            //selectedColor = Color.rgb(132, 205, 182);
            selectedColor = Color.rgb(164, 21, 99);
            selectedWhite = Color.rgb(255, 255, 255);
            int selectedBlack = Color.rgb(0, 0, 0);

            targetFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.GERMANY);
            try {
                if(Calendar.getInstance().get(Calendar.MONTH) + 1 < 8){
                    thisExpDate = userDetails.get(SessionManager.KEY_EXPERATIONDATE);
                    date = simpleDateFormat.parse(thisExpDate);
                    formattedDate = targetFormat.format(date);
                    expirationButton.setTransformationMethod(null);
                    expirationDate = getResources().getString(R.string.spring) + " " + Calendar.getInstance().get(Calendar.YEAR);
                    expirationButton.setText(expirationDate);
                    expirationButton.setTextColor(selectedWhite);
                    expirationButton.setBackgroundColor(selectedColor);
                    expirationButton.setTextSize(22);

                }else{
                    thisExpDate = userDetails.get(SessionManager.KEY_EXPERATIONDATE);
                    date = simpleDateFormat.parse(thisExpDate);
                    formattedDate = targetFormat.format(date);
                    expirationButton.setTransformationMethod(null);
                    expirationDate = getResources().getString(R.string.fall) + " " + Calendar.getInstance().get(Calendar.YEAR);
                    expirationButton.setText(expirationDate);
                    expirationButton.setTextColor(selectedWhite);
                    expirationButton.setBackgroundColor(selectedColor);
                    expirationButton.setTextSize(22);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        codeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivity.this, BarCodeActivity.class));
            }
        });


    }

    /**
     * Open settings.
     *
     * @param v the v
     */
    public void openSettings(View v) {
        setTheme(R.style.ActionSheetStyleiOS7);
        showActionSheet();
    }

    /**
     * Show action sheet.
     */
    public void showActionSheet() {
        ActionSheet.createBuilder(this, getSupportFragmentManager())
                .setCancelButtonTitle(R.string.closeSettings)
                .setOtherButtonTitles(getResources().getString(R.string.Loggout), getResources().getString(R.string.Terms), getResources().getString(R.string.setPicture),
                        getResources().getString(R.string.updateProfile), getResources().getString(R.string.policy))
                .setCancelableOnTouchOutside(true).setListener(this).show();
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        //Log out
        if (index == 0) {
            sessionManager.logoutUser();
            intent = new Intent(UserActivity.this, LandingPage.class);
            startActivity(intent);
        }

        //terms
        if (index == 1) {
            intent = new Intent(UserActivity.this, Terms1Activity.class);
            startActivity(intent);

        }
        //sett profilbilde
        if (index == 2) {
            userDetails = sessionManager.getUserDetails();
            fourDigits = userDetails.get(SessionManager.KEY_PICTURETOKEN);

            if (fourDigits.equals("BRUKT")) {
                context = getApplicationContext();
                duration = Toast.LENGTH_SHORT;
                toast = Toast.makeText(context, getResources().getString(R.string.DenyPicture), duration);
                toast.show();

            } else {
                selectedColor = Color.rgb(239, 146, 72);
                selectedWhite = Color.rgb(255, 255, 255);
                int selectedBlack = Color.rgb(50, 43, 43);
                int black = Color.rgb(0, 0, 0);

                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                setContentView(linearLayout);
                double widthParam = 0.92;
                double heightParam = 0.5;
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int width = dm.widthPixels;
                int height = dm.heightPixels;
                ImageView imageView = new ImageView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) (width * widthParam), (int) (height * heightParam));
                imageView.setLayoutParams(layoutParams);
                layoutParams.gravity = Gravity.CENTER;
                Button myButtonOk = new Button(this);
                Button myButtonCansel = new Button(this);
                myButtonOk.setText("OK");
                myButtonOk.setTextColor(selectedWhite);
                myButtonCansel.setTextColor(selectedWhite);
                myButtonOk.setBackgroundColor(selectedColor);
                myButtonCansel.setText(R.string.back);
                myButtonCansel.setBackgroundColor(selectedBlack);
                TextView myTextView = new TextView(this);
                myTextView.setTextSize(16);
                myTextView.setText(R.string.pictureMessage);
                myTextView.setTextColor(black);
                myTextView.setPadding(10, 10, 10, 10);
                linearLayout.addView(myTextView);
                linearLayout.addView(myButtonOk);
                linearLayout.addView(myButtonCansel);

                myButtonOk.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        pictureFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                        picturePath = pictureFile.getPath();
                        Uri data = Uri.parse(picturePath);
                        photoPickerIntent.setDataAndType(data, "image/*");
                        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
                    }
                });

                myButtonCansel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(UserActivity.this, UserActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }
        //Oppdater brukeren
        if (index == 3) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(KVTVariables.getBaseUrl())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            userDetails = sessionManager.getUserDetails();

            authenticateString = "Bearer " + userDetails.get(SessionManager.KEY_TOKEN);

            UserAPI userapi = retrofit.create(UserAPI.class);
            userapi.getUser(KVTVariables.getAcceptVersion(), authenticateString).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        User user = response.body();

                        // Session Manager
                        sessionManager = new SessionManager(getApplicationContext());

                        username = user.getName();
                        email = user.getEmail();
                        studentNumber = user.getStudentNumber();
                        id = user.getId();
                        role = user.getRole();
                        pictureToken = user.getPictureToken();
                        picture = user.getPicture();

                        userDateOfBirthDate = user.getDateOfBirth();
                        userExpirationDate = user.getExpirationDate();

                        dateTime = new DateTime(userDateOfBirthDate);
                        dateTime2 = new DateTime(userExpirationDate);

                        dateTimeFormatter = DateTimeFormat.forPattern("dd-MMM-yyyy");
                        dateTimeFormatter2 = DateTimeFormat.forPattern("yyyy-MM-dd");

                        dateTimeBirthday = dateTimeFormatter.print(dateTime);
                        dateTimeExpiration = dateTimeFormatter2.print(dateTime2);

                        sessionManager.deletePhoto();
                        sessionManager.createUpdatedLoginSession(username, email, studentNumber, id, role, pictureToken, dateTimeBirthday, dateTimeExpiration, picture);


                        startDate = null;
                        try {
                            startDate = simpleDateFormat.parse(dateTimeExpiration);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (System.currentTimeMillis() > startDate.getTime()) {
                            //Ugyldig
                            selectedColor = Color.rgb(254, 56, 36);
                            expirationButton.setText(R.string.expired);
                            expirationButton.setTextSize(30);
                            expirationButton.setBackgroundColor(selectedColor);
                        } else {
                            //gyldig
                            selectedColor = Color.rgb(164, 21, 99);
                            selectedWhite = Color.rgb(255, 255, 255);
                            targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
                            try {
                                if(Calendar.getInstance().get(Calendar.MONTH) + 1 < 8){
                                    date = simpleDateFormat.parse(dateTimeExpiration);
                                    formattedDate = targetFormat.format(date);
                                    expirationDate =getResources().getString(R.string.spring) + " " + Calendar.getInstance().get(Calendar.YEAR);
                                    expirationButton.setText(expirationDate);
                                    expirationButton.setTextColor(selectedWhite);
                                    expirationButton.setBackgroundColor(selectedColor);
                                    expirationButton.setTextSize(22);
                                }else{
                                    date = simpleDateFormat.parse(dateTimeExpiration);
                                    formattedDate = targetFormat.format(date);
                                    expirationDate =  getResources().getString(R.string.fall) + " " + Calendar.getInstance().get(Calendar.YEAR);
                                    expirationButton.setText(expirationDate);
                                    expirationButton.setTextColor(selectedWhite);
                                    expirationButton.setBackgroundColor(selectedColor);
                                    expirationButton.setTextSize(22);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        if(picture.equals("")){
                            view2.setImageResource(R.drawable.facebookgirl);
                        }else{

                            ContextWrapper cw = new ContextWrapper(getApplicationContext());
                            File directory = cw.getDir(studentNumber, Context.MODE_PRIVATE);
                            File myImageFile = new File(directory, "my_image.jpeg");
                            myImageFile.getAbsoluteFile().delete();
                            myImageFile.delete();
                            deleteFile("my_image.jpeg");
                            Picasso.with(getApplicationContext()).invalidate(myImageFile);
                            deleteTempFolder(studentNumber);

                            try {
                                myImageFile.getCanonicalFile().delete();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            try {
                                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(directory, "my_image.jpeg"))));
                            }catch (NullPointerException e){
                            }


                            try{
                                userDetails = sessionManager.getUserDetails();
                                userDetails = sessionManager.getUserDetails();
                                String kortfri = userDetails.get(SessionManager.KEY_TURN);
                                if(kortfri.equals("kortfri")){
                                    Picasso.with(getApplicationContext()).load(user.getPicture()).into(picassoImageTarget(getApplicationContext(), user.getStudentNumber(), "my_image.jpeg"));
                                    Picasso.with(getApplicationContext()).load(picture).resize(300,300).centerCrop().into(view2);
                                }else{
                                    float rotateImage = Float.parseFloat(userDetails.get(SessionManager.KEY_TURN));
                                    Picasso.with(getApplicationContext()).load(user.getPicture()).rotate(rotateImage).into(picassoImageTarget(getApplicationContext(), user.getStudentNumber(), "my_image.jpeg"));
                                    Picasso.with(getApplicationContext()).load(picture).rotate(rotateImage).resize(300,300).centerCrop().into(view2);
                                }

                            }catch (NullPointerException e){
                                Picasso.with(getApplicationContext()).load(user.getPicture()).into(picassoImageTarget(getApplicationContext(), user.getStudentNumber(), "my_image.jpeg"));
                                Picasso.with(getApplicationContext()).load(picture).resize(300,300).centerCrop().into(view2);
                            }
                           }

                        firstAndSirName.setText(username);
                        BirthDay.setText(dateTimeBirthday);
                        String extra = getResources().getString(R.string.studentnumber) + " " + studentNumber;
                        studentId.setText(extra);

                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.userUpdated), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.userNotUpdated), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.userNotUpdated), Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (index == 4) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://privacypolicy.kortfri.no"));
            startActivity(browserIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        buildVersion = Build.VERSION.RELEASE;
        firstLetter = String.valueOf(buildVersion.charAt(0));
        number = Integer.parseInt(firstLetter);
        if (number < 6) {
            System.gc();
            if (data == null) {
                return;
            }
            android.net.Uri imageUri = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContentResolver().query(imageUri, filePath, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            columnIndex = cursor.getColumnIndex(filePath[0]);
            mediaPath = cursor.getString(columnIndex);
            cursor.close();

            intent = new Intent(this, PictureActivity.class);
            intent.putExtra("picture", mediaPath);
            startActivity(intent);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (resultCode == RESULT_OK) {
                if (requestCode == IMAGE_GALLERY_REQUEST) {
                    android.net.Uri imageUri = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    android.database.Cursor cursor = getContentResolver().query(imageUri, filePath, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();
                    columnIndex = cursor.getColumnIndex(filePath[0]);
                    mediaPath = cursor.getString(columnIndex);
                    cursor.close();

                    intent = new Intent(this, PictureActivity.class);
                    intent.putExtra("picture", mediaPath);
                    startActivity(intent);
                }
            }

        } else {
            String[] permissionRequest = {Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permissionRequest, CAM_REQUEST_CODE);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAM_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "hallo", Toast.LENGTH_LONG).show();

            } else {
            }
        }
    }

    /**
     * Shake.
     *
     * @param v the v
     */
    public void shake(View v) {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.anime);
        findViewById(R.id.window1).startAnimation(shake);
    }

    public String hentString(Context context, String imageDir,String imageName){
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(imageDir, Context.MODE_PRIVATE);
        File myImageFile = new File(directory, imageName);
        return myImageFile.getAbsolutePath();
    }

    private Target picassoImageTarget(Context context, final String imageDir, final String imageName) {
        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE);
        return new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, imageName);
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {
                }
            }
        };
    }



    private void deleteTempFolder(String dir) {
        File myDir = new File(Environment.getExternalStorageDirectory() + "/"+dir);
        if (myDir.isDirectory()) {
            String[] children = myDir.list();
            for (int i = 0; i < children.length; i++) {
                new File(myDir, children[i]).delete();
            }
        }
    }

}