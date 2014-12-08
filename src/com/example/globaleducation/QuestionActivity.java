package com.example.globaleducation;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class QuestionActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question);

		if (getIntent().getExtras() != null) {
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

			final int correctChoice = Integer.parseInt(getIntent()
					.getStringExtra("Correct"));
			final String questionID = getIntent().getStringExtra("QuestionID");
			final String username = getIntent().getStringExtra("Username");

			final Button submitButton = (Button) findViewById(R.id.submit_button);
			submitButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String isCorrect = isAnswerCorrect(correctChoice) ? "Y"
							: "N";

					new SubmitQuestionGetTask().execute(username, questionID,
							isCorrect);

					AlertDialog.Builder builder = new AlertDialog.Builder(
							QuestionActivity.this);

					if (isCorrect.equals("Y")) {
						builder.setTitle("Correct!");
					}

					else {
						builder.setTitle("Incorrect");
					}

					String ans;

					if (buttonA.isChecked())
						ans = buttonA.getText().toString();
					else if (buttonB.isChecked())
						ans = buttonB.getText().toString();
					else if (buttonC.isChecked())
						ans = buttonC.getText().toString();
					else
						ans = buttonD.getText().toString();
					
					String correctAns;
					
					if(correctChoice == 1)
						correctAns = buttonA.getText().toString();
					else if(correctChoice == 2)
						correctAns = buttonB.getText().toString();
					else if(correctChoice == 3)
						correctAns = buttonC.getText().toString();
					else
						correctAns = buttonD.getText().toString();

					String message = "You answered: " + ans
							+ "\nCorrect answer: " + correctAns;
					builder.setMessage(message);

					builder.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							});

					builder.create().show();

				}

				public boolean isAnswerCorrect(int correctChoice) {
					if (correctChoice == 1 && buttonA.isChecked())
						return true;
					else if (correctChoice == 2 && buttonB.isChecked())
						return true;
					else if (correctChoice == 3 && buttonC.isChecked())
						return true;
					else if (correctChoice == 4 && buttonD.isChecked())
						return true;
					return false;
				}

			});

		}
	}

	private class SubmitQuestionGetTask extends AsyncTask<String, Void, Void> {

		AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

		@Override
		protected Void doInBackground(String... params) {

			String URL = "http://cmsc436.afh.co/php/submitquestion.php?username="
					+ params[0]
					+ "&questionID="
					+ params[1]
					+ "&isCorrect="
					+ params[2];
			HttpGet request = new HttpGet(URL);
			try {

				mClient.execute(request);

			} catch (ClientProtocolException exception) {
				exception.printStackTrace();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mClient.close();
		}
	}

}
