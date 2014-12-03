package com.example.globaleducation;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class AlarmService extends Service {

	private static final int NOTIFICATION_ID = 1;
	private NotificationManager notificationManager;
	private PendingIntent pendingIntent;
	   
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


	   @SuppressWarnings({ "static-access", "deprecation" })
	   @Override
	   public void onStart(Intent intent, int startId)
	   {
	       super.onStart(intent, startId);
	       Context context = this.getApplicationContext();
	       notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
	       Intent mIntent = new Intent(this, LoginActivity.class);
	       pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_CANCEL_CURRENT);
	       NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
	       builder.setContentTitle("Questions Ready");
	       builder.setContentText("Your daily questions are ready!");
	       builder.setSmallIcon(R.drawable.ic_launcher);
	       builder.setContentIntent(pendingIntent);

	       notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	       notificationManager.notify(NOTIFICATION_ID, builder.build());
	    }
}
