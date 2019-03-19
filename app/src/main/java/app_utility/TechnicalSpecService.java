package app_utility;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.autochip.trufrost.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import static android.content.ContentValues.TAG;
import static androidx.core.app.NotificationCompat.PRIORITY_MAX;
import static app_utility.DataReceiverService.refOfService;

public class TechnicalSpecService extends Service implements OnServiceInterfaceListener {

    NotificationManager notifyMgr;
    String channelId = "app_utility.DataReceiverService";
    String channelName = "data_sync";

    String TASK_STATUS = "NOT_RUNNING";

    public static TechnicalSpecService refOfService;

    public static OnServiceInterfaceListener onServiceInterfaceListener;

    Timer timer = new Timer();
    Handler handler = new Handler();

    private boolean isAlreadyExecuted = false;
    ArrayList<Integer> alOdooID;

    DatabaseHandler dbh;

    public TechnicalSpecService() {
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
        dbh = new DatabaseHandler(getApplicationContext());


        /*TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        alOdooID = new ArrayList<>(dbh.getProductsOdooID());
                        if(alOdooID.size()>=1) {
                            downloadTechSpecs();
                            timer.cancel();
                            timer.purge();
                        }
                    }
                });
            }

        };
        //Starts after 20 sec and will repeat on every 20 sec of time interval.
        timer.schedule(doAsynchronousTask, 0, 5000);*/
        downloadTechSpecs();
    }

    private void downloadTechSpecs() {
        if (!isAlreadyExecuted) {
            alOdooID = new ArrayList<>(dbh.getProductsOdooID());
            isAlreadyExecuted = true;
        }

        Integer[] nOdooIDArray;
        if (alOdooID.size() > 30) {
            nOdooIDArray = new Integer[30];
            for (int i = 0; i < 30; i++) {
                nOdooIDArray[i] = alOdooID.get(i);
                //alOdooID.remove(alOdooID.get(i));
            }
        } else {
            nOdooIDArray = new Integer[alOdooID.size()];
            alOdooID.toArray(nOdooIDArray);
            //timer.cancel();
            //timer.purge();
            //refOfService.stopSelf();
        }
        TrufrostAsyncTask trufrostAsyncTask = new TrufrostAsyncTask(getApplicationContext(), dbh, nOdooIDArray);
        trufrostAsyncTask.execute(String.valueOf(2), "");
        TASK_STATUS = "RUNNING";
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
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer.purge();

        refOfService.stopSelf();

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
                for (int i = 0; i < alIDFetched.size(); i++) {
                    alOdooID.removeAll(alIDFetched);
                }
                if (alOdooID.size() == 0)
                    TechnicalSpecService.refOfService.stopSelf();
                else
                    downloadTechSpecs();
                break;
        }
    }
}
