package si.ijs.ui_datalogger.services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import java.util.Timer;
import si.ijs.ui_datalogger.MainActivity;
import si.ijs.ui_datalogger.R;
import si.ijs.ui_datalogger.WellCoApp;
import si.ijs.ui_datalogger.helpers.DATA_TYPE;
import si.ijs.ui_datalogger.helpers.SharedPreferencesManager;
import si.ijs.ui_datalogger.helpers.WriterHelper;
import static si.ijs.ui_datalogger.helpers.Util.CHANNEL_ID;
import static si.ijs.ui_datalogger.helpers.Util.CHANNEL_NAME;


public class LoggerService extends Service implements SensorEventListener {

    //id for notification
    private static final int NOTIFICATION_ID = 31415;
    // isRecording boolean
    public static boolean isRecording = false;
    //TAG
    private final String TAG = LoggerService.class.getName();
    //sensor types
    private final int SENSOR0 = Sensor.TYPE_ACCELEROMETER;
    private final int SENSOR1 = Sensor.TYPE_GYROSCOPE;
    private final int SENSOR2 = Sensor.TYPE_MAGNETIC_FIELD;
    // shared preferences
    SharedPreferencesManager mSharedPreferencesManager;
    //my writer helper
    WriterHelper writerHelper;
    // timer
    Timer mTimer;
    //sensors
    private Sensor sensorAccelerometer;
    private Sensor sensorGyroscope;
    private Sensor sensorMagnetometer;
    //My sensor manager
    private SensorManager sensorManager;


    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate: ");

        //init sensor manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //create notification for foreground service
        createNotification();

        //get writer helper instance
        writerHelper = WriterHelper.getInstance();
        //writerHelper.initWriters();

        //get shared preferences
        mSharedPreferencesManager = ((WellCoApp) getApplication()).getSharedData();

        //start with recording
        registerSensors();

        String fName = getName();

        writerHelper.initWriters(fName);

        // start timer
        //startCountDownTimer();
    }

    private String getName() {
        DATA_TYPE type = mSharedPreferencesManager.getCurrentDataType();

        switch (type) {
            case OTHER:
                return "rest";
            case WALKING:
                return "walking";
            case RUNNING:
                return "running";
            case NOTHING:
                return "error";
        }
        return "error";
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //startTimer();

        Log.d(TAG, "onStartCommand: ");
        return START_STICKY;
    }

    /**
     * Method that create notification if SDK is 26 or higher (oreo) and start service in foreground mode
     */
    private void createNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //create channel for notification
            createChannel();

            //create notification for foreground service
            Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentTitle("WellCo Service")
                    .setContentText("Logger service is running")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setChannelId(CHANNEL_ID)
                    .build();

            startForeground(NOTIFICATION_ID, notification);
        }
    }

    /**
     * Method that create a channel for notification showed to user, for foreground service
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,  //name of the channel
                NotificationManager.IMPORTANCE_HIGH);   //importance level

        // Configure the notification channel.
        mChannel.setDescription("notification channel for WellCo");
        mChannel.enableLights(true);

        // Sets the notification light color for notifications posted to this channel, if the device supports this feature.
        mChannel.setShowBadge(true);
        nm.createNotificationChannel(mChannel);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //data
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            //timestamp
            long time = System.currentTimeMillis();

            //write data
            if (WriterHelper.isOpen && writerHelper != null)
                writerHelper.writeDataToFile(time, x, y, z);

        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            //data
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            //timestamp
            long time = System.currentTimeMillis();

            //write data
            if (WriterHelper.isOpen && writerHelper != null)
                writerHelper.writeDataToFile(time, x, y, z);

        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            //data
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            //timestamp
            long time = System.currentTimeMillis();

            //write data
            if (WriterHelper.isOpen && writerHelper != null)
                writerHelper.writeDataToFile(time, x, y, z);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /* register accelerometer sensor, gyro sensor and magnetometer sensor */
    private void registerSensors() {
        //check if sensors are already active
        if (isRecording) {
            Log.d(TAG, "registerSensors: is already recording!");
            return;
        }

        if (sensorManager == null)
            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        Log.i(TAG, "Register sensors");

        //register sensors
        sensorAccelerometer = sensorManager.getDefaultSensor(SENSOR0);
        sensorGyroscope = sensorManager.getDefaultSensor(SENSOR1);
        sensorMagnetometer = sensorManager.getDefaultSensor(SENSOR2);

        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);

        isRecording = true;
    }

    /* unregister sensors and close all writers */
    private void unregisterSensors() {
        Log.i(TAG, "Unregister sensors");
        sensorManager.unregisterListener(this);
        writerHelper.closeWriters();

        mSharedPreferencesManager.setIsRestOn(false);
        mSharedPreferencesManager.setIsWalkOn(false);
        mSharedPreferencesManager.setIsRunOn(false);

        isRecording = false;
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "OnDestroy");
        unregisterSensors();
        stopSelf();
    }

    private void startCountDownTimer() {
        new CountDownTimer(2 * 60 * 1000, 5000) {
            @Override
            public void onTick(long millisUntilFinish) {
                Log.d(TAG, "onTick: ");
            }

            @Override
            public void onFinish() {

                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(2000, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    v.vibrate(2000);
                }

                unregisterSensors();
            }
        }.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
