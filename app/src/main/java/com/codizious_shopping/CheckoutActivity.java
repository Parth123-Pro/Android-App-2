package com.codizious_shopping;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.codizious_shopping.R.id;

import util.Common;
import util.ConnectionDetector;
import Config.ConstValue;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

@SuppressWarnings("deprecation")
public class CheckoutActivity extends ActionBarActivity {
	public SharedPreferences settings;
	public ConnectionDetector cd;
	EditText txtCity,txtName,txtAddress,txtZipcode,txtEmail, txtPhone;
	Button btnSave;
	ProgressDialog dialog;
	Common common;

	@SuppressLint("CutPasteId") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkout);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		 common = new Common();
	        
		 
		 settings = getSharedPreferences(ConstValue.MAIN_PREF, 0);
			cd=new ConnectionDetector(this);

			txtCity = (EditText)findViewById(R.id.editCity);
			
			txtName = (EditText)findViewById(R.id.editFname);
			txtPhone = (EditText)findViewById(R.id.editPhone);
			txtAddress = (EditText)findViewById(R.id.editAddress);
			txtZipcode = (EditText)findViewById(R.id.editZipcode);
			txtEmail = (EditText)findViewById(R.id.editEmail);
			

	        txtName.setText(settings.getString("user_name", ""));

	        txtEmail.setText(settings.getString("user_email", ""));
	        txtPhone.setText(settings.getString("user_mobile", ""));
	    
	        txtAddress.setText(settings.getString("user_address", ""));
			
	    
	        txtZipcode.setText(settings.getString("user_zipcode", ""));
	        
	      
	        txtCity.setText(settings.getString("user_city", ""));
	        
	        btnSave = (Button)findViewById(id.btnsave);
	        btnSave.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new ProfileupdateTask().execute(true);

					if(txtCity.getText().toString().length()==0)
					{
						txtCity.setError("Enter City");
					}
					if(txtEmail.getText().toString().length()==0)
					{
						txtEmail.setError("Enter Email");
					}
					else if (txtName.getText().toString().length()==0) {
						txtName.setError("Enter Name");
					}
					else if (txtAddress.getText().toString().length()==0) {
						txtAddress.setError("Enter Address");
					}
					
					else if (txtZipcode.getText().toString().length()==0) {
						txtZipcode.setError("Enter Zipcode");
					}
					else{
						
					
						settings.edit().putString("order_name", txtName.getText().toString()).apply();
						settings.edit().putString("order_city", txtCity.getText().toString()).apply();
						settings.edit().putString("order_email", txtEmail.getText().toString()).apply();
						settings.edit().putString("order_zipcode", txtZipcode.getText().toString()).apply();
						settings.edit().putString("order_address", txtAddress.getText().toString()).apply();
						
						settings.edit().putString("order_phone", txtPhone.getText().toString()).apply();
						

						}

				}
			});


	    
	}

	class ProfileupdateTask extends AsyncTask<Boolean, Void, String> {
		String txtphone,txtcity,txtname,txtaddress,txtzipcode,txtemail,txtusername;
		@Override
		protected String doInBackground(Boolean... params) {
			String responceString = null;
			List<NameValuePair> nameVapluePairs = new ArrayList<NameValuePair>(2);
			nameVapluePairs.add(new BasicNameValuePair("mobile", txtphone));
			nameVapluePairs.add(new BasicNameValuePair("city",txtcity));
			nameVapluePairs.add(new BasicNameValuePair("name",txtname));
			nameVapluePairs.add(new BasicNameValuePair("address",txtaddress));
			nameVapluePairs.add(new BasicNameValuePair("zipcode",txtzipcode));
			nameVapluePairs.add(new BasicNameValuePair("email",txtemail));
			nameVapluePairs.add(new BasicNameValuePair("username",txtusername));
			nameVapluePairs.add(new BasicNameValuePair("id",settings.getString("userid","")));

			JSONObject jObj = common.sendJsonData(ConstValue.JSON_PROFILE_UPDATE, nameVapluePairs);
			try{
				if(jObj.getString("responce").equalsIgnoreCase("success")){
					JSONObject data = jObj.getJSONObject("data");
					if(!data.getString("id").equalsIgnoreCase("")){
						settings.edit().putString("userid", data.getString("id")).apply();
						settings.edit().putString("username", data.getString("username")).apply();
						settings.edit().putString("user_unique_code", data.getString("unique_code")).apply();
						settings.edit().putString("user_email", data.getString("email")).apply();
						settings.edit().putString("user_name", data.getString("name")).apply();
						settings.edit().putString("user_mobile", data.getString("mobile")).apply();
						settings.edit().putString("user_address", data.getString("address")).apply();
						settings.edit().putString("user_state", data.getString("state")).apply();
						settings.edit().putString("user_country", data.getString("country")).apply();
						settings.edit().putString("user_zipcode", data.getString("zipcode")).apply();
						settings.edit().putString("user_city", data.getString("city")).apply();
						settings.edit().putString("user_password", data.getString("password")).apply();
						settings.edit().putString("user_image", data.getString("image")).apply();
						settings.edit().putString("user_phone_verified", data.getString("phone_verified")).apply();
						settings.edit().putString("user_reg_date", data.getString("reg_date")).apply();
						settings.edit().putString("user_status", data.getString("status")).apply();


					}
				}
				else{
					responceString = jObj.getString("error");
				}
			}
			catch(JSONException e){
				responceString = e.getMessage();
			}
			// TODO Auto-generated method stub
			return responceString;
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		protected void onCancelled(String result) {
			// TODO Auto-generated method stub
			super.onCancelled(result);
		}

		@Override
		protected void onPostExecute(String result) {

			Intent intent = new Intent(CheckoutActivity.this,PlaceorderActivity.class);

			startActivity(intent);

			// TODO Auto-generated method stub
			dialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			txtphone = txtPhone.getText().toString();
			txtcity =txtCity.getText().toString();
			txtname =txtName.getText().toString();
			txtaddress = txtAddress.getText().toString();
			txtzipcode = txtZipcode.getText().toString();
			txtemail =txtEmail.getText().toString();

			dialog = ProgressDialog.show(CheckoutActivity.this, "",getString(R.string.loading), true);
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.checkout, menu);
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
		}else if(id == android.R.id.home){
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
