package com.example.globaleducation;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DailyQuestionsActivity extends Activity {
	private AlarmManager alarmManager;
	private Intent alarmIntent;
	private PendingIntent pendingIntent;
	private Calendar alarmStartTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_selector);
		
		TextView textView = (TextView) findViewById(R.id.question_selector_text);
			
		//TODO Set name based on account information from server
		textView.setText("Welcome back, *insert name here*!\n\nClick on one of the buttons to" +
				" try your daily question in each subject!");
		
		Button mathButton = (Button) findViewById(R.id.math_button);
		mathButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/* TODO Download math question from server and
				pass it into QuestionActivity as an extra
				in the Intent. The below example shows how I have it set up */
				
				
				Intent i = new Intent(DailyQuestionsActivity.this, QuestionActivity.class);
				i.putExtras(DailyQuestionsActivity.this.getIntent());
				i.putExtra("QuestionText", "This is a question.");
				i.putExtra("ChoiceA", "This is choice A");
				i.putExtra("ChoiceB", "This is choice B");
				i.putExtra("ChoiceC", "This is choice C");
				i.putExtra("ChoiceD", "This is choice D");
				i.putExtra("Correct", 1); //In this case, the correct choice is A
				
				startActivity(i);
			}
			
		});
		
		setAlarm();
		
		Button parentZoneButton = (Button) findViewById(R.id.parent_zone_button);
		parentZoneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(DailyQuestionsActivity.this, ParentZoneActivity.class);
				startActivity(i);
			}
			
		});
		
		
		//This is the code to (hopefully) notify the child once every 24 hours when a question is ready
		//Notification will be at exactly 9:00 am every day
		

	}
	
	public void setAlarm(){
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmIntent = new Intent(DailyQuestionsActivity.this, AlarmReceiver.class);
		pendingIntent = PendingIntent.getBroadcast(DailyQuestionsActivity.this, 0, alarmIntent, 0);

		alarmStartTime = Calendar.getInstance();
		alarmStartTime.set(Calendar.HOUR_OF_DAY, 10);
		alarmStartTime.set(Calendar.MINUTE, 00);
		alarmStartTime.set(Calendar.SECOND, 0);
		
		Calendar currentTimeCal = Calendar.getInstance();
		
		long intendedTime = alarmStartTime.getTimeInMillis();
		long currentTime = currentTimeCal.getTimeInMillis();
		
		if(intendedTime >= currentTime)
			alarmManager.setRepeating(AlarmManager.RTC, intendedTime, getInterval(), pendingIntent);
		else
		{
			alarmStartTime.add(Calendar.DAY_OF_MONTH, 1);
			intendedTime = alarmStartTime.getTimeInMillis();
			alarmManager.setRepeating(AlarmManager.RTC, intendedTime, getInterval(), pendingIntent);
		}
	}
	
	private int getInterval(){
		 int days = 1;
		 int hours = 24;
		 int minutes = 60;
		 int seconds = 60;
		 int milliseconds = 1000;
		 int repeatMS = days * hours * minutes * seconds * milliseconds;
		 return repeatMS;
	}

}
