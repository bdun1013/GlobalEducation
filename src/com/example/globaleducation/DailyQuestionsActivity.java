package com.example.globaleducation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
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
		
		final String childUsername = getIntent().getStringExtra("username");
		
		textView.setText("Welcome back, " + childUsername + "\n\nClick the button below" +
				" try your daily question!");
		
		Button mathButton = (Button) findViewById(R.id.math_button);
		mathButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				new QuestionGetTask().execute(childUsername);
			}
			
		});
		
		setAlarm();
		
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
	
    private class QuestionGetTask extends AsyncTask<String, Void, List<String>> {

		AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

		@Override
		protected List<String> doInBackground(String... params) {
			
			String URL = "http://cmsc436.afh.co/php/getquestion.php?username=" + params[0];
			HttpGet request = new HttpGet(URL);
			JSONResponseHandler responseHandler = new JSONResponseHandler();
			try {

				return mClient.execute(request, responseHandler);

			} catch (ClientProtocolException exception) {
				exception.printStackTrace();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<String> result) {
			mClient.close();
			
			

			Intent i = new Intent(DailyQuestionsActivity.this, QuestionActivity.class);
			i.putExtras(DailyQuestionsActivity.this.getIntent());
			i.putExtra("QuestionText", result.get(0));
			i.putExtra("ChoiceA", result.get(1));
			i.putExtra("ChoiceB", result.get(2));
			i.putExtra("ChoiceC", result.get(3));
			i.putExtra("ChoiceD", result.get(4));
			i.putExtra("Correct", result.get(5)); 
			i.putExtra("Category", result.get(6));
			i.putExtra("QuestionID", result.get(7));
			i.putExtra("Username", result.get(8));
			
			startActivity(i);
		}
	}
    
    private class JSONResponseHandler implements ResponseHandler<List<String>> {

		@Override
		public List<String> handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			
			List<String> result = new ArrayList<String>();
			String JSONResponse = new BasicResponseHandler().handleResponse(response);
			
			try {

				JSONObject responseObject = (JSONObject) new JSONTokener(JSONResponse).nextValue();
				result.add(responseObject.getString("questionText"));
				result.add(responseObject.getString("choiceOne"));
				result.add(responseObject.getString("choiceTwo"));
				result.add(responseObject.getString("choiceThree"));
				result.add(responseObject.getString("choiceFour"));
				result.add(responseObject.getString("correctChoice"));
				result.add(responseObject.getString("category"));
				result.add(responseObject.getString("questionID"));
				result.add(responseObject.getString("username"));
				
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return result;
		}
	}

}
