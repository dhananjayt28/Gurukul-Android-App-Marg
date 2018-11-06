package in.jivanmuktas.www.marg.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import in.jivanmuktas.www.marg.activity.Notification;
import in.jivanmuktas.www.marg.database.DataBase;

/**
 * Created by developer on 30-Oct-17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFcmService";
    //Bitmap bitmap;
    int i=0;
    String tabindex;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.i("!!!From: " , ""+remoteMessage.getFrom());

        Log.i("!!!!Notifi", remoteMessage.getFrom().toString());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            String notification = remoteMessage.getData().get("TYPE");

            /////////////// Get Current Date And Time
            Calendar c = Calendar.getInstance();
            System.out.println("Current time =&gt; "+c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
            String formattedDate = df.format(c.getTime());
            // Now formattedDate have current date/time
            String date = formattedDate;
            DataBase dataBase = new DataBase(this);
            if(notification.equals("GENERAL")){
                tabindex = "0";
                String Msg = remoteMessage.getData().get("MESSAGE");
                dataBase.addToGeneral(Msg,date);///####******Add to database
            }else if(notification.equals("HOD")){
                tabindex = "1";
                String Msg = remoteMessage.getData().get("MESSAGE");
                String mobile = remoteMessage.getData().get("MOBILE");
                String email = remoteMessage.getData().get("EMAIL");
                dataBase.addToHOD(Msg,date,mobile,email);///####******Add to database
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.d("!!!!!!! Notification", remoteMessage.getNotification().getBody().toString());
           // String imageUri = remoteMessage.getData().get("image");
           // bitmap = getBitmapfromUrl(imageUri);
            sendNotification(remoteMessage.getNotification().getBody()/*,bitmap*/, remoteMessage.getNotification().getTitle());

            // Also if you intend on generating your own notifications as a result of a received FCM
            // message, here is where that should be initiated. See sendNotification method below.
        }
    }
    private void sendNotification(String body, /*Bitmap image,*/ String title) {
        Intent notificationIntent = new Intent(this, Notification.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationIntent.putExtra("TYPE", tabindex);//// Intent data for particular tab opening 0 for general and 1 for HOD
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(in.jivanmuktas.www.marg.R.drawable.notification)
                        //.setLargeIcon(image)
                        .setContentTitle(getString(in.jivanmuktas.www.marg.R.string.app_name))
                       /* .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(image))*//*Notification with Image*/
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(body)
                                .setBigContentTitle(title))
                        .setContentText(body)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        //.setSound(defaultSoundUri)
                        .setDefaults(NotificationCompat.DEFAULT_SOUND);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notification.build());
    }

   /* public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }*/

    public MyFirebaseMessagingService() {
        super();
    }

}