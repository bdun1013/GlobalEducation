package com.example.globaleducation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class ParentZoneActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parent_zone);
		
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
				
				builder.setView(inflater.inflate(R.layout.add_child_dialog, null));
				builder.setNegativeButton(R.string.cancel_string, new DialogInterface.OnClickListener() {


					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
					
				});
				
				builder.setPositiveButton(R.string.add_child_string, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						EditText childNameBox = (EditText) findViewById(R.id.intended_child_username);
						String childName = childNameBox.getText().toString();
						
						EditText childPassBox = (EditText) findViewById(R.id.intended_child_password);
						String childPass = childPassBox.getText().toString();
						
						// TODO If UN and PW are valid, link parent and child accounts
						
						
					}
					
				});
				
				builder.setTitle(R.string.add_child_string);
				builder.create().show();
			}
			
		});
	}
}
