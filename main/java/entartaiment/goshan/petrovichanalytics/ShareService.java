package entartaiment.goshan.petrovichanalytics;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;

public class ShareService extends IntentService {
    private  static final String TAG="ShareService";
    private static final int POOL_INTERVAL=1000*60;
   // private NotificationManager nm;

    @Override
    public void onCreate() {
        super.onCreate();
       // nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
    public ShareService() {
        super(TAG);
    }
    public static void setServiceAlarm(Context context,boolean isOn)
    {
        Intent i=new Intent(context,ShareService.class);
        PendingIntent pi=PendingIntent.getService(context,0,i,0);

        AlarmManager alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if(isOn){
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),POOL_INTERVAL,pi);
        }
        else{
            alarmManager.cancel(pi);
            pi.cancel();
        }

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if(!isNetworkAvailableAncConnected())
        {
            return;
        }
        Log.i(TAG,"Service Started");

        ArrayList<Share> list=ShareArray.getShareArray(this).getArray();
        loop:
        for(Share share:list)
        {
            Bundle bundle=ShareFetchr.fetchItems(share.getName());
            if(bundle == null) {break loop;}
            double price = (double) bundle.getSerializable(ShareFetchr.TAG_PRICE);
           // double procent = (double) bundle.getSerializable(ShareFetchr.TAG_PROCENT);
            Bundle bundle1=ShareArray.getShareArray(this).getSettings();
            double percent=Double.parseDouble((String)bundle1.getSerializable("percent"));
            String notification2=(String)bundle1.getSerializable("notification");
            if((1-(share.getPrice()/price)>(percent/100))&&(notification2.equals("true")))
            {
                Context context = getApplicationContext();

                Intent notificationIntent = new Intent(context, MainActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(context,
                        0, notificationIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);

                Resources res = context.getResources();
                Notification.Builder builder = new Notification.Builder(context);

                builder.setContentIntent(contentIntent)
                        .setSmallIcon(R.drawable.kama)
                        // большая картинка
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.hqdefault))
                        //.setTicker(res.getString(R.string.warning)) // текст в строке состояния
                    //    .setTicker("Последнее китайское предупреждение!")
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)
                        //.setContentTitle(res.getString(R.string.notifytitle)) // Заголовок уведомления
                        .setContentTitle("Уведомление")
                        //.setContentText(res.getString(R.string.notifytext))
                        .setContentText("Акции "+ share.getName()+" выросли на "+ String.format("%.2f",(1-(share.getPrice()/price))*100) + " процентов" ); // Текст уведомления

                // Notification notification = builder.getNotification(); // до API 16
                Notification notification = builder.build();
                Log.i(TAG,"NOTIFY SENT");
                NotificationManager notificationManager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(228, notification);
            }
        }

    }
    private boolean isNetworkAvailableAncConnected()
    {
        ConnectivityManager manager=(ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable=manager.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && manager.getActiveNetworkInfo().isConnected();

        return isNetworkConnected;

    }
}
