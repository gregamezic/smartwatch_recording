package si.ijs.ui_datalogger.helpers;

import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferencesManager {

    private static final String TAG = SharedPreferencesManager.class.getName();

    //KEYS
    private static final String IS_REST_KEY = "si.ijs.ui.IS_REST_KEY";
    private static final String IS_WALK_KEY = "si.ijs.ui.IS_WALKING_KEY";
    private static final String IS_RUN_KEY = "si.ijs.ui.IS_RUN_KEY";

    //DEFAULT VALUES
    public static final boolean DEFAULT_BOOLEAN_VALUE = false;
    public static final long DEFAULT_LONG_VALUE = -1;
    public static final int DEFAULT_INT_VALUE = -1;
    public static final String DEFAULT_STRING_VALUE = "";
    public static final int DEFAULT_HEART_RATE_VALUE = 0;
    public static final int ZERO_INT_VALUE = 0;


    //instance of shared preferences
    private SharedPreferences mSharedPreferences;

    /**
     * Default constructor
     * @param sharedPreferences of WellCoApp app obtained from applicationContext.
     */
    public SharedPreferencesManager(SharedPreferences sharedPreferences) {
        this.mSharedPreferences = sharedPreferences;
    }


    /**
     * Methods for get and set data for shared preferences
     */


    // RESTING

    //Set is rest on
    public void setIsRestOn(boolean isResting) {
        mSharedPreferences.edit().putBoolean(IS_REST_KEY, isResting).apply();
    }

    //get resting state
    public boolean getIsRestOn() {
        return mSharedPreferences.getBoolean(IS_REST_KEY, DEFAULT_BOOLEAN_VALUE);
    }


    // WALKING

    //Set is walk on
    public void setIsWalkOn(boolean isWalking) {
        mSharedPreferences.edit().putBoolean(IS_WALK_KEY, isWalking).apply();
    }

    //get walking state
    public boolean getIsWalkOn() {
        return mSharedPreferences.getBoolean(IS_WALK_KEY, DEFAULT_BOOLEAN_VALUE);
    }


    // RUNNING

    //Set is running on
    public void setIsRunOn(boolean isRunning) {
        mSharedPreferences.edit().putBoolean(IS_RUN_KEY, isRunning).apply();
    }

    //get running state
    public boolean getIsRunningOn() {
        return mSharedPreferences.getBoolean(IS_RUN_KEY, DEFAULT_BOOLEAN_VALUE);
    }


    /**
     * Return current recording activity
     * @return DATA_TYPE type of the current recording activity
     */
    public DATA_TYPE getCurrentDataType() {
        if (getIsRestOn())
            return DATA_TYPE.OTHER;
        else if (getIsWalkOn())
            return DATA_TYPE.WALKING;
        else if (getIsRunningOn())
            return DATA_TYPE.RUNNING;
        return DATA_TYPE.NOTHING;
    }


}
