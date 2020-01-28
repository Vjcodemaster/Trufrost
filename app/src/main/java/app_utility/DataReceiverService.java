package app_utility;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.autochip.trufrost.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;
import static androidx.core.app.NotificationCompat.PRIORITY_MAX;

public class DataReceiverService extends Service implements OnServiceInterfaceListener {

    int count = 0;
    String channelId = "app_utility.DataReceiverService";
    String channelName = "data_sync";

    public static boolean isTechnicalSpecServiceCompleted = true;
    public static DataReceiverService refOfService;

    public static OnServiceInterfaceListener onServiceInterfaceListener;
    /*startService(new Intent(MyService.ServiceIntent));
    stopService(new Intent((MyService.ServiceIntent));*/

    //public static OnAsyncInterfaceListener onAsyncInterfaceListener;
    //SharedPreferencesClass sharedPreferencesClass;

    NotificationManager notifyMgr;
    //NotificationCompat.Builder nBuilder;
    //NotificationCompat.InboxStyle inboxStyle;

    //AsyncInterface asyncInterface;

    Timer timer = new Timer();
    Handler handler = new Handler();
    //public String VOLLEY_STATUS = "NOT_RUNNING";
    //public boolean isMultipleImages = false;


    String TASK_STATUS = "NOT_RUNNING";

    //BELAsyncTask belAsyncTask;
    DatabaseHandler dbh;
    //ArrayList<DataBaseHelper> alDBTemporaryData;
    public DataStorage dataStorage;
    ArrayList<String> alImageAddress;

    //ArrayList<Integer> alAlreadyRequested = new ArrayList<>();

    //private boolean isAlreadyExecuted = false;
    //ArrayList<Integer> alOdooID;
    boolean isTimerStopped = false;

    public DataReceiverService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        /*
        this will make sure service will run on background in oreo and above
        service wont run without a notification from oreo version.
        After the system has created the service, the app has five seconds to call the service's startForeground() method
        to show the new service's user-visible notification. If the app does not call startForeground() within the time limit,
        the system stops the service and declares the app to be ANR.
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground();
        }

        refOfService = this;
        onServiceInterfaceListener = this;
        dataStorage = new DataStorage();
        //onAsyncInterfaceListener = this;

        alImageAddress = new ArrayList<>();
        dbh = new DatabaseHandler(getApplicationContext());

        //sharedPreferencesClass = new SharedPreferencesClass(getApplicationContext());
        /*TimerTask doMultipleAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {

                    }
                });
            }

        };

        //Starts after 20 sec and will repeat on every 20 sec of time interval.
        timer.schedule(doMultipleAsynchronousTask, 0, 5000);*/
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        if (isTechnicalSpecServiceCompleted)
                            downloadImageTask();
                    }
                });
            }

        };
        //Starts after 20 sec and will repeat on every 5 sec of time interval.
        timer.schedule(doAsynchronousTask, 0, 5000);
    }

    private void downloadImageTask() {
        if (dataStorage != null && dataStorage.alDBIDWithAddress.size() >= 1 && TASK_STATUS.equals("NOT_RUNNING")) {
            String[] saData = dataStorage.alDBIDWithAddress.get(0).split("##");
            final int id = Integer.valueOf(saData[0]);
            String sURL = saData[1];
            int nSwitchCase;
            if (saData.length == 4)
                nSwitchCase = Integer.valueOf(saData[3]);
            else if (saData.length == 3) {
                nSwitchCase = Integer.valueOf(saData[2]);
            } else
                nSwitchCase = 0;
            if (sURL.equals("null") || sURL.equals("")) {
                dataStorage.alDBIDWithAddress.remove(0);
                if (isTimerStopped)
                    downloadImageTask();
            } else {
                TASK_STATUS = "RUNNING";
                getBitmapFromURL(sURL, id, nSwitchCase);
                count++;
                if (dataStorage.alDBIDWithAddress.size() >= 20) {
                    timer.cancel();
                    timer.purge();
                    isTimerStopped = true;
                }
            }
        } else if (dataStorage != null && dataStorage.isDataUpdatedAtleastOnce && TASK_STATUS.equals("NOT_RUNNING")) {
            /*Intent techSpecsIn = new Intent(getApplicationContext(), TechnicalSpecService.class);
            startService(techSpecsIn);*/
            timer.cancel();
            timer.purge();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                refOfService.stopForeground(true);
            }
            refOfService.stopSelf();
        }
    }

    public void getBitmapFromURL(final String src, final int ID, final int nSwitchCase) {
        AsyncTask.execute(new Runnable() {
            Bitmap myBitmap;

            @Override
            public void run() {
                try {
                    //TASK_STATUS = "RUNNING";
                    //login(USER_ID, PASSWORD);
                    java.net.URL url = new java.net.URL(src);
                    HttpURLConnection connection = (HttpURLConnection) url
                            .openConnection();
                    connection.setDoInput(true);
                    connection.getPermission();
                    connection.setConnectTimeout(20000);
                    connection.setReadTimeout(15000);
                    connection.getResponseMessage();
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    myBitmap = BitmapFactory.decodeStream(input);
                    //saveImage(myBitmap);
                    String imagePath = saveImage(myBitmap);
                    String sIDWithPath = ID + ",," + imagePath;
                    dataStorage.alDBIDWithPath.add(sIDWithPath);
                    dataStorage.alDBIDWithAddress.remove(0);

                    String sOldImagePath;
                    switch (nSwitchCase) {
                        case 1:
                            sOldImagePath = dbh.getImagePathFromMainDB(ID);
                            if (sOldImagePath != null && sOldImagePath.length() > 2) {
                                String sTmp = sOldImagePath + "," + imagePath;
                                dbh.updateFirstSubCategoryImagePathMainDB(new DataBaseHelper(sTmp, 1, false), ID);
                            } else {
                                dbh.updateFirstSubCategoryImagePathMainDB(new DataBaseHelper(imagePath, 1, false), ID);
                            }
                            break;
                        case 2:
                            sOldImagePath = dbh.getImagePathFromSubCategory(ID);
                            if (sOldImagePath != null && sOldImagePath.length() > 2) {
                                String sTmp = sOldImagePath + "," + imagePath;
                                dbh.updateSubCategoryImagePathMainDB(new DataBaseHelper(sTmp, 2, false), ID);
                            } else {
                                dbh.updateSubCategoryImagePathMainDB(new DataBaseHelper(imagePath, 2, false), ID);
                            }
                            break;
                        case 4:
                            dbh.updateFirstSubCategoryImagePathSubCategoryDB(new DataBaseHelper(imagePath, 1, false), ID);
                            break;
                        default:
                            sOldImagePath = dbh.getImagePathFromProducts(ID);
                            if (sOldImagePath != null && sOldImagePath.length() > 2) {
                                String sTmp = sOldImagePath + "," + imagePath;
                                dbh.updateImagePathIndividualProducts(new DataBaseHelper(sTmp), ID);
                            } else {
                                dbh.updateImagePathIndividualProducts(new DataBaseHelper(imagePath), ID);
                            }
                            break;
                    }
                    //added on 28/03/2019
                    /*if (nSwitchCase == 1) {
                        String sOldImagePath = dbh.getImagePathFromMainDB(ID);
                        if (sOldImagePath != null && sOldImagePath.length() > 2) {
                            String sTmp = sOldImagePath + "," + imagePath;
                            dbh.updateSubCategoryImagePathMainDB(new DataBaseHelper(sTmp, 1, false), ID);
                        } else {
                            dbh.updateSubCategoryImagePathMainDB(new DataBaseHelper(imagePath, 1, false), ID);
                        }
                    } else {
                        String sOldImagePath = dbh.getImagePathFromProducts(ID);
                        if (sOldImagePath != null && sOldImagePath.length() > 2) {
                            String sTmp = sOldImagePath + "," + imagePath;
                            dbh.updateImagePathIndividualProducts(new DataBaseHelper(sTmp), ID);
                        } else {
                            dbh.updateImagePathIndividualProducts(new DataBaseHelper(imagePath), ID);
                        }
                    }*/
                    TASK_STATUS = "NOT_RUNNING";
                    if (isTimerStopped)
                        downloadImageTask();
                } catch (Exception e) { // catch (IOException e) {
                    e.printStackTrace();
                    if (src.equals("null"))
                        dataStorage.alDBIDWithAddress.remove(0);
                    TASK_STATUS = "NOT_RUNNING";
                    if (isTimerStopped)
                        downloadImageTask();
                } /*finally {

                }*/
            }
        });

        //return myBitmap;
    }

    /*public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }*/

    private String saveImage(Bitmap image) {
        String savedImagePath = null;

        Date d = new Date();
        CharSequence s = DateFormat.format("MM-dd-yy hh-mm-ss", d.getTime());
        String imageFileName = "IMG" + s + count + ".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/Kiosk");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            //galleryAddPic(savedImagePath);
            //Toast.makeText(mContext, "IMAGE SAVED", Toast.LENGTH_LONG).show();
        }
        return savedImagePath;
    }


    private void startForeground() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), createNotificationChannel());
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(PRIORITY_MAX)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(101, notification);
    }


    @TargetApi(Build.VERSION_CODES.O)
    private String createNotificationChannel() {
        NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifyMgr.createNotificationChannel(chan);
        return channelId;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent in) {
        Log.e("Service is killed", "");
        super.onTaskRemoved(in);
        /*if (sharedPreferenceClass.getUserTraceableInfo()) {
            Intent intent = new Intent("app_utility.TrackingService.
            ServiceStopped");
            sendBroadcast(intent);
        }*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //TrackingService.this.stopSelf();
        timer.cancel();
        timer.purge();

        //refOfService.stopForeground(true);
        refOfService.stopSelf();
        /*if (sharedPreferenceClass.getUserTraceableInfo()) {
            Intent intent = new Intent("app_utility.TrackingService.ServiceStopped");
            sendBroadcast(intent);
        }*/

        Log.i(TAG, "Service destroyed ...");
    }

    @Override
    public void onServiceMessage(String sMSG, ArrayList<Integer> alIDFetched) {
        switch (sMSG) {
            case "TASK_OVER":
                refOfService.stopSelf();
                break;
            case "TASK_COMPLETE":
                TASK_STATUS = "NOT_RUNNING";
                break;
        }
    }

    public class DataStorage {
        boolean isDataUpdatedAtleastOnce = false;
        //public HashMap<Integer, String> hmImageAddressWithDBID;
        ArrayList<String> alDBIDWithAddress = new ArrayList<>();
        //public ArrayList<Integer> alDBID = new ArrayList<>();
        ArrayList<String> alDBIDWithPath = new ArrayList<>();
    }

}
