package app_utility;


import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesClass {

    // Context
    SharedPreferences sharedPreferences;
    private Context _context;

    // Shared_pref file name
    //private static final String PREF_NAME = "TagProPref";

    private static final String APP_PREFERENCES = "TagProPreferences";

    private static final int PRIVATE_MODE = 0;



    // 0 = stall;
    // 1 = admin

    // All Shared Preferences Keys
    // private static final String IS_LOGIN = "IsLoggedIn";


    // User name (make variable public to access from outside)
    private static final String IS_USER_FIRST_TIME = "IS_USER_FIRST_TIME";

    private static final String USER_TYPE = "USER_TYPE";

    private static final String IS_LOGGED_IN = "IS_LOGGED_IN";

    private static final String USER_ODOO_ID = "USER_ODOO_ID";

    private static final String USER_NAME = "USER_NAME";

    //private static final String KEY_TAG_ID = "tagID";
    SharedPreferences.Editor editor;

    // Constructor
    public SharedPreferencesClass(Context context) {
        this._context = context;

        sharedPreferences = _context.getSharedPreferences(APP_PREFERENCES, PRIVATE_MODE);
        editor = sharedPreferences.edit();
        //editor.apply();
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */


    public void setUserLogStatus(boolean bValue){
        /*SharedPreferences sharedPreferences = _context.getSharedPreferences(APP_PREFERENCES, PRIVATE_MODE);
        SharedPreferences.Editor editor;
        editor = sharedPreferences.edit();*/
        editor.putBoolean(IS_LOGGED_IN, bValue);
        editor.apply();
    }

    public boolean getUserLogStatus(){
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    /*public String getUserName(){
        return sharedPreferences.getString(USER_NAME, null);
    }


    public void setUserType(int userType, boolean bValue){
        editor.putInt(USER_TYPE, userType);
        editor.putBoolean(IS_LOGGED_IN,bValue );
        editor.apply();
    }
    public int getUserType(){
        return sharedPreferences.getInt(USER_TYPE, DEFAULT_USER_TYPE);
    }*/
}