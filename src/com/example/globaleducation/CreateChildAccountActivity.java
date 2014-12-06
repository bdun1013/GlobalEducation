package com.example.globaleducation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateChildAccountActivity extends Activity {
	
	Map<String,String> countries;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        
        final NumberPicker gradePicker = (NumberPicker) findViewById(R.id.grade_picker);
        gradePicker.setMinValue(1);
        gradePicker.setMaxValue(8);
        
        Spinner countrySpinner = (Spinner) findViewById(R.id.country_spinner);
        
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, 
                new ArrayList<CharSequence>());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		countrySpinner.setAdapter(adapter);
        
        CountryListGetTask countryListTask = new CountryListGetTask();
        countryListTask.execute();
        try {
			Log.i("GlobalEducation", "Getting results from Async Task");
			countries = countryListTask.get();
			Log.i("GlobalEducation", "Finished Async Task");
			for (String country : (countries.keySet())) {
				adapter.add(country);
			}
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        Log.i("GlobalEducation", "Finished Country List");
        
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
				
				EditText zipBox = (EditText) findViewById(R.id.zip_code_text);
				String zip = zipBox.getText().toString();
				
				Spinner countrySpinner = (Spinner) findViewById(R.id.country_spinner);
				String country = countrySpinner.getSelectedItem().toString();
				String countryCode = countries.get(country);
				
				ChildLocationGetTask locationTask = new ChildLocationGetTask();
				locationTask.execute(zip, countryCode);
				List<String> locationArray;
				String city, state;
				
				try {
					locationArray = locationTask.get();
					city = locationArray.get(0);
					state = locationArray.get(1);
					
					new ChildSignupGetTask().execute(accountName, password, Integer.toString(grade), city, 
							 state, country, name);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
        	
        });
        
		Log.i("GlobalEducation", "At end of onCreate");
    }
	
	private class CountryListGetTask extends AsyncTask<Void, Void, Map<String,String>> {

		AndroidHttpClient mClient = AndroidHttpClient.newInstance("");
		
		@Override
		protected Map<String, String> doInBackground(Void... params) {
			String URL = "http://api.geonames.org/postalCodeCountryInfoJSON?username=mjsmith";
			
			HttpGet request = new HttpGet(URL);
			
			JSONCountryResponseHandler responseHandler = new JSONCountryResponseHandler();
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
		protected void onPostExecute(Map<String,String> result) {
			mClient.close();
		}
		
	}
	
	private class JSONCountryResponseHandler implements ResponseHandler<Map<String,String>> {

		@Override
		public Map<String, String> handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			
			Map<String,String> result = new TreeMap<String,String>();
			String JSONResponse = new BasicResponseHandler()
					.handleResponse(response);
			Log.i("GlobalEducation", JSONResponse);
			
			try {

				JSONObject responseObject = (JSONObject) new JSONTokener(JSONResponse).nextValue();
				JSONArray responseArray = responseObject.getJSONArray("geonames");
				
				for (int i = 0; i < responseArray.length(); i ++) {
					JSONObject country = (JSONObject) responseArray.get(i);
					String countryName = country.getString("countryName");
					String countryCode = country.getString("countryCode");
					
					result.put(countryName, countryCode);
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return result;
		}
	}
	
	private class ChildLocationGetTask extends AsyncTask<String, Void, List<String>> {

		AndroidHttpClient mClient = AndroidHttpClient.newInstance("");
		
		@Override
		protected List<String> doInBackground(String... params) {
			String URL = "http://api.geonames.org/postalCodeSearchJSON?postalcode=" + params[0] + "&country=" + params[1] + "&username=mjsmith";
			
			HttpGet request = new HttpGet(URL);
			
			JSONLocationResponseHandler responseHandler = new JSONLocationResponseHandler();
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
		}
		
	}
	
	private class JSONLocationResponseHandler implements ResponseHandler<List<String>> {

		@Override
		public List<String> handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			
			List<String> result = new ArrayList<String>();
			String JSONResponse = new BasicResponseHandler()
					.handleResponse(response);
			
			try {

				JSONObject responseObject = (JSONObject) new JSONTokener(JSONResponse).nextValue();
				JSONArray responseArray = responseObject.getJSONArray("postalCodes");
				JSONObject postalCode = (JSONObject) responseArray.get(0);
				String city = postalCode.getString("placeName");
				String state = postalCode.getString("adminName1");
				result.add(city);
				result.add(state);
				
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return result;
		}
	}
	
	private class ChildSignupGetTask extends AsyncTask<String, Void, Void> {

		AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

		@Override
		protected Void doInBackground(String... params) {
			String city = params[3];
			city = city.replace(' ', '+');
			String state = params[4];
			state = state.replace(' ', '+');
			String country = params[5];
			country = country.replace(' ', '+');
			
			String URL = "http://cmsc436.afh.co/php/addchild.php?username=" + params[0] + "&password=" + 
						  params[1] + "&level=" + params[2] + "&city=" + city + "&state=" + state + 
						  "&country=" + country + "&name=" + params[6];
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
