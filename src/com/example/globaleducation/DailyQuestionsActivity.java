package com.example.globaleducation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DailyQuestionsActivity extends Activity {
	
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
				i.putExtra("QuestionText", "This is a question.");
				i.putExtra("ChoiceA", "This is choice A");
				i.putExtra("ChoiceB", "This is choice B");
				i.putExtra("ChoiceC", "This is choice C");
				i.putExtra("ChoiceD", "This is choice D");
				i.putExtra("Correct", 1); //In this case, the correct choice is A
				
				startActivity(i);
			}
			
		});
		
		Button scienceButton = (Button) findViewById(R.id.science_button);
		scienceButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/* TODO Download science question from server and
				pass it into QuestionActivity as an extra
				in the Intent. The below example shows how I have it set up */
				
				
				Intent i = new Intent(DailyQuestionsActivity.this, QuestionActivity.class);
				i.putExtra("QuestionText", "This is a question.");
				i.putExtra("ChoiceA", "This is choice A");
				i.putExtra("ChoiceB", "This is choice B");
				i.putExtra("ChoiceC", "This is choice C");
				i.putExtra("ChoiceD", "This is choice D");
				i.putExtra("Correct", 2); //In this case, the correct choice is B
				
				startActivity(i);
			}
			
		});
		
		Button englishButton = (Button) findViewById(R.id.english_button);
		englishButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/* TODO Download English question from server and
				pass it into QuestionActivity as an extra
				in the Intent. The below example shows how I have it set up */
				
				
				Intent i = new Intent(DailyQuestionsActivity.this, QuestionActivity.class);
				i.putExtra("QuestionText", "This is a question.");
				i.putExtra("ChoiceA", "This is choice A");
				i.putExtra("ChoiceB", "This is choice B");
				i.putExtra("ChoiceC", "This is choice C");
				i.putExtra("ChoiceD", "This is choice D");
				i.putExtra("Correct", 3); //In this case, the correct choice is C
				
				startActivity(i);
			}
			
		});
		
		Button historyButton = (Button) findViewById(R.id.history_button);
		historyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/* TODO Download history question from server and
				pass it into QuestionActivity as an extra
				in the Intent. The below example shows how I have it set up */
				
				
				Intent i = new Intent(DailyQuestionsActivity.this, QuestionActivity.class);
				i.putExtra("QuestionText", "This is a question.");
				i.putExtra("ChoiceA", "This is choice A");
				i.putExtra("ChoiceB", "This is choice B");
				i.putExtra("ChoiceC", "This is choice C");
				i.putExtra("ChoiceD", "This is choice D");
				i.putExtra("Correct", 4); //In this case, the correct choice is D
				
				startActivity(i);
			}
			
		});
		
		Button parentZoneButton = (Button) findViewById(R.id.parent_zone_button);
		parentZoneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(DailyQuestionsActivity.this, ParentZoneActivity.class);
				startActivity(i);
			}
			
		});
	}

}
