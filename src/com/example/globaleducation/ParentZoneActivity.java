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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ParentZoneActivity extends Activity {
	
	Spinner childSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parent_zone);
		
		final String parentUsername = getIntent().getStringExtra("username");
		
		final Spinner rangeSpinner = (Spinner) findViewById(R.id.range_spinner);
		ArrayAdapter<CharSequence> rangeAdapter = ArrayAdapter.createFromResource(this, R.array.scope_selector, android.R.layout.simple_spinner_dropdown_item);
		rangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		rangeSpinner.setAdapter(rangeAdapter);
		rangeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Retrieve and display statistics depending on what scale the parent has selected in the spinner
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Nothing should happen if nothing is selected
				
			}
			
		});
		
		
		childSpinner = (Spinner) findViewById(R.id.child_spinner);
		new GetChildrenGetTask().execute(parentUsername);
		childSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Nothing
				
			}
			
		});
		
		TextView parentText = (TextView) findViewById(R.id.parent_view_info);
		parentText.setText("Here is how *insert name here* is doing relative to:");
		
		Button addChildButton = (Button) findViewById(R.id.add_child_button);
		addChildButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ParentZoneActivity.this);
				LayoutInflater inflater = ParentZoneActivity.this.getLayoutInflater();
				
				final View dialogView = inflater.inflate(R.layout.add_child_dialog, null);
				builder.setView(dialogView);
				builder.setNegativeButton(R.string.cancel_string, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
					
				});
				
				builder.setPositiveButton(R.string.add_child_string, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						EditText childUsernameBox = (EditText) dialogView.findViewById(R.id.intended_child_username);
						String childUsername = childUsernameBox.getText().toString();
						
						new ChildParentLinkGetTask().execute(parentUsername, childUsername);
					}
					
				});
				
				builder.setTitle(R.string.add_child_string);
				builder.create().show();
			}
			
		});
	}
		
	private class ChildParentLinkGetTask extends AsyncTask<String, Void, Void> {

		AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

		@Override
		protected Void doInBackground(String... params) {
			
			String URL = "http://cmsc436.afh.co/php/linkchild.php?parentUsername=" +
						  params[0] + "&childUsername=" + params[1];
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
			Toast.makeText(ParentZoneActivity.this, "Child Account Linked", Toast.LENGTH_LONG).show();
			finish();

		}
	}
	
    private class GetChildrenGetTask extends AsyncTask<String, Void, List<String>> {

		AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

		@Override
		protected List<String> doInBackground(String... params) {
			
			String URL = "http://cmsc436.afh.co/php/getchildren.php?username=" + params[0];
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
			
			childSpinner.setAdapter(new ArrayAdapter<String>(
					ParentZoneActivity.this,
					android.R.layout.simple_spinner_dropdown_item, result));

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
				
				
				int childNumber = 1;
				String childUsername;
				
				while(responseObject.getString("child" + childNumber) != null) {
					childUsername = responseObject.getString("child" + childNumber);
					result.add(childUsername);
					childNumber++;
				}
				
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return result;
		}
	}
	
}
