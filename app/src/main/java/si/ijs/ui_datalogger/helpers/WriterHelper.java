package si.ijs.ui_datalogger.helpers;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import si.ijs.ui_datalogger.WellCoApp;


public class WriterHelper {

    //TAG
    private static final String TAG = WriterHelper.class.getName();

    // vars
    public static boolean isOpen = false;
    private static volatile WriterHelper mWriterHelper = new WriterHelper();
    SharedPreferencesManager mSharedPreferencesManager;

    //writers
    private BufferedWriter accelerometerWriter;

    //private constructor
    private WriterHelper() {
    }

    public static WriterHelper getInstance() {
        if (mWriterHelper == null)
            mWriterHelper = new WriterHelper();

        return mWriterHelper;
    }

    /**
     * Method that return the folder which contains raw sensord data files
     *
     * @return
     */
    public static File getFileDestination() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/sensors/");

        if (folder.isDirectory())
            Log.i(TAG, "getFileDestination: file is directory");
        else
            Log.i(TAG, "getFileDestination: file is not directory");


        //return folder!
        return folder;
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* initialize writers for all three sensors  */
    public void initWriters(String name) {

        try {
            //check if external storage is writable
            if (isExternalStorageWritable()) {
                Log.i(TAG, "Writing to external file system");

                accelerometerWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getFullFileName(name + " - Accelerometer"))));


                isOpen = true;
            } else {
                Log.e(TAG, "Cannot write in external storage!");
            }
        } catch (IOException e) {
            Log.e(TAG, "initWriters: ", e);
        }
    }


    /* Close all three writers */
    public void closeWriters() {
        isOpen = false;

        try {
            if (accelerometerWriter != null)
                accelerometerWriter.close();

            accelerometerWriter = null;

        } catch (IOException e) {
            Log.e(TAG, "closeWriters: ", e);
        }
    }

    public void writeDataToFile(long timestamp, float x, float y, float z) {

        //write data to file
        try {
            if (accelerometerWriter != null) {
                accelerometerWriter.write(String.valueOf(timestamp) + ",");
                accelerometerWriter.write(String.valueOf(x) + ",");
                accelerometerWriter.write(String.valueOf(y) + ",");
                accelerometerWriter.write(String.valueOf(z) + ",");
                accelerometerWriter.newLine();

                accelerometerWriter.flush();
            }
        } catch (IOException e) {
            Log.e(TAG, "onSensorChanged: ", e);
        }


    }

    /**
     * Method that return file object from sd card/sensors with given file name
     *
     * @param fileName name of the file
     * @return File (object)
     */
    public File getFullFileName(String fileName) {
        File folder = new File(Environment.getExternalStorageDirectory() + "/sensors/");
        folder.mkdirs();
        File outputFile = new File(folder.getPath() + "/" + fileName + "-" + System.currentTimeMillis() + ".txt");
        Log.d(TAG, "New file created: " + outputFile.getAbsolutePath());
        return outputFile;
    }
}
