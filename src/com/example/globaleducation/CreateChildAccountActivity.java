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
import android.widget.NumberPicker;
import android.widget.Toast;

public class CreateChildAccountActivity extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        
        final NumberPicker gradePicker = (NumberPicker) findViewById(R.id.grade_picker);
        gradePicker.setMinValue(1);
        gradePicker.setMaxValue(8);
        
        Button cancelButton = (Button) findViewById(R.id.cancel_acc_create_button);
        cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
        	
        });
        
        Button createButton = (Button) findViewById(R.id.confirm_new_account_button);
        createButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText accountNameBox = (EditText) findViewById(R.id.new_username);
				String accountName = accountNameBox.getText().toString();
				if(accountName.length() < 6)
				{
					Toast.makeText(getApplicationContext(), R.string.invalid_username, Toast.LENGTH_LONG).show();
					return;
				}
				
				EditText passwordBox = (EditText) findViewById(R.id.new_password);
				String password = passwordBox.getText().toString();
				
				EditText confirmPasswordBox = (EditText) findViewById(R.id.new_password_confirm);
				String confirmPassword = confirmPasswordBox.getText().toString();
				
				if(!password.equals(confirmPassword))
				{
					Toast.makeText(getApplicationContext(), R.string.nonmatching_passwords, Toast.LENGTH_LONG).show();
					return;
				}
				
				EditText nameBox = (EditText) findViewById(R.id.student_name);
				String name = nameBox.getText().toString();
				
				int grade = gradePicker.getValue();
				
				// TODO get location information
				String city = "College Park";
				city = city.replace(" ", "+");
				String state = "Maryland";
				state = state.replace(" ", "+");
				String country = "United States";
				country = country.replace(" ", "+");
				
				System.out.println(city + " " + state + "country");
				
				new ChildSignupGetTask().execute(accountName, password, Integer.toString(grade), city, 
												 state, country, name);
			}
        	
        });
    }
	
	private class ChildSignupGetTask extends AsyncTask<String, Void, Void> {

		AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

		@Override
		protected Void doInBackground(String... params) {
			
			String URL = "http://cmsc436.afh.co/php/addchild.php?username=" + params[0] + "&password=" + 
						  params[1] + "&level=" + params[2] + "&city=" + params[3] + "&state=" + params[4] + 
						  "&country=" + params[5] + "&name=" + params[6];
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
			Toast.makeText(CreateChildAccountActivity.this, "Account Created", Toast.LENGTH_LONG).show();
			finish();

		}
	}
}
