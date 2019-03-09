package app_utility;

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

import com.autochip.trufrost.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import static android.content.ContentValues.TAG;
import static androidx.core.app.NotificationCompat.PRIORITY_MAX;

public class DataReceiverService extends Service {

    String channelId = "app_utility.DataReceiverService";
    String channelName = "data_sync";

    public static DataReceiverService refOfService;
    /*startService(new Intent(MyService.ServiceIntent));
    stopService(new Intent((MyService.ServiceIntent));*/

    //public static OnAsyncInterfaceListener onAsyncInterfaceListener;
    //SharedPreferencesClass sharedPreferencesClass;

    NotificationManager notifyMgr;
    NotificationCompat.Builder nBuilder;
    NotificationCompat.InboxStyle inboxStyle;

    //AsyncInterface asyncInterface;

    Timer timer = new Timer();
    Handler handler = new Handler();
    public String VOLLEY_STATUS = "NOT_RUNNING";
    public boolean isMultipleImages = false;

    long startTime = 0;
    long endTime = 0;
    long totalTime = 0;

    int index = 0;

    String TASK_STATUS = "NOT_RUNNING";

    //BELAsyncTask belAsyncTask;
    DatabaseHandler dbh;
    ArrayList<DataBaseHelper> alDBTemporaryData;
    public DataStorage dataStorage;
    ArrayList<String> alImageAddress;

    ArrayList<Integer> alAlreadyRequested = new ArrayList<>();

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
                        /*if(dataStorage!=null && dataStorage.alDBIDWithAddress.size()>=1 && TASK_STATUS.equals("NOT_RUNNING")){
                            final int id = Integer.valueOf(dataStorage.alDBIDWithAddress.get(0).split("##")[0]);
                            //String sURL = alImageAddress;
                            alImageAddress = new ArrayList<>(Collections.singletonList(dataStorage.alDBIDWithAddress.get(0).split("##")[1]));
                            if(alImageAddress.size()>1){
                                isMultipleImages = true;
                            } else {
                                getBitmapFromURL(alImageAddress.get(0), id);
                                isMultipleImages = false;
                            }
                            //getBitmapFromURL(sURL, id);
                        }*/
                        if (dataStorage != null && dataStorage.alDBIDWithAddress.size() >= 1 && TASK_STATUS.equals("NOT_RUNNING")) {
                            final int id = Integer.valueOf(dataStorage.alDBIDWithAddress.get(0).split("##")[0]);
                            String sURL = dataStorage.alDBIDWithAddress.get(0).split("##")[1];
                            if (sURL.equals("null"))
                                dataStorage.alDBIDWithAddress.remove(0);
                            else {
                                TASK_STATUS = "RUNNING";
                                getBitmapFromURL(sURL, id);
                            }
                        } else if (dataStorage.isDataUpdatedAtleastOnce && TASK_STATUS.equals("NOT_RUNNING") &&
                                dataStorage.alDBIDWithAddress.size() == 0) {
                            timer.cancel();
                            timer.purge();
                            refOfService.stopSelf();
                            TrufrostAsyncTask trufrostAsyncTask = new TrufrostAsyncTask(getApplicationContext(), dbh);
                            trufrostAsyncTask.execute(String.valueOf(2), "");
                        }/*else if(dataStorage.alDBIDWithAddress.size() == 0 && TASK_STATUS.equals("NOT_RUNNING")){
                            timer.cancel();
                            timer.purge();
                        }*/
                        /*if (dataStorage != null && dataStorage.alDBIDWithAddress.size() >= 1) {
                            final int id = Integer.valueOf(dataStorage.alDBIDWithAddress.get(0).split(",,")[0]);
                            String sURL = dataStorage.alDBIDWithAddress.get(0).split(",,")[1];

                            Glide.with(getApplicationContext())
                                    .asBitmap()
                                    .load(sURL)
                                    .into(new Target<Bitmap>() {
                                        @Override
                                        public void onStart() {

                                        }

                                        @Override
                                        public void onStop() {

                                        }

                                        @Override
                                        public void onDestroy() {

                                        }

                                        @Override
                                        public void onLoadStarted(@Nullable Drawable placeholder) {

                                        }

                                        @Override
                                        public void onLoadFailed(@Nullable Drawable errorDrawable) {

                                        }

                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            String sIDWithPath = String.valueOf(id) + ",," + saveImage(resource);
                                            dataStorage.alDBIDWithPath.add(sIDWithPath);
                                            dataStorage.alDBIDWithAddress.remove(0);
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {

                                        }

                                        @Override
                                        public void getSize(@NonNull SizeReadyCallback cb) {
                                            cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                                        }

                                        @Override
                                        public void removeCallback(@NonNull SizeReadyCallback cb) {

                                        }

                                        @Override
                                        public void setRequest(@Nullable Request request) {

                                        }

                                        @Nullable
                                        @Override
                                        public Request getRequest() {
                                            return null;
                                        }

                                    });
                        } else {
                            timer.cancel();
                            timer.purge();
                        }*/
                        /*if (dataStorage != null && dataStorage.alDBIDWithAddress.size() >= 1) {
                            timer.cancel();
                            timer.purge();
                            for (int i = 0; i < dataStorage.alDBIDWithAddress.size(); i++) {
                                final int id = Integer.valueOf(dataStorage.alDBIDWithAddress.get(i).split(",,")[0]);
                                String sURL = dataStorage.alDBIDWithAddress.get(i).split(",,")[1];
                                Glide.with(getApplicationContext())
                                        .asBitmap()
                                        .load(sURL)
                                        .into(new Target<Bitmap>() {
                                            @Override
                                            public void onStart() {

                                            }

                                            @Override
                                            public void onStop() {

                                            }

                                            @Override
                                            public void onDestroy() {

                                            }

                                            @Override
                                            public void onLoadStarted(@Nullable Drawable placeholder) {

                                            }

                                            @Override
                                            public void onLoadFailed(@Nullable Drawable errorDrawable) {

                                            }

                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                String sIDWithPath = String.valueOf(id) + ",," + saveImage(resource);
                                                dataStorage.alDBIDWithPath.add(sIDWithPath);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }

                                            @Override
                                            public void getSize(@NonNull SizeReadyCallback cb) {
                                                cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                                            }

                                            @Override
                                            public void removeCallback(@NonNull SizeReadyCallback cb) {

                                            }

                                            @Override
                                            public void setRequest(@Nullable Request request) {

                                            }

                                            @Nullable
                                            @Override
                                            public Request getRequest() {
                                                return null;
                                            }

                                        });
                            }
                        }*/
                    }
                });
            }

        };
        //Starts after 20 sec and will repeat on every 20 sec of time interval.
        timer.schedule(doAsynchronousTask, 0, 5000);
    }

    /*public String login(String uname, String password)
    {
        InputStream is = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.1.15:8040/web/image/product.template/370/image/");

            nameValuePairs.add(new BasicNameValuePair("emailid", uname));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            org.apache.http.HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            is = entity.getContent();
        }
        catch (Exception e)
        {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }

        // convert response to string
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader( is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null)
            {
                sb.append(line).append("\n");
            }

            is.close();
            result = sb.toString();

            Log.v("log","Result: " + result);
        }
        catch (Exception e)
        {
            Log.v("log", "Error converting result " + e.toString());
        }

        return result;
    }*/
    public void getBitmapFromURL(final String src, final int ID) {
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
                    connection.getResponseMessage();
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    myBitmap = BitmapFactory.decodeStream(input);
                    //saveImage(myBitmap);
                    String imagePath = saveImage(myBitmap);
                    String sIDWithPath = String.valueOf(ID) + ",," + imagePath;
                    dataStorage.alDBIDWithPath.add(sIDWithPath);
                    dataStorage.alDBIDWithAddress.remove(0);
                    String sOldImagePath = dbh.getImagePathFromProducts(ID);
                    if (sOldImagePath != null && sOldImagePath.length() > 2) {
                        String sTmp = sOldImagePath + "," + imagePath;
                        dbh.updateImagePathIndividualProducts(new DataBaseHelper(sTmp), ID);
                    } else
                        dbh.updateImagePathIndividualProducts(new DataBaseHelper(imagePath), ID);
                    TASK_STATUS = "NOT_RUNNING";

                } catch (Exception e) { // catch (IOException e) {
                    e.printStackTrace();
                    if (src.equals("null"))
                        dataStorage.alDBIDWithAddress.remove(0);
                    TASK_STATUS = "NOT_RUNNING";
                    //return null;
                }
                //TODO your background code
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
        String imageFileName = "IMG" + s + ".jpg";
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


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startForeground() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), createNotificationChannel());
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(PRIORITY_MAX)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(101, notification);
    }


    @RequiresApi(Build.VERSION_CODES.O)
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
            Intent intent = new Intent("app_utility.TrackingService.ServiceStopped");
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

    public class DataStorage {
        public boolean isDataUpdatedAtleastOnce = false;
        public HashMap<Integer, String> hmImageAddressWithDBID;
        public ArrayList<String> alDBIDWithAddress = new ArrayList<>();
        public ArrayList<Integer> alDBID = new ArrayList<>();
        public ArrayList<String> alDBIDWithPath = new ArrayList<>();
    }
    /*@Override
    public void onAsyncComplete(String sMSG, int type, String sResult) {
        switch (sMSG) {
            case "ODOO_ID_RETRIEVED":
                if (type != StaticReferenceClass.DEFAULT_ODOO_ID) {
                    sharedPreferencesClass.setUserOdooID(type);
                }
                break;
        }
        VOLLEY_STATUS = "NOT_RUNNING";
    }*/

}
