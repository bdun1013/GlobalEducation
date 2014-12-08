package com.example.globaleducation;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ParentZoneActivity extends Activity {

	private Spinner rangeSpinner;
	private Spinner childSpinner;
	private TextView totalText;
	private TextView percentageText;
	private TextView percentileText;
	private ProgressBar percentageBar;
	private ProgressBar percentileBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parent_zone);

		totalText = (TextView) findViewById(R.id.total_questions_display);
		percentageText = (TextView) findViewById(R.id.percentage_text);
		percentileText = (TextView) findViewById(R.id.percentile_text);
		percentageBar = (ProgressBar) findViewById(R.id.percentage_progress_bar);
		percentileBar = (ProgressBar) findViewById(R.id.percentile_progress_bar);

		final String parentUsername = getIntent().getStringExtra("username");

		rangeSpinner = (Spinner) findViewById(R.id.range_spinner);
		ArrayAdapter<CharSequence> rangeAdapter = ArrayAdapter
				.createFromResource(this, R.array.scope_selector,
						android.R.layout.simple_spinner_dropdown_item);
		rangeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		rangeSpinner.setAdapter(rangeAdapter);

		childSpinner = (Spinner) findViewById(R.id.child_spinner);
		new GetChildrenGetTask().execute(parentUsername);

		TextView parentText = (TextView) findViewById(R.id.parent_view_info);
		parentText.setText("Show statistics relative to:");

		Button statsButton = (Button) findViewById(R.id.statistics_button);
		statsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateStats();

			}

		});

		Button addChildButton = (Button) findViewById(R.id.add_child_button);
		addChildButton.setOnClickListener(new OnClickListener() {

			@SuppressLint("InflateParams")
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ParentZoneActivity.this);
				LayoutInflater inflater = ParentZoneActivity.this
						.getLayoutInflater();

				final View dialogView = inflater.inflate(
						R.layout.add_child_dialog, null);
				builder.setView(dialogView);
				builder.setNegativeButton(R.string.cancel_string,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}

						});

				builder.setPositiveButton(R.string.add_child_string,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								EditText childUsernameBox = (EditText) dialogView
										.findViewById(R.id.intended_child_username);
								String childUsername = childUsernameBox
										.getText().toString();

								new ChildParentLinkGetTask().execute(
										parentUsername, childUsername);
							}

						});

				builder.setTitle(R.string.add_child_string);
				builder.create().show();
			}

		});
	}

	private class StatsGetTask extends
			AsyncTask<String, Void, List<Pair<Double, Double>>> {

		AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

		@Override
		protected List<Pair<Double, Double>> doInBackground(String... params) {

			String childUsername = params[0];
			String locationType = params[1];

			String URL = "http://cmsc436.afh.co/php/getstats.php?username="
					+ childUsername + "&location=" + locationType;
			HttpGet request = new HttpGet(URL);

			JSONStatsResponseHandler responseHandler = new JSONStatsResponseHandler();

			try {
				return mClient.execute(request, responseHandler);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<Pair<Double, Double>> result) {
			mClient.close();
		}

	}

	private class JSONStatsResponseHandler implements
			ResponseHandler<List<Pair<Double, Double>>> {

		@Override
		public List<Pair<Double, Double>> handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {

			List<Pair<Double, Double>> result = new ArrayList<Pair<Double, Double>>();
			String JSONResponse = new BasicResponseHandler()
					.handleResponse(response);

			try {

				JSONArray responseArray = (JSONArray) new JSONTokener(
						JSONResponse).nextValue();
				
				JSONObject user = (JSONObject) responseArray.get(0);
				if (!user.isNull("correct")) {
					Double numCorrect = Double.parseDouble(user
							.getString("correct"));
					Double numTotal = Double.parseDouble(user
							.getString("total"));

					result.add(new Pair<Double, Double>(numCorrect,
							numTotal));
				}
				else {
					result.add(new Pair<Double,Double>(null,null));
				}

				for (int i = 1; i < responseArray.length(); i++) {
					user = (JSONObject) responseArray.get(i);
					if (!user.isNull("correct")) {
						Double numCorrect = Double.parseDouble(user
								.getString("correct"));
						Double numTotal = Double.parseDouble(user
								.getString("total"));

						result.add(new Pair<Double, Double>(numCorrect,
								numTotal));
					}

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return result;
		}
	}

	private class ChildParentLinkGetTask extends AsyncTask<String, Void, Void> {

		AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

		@Override
		protected Void doInBackground(String... params) {

			String URL = "http://cmsc436.afh.co/php/linkchild.php?parentUsername="
					+ params[0] + "&childUsername=" + params[1];
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
			Toast.makeText(ParentZoneActivity.this, "Child Account Linked",
					Toast.LENGTH_LONG).show();
			finish();

		}
	}

	private void updateStats() {
		StatsGetTask statsTask = new StatsGetTask();

		String childUsername = childSpinner.getSelectedItem().toString();
		String locationType = rangeSpinner.getSelectedItem().toString();

		statsTask.execute(childUsername, locationType);

		try {
			List<Pair<Double, Double>> statsList = statsTask.get();

			// Current child is first in the list
			Double currentChildCorrect = statsList.get(0).first;
			
			if (currentChildCorrect == null) {
				Toast.makeText(getApplicationContext(), "This child has not answered any questions", Toast.LENGTH_LONG).show();
				totalText.setText("0");
				percentageText.setText("0.00 %");
				percentileText.setText("0.00 %");
				percentageBar.setProgress(0);
				percentileBar.setProgress(0);
				return;
			}
			
			Double currentChildTotal = statsList.get(0).second;
			Double currentChildPercent = currentChildCorrect
					/ currentChildTotal * 100;

			// Get the rest of the users' percentages
			List<Double> percents = new ArrayList<Double>();
			for (int i = 1; i < statsList.size(); i++) {
				Pair<Double, Double> user = statsList.get(i);
				percents.add(user.first / user.second * 100);
			}

			// Calculate the current child's percentile
			double betterThan = 0;
			for (Double userPercent : percents) {
				if (currentChildPercent > userPercent) {
					betterThan++;
				}
			}

			Double currentChildPercentile = betterThan / percents.size() * 100;

			// Update displayed stats info
			totalText.setText(Integer.toString(currentChildTotal.intValue()));
			percentageText.setText(new DecimalFormat("##.##")
					.format(currentChildPercent) + " %");
			percentileText.setText(new DecimalFormat("##.##")
					.format(currentChildPercentile) + " %");
			percentageBar.setProgress(currentChildPercent.intValue());
			percentileBar.setProgress(currentChildPercentile.intValue());

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	private class GetChildrenGetTask extends
			AsyncTask<String, Void, List<String>> {

		AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

		@Override
		protected List<String> doInBackground(String... params) {

			String URL = "http://cmsc436.afh.co/php/getchildren.php?username="
					+ params[0];
			HttpGet request = new HttpGet(URL);
			JSONGetChildrenResponseHandler responseHandler = new JSONGetChildrenResponseHandler();
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

	private class JSONGetChildrenResponseHandler implements
			ResponseHandler<List<String>> {

		@Override
		public List<String> handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {

			List<String> result = new ArrayList<String>();
			String JSONResponse = new BasicResponseHandler()
					.handleResponse(response);

			try {

				JSONObject responseObject = (JSONObject) new JSONTokener(
						JSONResponse).nextValue();

				int childNumber = 1;
				String childUsername;

				while (responseObject.getString("child" + childNumber) != null) {
					childUsername = responseObject.getString("child"
							+ childNumber);
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
