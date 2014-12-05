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

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parent_zone);
		// TODO guarantee username is correct
		
		final String parentUsername = getIntent().getStringExtra("username");
		
		Spinner spinner = (Spinner) findViewById(R.id.range_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.scope_selector, android.R.layout.simple_spinner_dropdown_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

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
	
}
