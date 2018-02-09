package com.example.jorgenskevik.e_cardholders.Variables;

import android.content.SharedPreferences;

/**
 * Created by jorgenskevik on 23.03.2017.
 */
public class KVTVariables {

    private static String Local_URL = "http://10.0.2.2:9000/";
    private static String AcceptVersion = "V2";
    private static String TWITTER_KEY = "OwT8vAGt92TMSDRK3O9naZznW";
    private static String TWITTER_SECRET = "PfC1rhbLQ8WSnmJCgqKwyfPZJyjyAoLHzx61sClci42tiiA38R";
    private static String Appkey = "d034dd9794d1159b0a3f734fe5d3dfce";
    private static String BASE_URL = "https://apisigno.kortfri.no/";

    private static String tokenlogin = "";
    private static String userID = "";
    private static String userName = "";
    private static String userEmail = "";
    private static String tokenPhone = "";
    private static String pictureToken = "";
    private static String picture = "";
    private static String role = "";
    private static String birthday = "";
    private static SharedPreferences userIdPrefs;


    /**
     * Gets local url.
     *
     * @return the local url
     */
    public static String getLocal_URL() {
        return Local_URL;
    }

    /**
     * Gets twitter key.
     *
     * @return the twitter key
     */
    public static String getTwitterKey() {
        return TWITTER_KEY;
    }

    /**
     * Gets twitter secret.
     *
     * @return the twitter secret
     */
    public static String getTwitterSecret() {
        return TWITTER_SECRET;
    }

    /**
     * Gets appkey.
     *
     * @return the appkey
     */
    public static String getAppkey() {
        return Appkey;
    }

    /**
     * Gets accept version.
     *
     * @return the accept version
     */
    public static String getAcceptVersion() {
        return AcceptVersion;
    }

    /**
     * Gets base url.
     *
     * @return the base url
     */
    public static String getBaseUrl() {
        return BASE_URL;
    }

    /**
     * Gets tokenlogin.
     *
     * @return the tokenlogin
     */
    public static String getTokenlogin() {
        return tokenlogin;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public static String getUserID() {
        return userID;
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public static String getUserName() {
        return userName;
    }

    /**
     * Gets user email.
     *
     * @return the user email
     */
    public static String getUserEmail() {
        return userEmail;
    }

    /**
     * Gets token phone.
     *
     * @return the token phone
     */
    public static String getTokenPhone() {
        return tokenPhone;
    }

    /**
     * Gets picture token.
     *
     * @return the picture token
     */
    public static String getPictureToken() {
        return pictureToken;
    }

    /**
     * Gets picture.
     *
     * @return the picture
     */
    public static String getPicture() {
        return picture;
    }

    /**
     * Gets role.
     *
     * @return the role
     */
    public static String getRole() {
        return role;
    }

    /**
     * Sets local url.
     *
     * @param local_URL the local url
     */
    public static void setLocal_URL(String local_URL) {
        Local_URL = local_URL;
    }

    /**
     * Sets twitter key.
     *
     * @param twitterKey the twitter key
     */
    public static void setTwitterKey(String twitterKey) {
        TWITTER_KEY = twitterKey;
    }

    /**
     * Sets twitter secret.
     *
     * @param twitterSecret the twitter secret
     */
    public static void setTwitterSecret(String twitterSecret) {
        TWITTER_SECRET = twitterSecret;
    }

    /**
     * Sets appkey.
     *
     * @param appkey the appkey
     */
    public static void setAppkey(String appkey) {
        Appkey = appkey;
    }

    /**
     * Sets accept version.
     *
     * @param acceptVersion the accept version
     */
    public static void setAcceptVersion(String acceptVersion) {
        AcceptVersion = acceptVersion;
    }

    /**
     * Sets base url.
     *
     * @param baseUrl the base url
     */
    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }

    /**
     * Sets tokenlogin.
     *
     * @param tokenlogin the tokenlogin
     */
    public static void setTokenlogin(String tokenlogin) {
        KVTVariables.tokenlogin = tokenlogin;
    }

    /**
     * Sets user id.
     *
     * @param userID the user id
     */
    public static void setUserID(String userID) {
        KVTVariables.userID = userID;
    }

    /**
     * Sets user name.
     *
     * @param userName the user name
     */
    public static void setUserName(String userName) {
        KVTVariables.userName = userName;
    }

    /**
     * Sets user email.
     *
     * @param userEmail the user email
     */
    public static void setUserEmail(String userEmail) {
        KVTVariables.userEmail = userEmail;
    }

    /**
     * Sets token phone.
     *
     * @param tokenPhone the token phone
     */
    public static void setTokenPhone(String tokenPhone) {
        KVTVariables.tokenPhone = tokenPhone;
    }

    /**
     * Sets picture token.
     *
     * @param pictureToken the picture token
     */
    public static void setPictureToken(String pictureToken) {
        KVTVariables.pictureToken = pictureToken;
    }

    /**
     * Sets picture.
     *
     * @param picture the picture
     */
    public static void setPicture(String picture) {
        KVTVariables.picture = picture;
    }

    /**
     * Sets role.
     *
     * @param role the role
     */
    public static void setRole(String role) {
        KVTVariables.role = role;
    }

    /**
     * Gets user id prefs.
     *
     * @return the user id prefs
     */
    public static SharedPreferences getUserIdPrefs() {
        return userIdPrefs;
    }

    /**
     * Sets user id prefs.
     *
     * @param userIdPrefs the user id prefs
     */
    public static void setUserIdPrefs(SharedPreferences userIdPrefs) {
        KVTVariables.userIdPrefs = userIdPrefs;
    }

    /**
     * Gets birthday.
     *
     * @return the birthday
     */
    public static String getBirthday() {
        return birthday;
    }

    /**
     * Sets birthday.
     *
     * @param birthday the birthday
     */
    public static void setBirthday(String birthday) {
        KVTVariables.birthday = birthday;
    }
}
