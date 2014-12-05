package com.example.globaleducation;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import android.app.Activity;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateParentAccountActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_parent_account);
		
		Button createAccountButton = (Button) findViewById(R.id.parent_confirm_new_account_button);
		createAccountButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText accountNameBox = (EditText) findViewById(R.id.parent_username);
				String accountName = accountNameBox.getText().toString();
				if(accountName.length() < 6)
				{
					Toast.makeText(getApplicationContext(), R.string.invalid_username, Toast.LENGTH_LONG).show();
					return;
				}
				
				EditText passwordBox = (EditText) findViewById(R.id.parent_password);
				String password = passwordBox.getText().toString();
				
				EditText confirmPasswordBox = (EditText) findViewById(R.id.parent_confirm_password);
				String confirmPassword = confirmPasswordBox.getText().toString();
				
				if(!password.equals(confirmPassword))
				{
					Toast.makeText(getApplicationContext(), R.string.nonmatching_passwords, Toast.LENGTH_LONG).show();
					return;
				}
				
				new ParentSignupGetTask().execute(accountName, password);
			}
			
		});
		
		Button cancelButton = (Button) findViewById(R.id.parent_cancel_button);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
			
		});
	}
	
	private class ParentSignupGetTask extends AsyncTask<String, Void, Void> {

		AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

		@Override
		protected Void doInBackground(String... params) {
			
			String URL = "http://cmsc436.afh.co/php/addparent.php?username=" + params[0] + "&password=" + params[1];
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
			Toast.makeText(CreateParentAccountActivity.this, "Account Created", Toast.LENGTH_LONG).show();
			finish();

		}
	}

}
