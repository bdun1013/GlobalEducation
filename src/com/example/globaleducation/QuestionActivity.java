package com.example.globaleducation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question);
		
		if(getIntent().getExtras() != null)
		{
			TextView questionText = (TextView) findViewById(R.id.question_text);
			questionText.setText(getIntent().getStringExtra("QuestionText"));
			
			final RadioButton buttonA = (RadioButton) findViewById(R.id.option_a);
			buttonA.setText(getIntent().getStringExtra("ChoiceA"));
			
			final RadioButton buttonB = (RadioButton) findViewById(R.id.option_b);
			buttonB.setText(getIntent().getStringExtra("ChoiceB"));
		
			final RadioButton buttonC = (RadioButton) findViewById(R.id.option_c);
			buttonC.setText(getIntent().getStringExtra("ChoiceC"));
			
			final RadioButton buttonD = (RadioButton) findViewById(R.id.option_d);
			buttonD.setText(getIntent().getStringExtra("ChoiceD"));
			
			final int correctChoice = getIntent().getIntExtra("Correct", 0);
			
			final Button submitButton = (Button) findViewById(R.id.submit_button);
			submitButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Send right/wrong to the server to track stats
					
					TextView resultText = (TextView) findViewById(R.id.result_text);
					if(isAnswerCorrect(correctChoice))
						resultText.setText(R.string.correct_answer);
					else
						resultText.setText(R.string.wrong_answer);
					
					submitButton.setClickable(false);
				}
				
				public boolean isAnswerCorrect(int correctChoice)
				{
					if(correctChoice == 0)
						Toast.makeText(getApplicationContext(), R.string.question_error, Toast.LENGTH_LONG).show();
					else if(correctChoice == 1 && buttonA.isChecked())
						return true;
					else if(correctChoice == 2 && buttonB.isChecked())
						return true;
					else if(correctChoice == 3 && buttonC.isChecked())
						return true;
					else if(correctChoice == 4 && buttonD.isChecked())
						return true;
					return false;
				}
				
			});
			
			Button backButton = (Button) findViewById(R.id.back_button);
			backButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
				
			});
		}
	}

}
