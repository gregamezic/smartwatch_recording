package si.ijs.ui_datalogger;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.function.ToDoubleBiFunction;

import si.ijs.ui_datalogger.helpers.PermissionsHelper;
import si.ijs.ui_datalogger.helpers.SharedPreferencesManager;
import si.ijs.ui_datalogger.services.LoggerService;

import static si.ijs.ui_datalogger.helpers.Util.PERMISSION_REQUEST_ID;

public class MainActivity extends WearableActivity {

    private final String TAG = MainActivity.class.getName();

    //views
    private ToggleButton btnRest;
    private ToggleButton btnWalk;
    private ToggleButton btnRun;

    //vars
    SharedPreferencesManager mSharedPreferencesManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get shared preferences
        mSharedPreferencesManager = ((WellCoApp)getApplication()).getSharedData();

        // check for permission
        checkPermissions();

        // Enables Always-on
        setAmbientEnabled();
    }

    private void checkPermissions() {
        //check if we already have permissions
        if (PermissionsHelper.hasPermissions(MainActivity.this, Manifest.permission.BODY_SENSORS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            //when we have permission let's check if the user is logged in
            setupActivity();


        } else { //no permissions
            //request permissions
            requestPermissions(new String[]{Manifest.permission.BODY_SENSORS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_ID);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_ID: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the job
                    setupActivity();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permissions denied!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void setupActivity() {

        // init views
        btnRest = findViewById(R.id.btn_rest);
        btnWalk = findViewById(R.id.btn_walk);
        btnRun = findViewById(R.id.btn_run);


        //check shared data
        boolean isRestOn = mSharedPreferencesManager.getIsRestOn();
        boolean isWalkOn = mSharedPreferencesManager.getIsWalkOn();
        boolean isRunOn = mSharedPreferencesManager.getIsRunningOn();

        // set button state
        btnRest.setChecked(isRestOn);
        btnWalk.setChecked(isWalkOn);
        btnRun.setChecked(isRunOn);


        /**
         * ON BUTTON CLICK
         */

        // REST
        btnRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!btnRest.isChecked()) { // stop service

                    Intent intent = new Intent(MainActivity.this, LoggerService.class);
                    stopService(intent);

                    Log.d(TAG, "onClick: service stopped!");
                    Toast.makeText(MainActivity.this, "Service stopped!", Toast.LENGTH_SHORT).show();

                    mSharedPreferencesManager.setIsRestOn(false);
                    mSharedPreferencesManager.setIsWalkOn(false);
                    mSharedPreferencesManager.setIsRunOn(false);

                } else {
                    // start service
                    Intent intent = new Intent(MainActivity.this, LoggerService.class);
                    startService(intent);

                    mSharedPreferencesManager.setIsRestOn(true);

                    Log.d(TAG, "onClick: rest started!");
                    Toast.makeText(MainActivity.this, "Rest started!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // WALK
        btnWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!btnWalk.isChecked()) { // stop service

                    Intent intent = new Intent(MainActivity.this, LoggerService.class);
                    stopService(intent);

                    Log.d(TAG, "onClick: service stopped!");
                    Toast.makeText(MainActivity.this, "Service stopped!", Toast.LENGTH_SHORT).show();

                    mSharedPreferencesManager.setIsRestOn(false);
                    mSharedPreferencesManager.setIsWalkOn(false);
                    mSharedPreferencesManager.setIsRunOn(false);

                } else {
                    // start service
                    Intent intent = new Intent(MainActivity.this, LoggerService.class);
                    startService(intent);

                    mSharedPreferencesManager.setIsWalkOn(true);

                    Log.d(TAG, "onClick: rest started!");
                    Toast.makeText(MainActivity.this, "Walk started!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // RUN
        btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!btnRun.isChecked()) { // stop service

                    Intent intent = new Intent(MainActivity.this, LoggerService.class);
                    stopService(intent);

                    Log.d(TAG, "onClick: service stopped!");
                    Toast.makeText(MainActivity.this, "Service stopped!", Toast.LENGTH_SHORT).show();

                    mSharedPreferencesManager.setIsRestOn(false);
                    mSharedPreferencesManager.setIsWalkOn(false);
                    mSharedPreferencesManager.setIsRunOn(false);

                } else {
                    // start service
                    Intent intent = new Intent(MainActivity.this, LoggerService.class);
                    startService(intent);

                    mSharedPreferencesManager.setIsRunOn(true);

                    Log.d(TAG, "onClick: rest started!");
                    Toast.makeText(MainActivity.this, "Run started!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
