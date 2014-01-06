package be.arno.crud;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

	public class NotificationMaker {

	private Notification notif;

	public NotificationMaker(Context context, String title, String body, String subText, String contentInfo, Class classToLauchOnClick) {
		NotificationCompat.Builder mBuilder;
		mBuilder = new NotificationCompat.Builder(context)
													.setSmallIcon(R.drawable.ic_launcher)
													.setContentTitle(title)
													.setSubText(subText)
													.setContentInfo(contentInfo)
													.setContentText(body);

		Intent i = new Intent(context, classToLauchOnClick);
		PendingIntent onNotificationClickPentingIntent = PendingIntent.getActivity(context, 0, i, 0);
		mBuilder.setContentIntent(onNotificationClickPentingIntent);

		notif = mBuilder.build();
	}
	
	public NotificationMaker(Context context, String title, String body, String subText, String contentInfo, Class<MainActivity> classToLauchOnClick, int maxProgress, int progress) {
		NotificationCompat.Builder mBuilder;
		mBuilder = new NotificationCompat.Builder(context)
													.setSmallIcon(R.drawable.ic_launcher)
													.setContentTitle(title)
													.setSubText(subText)
													.setContentInfo(progress + " / " + maxProgress)
													.setContentText(body)
													.setProgress(100, progress * 100 / maxProgress, false);
		/*
		Intent i = new Intent(context, classToLauchOnClick);
		PendingIntent onNotificationClickPentingIntent = PendingIntent.getActivity(context, 0, i, 0);
		mBuilder.setContentIntent(onNotificationClickPentingIntent);
		*/
		notif = mBuilder.build();
	}
	
	public Notification getNotif() {
		return notif;
	}
	
}
