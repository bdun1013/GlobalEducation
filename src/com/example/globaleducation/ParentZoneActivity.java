package com.example.globaleducation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ParentZoneActivity extends Activity implements OnItemSelectedListener {

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Retrieve and display statistics depending on what scale the parent has selected in the spinner
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// Nothing should happen if nothing is selected
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parent_zone);
		
		Spinner spinner = (Spinner) findViewById(R.id.range_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.scope_selector, android.R.layout.simple_spinner_dropdown_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		
		TextView parentText = (TextView) findViewById(R.id.parent_view_info);
		parentText.setText("Here is how *insert name here* is doing relative to:");
	}
}
