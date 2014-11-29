package com.example.globaleducation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;

public class CreateAccountActivity extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        
        NumberPicker gradePicker = (NumberPicker) findViewById(R.id.grade_picker);
        gradePicker.setMinValue(1);
        gradePicker.setMaxValue(12);
        
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
				// TODO Create new account with given information on the server
				
			}
        	
        });
    }
}
