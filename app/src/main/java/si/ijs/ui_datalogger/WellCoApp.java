package si.ijs.ui_datalogger;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import si.ijs.ui_datalogger.helpers.SharedPreferencesManager;

import static si.ijs.ui_datalogger.helpers.Util.CHANNEL_ID;
import static si.ijs.ui_datalogger.helpers.Util.CHANNEL_NAME;

public class WellCoApp extends Application {

    private SharedPreferencesManager mSharedPreferencesManager;

    @Override
    public void onCreate() {
        super.onCreate();

        //create notification channel for foreground service notification
        createNotificationChannel();

        setupSharedPreferences();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void setupSharedPreferences() {
        Context ac = getApplicationContext();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ac);
        mSharedPreferencesManager = new SharedPreferencesManager(sp);
    }

    public SharedPreferencesManager getSharedData() {
        return mSharedPreferencesManager;
    }
}
