package com.example.globaleducation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class CreateAccountActivity extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        
        final NumberPicker gradePicker = (NumberPicker) findViewById(R.id.grade_picker);
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
				
				// TODO Create new account with given information on the server
				
			}
        	
        });
    }
}
