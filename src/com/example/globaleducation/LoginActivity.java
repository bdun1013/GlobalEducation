package com.example.globaleducation;

import java.io.IOException;
import java.util.ArrayList;
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
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends Activity {
	
	String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        Button createChildAccountButton = (Button) findViewById(R.id.create_child_account);
        createChildAccountButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(LoginActivity.this, CreateChildAccountActivity.class);
				startActivity(i);
			}
        	
        });
        
        Button createParentAccountButton = (Button) findViewById(R.id.create_parent_account);
        createParentAccountButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(LoginActivity.this, CreateParentAccountActivity.class);
				startActivity(i);
			}
        	
        });
        
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				EditText userNameBox = (EditText) findViewById(R.id.username);
				EditText passwordBox = (EditText) findViewById(R.id.password);
				
				username = userNameBox.getText().toString();
				final String password = passwordBox.getText().toString();
				
				new LoginGetTask().execute(username, password);
			}
        	
        });
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private class LoginGetTask extends AsyncTask<String, Void, List<String>> {

		AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

		@Override
		protected List<String> doInBackground(String... params) {
			
			String URL = "http://cmsc436.afh.co/php/login.php?username=" + params[0] + "&password=" + params[1];
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
			
			String login = result.get(0);
			// TODO Send parent and child to different activities

			if (login.equals("parent")) {
				Intent i = new Intent(LoginActivity.this, DailyQuestionsActivity.class);
				i.putExtra("username", username);
				startActivity(i);
			} 
			else if (login.equals("child")) {
				Intent i = new Intent(LoginActivity.this, DailyQuestionsActivity.class);
				i.putExtra("username", username);
				startActivity(i);
			}
			else if (login.equals("invalid")) {
				Toast.makeText(LoginActivity.this, "Invalid Login", Toast.LENGTH_LONG).show();
			}
			else {
				throw new IllegalArgumentException();
			}

		}
	}
    
    private class JSONResponseHandler implements ResponseHandler<List<String>> {

		@Override
		public List<String> handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			
			List<String> result = new ArrayList<String>();
			String JSONResponse = new BasicResponseHandler()
					.handleResponse(response);
			
			try {

				JSONObject responseObject = (JSONObject) new JSONTokener(JSONResponse).nextValue();
				String login = responseObject.getString("login");
				result.add(login);
				
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return result;
		}
	}
    
}
