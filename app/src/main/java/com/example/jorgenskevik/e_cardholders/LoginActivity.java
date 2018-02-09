package com.example.jorgenskevik.e_cardholders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.digits.sdk.android.Digits;
import com.example.jorgenskevik.e_cardholders.Variables.KVTVariables;
import com.example.jorgenskevik.e_cardholders.models.LoginModel;
import com.example.jorgenskevik.e_cardholders.models.SessionManager;
import com.example.jorgenskevik.e_cardholders.remote.UserAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity  implements
        View.OnClickListener {

    private static final String TAG = "PhoneAuthActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private ViewGroup mPhoneNumberViews;
    private ViewGroup mSignedInViews;

    private TextView mStatusText;
    private TextView mDetailText;

    private EditText mPhoneNumberField;
    private EditText mVerificationField;
    private EditText landskode;


    private Button mStartButton;
    private Button mVerifyButton;
    private Button mResendButton;
    private Button mSignOutButton;

    ProgressBar progressBar;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        JodaTimeAndroid.init(this);
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();

            progressBar = (ProgressBar) findViewById(R.id.progressBar);

            // Assign views
            mPhoneNumberViews = (ViewGroup) findViewById(R.id.phone_auth_fields);
            mSignedInViews = (ViewGroup) findViewById(R.id.signed_in_buttons);

            mStatusText = (TextView) findViewById(R.id.status);
            mDetailText = (TextView) findViewById(R.id.detail);

            mPhoneNumberField = (EditText) findViewById(R.id.field_phone_number);
            mVerificationField = (EditText) findViewById(R.id.field_verification_code);
            landskode = (EditText) findViewById(R.id.landcode);

            mStartButton = (Button) findViewById(R.id.button_start_verification);
            mVerifyButton = (Button) findViewById(R.id.button_verify_phone);
            mResendButton = (Button) findViewById(R.id.button_resend);
            mSignOutButton = (Button) findViewById(R.id.sign_out_button);

            // Assign click listeners
            mStartButton.setOnClickListener(this);
            mVerifyButton.setOnClickListener(this);
            mResendButton.setOnClickListener(this);
            mSignOutButton.setOnClickListener(this);

            // [START initialize_auth]
            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();

            // [END initialize_auth]

            // Initialize phone auth callbacks
            // [START phone_auth_callbacks]
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(PhoneAuthCredential credential) {

                    // This callback will be invoked in two situations:
                    // 1 - Instant verification. In some cases the phone number can be instantly
                    //     verified without needing to send or enter a verification code.
                    // 2 - Auto-retrieval. On some devices Google Play services can automatically
                    //     detect the incoming verification SMS and perform verificaiton without
                    //     user action.
                    Log.d(TAG, "onVerificationCompleted:" + credential);
                    // [START_EXCLUDE silent]
                    mVerificationInProgress = false;
                    // [END_EXCLUDE]

                    // [START_EXCLUDE silent]
                    // Update the UI and attempt sign in with the phone credential
                    updateUI(STATE_VERIFY_SUCCESS, credential);
                    // [END_EXCLUDE]
                    signInWithPhoneAuthCredential(credential);
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    // This callback is invoked in an invalid request for verification is made,
                    // for instance if the the phone number format is not valid.
                    Log.w(TAG, "onVerificationFailed", e);
                    // [START_EXCLUDE silent]
                    mVerificationInProgress = false;
                    // [END_EXCLUDE]

                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        // Invalid request
                        // [START_EXCLUDE]
                        try {
                            mPhoneNumberField.setError("Invalid phone number.");
                        }catch (NullPointerException r){
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internett), Toast.LENGTH_SHORT).show();

                        }
                        // [END_EXCLUDE]
                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        // The SMS quota for the project has been exceeded
                        // [START_EXCLUDE]
                        Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                                Snackbar.LENGTH_SHORT).show();
                        // [END_EXCLUDE]
                    }

                    // Show a message and update the UI
                    // [START_EXCLUDE]
                    updateUI(STATE_VERIFY_FAILED);
                    // [END_EXCLUDE]
                }

                @Override
                public void onCodeSent(String verificationId,
                                       PhoneAuthProvider.ForceResendingToken token) {
                    // The SMS verification code has been sent to the provided phone number, we
                    // now need to ask the user to enter the code and then construct a credential
                    // by combining the code with a verification ID.
                    Log.d(TAG, "onCodeSent:" + verificationId);

                    // Save verification ID and resending token so we can use them later
                    mVerificationId = verificationId;
                    mResendToken = token;

                    // [START_EXCLUDE]
                    // Update UI
                    updateUI(STATE_CODE_SENT);
                    // [END_EXCLUDE]
                }
            };

    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);

        // [START_EXCLUDE]
        if (mVerificationInProgress && validatePhoneNumber()) {
            try {
                startPhoneNumberVerification(mPhoneNumberField.getText().toString());
            }catch (NullPointerException e){
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.internett), Toast.LENGTH_SHORT).show();

            }
        }
        // [END_EXCLUDE]
    }
    // [END on_start_check_user]

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                landskode.getText().toString() + phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
        mStatusText.setVisibility(View.INVISIBLE);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                landskode.getText().toString() + phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                            updateUI(STATE_SIGNIN_SUCCESS, user);
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                mVerificationField.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }
    // [END sign_in_with_phone]

    private void signOut() {
        mAuth.signOut();
        updateUI(STATE_INITIALIZED);
    }

    private void updateUI(int uiState) {
        updateUI(uiState, mAuth.getCurrentUser(), null);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            updateUI(STATE_SIGNIN_SUCCESS, user);
        } else {
            updateUI(STATE_INITIALIZED);
        }
    }

    private void updateUI(int uiState, FirebaseUser user) {
        updateUI(uiState, user, null);
    }

    private void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred);
    }

    private void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {
        switch (uiState) {
            case STATE_INITIALIZED:
                // Initialized state, show only the phone number field and start button
                try {
                    enableViews(mStartButton, mPhoneNumberField);
                    disableViews(mVerifyButton, mResendButton, mVerificationField);
                    mDetailText.setText(null);
                    break;
                }catch (NullPointerException e){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.internett), Toast.LENGTH_SHORT).show();
                }

            case STATE_CODE_SENT:
                try {
                    // Code sent state, show the verification field, the
                    enableViews(mVerifyButton, mResendButton, mPhoneNumberField, mVerificationField);
                    disableViews(mStartButton);
                    mDetailText.setText(R.string.status_code_sent);
                    mDetailText.setTextColor(Color.parseColor("#43a047"));
                    break;
                }catch (NullPointerException e){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.internett), Toast.LENGTH_SHORT).show();
                }
            case STATE_VERIFY_FAILED:
                try {
                    // Verification has failed, show all options
                    enableViews(mStartButton, mVerifyButton, mResendButton, mPhoneNumberField,
                            mVerificationField);
                    mDetailText.setText(R.string.status_verification_failed);
                    mDetailText.setTextColor(Color.parseColor("#dd2c00"));
                    progressBar.setVisibility(View.INVISIBLE);
                    break;
                }catch (NullPointerException e){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.internett), Toast.LENGTH_SHORT).show();

                }
            case STATE_VERIFY_SUCCESS:
                try {
                    // Verification has succeeded, proceed to firebase sign in
                    disableViews(mStartButton, mVerifyButton, mResendButton, mPhoneNumberField,
                            mVerificationField);
                    mDetailText.setText("Verfication Sucessfull");
                    mDetailText.setTextColor(Color.parseColor("#43a047"));
                    progressBar.setVisibility(View.INVISIBLE);
                }catch (NullPointerException e){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.internett), Toast.LENGTH_SHORT).show();

                }

                // Set the verification text based on the credential
                if (cred != null) {

                    if (cred.getSmsCode() != null) {
                        mVerificationField.setText(cred.getSmsCode());
                    } else {
                        mVerificationField.setText(R.string.instant_validation);
                        mVerificationField.setTextColor(Color.parseColor("#4bacb8"));
                    }
                }

                break;
            case STATE_SIGNIN_FAILED:

                // No-op, handled by sign-in check
                mDetailText.setText(R.string.status_sign_in_failed);
                mDetailText.setTextColor(Color.parseColor("#dd2c00"));
                progressBar.setVisibility(View.INVISIBLE);
                break;
            case STATE_SIGNIN_SUCCESS:

                // Np-op, handled by sign-in check
                mStatusText.setText(R.string.signed_in);
                break;
        }

        if (user == null) {
            // Signed out
            mPhoneNumberViews.setVisibility(View.VISIBLE);
            mSignedInViews.setVisibility(View.GONE);

            mStatusText.setText(R.string.sign_out);;
        } else {

            // Signed in
            mPhoneNumberViews.setVisibility(View.GONE);
            /*
            mSignedInViews.setVisibility(View.VISIBLE);
            enableViews(mPhoneNumberField, mVerificationField);
            mPhoneNumberField.setText(null);
            mVerificationField.setText(null);
            mStatusText.setText(R.string.signed_in);
            mDetailText.setText(getString(R.string.firebase_status_fmt, user.getUid()));
            */


            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            mUser.getToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                String idToken = task.getResult().getToken();

                                HashMap<String,String> authHeader = new HashMap<String, String>();

                                authHeader.put("phoneNumber", mPhoneNumberField.getText().toString());
                                authHeader.put("firebase-token", idToken);
                                authHeader.put("client_key", KVTVariables.getAppkey());
                                authHeader.put("Accept-Version", KVTVariables.getAcceptVersion());

                                Gson gson = new GsonBuilder()
                                        .setLenient()
                                        .create();

                                //local eller base
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(KVTVariables.getBaseUrl())
                                        .addConverterFactory(GsonConverterFactory.create(gson))
                                        .build();

                                UserAPI userapi = retrofit.create(UserAPI.class);

                                userapi.userLogin(
                                        authHeader.get("phoneNumber"),
                                        authHeader.get("firebase-token"),
                                        authHeader.get("client_key"),
                                        authHeader.get("Accept-Version")).enqueue(new Callback<LoginModel>() {
                                    @Override
                                    public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                                        if (response.isSuccessful()) {
                                            LoginModel LoginList = response.body();

                                            sessionManager = new SessionManager(getApplicationContext());

                                            String usernameString = LoginList.user.getName();
                                            String emailString = LoginList.user.getEmail();
                                            String tokenString = LoginList.token;
                                            String picture = LoginList.user.getPicture();

                                            String studentNumber = LoginList.user.getStudentNumber();
                                            String id = LoginList.user.getId();

                                            String role = LoginList.user.getRole();
                                            String pictureToken = LoginList.user.getPictureToken();

                                            java.util.Date dateToExpiration = LoginList.user.getExpirationDate();
                                            java.util.Date birthdayDate = LoginList.user.getDateOfBirth();


                                            DateTime timeToExpiration = new DateTime(dateToExpiration);
                                            DateTime timeBirthday = new DateTime(birthdayDate);


                                            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd-MMM-yyyy");
                                            DateTimeFormatter dateTimeFormatter2 = DateTimeFormat.forPattern("yyyy-MM-dd");

                                            String birthDateString = dateTimeFormatter.print(timeBirthday);
                                            String expirationString = dateTimeFormatter2.print(timeToExpiration);

                                            //skriv noe her!
                                            sessionManager.createLoginSession(usernameString,emailString, tokenString, studentNumber, id, role, pictureToken, expirationString, birthDateString, picture);

                                            if (role.equals("admin")) {
                                                Context context = getApplicationContext();
                                                int duration = Toast.LENGTH_LONG;
                                                Toast toast = Toast.makeText(context, R.string.youareadmin, duration);
                                                toast.show();


                                            } else if (emailString.trim().equals("") || id.trim().equals("") || usernameString.trim().equals("") || role.trim().equals("") || pictureToken.trim().equals("")) {
                                                Context context = getApplicationContext();
                                                int duration = Toast.LENGTH_SHORT;
                                                Toast toast = Toast.makeText(context, R.string.contactIT, duration);
                                                toast.show();
                                                Intent intent = new Intent(LoginActivity.this, ContactUsActivity.class);
                                                startActivity(intent);

                                            } else {
                                                Intent intent = new Intent(LoginActivity.this, TermsActivity.class);
                                                startActivity(intent);

                                            }
                                        } else {
                                            Context context = getApplicationContext();
                                            CharSequence text = response.message();
                                            int duration = Toast.LENGTH_SHORT;
                                            Toast toast = Toast.makeText(context, text, duration);
                                            toast.show();
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<LoginModel> call, Throwable t) {
                                        Context context = getApplicationContext();
                                        CharSequence text = t.getMessage();
                                        int duration = Toast.LENGTH_SHORT;
                                        Toast toast = Toast.makeText(context, text, duration);
                                        toast.show();
                                    }
                                });
                                // Send token to your backend via HTTPS
                                // ...
                            } else {
                                // Handle error -> task.getException();
                            }
                        }
                    });

            //final Map<String, String> authHeader;
            //finish();
        }
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            try {
                mPhoneNumberField.setError("Invalid phone number.");

            }catch (NullPointerException e){
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.internett), Toast.LENGTH_SHORT).show();
            }
            //mPhoneNumberField.setTextColor(Color.parseColor("#ff1744"));
            return false;
        }

        return true;
    }

    private void enableViews(View... views) {
        for (View v : views) {
            v.setEnabled(true);
        }
    }

    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_start_verification:
                if (!validatePhoneNumber()) {
                    return;
                }

                ///////hide keyboard start
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                /////////hide keyboard end


                //mStatusText.setText("Authenticating....!");
                progressBar.setVisibility(View.VISIBLE);
                try {
                    startPhoneNumberVerification(mPhoneNumberField.getText().toString());
                }catch (NullPointerException e){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.internett), Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.button_verify_phone:
                String code = mVerificationField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mVerificationField.setError("Cannot be empty.");
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);
                break;
            case R.id.button_resend:
                try {
                    resendVerificationCode(mPhoneNumberField.getText().toString(), mResendToken);
                }catch (NullPointerException e){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.internett), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.sign_out_button:
                signOut();
                break;
        }
    }

}