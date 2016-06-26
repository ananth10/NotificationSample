package com.ananth.notificationsample;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView mCreateSimpleNotification;
    private TextView mCreateStyleNotification;
    private TextView mUpdateNotification;
    private int numMessages = 0;
    private NotificationCompat.Builder mNotifyBuilder;
    private TextView mBigViewNotification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);

        mCreateSimpleNotification = (TextView) findViewById(R.id.create_simple_notification);
        mCreateStyleNotification = (TextView) findViewById(R.id.create_style_notification);
        mUpdateNotification = (TextView) findViewById(R.id.update_notification);
        mBigViewNotification = (TextView) findViewById(R.id.big_view_notification);

        mCreateSimpleNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleNotifcation();
            }
        });
        mCreateStyleNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new generatePictureStyleNotification(MainActivity.this, "MyNofi", "Hello", "http://cdn01.androidauthority.net/wp-content/uploads/2015/09/android-6.0-marshmallow.jpg").execute();
            }
        });

        mUpdateNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNotifcation();
            }
        });

        mBigViewNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bigViewNotifcation();
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return true;
    }


    public class generatePictureStyleNotification extends AsyncTask<String, Void, Bitmap> {

        private Context mContext;
        private String title, message, imageUrl;

        public generatePictureStyleNotification(Context context, String title, String message, String imageUrl) {
            super();
            this.mContext = context;
            this.title = title;
            this.message = message;
            this.imageUrl = imageUrl;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            try {
                URL url = new URL(this.imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            Intent intent = new Intent(mContext, MainActivity.class);
            intent.putExtra("key", "value");
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 100, intent, PendingIntent.FLAG_ONE_SHOT);

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notif = new Notification.Builder(mContext)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(result)
                    .setStyle(new Notification.BigPictureStyle().bigPicture(result))
                    .build();
            notif.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(1, notif);
        }
    }

    private void simpleNotifcation() {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.putExtra("key", "value");
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 100, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notif = new Notification.Builder(MainActivity.this)
                .setContentIntent(pendingIntent)
                .setContentTitle("Simple Notification")
                .setContentText("Happy Coding!!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(1, notif);
    }

    private void updateNotifcation() {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.putExtra("key", "value");
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 100, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
        int notifyID = 1;
        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentIntent(pendingIntent)
                .setContentTitle("New Message")
                .setContentText("You've received new messages.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("text").setNumber(++numMessages);
        notificationManager.notify(
                notifyID,
                mNotifyBuilder.build());
    }


    private void bigViewNotifcation() {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.putExtra("key", "value");
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 100, intent, PendingIntent.FLAG_ONE_SHOT);


        Intent dismissIntent = new Intent(this, MainActivity.class);
        PendingIntent piDismiss = PendingIntent.getService(this, 0, dismissIntent, 0);

        Intent snoozeIntent = new Intent(this, MainActivity.class);
        PendingIntent piSnooze = PendingIntent.getService(this, 0, snoozeIntent, 0);
        NotificationManager notificationManager = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentIntent(pendingIntent)
                .setContentTitle("New Message")
                .setContentText("You've received new messages.")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(getString(R.string.notification_msg)))
                .setSmallIcon(R.mipmap.ic_launcher)
                .addAction(R.mipmap.dismiss,
                        getString(R.string.dismiss), piDismiss)
                .addAction(R.mipmap.snooze,
                        getString(R.string.ok), piSnooze);
        notificationManager.notify(
                1,
                mNotifyBuilder.build());
    }
}